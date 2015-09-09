package cz.hartrik.linecount.analyze;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Stará se o filtrování souborů.
 *
 * @version 2015-09-09
 * @author Patrik Harag
 */
public class FileFilter {

    private final Predicate<Path> filter;
    private final BiConsumer<Path, IOException> errConsumer;

    public FileFilter(Predicate<Path> filter) {
        this(filter, (path, e) -> {});
    }

    public FileFilter(
            Predicate<Path> filter, BiConsumer<Path, IOException> errConsumer) {

        this.filter = filter;
        this.errConsumer = errConsumer;
    }

    // ---

    public Set<Path> filter(Path... paths) {
        return filter(Arrays.asList(paths));
    }

    /**
     * Rekurzivně navštíví všechny složky a soubory a ty, které projdou testem
     * uloží do množiny.
     *
     * @param paths cesty k souborům nebo složkám
     * @return filtrovaná množina složek a souborů
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
        return filter.test(path);
    }

}