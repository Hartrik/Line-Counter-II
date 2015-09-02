
package cz.hartrik.linecount.analyze;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Analyzuje soubory neznámého typu.
 *
 * @version 2015-09-02
 * @author Patrik Harag
 */
public class UnknownFileAnalyzer implements FileAnalyzer<DataTypeFile> {

    @Override
    public DataTypeFile analyze(Path file, FileType fileType) throws IOException {
        DataTypeFile data = new DataTypeFile(fileType);
        analyze(file, data);
        return data;
    }

    @Override
    public void analyze(Path path, DataTypeFile data) throws IOException {
        data.addFiles(1);
        data.addSizeTotal(Files.size(path));
    }

}