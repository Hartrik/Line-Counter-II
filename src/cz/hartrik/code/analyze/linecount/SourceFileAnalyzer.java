
package cz.hartrik.code.analyze.linecount;

import cz.hartrik.code.analyze.CommentParser;
import cz.hartrik.code.analyze.CommentStyle;
import cz.hartrik.common.Pair;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Analyzuje zdrojový kód.
 * 
 * @version 2014-08-05
 * @author Patrik Harag
 */
public class SourceFileAnalyzer implements IFileAnalyzer<DataTypeSource> {

    @Override
    public void analyze(Path path, DataTypeSource data) throws IOException {
        
        StringBuilder builder = new StringBuilder();
        
        Files.lines(path).forEach(line -> {
            final int length = line.length();
            builder.append(line).append('\n');
            
            int comment = 0;
            int whitespace = 1;  // počítá se i \n
            int indent = 0; boolean isIndent = true;

            for (char character : line.toCharArray()) {
                if (Character.isWhitespace(character)) {
                    whitespace++;
                    if (isIndent) indent++;

                } else {
                    isIndent = false;
                    
                    if (comment == 0) {
                        comment = startWithComment(data, line, character);
                    }
                }
            }
            
            data.addCharsIndent(indent);
            data.addCharsTotal(length + 1);
            data.addCharsWhitespace(whitespace);
            
            data.addLinesTotal(1);
            if ((whitespace - 1) == length) data.addLinesEmpty(1);
            if (comment == 1)               data.addLinesComment(1);
        });
        
        finalizeComments(data, builder);
        
        data.addFiles(1);
        data.addSizeTotal(Files.size(path));
    }
    
    private int startWithComment(DataTypeSource data, String line,
            char character) {
        
        CommentStyle cs = data.getFileType().getCommentStyle();
        Pair<String, String>[] comments = cs.getComments();

        for (Pair<String, String> pair : comments) {
            if (pair.getFirst().charAt(0) == character
                    && line.contains(pair.getFirst())) {

                return 1;
            }
        }
        return -1;
    }
    
    private void finalizeComments(DataTypeSource data, StringBuilder builder) {
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