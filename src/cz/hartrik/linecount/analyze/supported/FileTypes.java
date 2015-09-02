package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.common.Checker;
import cz.hartrik.linecount.analyze.CommentStyle;
import cz.hartrik.linecount.analyze.FileType;
import java.util.HashMap;
import java.util.Map;

/**
 * Definuje podporované typy souborů.
 *
 * @version 2015-09-02
 * @author Patrik Harag
 */
public enum FileTypes implements FileType {

    C("C", CommentStyles.C_LIKE, "c", "ec", "pgc"),
    C_H("C/C++ Header", CommentStyles.C_LIKE, "H", "h", "hh", "hpp"),
    C_PP("C++", CommentStyles.C_LIKE, "C", "c++", "cc", "cpp", "cxx", "pcc"),
    C_SHARP("C#", CommentStyles.C_LIKE, "cs"),
    SCALA("Scala", CommentStyles.C_LIKE, "scala"),
    GROOVY("Groovy", CommentStyles.C_LIKE, "groovy"),
    JAVA("Java", CommentStyles.JAVA, "java"),
    PASCAL("Pascal", CommentStyles.PASCAL, "dpr", "p", "pas"),
    PYTHON("Python", CommentStyles.PYTHON, "py", "python"),
    RUBY("Ruby", CommentStyles.RUBY, "rake", "rb"),
    VBS("VBScript", CommentStyles.VB, "vbs"),

    TXT("Plain text", CommentStyles.NONE, true, false, "txt", "TXT"),
    OTHER("• Ostatní", CommentStyles.NONE, false, false, "*"),

    // nové

    XML("XML", CommentStyles.XML, "xml"),
    FXML("FXML", CommentStyles.XML, "fxml"),
    HTML("HTML", CommentStyles.XML, "html", "htm"),

    CSS("CSS", CommentStyles.CSS, "css"),

    KOTLIN("Kotlin", CommentStyles.C_LIKE, "kt"),
    JS("JavaScript", CommentStyles.C_LIKE, "js"),
    PHP("PHP", CommentStyles.PHP, "php"),
    OBJECTIVE_C("Objective-C", CommentStyles.C_LIKE, "m"),
    VB("Visual Basic", CommentStyles.VB, "vb"),
    LISP("Lisp", CommentStyles.LISP, "lisp", "cl"),
    CLOJURE("Clojure", CommentStyles.LISP, "clj"),
    ERLANG("Erlang", CommentStyles.ERLANG, "erl"),
    LUA("Lua", CommentStyles.LUA, "lua"),

    PROPERTIES("Properties", CommentStyles.NONE, true, false, "properties"),
    ;

    // vyhledávání

    private static final Map<String, FileType> map = new HashMap<>();
    static {
        for (FileType type : values())
            for (String ext : type.getExtensions())
                map.put(ext, type);
    }

    public static FileType getByExtension(String extension) {
        if (extension == null || extension.isEmpty())
            return OTHER;

        return map.getOrDefault(extension, OTHER);
    }

    public static String[] getAllFileExtensions() {
        return map.keySet().toArray(new String[0]);
    }

    // instance

    private final String name;
    private final String[] extensions;
    private final CommentStyle commentStyle;
    private final boolean textDocument;
    private final boolean sourceCode;

    @SafeVarargs
    private FileTypes(String name, CommentStyle commentStyle,
            String... extensions) {

        this(name, commentStyle, true, true, extensions);
    }

    @SafeVarargs
    private FileTypes(String name, CommentStyle commentStyle,
            boolean textDocument, boolean sourceCode, String... extensions) {

        this.name = Checker.requireNonEmpty(name);
        this.extensions = Checker.requireNonEmpty(extensions);
        this.commentStyle = Checker.requireNonNull(commentStyle);
        this.textDocument = textDocument;
        this.sourceCode = sourceCode;
    }

    @Override
    public boolean containsExtension(String extension) {
        for (String string : extensions) {
            if (extension.equals(string))
                return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getExtensions() {
        return extensions;
    }

    @Override
    public CommentStyle getCommentStyle() {
        return commentStyle;
    }

    @Override
    public boolean isSourceCode() {
        return sourceCode;
    }

    @Override
    public boolean isTextDocument() {
        return textDocument;
    }

}