
package cz.hartrik.linecount.analyze;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Analyzuje textový soubor.
 *
 * @version 2015-09-02
 * @author Patrik Harag
 */
public class TextFileAnalyzer implements FileAnalyzer<DataTypeText> {

    @Override
    public DataTypeText analyze(Path file, FileType fileType) throws IOException {
        DataTypeText data = new DataTypeText(fileType);
        analyze(file, data);
        return data;
    }

    @Override
    public void analyze(Path path, DataTypeText data) throws IOException {

        Files.lines(path).forEach(line -> {
            data.addLinesTotal(1);
            data.addCharsTotal(line.length());
            data.addCharsWhitespace(countWhitespace(line) + 1); // včetně \n
        });

        // pokud soubor nepůjde načíst, tak se ani nezapočítá
        data.addFiles(1);
        data.addSizeTotal(Files.size(path));
    }

    protected static int countWhitespace(String string) {
        int whitespace = 0;
        for (char character : string.toCharArray())
            if (Character.isWhitespace(character)) whitespace++;

        return whitespace;
    }

}