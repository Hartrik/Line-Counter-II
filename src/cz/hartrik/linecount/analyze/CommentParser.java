
package cz.hartrik.linecount.analyze;

import cz.hartrik.common.IntPair;
import cz.hartrik.common.Iterators;
import cz.hartrik.common.Pair;
import cz.hartrik.common.Streams;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parsuje komentáře ze zdrojového kódu.
 * Každá instance této třídy by měla být použita jen jednou.
 *
 * @version 2015-09-10
 * @author Patrik Harag
 */
public class CommentParser {

    private final Pair<Pattern, Pattern>[] comments;
    private final Pair<Pattern, Pattern>[] ignore;

    private int index = 0;
    private String nextResult = null;
    private int nextStart = -1;

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

        List<String> results = new ArrayList<>();

        for (IntPair<String> pair : getIterable(source))
            results.add(pair.getSecond());

        return results;
    }

    public Iterable<IntPair<String>> getIterable(StringBuilder source) {
        return Iterators.wrap(getIterator(source));
    }

    public Iterator<IntPair<String>> getIterator(StringBuilder source) {
        index = 0;
        nextResult = null;

        return new Iterator<IntPair<String>>() {

            @Override
            public boolean hasNext() {
                if (nextResult != null)
                    return true;

                if (index == source.length())
                    return false;

                while (nextResult == null && index < source.length())
                    findNext(source);

                return nextResult != null;
            }

            @Override
            public IntPair<String> next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                IntPair<String> pair = IntPair.of(nextStart, nextResult);
                nextResult = null;
                return pair;
            }
        };
    }

    protected void findNext(StringBuilder source) {
        final SearchResult nextComment = getNextStart(source, comments, index);
        final SearchResult nextIgnore = getNextStart(source, ignore, index);

        if (nextComment != null) {

            if (nextIgnore != null && nextIgnore.start < nextComment.start) {
                // komentář je ignorován - např. uvnitř řetězce

                // hledání konce oblasti, ve které jsou ignorovány komentáře
                Matcher m = nextIgnore.pair.getSecond().matcher(source);
                if (m.find(nextIgnore.end)) {
                    index = m.end();

                } else {
                    // ignorováno až do konce
                    nextResult = null;
                    index = source.length();
                }

            } else {

                // nalezení konce komentáře
                Matcher m = nextComment.pair.getSecond().matcher(source);
                if (m.find(nextComment.end)) {
                    nextResult = source.substring(nextComment.end, m.start());
                    nextStart = nextComment.start;
                    index = m.end();

                } else {
                    // komentář až do konce
                    isContinue = true;
                    nextResult = source.substring(nextComment.end);
                    nextStart = nextComment.start;
                    index = source.length();
                }
            }
        } else {
            // nenalezeny další komentáře
            index = source.length();
        }
    }

    /**
     * Vrátí nejbližší výskyt určitého vzoru znaků.
     *
     * @param text text k prohledání
     * @param patterns regulární výrazy
     * @param start počátek hledání
     * @return výsledky hledání, {@code null} pokud nebylo nic nalezeno
     */
    protected SearchResult getNextStart(
            StringBuilder text, Pair<Pattern, Pattern>[] patterns, int start) {

        if (patterns.length == 0)
            return null;

        return Streams.stream(patterns)
                .map(commentType -> {
                    Matcher m = commentType.getFirst().matcher(text);
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