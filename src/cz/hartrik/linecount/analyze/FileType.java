package cz.hartrik.linecount.analyze;

import java.util.List;
import java.util.function.Predicate;

/**
 * @version 2015-09-04
 * @author Patrik Harag
 */
public interface FileType {

    CommentStyle getCommentStyle();

    List<Predicate<String>> getFilters();

    String getName();

    boolean isSourceCode();

    boolean isTextDocument();

    boolean matches(String fileName);

}