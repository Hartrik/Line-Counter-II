
package cz.hartrik.linecount.analyze;

import cz.hartrik.common.Pair;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Rozhraní pro styl zapisování komentářů.
 *
 * @version 2015-09-05
 * @author Patrik Harag
 */
public interface CommentStyle {

    /**
     * Vrátí pole obsahující páry regulárních výrazů, které definují začátek
     * a konec komentáře.
     *
     * @return pole s páry regulárních výrazů
     */
    public Pair<Pattern, Pattern>[] getCommentPatterns();

    /**
     * Vrátí pole obsahující páry regulárních výrazů, které definují začátek
     * a konec oblasti, ve které mají být komentáře ignorovány.
     *
     * @return pole s páry regulárních výrazů
     */
    public Pair<Pattern, Pattern>[] getIgnorePatterns();

    /**
     * Název tohoto stylu komentářů.
     *
     * @return název
     */
    public String getName();

    /**
     * Vytvoří nový styl komentáře.
     *
     * @param name název tohoto stylu
     * @param commentPatterns seznam obsahující páry regulárních výrazů, které
     *        definují začátek a konec komentáře
     * @param ignorePatterns seznam obsahující páry regulárních výrazů, které
     *        definují začátek a konec oblasti, ve které mají být
     *        komentáře ignorovány
     * @return nová instance
     */
    public static CommentStyle of(
            String name,
            List<Pair<Pattern, Pattern>> commentPatterns,
            List<Pair<Pattern, Pattern>> ignorePatterns) {

        return new CommentStyleImpl(name, commentPatterns, ignorePatterns);
    }

}