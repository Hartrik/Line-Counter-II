package cz.hartrik.linecount.analyze;

/**
 * @version 2015-09-02
 * @author Patrik Harag
 */
public interface FileType {

    boolean containsExtension(String extension);

    CommentStyle getCommentStyle();

    String[] getExtensions();

    String getName();

    boolean isSourceCode();

    boolean isTextDocument();

}
