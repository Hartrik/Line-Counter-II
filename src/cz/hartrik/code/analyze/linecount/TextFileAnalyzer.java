
package cz.hartrik.code.analyze.linecount;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Analyzuje textový soubor.
 * 
 * @version 2014-08-05
 * @author Patrik Harag
 */
public class TextFileAnalyzer implements IFileAnalyzer<DataTypeText> {

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