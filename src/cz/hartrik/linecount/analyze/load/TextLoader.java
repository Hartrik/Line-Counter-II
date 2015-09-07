package cz.hartrik.linecount.analyze.load;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Rozhraní pro objekt, který zprostředkovává načítání textu.
 *
 * @version 2015-09-06
 * @author Patrik Harag
 */
public interface TextLoader {

    /**
     * Načte text jako "proud" řádků.
     *
     * @param path cesta k souboru
     * @return proud obsahující jednotlivé řádky textu
     * @throws UncheckedIOException IO výjimka
     */
    Stream<String> load(Path path) throws UncheckedIOException;

}