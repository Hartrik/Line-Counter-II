
package cz.hartrik.code.analyze;

import java.nio.file.Path;
import java.util.Collection;

/**
 * Rozhraní pro třídu, která analyzuje soubory.
 *
 * @version 2014-08-05
 * @author Patrik Harag
 */
public interface IStats {

    public void analyze(Path... paths);

    public void analyze(Collection<Path> paths);

}