package cz.hartrik.linecount.analyze;

import cz.hartrik.common.Pair;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @version 2015-09-05
 * @author Patrik Harag
 */
class CommentStyleImpl implements CommentStyle {

    private final String name;
    private final Pair<Pattern, Pattern>[] commentPatterns;
    private final Pair<Pattern, Pattern>[] ignorePatterns;

    CommentStyleImpl(
            String name,
            List<Pair<Pattern, Pattern>> commentPatterns,
            List<Pair<Pattern, Pattern>> ignorePatterns) {

        this(name, toArray(commentPatterns), toArray(ignorePatterns));
    }

    CommentStyleImpl(
            String name,
            Pair<Pattern, Pattern>[] commentPatterns,
            Pair<Pattern, Pattern>[] ignorePatterns) {

        this.name = name;
        this.commentPatterns = commentPatterns;
        this.ignorePatterns = ignorePatterns;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Pair<Pattern, Pattern>[] getCommentPatterns() {
        return commentPatterns;
    }

    @Override
    public Pair<Pattern, Pattern>[] getIgnorePatterns() {
        return ignorePatterns;
    }

    // pomocné statické metody

    private static Pair<Pattern, Pattern>[] toArray(
            Collection<Pair<Pattern, Pattern>> coll) {

        return coll.stream().toArray(CommentStyleImpl::createArray);
    }

    @SuppressWarnings("unchecked")
    private static Pair<Pattern, Pattern>[] createArray(int size) {
        return (Pair<Pattern, Pattern>[]) Array.newInstance(Pair.class, size);
    }

}