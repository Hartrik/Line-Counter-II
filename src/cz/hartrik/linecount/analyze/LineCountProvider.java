
package cz.hartrik.linecount.analyze;

import cz.hartrik.linecount.analyze.load.TextLoaders;
import cz.hartrik.linecount.analyze.supported.FileTypes;
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
 * @version 2015-09-07
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
            doAnalysis(path, typeData);

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

    protected void doAnalysis(Path path, DataTypeCode typeData) {
        final FileType fileType = typeData.getFileType();

        @SuppressWarnings("unchecked")
        final FileAnalyzer<DataTypeFile> analyzer =
                (FileAnalyzer<DataTypeFile>) getAnalyzer(fileType);

        try {
            Optional<DataTypeCode> o = TextLoaders.getDefaultGuessLoader().load(
                    (l) -> analyzer.analyze(path, fileType, l, DataTypeCode::new));

            if (o.isPresent()) {
                DataTypeCode data = o.get();

                typeData.addFiles(data.getFiles());
                typeData.addSizeTotal(data.getSizeTotal());

                typeData.addLinesTotal(data.getLinesTotal());
                typeData.addLinesCode(data.getLinesCode());
                typeData.addLinesComment(data.getLinesComment());
                typeData.addLinesEmpty(data.getLinesEmpty());

                typeData.addCharsTotal(data.getCharsTotal());
                typeData.addCharsIndent(data.getCharsIndent());
                typeData.addCharsComment(data.getCharsComment());
                typeData.addCharsWhitespace(data.getCharsWhitespace());

            } else {
                logConsumer.accept("Nepodporované kódování - " + path.toString());
            }
        } catch (Exception e) {
            logConsumer.accept("Chyba při čtení souboru - " + path.toString());
        }
    }

    protected FileAnalyzer<? extends DataTypeFile> getAnalyzer(FileType type) {
        if (type.isSourceCode())
            return sourceFileAnalyzer;
        else if (type.isTextDocument())
            return textFileAnalyzer;
        else
            return unknownFileAnalyzer;
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