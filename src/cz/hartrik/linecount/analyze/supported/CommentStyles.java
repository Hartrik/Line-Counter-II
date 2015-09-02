package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.common.Pair;
import cz.hartrik.linecount.analyze.CommentStyle;

/**
 * Definuje různé styly zapisování komentářů.
 *
 * @version 2015-09-02
 * @author Patrik Harag
 */
public enum CommentStyles implements CommentStyle {

    // pořadí parametrů může mít vliv!
    //  - pokud řetězce začínají stejně (/*, /**)

    NONE(),

    C_LIKE(pair("//", "\n"), pair("/*", "*/")),
    JAVA(pair("//", "\n"), pair("/**", "*/"), pair("/*", "*/")),
    PYTHON(pair("#", "\n"), pair("'''", "'''")),
    RUBY(pair("#", "\n"), pair("=begin", "=end")),
    VB(pair("'", "\n"), pair("REM ", "\n"), pair("Rem ", "\n"), pair("rem ", "\n")),
    PASCAL(pair("//", "\n"), pair("(*", "*)"), pair("{", "}")),

    XML(pair("<!--", "-->")),
    CSS(pair("/*", "*/")),
    PHP(pair("#", "\n"), pair("//", "\n"), pair("/*", "*/")),
    LISP(pair(";;;;", "\n"), pair(";;;", "\n"), pair(";;", "\n"), pair(";", "\n")),
    ERLANG(pair("%", "\n")),
    LUA(pair("--[[", "--]]"), pair("--", "\n")),
    ;

    @SafeVarargs
    private CommentStyles(Pair<String, String>... commentTypes) {
        this.comments = commentTypes;
    }

    private final Pair<String, String>[] comments;

    @Override
    public Pair<String, String>[] getComments() {
        return comments;
    }

    // StringPair

    private static Pair<String, String> pair(String s1, String s2) {
        return Pair.ofNonNull(s1, s2);
    }

}