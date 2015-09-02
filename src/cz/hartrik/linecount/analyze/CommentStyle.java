
package cz.hartrik.linecount.analyze;

import cz.hartrik.common.Pair;

/**
 * Rozhraní pro styl zapisování komentářů.
 *
 * @version 2015-09-02
 * @author Patrik Harag
 */
public interface CommentStyle {

    /**
     * Vrátí pole, které obsahuje všechny typy komentářů.
     *
     * @return pole s páry, které obsahují otevírající a uzavírající sekvence
     */
    public Pair<String, String>[] getComments();

}