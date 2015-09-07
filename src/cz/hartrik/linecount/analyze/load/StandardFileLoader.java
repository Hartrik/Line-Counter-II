package cz.hartrik.linecount.analyze.load;

import cz.hartrik.common.Exceptions;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @version 2015-09-06
 * @author Patrik Harag
 */
public class StandardFileLoader implements TextLoader {

    private final Charset charset;

    public StandardFileLoader(Charset charset) {
        this.charset = charset;
    }

    @Override
    public Stream<String> load(Path path) {
        return Exceptions.uncheckedApply(this::loadChecked, path);
    }

    public Stream<String> loadChecked(Path path) throws IOException {
        return Files.lines(path, charset);
    }

}