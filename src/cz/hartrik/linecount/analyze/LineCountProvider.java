
package cz.hartrik.linecount.analyze;

import cz.hartrik.linecount.analyze.load.TextLoaders;
import cz.hartrik.linecount.analyze.supported.FileTypes;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Vytváří statistiky počtu řádků, znaků atd...
 *
 * @version 2015-09-09
 * @author Patrik Harag
 */
public class LineCountProvider {

    private final Consumer<String> logConsumer;
    private final Map<FileType, DataTypeCode> stats = new LinkedHashMap<>();

    private final UnknownFileAnalyzer unknownFileAnalyzer;
    private final TextFileAnalyzer textFileAnalyzer;
    private final SourceCodeAnalyzer sourceFileAnalyzer;

    public LineCountProvider(Consumer<String> logConsumer) {
        this.logConsumer = logConsumer;

        this.unknownFileAnalyzer = new UnknownFileAnalyzer();
        this.textFileAnalyzer    = new TextFileAnalyzer();
        this.sourceFileAnalyzer  = new SourceCodeAnalyzer();
    }

    public void analyze(Path path) {
        consumePath(path);
    }

    public void analyze(Path... paths) {
        analyze(Arrays.asList(paths));
    }

    public void analyze(Collection<Path> paths) {
        for (Path path : paths) {
            consumePath(path);
        }
    }

    protected void consumeLog(String message) {
        logConsumer.accept(message);
    }

    protected void consumePath(Path path) {
        if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {

            if (!Files.isReadable(path)) {
                consumeLog("Soubor není určen ke čtení - " + path.toString());
                return;
            }

            FileType type = detectFileType(path);
            DataTypeCode typeData = getData(type);
            doAnalysis(path, typeData);

        } else if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            consumeLog("Cesta ukazuje na adresář - " + path.toString());

        } else if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            consumeLog("Neexistující složka/soubor - " + path.toString());
        }
    }

    protected FileType detectFileType(Path path) {
        String fileName = path.getFileName().toString();

        Optional<FileType> found = (fileName.contains("."))
                ? FileTypes.find(fileName)
                : Optional.empty();

        if (!found.isPresent())
            consumeLog("Soubor neznámého typu - " + path.toString());

        return found.orElse(FileTypes.OTHER);
    }

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
                consumeLog("Nepodporované kódování - " + path.toString());
            }
        } catch (Exception e) {
            consumeLog("Chyba při čtení souboru - " + path.toString());
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