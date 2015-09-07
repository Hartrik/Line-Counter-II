
package cz.hartrik.linecount.analyze;

import cz.hartrik.common.Exceptions;
import cz.hartrik.linecount.analyze.load.TextLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Analyzuje textový soubor.
 *
 * @version 2015-09-07
 * @author Patrik Harag
 */
public class TextFileAnalyzer implements FileAnalyzer<DataTypeText> {

    static final Function<FileType, DataTypeText> DEFAULT_DATA_PROVIDER
            = DataTypeText::new;

    @Override
    public DataTypeText analyze(Path file, FileType type, TextLoader loader) {
        return analyze(file, type, loader, DEFAULT_DATA_PROVIDER);
    }

    @Override
    public <U extends DataTypeText> U analyze(Path file, FileType type,
            TextLoader loader, Function<FileType, U> dataProvider) {

        U data = dataProvider.apply(type);

        try (Stream<String> lines = loader.load(file)) {
            lines.forEach(line -> {
                data.addLinesTotal(1);
                data.addCharsTotal(line.length());
                data.addCharsWhitespace(countWhitespace(line) + 1); // včetně \n
            });
        }

        // pokud soubor nepůjde načíst, tak se ani nezapočítá
        data.addSizeTotal(Exceptions.uncheckedApply(Files::size, file));
        data.addFiles(1);

        return data;
    }

    private int countWhitespace(String string) {
        int whitespace = 0;
        for (char character : string.toCharArray())
            if (Character.isWhitespace(character)) whitespace++;

        return whitespace;
    }

}