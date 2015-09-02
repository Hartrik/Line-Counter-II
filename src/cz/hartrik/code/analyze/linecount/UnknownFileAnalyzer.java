
package cz.hartrik.code.analyze.linecount;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Analyzuje soubory neznámého typu.
 * 
 * @version 2014-08-05
 * @author Patrik Harag
 */
public class UnknownFileAnalyzer implements IFileAnalyzer<DataTypeFile> {

    @Override
    public void analyze(Path path, DataTypeFile data) throws IOException {
        data.addFiles(1);
        data.addSizeTotal(Files.size(path));
    }
    
}