
package cz.hartrik.linecount.analyze;

import cz.hartrik.common.Pair;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analyzuje zdrojový kód.
 *
 * @version 2015-09-06
 * @author Patrik Harag
 */
public class SourceCodeAnalyzer implements FileAnalyzer<DataTypeCode> {

    @Override
    public DataTypeCode analyze(Path file, FileType fileType) throws IOException {
        DataTypeCode data = new DataTypeCode(fileType);
        analyze(file, data);
        return data;
    }

    @Override
    public void analyze(Path path, DataTypeCode data) throws IOException {
        final int defaultLines = data.getLinesTotal();
        final int defaultLinesEmpty = data.getLinesEmpty();
        final int defaultLinesComment = data.getLinesComment();

        StringBuilder builder = new StringBuilder();

        Files.lines(path).forEach(line -> {
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

        finalizeComments(data, builder);

        data.addLinesCode(
                (data.getLinesTotal() - defaultLines)
                - (data.getLinesEmpty() - defaultLinesEmpty)
                - (data.getLinesComment() - defaultLinesComment));

        data.addSizeTotal(Files.size(path));
        data.addFiles(1);
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
        CommentParser parser = new CommentParser(
                data.getFileType().getCommentStyle());
        List<String> analyze = parser.analyze(builder.toString());

        for (String string : analyze) {
            data.addLinesComment(
                    string.length() - string.replace("\n", "").length());
            data.addCharsComment(string.length());
        }
    }

}