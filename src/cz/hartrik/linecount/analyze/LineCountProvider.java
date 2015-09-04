
package cz.hartrik.linecount.analyze;

import cz.hartrik.linecount.analyze.supported.FileTypes;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Vytváří statistiky počtu řádků, znaků atd...
 *
 * @version 2015-09-04
 * @author Patrik Harag
 */
public class LineCountProvider extends FileAnalyzeProvider {

    protected final Predicate<Path> filter;
    protected final Consumer<String> logConsumer;
    protected final Map<FileType, DataTypeCode> stats = new LinkedHashMap<>();

    protected final UnknownFileAnalyzer unknownFileAnalyzer;
    protected final TextFileAnalyzer textFileAnalyzer;
    protected final SourceCodeAnalyzer sourceFileAnalyzer;

    public LineCountProvider(Predicate<Path> filter, Consumer<String> logConsumer) {
        this.filter = filter;
        this.logConsumer = logConsumer;

        this.unknownFileAnalyzer = new UnknownFileAnalyzer();
        this.textFileAnalyzer    = new TextFileAnalyzer();
        this.sourceFileAnalyzer  = new SourceCodeAnalyzer();
    }

    // implementace abstraktních metod

    @Override
    protected void consumeLog(String message) {
        logConsumer.accept(message);
    }

    @Override
    protected void consumePath(Path path) {
        if (!filter.test(path)) return;

        if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {

            if (!Files.isReadable(path)) {
                logConsumer.accept("Soubor není určen ke čtení - " + path.toString());
                return;
            }

            FileType type = getFileType(path);

            DataTypeCode typeData = getData(type);
            chooseFileAnalyzer(path, typeData);

        } else if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {

        } else if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            logConsumer.accept(
                    "Neexistující složka/soubor - " + path.toString());
        }
    }

    protected FileType getFileType(Path path) {
        String fileName = path.getFileName().toString();

        Optional<FileType> found = (fileName.contains("."))
                ? FileTypes.find(fileName)
                : Optional.empty();

        if (!found.isPresent())
            logConsumer.accept("Soubor neznámého typu - " + path.toString());

        return found.orElse(FileTypes.OTHER);
    }

    // metody

    protected void chooseFileAnalyzer(Path path, DataTypeCode typeData) {
        FileType fileType = typeData.getFileType();

        try {
            if (fileType.isSourceCode())
                sourceFileAnalyzer.analyze(path, typeData);
            else if (fileType.isTextDocument())
                textFileAnalyzer.analyze(path, typeData);
            else
                unknownFileAnalyzer.analyze(path, typeData);

        } catch (UncheckedIOException e) {
            logConsumer.accept("Nepodporované kódování - " + path.toString());
        } catch (IOException e) {
            logConsumer.accept("Chyba při čtení souboru - " + path.toString());
        }
    }

    protected DataTypeCode getData(FileType type) {
        if (!stats.containsKey(type)) {
            // nová položka
            DataTypeCode typeData = new DataTypeCode(type);
            stats.put(type, typeData);
            return typeData;

        } else {
            // stávající položka
            return stats.get(type);
        }
    }

    // gettery

    public Map<FileType, DataTypeCode> getStats() {
        return stats;
    }

}