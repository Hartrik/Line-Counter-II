package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.common.Pair;
import cz.hartrik.linecount.analyze.CommentStyle;
import java.lang.reflect.Array;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @version 2015-09-04
 * @author Patrik Harag
 */
public class CommentStyleImpl implements CommentStyle {

    private final String name;
    private final Pair<Pattern, Pattern>[] commentPatterns;
    private final Pair<Pattern, Pattern>[] ignorePatterns;

    public CommentStyleImpl(
            String name,
            List<Pair<Pattern, Pattern>> commentPatterns,
            List<Pair<Pattern, Pattern>> ignorePatterns) {

        this.name = name;
        this.commentPatterns = commentPatterns.stream().toArray(this::createArray);
        this.ignorePatterns = ignorePatterns.stream().toArray(this::createArray);
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

    @SuppressWarnings("unchecked")
    private Pair<Pattern, Pattern>[] createArray(int size) {
        return (Pair<Pattern, Pattern>[]) Array.newInstance(Pair.class, size);
    }

}