package cz.hartrik.linecount.analyze.load;

import cz.hartrik.common.Checker;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Bude zkoušet různé znakové sady, dokud se nepodaří textový soubor načíst.
 *
 * @version 2015-09-07
 * @author Patrik Harag
 */
public class GuessFileLoader implements TextLoader{

    private final Charset[] charsets;

    public GuessFileLoader(Charset[] charsets) {
        this.charsets = Checker.requireNonEmpty(charsets);
    }

    /**
     * Bude se snažit načíst celý soubor do paměti, a když se mu to s některým
     * kódováním podaří bez chyby, tak vrátí {@code Stream}.
     * Tato metoda je neefektivní pro velké soubory.
     *
     * @param path cesta k souboru
     * @return proud obsahující jednotlivé řádky
     * @throws UncheckedIOException neznámé kódování nebo jiná IO výjimka
     */
    @Override
    public Stream<String> load(Path path) {
        for (Charset charset : charsets) {
            try {
                return loadWithBuffer(path, charset);

            } catch (UncheckedIOException e) {
                if (e.getCause() instanceof MalformedInputException)
                    ; // zkusit další
                else
                    throw e;

            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
                // chyba by se opakovala i při příštích pokusech
            }
        }

        throw new UncheckedIOException(new IOException("Unknown charset!"));
    }

    private Stream<String> loadWithBuffer(Path path, Charset charset)
            throws IOException {

        List<String> buffer = new ArrayList<>();

        try (Stream<String> lines = Files.lines(path, charset)) {
            lines.forEach(buffer::add);
        }

        return buffer.stream();
    }

    /**
     * Metoda bude vytvářet {@link TextLoader} pro každé kódování a předávat ho
     * funkci, která se bude pokoušet udělat svoji práci. <p>
     *
     * Pokud funkce vrátí výsledek bez vyhození {@link MalformedInputException}
     * (resp. její zabalené varianty), je tento výsledek vrácen.
     * Jiné výjimky způsobené uvnitř funkce nejsou zachytávány. <p>
     *
     * Pokud žádné kódování neodpovídá, bude {@code Optional} prázdný.
     *
     * @param <T> typ výstupu
     * @param function funkce, která zpracovává textový vstup;
     *        může být volána opakovaně
     * @return Optional
     * @throws UncheckedIOException jiná IO výjimka
     * @throws NullPointerException funkce vrátí {@code null}
     */
    public <T> Optional<T> load(Function<TextLoader, T> function)
            throws UncheckedIOException {

        for (Charset charset : charsets) {
            try {
                TextLoader loader = new StandardFileLoader(charset);
                T result = function.apply(loader);
                return Optional.of(result);

            } catch (UncheckedIOException e) {
                if (e.getCause() instanceof MalformedInputException)
                    ; // zkusit další
                else
                    throw e; // chyba je způsobena něčím jiným než kódováním
            }
        }
        return Optional.empty();
    }

    public Charset[] getCharsets() {
        return charsets;
    }

}