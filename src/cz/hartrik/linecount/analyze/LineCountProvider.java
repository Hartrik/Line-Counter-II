
package cz.hartrik.linecount.analyze;

import cz.hartrik.linecount.analyze.load.TextLoaders;
import cz.hartrik.linecount.analyze.supported.FileTypes;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

/**
 * Vytváří statistiky počtu řádků, znaků atd...
 *
 * @version 2015-09-13
 * @author Patrik Harag
 */
public class LineCountProvider {

    private final Consumer<String> logConsumer;
    private final ResourceBundle resourceBundle;
    private final Map<FileType, DataTypeCode> stats = new LinkedHashMap<>();

    private final UnknownFileAnalyzer unknownFileAnalyzer;
    private final TextFileAnalyzer textFileAnalyzer;
    private final SourceCodeAnalyzer sourceFileAnalyzer;

    public LineCountProvider(Consumer<String> logConsumer, ResourceBundle rb) {
        this.logConsumer = logConsumer;
        this.resourceBundle = rb;

        this.unknownFileAnalyzer = new UnknownFileAnalyzer();
        this.textFileAnalyzer    = new TextFileAnalyzer();
        this.sourceFileAnalyzer  = new SourceCodeAnalyzer();
    }

    public void analyze(Path path) {
        consumePath(path);
    }

    public void analyze(Path... paths) {
        for (Path path : paths)
            consumePath(path);
    }

    public void analyze(Collection<Path> paths) {
        for (Path path : paths)
            consumePath(path);
    }

    protected void log(String key, Object... params) {
        String format = resourceBundle.getString(key);
        logConsumer.accept(String.format(format, params));
    }

    protected void consumePath(Path path) {
        if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {

            if (!Files.isReadable(path)) {
                log("log/not-readable", path);
                return;
            }

            FileType type = detectFileType(path);
            DataTypeCode typeData = getData(type);
            doAnalysis(path, typeData);

        } else if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            log("log/not-file", path);

        } else if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            log("log/not-exists", path);
        }
    }

    protected FileType detectFileType(Path path) {
        String fileName = path.getFileName().toString();

        Optional<FileType> found = (fileName.contains("."))
                ? FileTypes.find(fileName)
                : Optional.empty();

        if (!found.isPresent())
            log("log/not-supported", path);

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
                log("log/unknown-encoding", path);
            }
        } catch (Exception e) {
            log("log/io-error", path);
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