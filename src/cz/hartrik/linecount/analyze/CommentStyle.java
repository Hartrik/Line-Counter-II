
package cz.hartrik.linecount.analyze;

import cz.hartrik.common.Pair;
import java.util.regex.Pattern;

/**
 * Rozhraní pro styl zapisování komentářů.
 *
 * @version 2015-09-02
 * @author Patrik Harag
 */
public interface CommentStyle {

    public Pair<Pattern, Pattern>[] getCommentPatterns();

    public Pair<Pattern, Pattern>[] getIgnorePatterns();

    public String getName();

}