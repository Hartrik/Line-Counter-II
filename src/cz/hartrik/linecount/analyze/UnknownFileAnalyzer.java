
package cz.hartrik.linecount.analyze;

import cz.hartrik.common.Exceptions;
import cz.hartrik.linecount.analyze.load.TextLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

/**
 * Analyzuje soubory neznámého typu.
 *
 * @version 2015-09-07
 * @author Patrik Harag
 */
public class UnknownFileAnalyzer implements FileAnalyzer<DataTypeFile> {

    static final Function<FileType, DataTypeFile> DEFAULT_DATA_PROVIDER
            = DataTypeFile::new;

    @Override
    public DataTypeFile analyze(Path file, FileType type, TextLoader loader) {
        return analyze(file, type, loader, DEFAULT_DATA_PROVIDER);
    }

    @Override
    public <U extends DataTypeFile> U analyze(Path file, FileType type,
            TextLoader loader, Function<FileType, U> dataProvider) {

        U data = dataProvider.apply(type);

        data.addSizeTotal(Exceptions.uncheckedApply(Files::size, file));
        data.addFiles(1);

        return data;
    }

}