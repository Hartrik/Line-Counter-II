package cz.hartrik.linecount.analyze.load;

import cz.hartrik.common.Exceptions;
import cz.hartrik.common.io.Resources;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Poskytuje tovární metody pro tvorbu {@link TextLoader}.
 *
 * @version 2015-09-07
 * @author Patrik Harag
 */
public class TextLoaders {

    private TextLoaders() {}

    static final String CHARSET_LIST = "charsets.txt";

    private static GuessFileLoader defaultGuessLoader;

    private static GuessFileLoader createDefaultGuessLoader() {
        Charset[] charsets = Resources
                .lines(CHARSET_LIST, TextLoaders.class, Charset.forName("UTF-8"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> Exceptions.call(Charset::forName, line).orElse(null))
                .filter(Objects::nonNull)
                .toArray(Charset[]::new);

        return new GuessFileLoader(charsets);
    }

    // ---

    public static synchronized GuessFileLoader getDefaultGuessLoader() {
        if (defaultGuessLoader == null)
            defaultGuessLoader = createDefaultGuessLoader();

        return defaultGuessLoader;
    }

    public static TextLoader standardTextLoader(String charset) {
        return standardTextLoader(Charset.forName(charset));
    }

    public static TextLoader standardTextLoader(Charset charset) {
        return new StandardFileLoader(charset);
    }

    public static GuessFileLoader guessLoader(Charset... charsets) {
        return new GuessFileLoader(charsets);
    }

}