package cz.hartrik.linecount.analyze;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Stará se o filtrování souborů.
 *
 * @version 2016-05-18
 * @author Patrik Harag
 */
public class FileFilter {

    private final Predicate<Path> filter;
    private final BiConsumer<Path, IOException> errConsumer;

    private int passed;
    private int failed;

    public FileFilter(Predicate<Path> filter) {
        this(filter, (path, e) -> {});
    }

    public FileFilter(
            Predicate<Path> filter, BiConsumer<Path, IOException> errConsumer) {

        this.filter = filter;
        this.errConsumer = errConsumer;
    }

    // ---

    /**
     * Rekurzivně navštíví všechny složky a soubory a ty, které projdou testem
     * uloží do množiny.
     *
     * @param paths cesty k souborům nebo složkám
     * @return filtrovaná množina souborů
     */
    public Set<Path> filter(Collection<Path> paths) {
        Set<Path> filtered = new LinkedHashSet<>();

        for (Path path : paths) {
            try {
                Files.walk(path).forEach((nextPath) -> {
                    nextPath = nextPath.toAbsolutePath();

                    if (test(nextPath))
                        filtered.add(nextPath);
                });
            } catch (IOException e) {
                errConsumer.accept(path, e);
            }
        }

        return filtered;
    }

    private boolean test(Path path) {
        if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS))
            return false;

        boolean result = filter.test(path);

        if (result)
            passed++;
        else
            failed++;

        return result;
    }

    /**
     * Vrátí počet souborů, které prošly testem.
     *
     * @return počet souborů
     */
    public int getPassed() {
        return passed;
    }

    /**
     * Vrátí počet souborů, které neprošly testem.
     *
     * @return počet souborů
     */
    public int getFailed() {
        return failed;
    }

}