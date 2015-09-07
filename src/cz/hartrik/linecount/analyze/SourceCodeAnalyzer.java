
package cz.hartrik.linecount.analyze;

import cz.hartrik.common.Exceptions;
import cz.hartrik.common.Pair;
import cz.hartrik.linecount.analyze.load.TextLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Analyzuje zdrojový kód.
 *
 * @version 2015-09-07
 * @author Patrik Harag
 */
public class SourceCodeAnalyzer implements FileAnalyzer<DataTypeCode> {

    static final Function<FileType, DataTypeCode> DEFAULT_DATA_PROVIDER
            = DataTypeCode::new;

    @Override
    public DataTypeCode analyze(Path file, FileType type, TextLoader loader) {
        return analyze(file, type, loader, DEFAULT_DATA_PROVIDER);
    }

    @Override
    public <U extends DataTypeCode> U analyze(Path file, FileType type,
            TextLoader loader, Function<FileType, U> dataProvider) {

        U data = dataProvider.apply(type);
        StringBuilder builder;

        try (Stream<String> lines = loader.load(file)) {
            builder = readLines(lines, data);
        }

        finalizeComments(data, builder);

        data.addLinesCode(data.getLinesTotal() - data.getLinesEmpty()
                - data.getLinesComment());

        data.addSizeTotal(Exceptions.uncheckedApply(Files::size, file));
        data.addFiles(1);

        return data;
    }

    private StringBuilder readLines(Stream<String> lines, DataTypeCode data) {
        StringBuilder builder = new StringBuilder();

        lines.forEach(line -> {
            final int length = line.length();
            builder.append(line).append('\n');

            int whitespace = 1;  // počítá se i \n
            int indent = 0; boolean isIndent = true;

            for (char character : line.toCharArray()) {
                if (Character.isWhitespace(character)) {
                    whitespace++;
                    if (isIndent) indent++;

                } else {
                    isIndent = false;
                }
            }

            data.addCharsIndent(indent);
            data.addCharsTotal(length + 1); // počítá se i \n
            data.addCharsWhitespace(whitespace);

            data.addLinesTotal(1);

            if ((whitespace - 1) == length)
                data.addLinesEmpty(1);
            else if (startsWithComment(data, line))
                data.addLinesComment(1);
        });

        return builder;
    }

    private boolean startsWithComment(DataTypeCode data, String line) {
        CommentStyle cs = data.getFileType().getCommentStyle();

        for (Pair<Pattern, Pattern> commentPattern : cs.getCommentPatterns()) {
            Matcher matcher = commentPattern.getFirst().matcher(line);
            if (matcher.find()) {
                if (line.substring(0, matcher.start()).trim().isEmpty())
                    return true;
            }
        }

        return false;
    }

    private void finalizeComments(DataTypeCode data, StringBuilder builder) {
        CommentStyle commentStyle = data.getFileType().getCommentStyle();
        CommentParser parser = new CommentParser(commentStyle);

        List<String> comments = parser.analyze(builder.toString());

        for (String comment : comments) {
            data.addLinesComment(countLinesInComment(comment));
            data.addCharsComment(comment.length());
        }
    }

    private int countLinesInComment(String comment) {
        if (comment.isEmpty()) return 0;

        final String[] arrayOfLines = comment.split("\n");
        final int length = arrayOfLines.length;

        if (length == 0) {
            return 0;  // to by se stát nemělo

        } else if (length == 1) {
            return 0;  // jednořádkový komentář, pokud jím řádka začínala,
                       // již je započínaý
        } else {
            int lines = 1;  // poslední řádka začná komentářem a není prázdná
                            // (kvůli sekvenci, která ukončuje komentář)

            for (int i = 1; i < length - 1; i++)
                if (!isBlank(arrayOfLines[i]))
                    lines++;

            return lines;
        }
    }

    private static boolean isBlank(String str) {
        int length = str.length();

        if (length == 0)
            return true;

        for (int i = 0; i < length; i++)
            if ((Character.isWhitespace(str.charAt(i)) == false))
                return false;

        return true;
    }

}