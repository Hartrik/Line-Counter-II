
package cz.hartrik.linecount.analyze;

import java.util.function.Consumer;

/**
 * Tento consumer jednoduše skládá řetězce dohromady. Za každý přidaný řetězec
 * vloží <code>/n</code>.
 *
 * @version 2014-08-05
 * @author Patrik Harag
 */
public class SimpleStringConsumer implements Consumer<String> {

    private final StringBuilder stringBuilder;

    /**
     * Při tvorbě instance vytvoří i nový {@link StringBuilder}.
     */
    public SimpleStringConsumer() {
        this(new StringBuilder());
    }

    /**
     * Při tvorbě instance vytvoří i nový {@link StringBuilder}.
     *
     * @param initial počáteční řetězec
     */
    public SimpleStringConsumer(String initial) {
        this(new StringBuilder(initial));
    }

    /**
     * Použije stávající {@link StringBuilder}.
     *
     * @param stringBuilder {@link StringBuilder}
     */
    public SimpleStringConsumer(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public void accept(String string) {
        stringBuilder.append(string).append('\n');
    }

    /**
     * Vrátí složený řetězec.
     *
     * @return složený řetězec
     */
    @Override
    public String toString() {
        return stringBuilder.toString();
    }

}