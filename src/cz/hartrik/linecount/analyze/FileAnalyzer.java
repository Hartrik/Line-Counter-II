
package cz.hartrik.linecount.analyze;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Rozhraní pro třídy, které mají přímo na starosti analýzu určitého typu
 * souborů.
 *
 * @version 2015-09-02
 * @author Patrik Harag
 * @param <T> typ dat
 */
public interface FileAnalyzer<T extends DataTypeFile> {

    /**
     * Analyzuje soubor.
     *
     * @param file cesta k souboru, který má být analyzován
     * @param data data, ke kterým budou připojena data z této analýzy
     * @throws IOException chyba při čtení souboru
     */
    public void analyze(Path file, T data) throws IOException;

    /**
     * Analyzuje soubor.
     *
     * @param file cesta k souboru, který má být analyzován
     * @param fileType typ analyzovaného souboru
     * @return výstupní data
     * @throws IOException chyba při čtení souboru
     */
    public T analyze(Path file, FileType fileType) throws IOException;

}