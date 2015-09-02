
package cz.hartrik.code.analyze;

import cz.hartrik.common.Pair;

/**
 * Definuje různé styly zapisování komentářů.
 *
 * @version 2014-09-02
 * @author Patrik Harag
 */
public enum CommentStyle {

    // pořadí parametrů může mít vliv!

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
    private CommentStyle(Pair<String, String>... commentTypes) {
        this.comments = commentTypes;
    }

    private final Pair<String, String>[] comments;

    public Pair<String, String>[] getComments() {
        return comments;
    }

    // StringPair

    private static Pair<String, String> pair(String s1, String s2) {
        return Pair.ofNonNull(s1, s2);
    }

}