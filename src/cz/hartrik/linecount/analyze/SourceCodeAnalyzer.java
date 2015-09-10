
package cz.hartrik.linecount.analyze;

import cz.hartrik.common.Exceptions;
import cz.hartrik.common.IntPair;
import cz.hartrik.linecount.analyze.load.TextLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Analyzuje zdrojový kód.
 *
 * @version 2015-09-10
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

        countComments(data, builder);

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
        });

        return builder;
    }

    private void countComments(DataTypeCode data, StringBuilder builder) {
        CommentStyle commentStyle = data.getFileType().getCommentStyle();
        CommentParser parser = new CommentParser(commentStyle);

        for (IntPair<String> pair : parser.getIterable(builder)) {
            Integer startPos = pair.getValue();
            String comment = pair.getSecond();

            if (lineStartsWithComment(builder, startPos - 1))
                data.addLinesComment(1);

            data.addLinesComment(countLinesInComment(comment));
            data.addCharsComment(comment.length());
        }
    }

    private boolean lineStartsWithComment(CharSequence sequence, int intex) {
        if (sequence.charAt(intex + 1) == '\n')
            return true;  // regex obsahuje ^

        int i = intex;
        while (i >= 0) {
            char next = sequence.charAt(i);
            if (next == '\n')
                return true;
            else if (Character.isWhitespace(next))
                i--;
            else
                return false;
        }
        return true;
    }

    private int countLinesInComment(String comment) {
        if (comment.isEmpty()) return 0;

        final String[] arrayOfLines = comment.split("\n");
        final int arrayLength = arrayOfLines.length;

        int lines = 0;

        if (!comment.isEmpty() && comment.charAt(comment.length() - 1) == '\n') {
            // na posledním řádku komentáře není kromě ukončovací sekvence nic
            lines = 1;
        }

        // jednořádkový komentář, pokud jím řádka začínala, již je započínaý
        if (arrayLength > 1) {
            lines += 1;  // poslední řádka začná komentářem a není prázdná
                         // (kvůli sekvenci, která ukončuje komentář)

            for (int i = 1; i < arrayLength - 1; i++)
                if (!isBlank(arrayOfLines[i]))
                    lines++;
        }

        return lines;
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