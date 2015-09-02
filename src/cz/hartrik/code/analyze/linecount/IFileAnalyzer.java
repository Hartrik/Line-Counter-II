
package cz.hartrik.code.analyze.linecount;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Rozhraní pro třídy, které mají přímo na starosti analýzu určitého typu
 * souborů.
 *
 * @version 2014-08-05
 * @author Patrik Harag
 * @param <T> typ dat
 */
public interface IFileAnalyzer<T extends DataTypeFile> {

    public void analyze(Path path, T data) throws IOException;

}