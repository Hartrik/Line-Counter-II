
package cz.hartrik.linecount.analyze;

import cz.hartrik.linecount.analyze.load.TextLoader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.function.Function;

/**
 * Rozhraní pro třídy, které mají přímo na starosti analýzu určitého typu
 * souborů.
 *
 * @version 2015-09-07
 * @author Patrik Harag
 * @param <T> typ dat
 */
public interface FileAnalyzer<T extends DataTypeFile> {

    /**
     * Analyzuje soubor.
     *
     * @param file cesta k souboru, který má být analyzován
     * @param fileType typ analyzovaného souboru
     * @param loader zprostředkovatel načtení souboru
     * @return výstupní data
     * @throws UncheckedIOException chyba při čtení souboru
     */
    public T analyze(Path file, FileType fileType, TextLoader loader)
            throws UncheckedIOException;

    /**
     * Analyzuje soubor.
     *
     * @param <U> typ datového objektu, do kterého budou uloženy výsledky
     * @param file cesta k souboru, který má být analyzován
     * @param fileType typ analyzovaného souboru
     * @param loader zprostředkovatel načtení souboru
     * @param dataTypeProvider vytváří nové instance
     * @return výstupní data
     * @throws UncheckedIOException chyba při čtení souboru
     */
    public <U extends T> U analyze(Path file, FileType fileType, TextLoader loader,
            Function<FileType, U> dataTypeProvider)
            throws UncheckedIOException;

}