package cz.hartrik.linecount.analyze;

import java.util.List;
import java.util.function.Predicate;

/**
 * Rozhraní pro typ souboru.
 *
 * @version 2016-02-28
 * @author Patrik Harag
 */
public interface FileType {

    /**
     * Vrátí styl komentářů.
     *
     * @return styl komentářů
     */
    CommentStyle getCommentStyle();

    /**
     * Vrátí neměnný seznam filtrů, které testují, jestli název souboru
     * odpovídá tomuto typu souboru.
     *
     * @see #matches(String)
     * @return seznam filtrů
     */
    List<Predicate<String>> getFilters();

    /**
     * Vrátí název tohoto typu souboru. <p>
     * Např.: Java, Plain text, XML...
     *
     * @return název typu souboru
     */
    String getName();

    /**
     * Vrátí informaci o druhu tohoto typu souboru.
     *
     * @see #isTextDocument()
     * @see #isSourceCode()
     * @return DataType
     */
    DataType getDataType();

    /**
     * Vrátí {@code true}, pokud je tento typ souboru považován za zdrojový
     * kód. To bude mít za následek počítání komentářů atd...
     *
     * @see SourceCodeAnalyzer
     * @return boolean
     */
    boolean isSourceCode();

    /**
     * Vrátí {@code true}, pokud je tento typ souboru považován za textový
     * dokument. To bude mít za následek počítání řádků atd...
     *
     * @see TextFileAnalyzer
     * @see SourceCodeAnalyzer
     * @return boolean
     */
    boolean isTextDocument();

    /**
     * Otestuje, jestli název souboru odpovídá některému z filtrů.
     *
     * @param fileName název souboru
     * @return boolean
     */
    boolean matches(String fileName);

    // -----------

    public static enum DataType {
        UNKNOWN, TEXT, SOURCE;
    }

    /**
     * Vytvoří nový typ souboru.
     *
     * @param name jméno
     * @param dataType typ dat, které tento typ souboru obsahuje
     * @param commentStyle styl komentářů
     * @param filters filtry
     * @return nová instance
     */
    public static FileType of(String name, DataType dataType,
            CommentStyle commentStyle, List<Predicate<String>> filters) {

        return new FileTypeImpl(name, dataType, commentStyle, filters);
    }

}