
package cz.hartrik.code.analyze;

import cz.hartrik.common.Checker;
import java.util.HashMap;
import java.util.Map;

/**
 * Definuje podporované typy souborů.
 * 
 * @version 2014-08-05
 * @author Patrik Harag
 */
public enum FileType {
    
    // typy
    
    C("C", CommentStyle.C_LIKE, "c", "ec", "pgc"),
    C_H("C/C++ Header", CommentStyle.C_LIKE, "H", "h", "hh", "hpp"),
    C_PP("C++", CommentStyle.C_LIKE, "C", "c++", "cc", "cpp", "cxx", "pcc"),
    C_SHARP("C#", CommentStyle.C_LIKE, "cs"),
    SCALA("Scala", CommentStyle.C_LIKE, "scala"),
    GROOVY("Groovy", CommentStyle.C_LIKE, "groovy"),
    
    JAVA("Java", CommentStyle.JAVA, "java"),
    PASCAL("Pascal", CommentStyle.PASCAL, "dpr", "p", "pas"),
    PYTHON("Python", CommentStyle.PYTHON, "py", "python"),
    RUBY("Ruby", CommentStyle.RUBY, "rake", "rb"),
    VBS("VBScript", CommentStyle.VBS, "vbs"),
    
    TXT("* Textové dokumenty", CommentStyle.NONE, true, false, "txt", "TXT"),
    OTHER("* Ostatní", CommentStyle.NONE, false, false, "*");
    
    // vyhledávání
    
    private static final Map<String, FileType> map = new HashMap<>();
    static {
        for (FileType type : FileType.values())
            for (String ext : type.getExtensions())
                map.put(ext, type);
    }
    
    public static FileType getByExtension(String extension) {
        if (extension == null || extension.isEmpty())
            return OTHER;
        
        return map.getOrDefault(extension, OTHER);
    }
    
    // instance
    
    private final String name;
    private final String[] extensions;
    private final CommentStyle commentStyle;
    private final boolean textDocument;
    private final boolean sourceCode;
    
    @SafeVarargs
    private FileType(String name, CommentStyle commentStyle,
            String... extensions) {
        
        this(name, commentStyle, true, true, extensions);
    }
    
    @SafeVarargs
    private FileType(String name, CommentStyle commentStyle,
            boolean textDocument, boolean sourceCode, String... extensions) {
        
        this.name = Checker.requireNonEmpty(name);
        this.extensions = Checker.requireNonEmpty(extensions);
        this.commentStyle = Checker.requireNonNull(commentStyle);
        this.textDocument = textDocument;
        this.sourceCode = sourceCode;
    }
    
    public boolean containsExtension(String extension) {
        for (String string : extensions) {
            if (extension.equalsIgnoreCase(string))
                return true;
        }
        return false;
    }
    
    public String getName() {
        return name;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public CommentStyle getCommentStyle() {
        return commentStyle;
    }
    
    public boolean isSourceCode() {
        return sourceCode;
    }

    public boolean isTextDocument() {
        return textDocument;
    }
    
}