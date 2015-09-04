package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.common.Exceptions;
import cz.hartrik.linecount.analyze.CommentStyle;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Spravuje podporované styly zapisování komentářů.
 *
 * @version 2015-09-04
 * @author Patrik Harag
 */
public class CommentStyles {

    private CommentStyles() { }

    static final String DEFAULT_STYLES_FILE_NAME = "comment styles.xml";

    /** Žádné komentáře. */
    public static final CommentStyle NONE = new CommentStyleImpl(
            "NONE", Collections.emptyList(), Collections.emptyList());

    private static final Map<String, CommentStyle> styles = new LinkedHashMap<>();

    /**
     * Načte výchozí, vestavěné styly komentářů.
     */
    public static void initDefaultStyles() {
        initStyles(CommentStyles.class.getResourceAsStream(DEFAULT_STYLES_FILE_NAME));
    }

    /**
     * Načte nové styly komentářů. Pokud bude mít některý z nových stylů stejný
     * název, jako některý z aktuálních, aktuální styl se přepíše.
     *
     * @param in vstupní proud obsahující validní XML
     */
    public static void initStyles(InputStream in) {
        Exceptions.unchecked(() -> {
            CommentStylesXMLParser parser = new CommentStylesXMLParser();

            parser.parse(in).stream()
                    .forEach(style -> styles.put(style.getName(), style));
        });
    }

    /**
     * Vrátí styl komentářů podle jména. Case sensitive!
     *
     * @param name název stylu komentářů
     * @return styl komentářů, {@link null}, pokud není nalezen
     */
    public static CommentStyle getByName(String name) {
        return styles.get(name);
    }

    /**
     * Vrátí neměnný slovník všech stylů komentářů.
     * Klíčem ve slovníku je název.
     *
     * @return styly komentářů
     */
    public static Map<String, CommentStyle> getStyles() {
        return Collections.unmodifiableMap(styles);
    }

}