
package cz.hartrik.linecount.analyze;

import cz.hartrik.common.Pair;
import cz.hartrik.common.Streams;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parsuje komentáře ze zdrojového kódu.
 *
 * @version 2015-09-03
 * @author Patrik Harag
 */
public class CommentParser {

    private final Pair<Pattern, Pattern>[] comments;
    private final Pair<Pattern, Pattern>[] ignore;

    private boolean isContinue;

    /**
     * Vytvoří novou instanci.
     *
     * @param commentStyle typ komentářů
     */
    public CommentParser(CommentStyle commentStyle) {
        this.comments = commentStyle.getCommentPatterns();
        this.ignore = commentStyle.getIgnorePatterns();
    }

    /**
     * Analyzuje zdrojový kód a vrátí seznam komentářů v něj obsažených.
     * Nepodporuje vnořené komentáře (Pascal).
     *
     * @param source zdrojový kód
     * @return seznam obsahů komentářů
     */
    public List<String> analyze(String source) {
        return analyze(new StringBuilder(source));
    }

    /**
     * Analyzuje zdrojový kód a vrátí seznam komentářů v něj obsažených.
     * Nepodporuje vnořené komentáře (Pascal).
     *
     * @param source zdrojový kód
     * @return seznam obsahů komentářů
     */
    public List<String> analyze(StringBuilder source) {
        isContinue = false;
        List<String> content = new ArrayList<>();

        while (true) {
            final SearchResult nextComment = getNextStart(source, comments, 0);
            final SearchResult nextIgnore = getNextStart(source, ignore, 0);

            if (nextComment != null) {

                // ! ošetřit situaci, když jsou stejně "blízko"
                //    - např. mohou mít stejný začátek

                if (nextIgnore != null && nextIgnore.start < nextComment.start) {
                    // komentář je ignorován - např. uvnitř řetězce

                    // hledání konce oblasti, ve které jsou ignorovány komentáře
                    Matcher m = nextIgnore.pair.getSecond().matcher(source);
                    if (m.find(nextIgnore.end))
                        source.delete(0, m.end());
                    else
                        return content;  // ignorováno až do konce

                } else {

                    // nalezení konce komentáře
                    Matcher m = nextComment.pair.getSecond().matcher(source);
                    if (m.find(nextComment.end)) {
                        content.add(source.substring(nextComment.end, m.start()));
                        source.delete(0, m.end());

                    } else {
                        // komentář až do konce
                        isContinue = true;

                        content.add(source.substring(nextComment.end));
                        return content;
                    }
                }
            } else {
                // nenalezeny další komentáře
                return content;
            }
        }
    }

    /**
     * Vrátí nejbližší výskyt
     *
     * @param source
     * @param patterns
     * @param start
     * @return
     */
    protected SearchResult getNextStart(
            StringBuilder source, Pair<Pattern, Pattern>[] patterns, int start) {

        return Streams.stream(patterns)
                .map(commentType -> {
                    Matcher m = commentType.getFirst().matcher(source);
                    return (m.find(start))
                            ? new SearchResult(m.start(), m.end(), commentType)
                            : null;
                })
                .filter(Objects::nonNull)
                .min((r1, r2) -> Integer.compare(r1.start, r2.start))
                    .orElse(null);
    }

    /**
     * Informuje o tom, zda by poslední komentář pokračoval i za konec řetězce.
     * Tedy pokud nebylo nalezeno zakončení pro poslední blokový komentář.
     *
     * @return komentář by pokračoval
     */
    public boolean isContinue() {
        return isContinue;
    }

    protected static class SearchResult {
        protected SearchResult(int start, int end, Pair<Pattern, Pattern> pair) {
            this.start = start;
            this.end = end;
            this.pair = pair;
        }

        protected final int start, end;
        protected final Pair<Pattern, Pattern> pair;
    }

}