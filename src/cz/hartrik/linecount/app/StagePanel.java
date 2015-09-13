package cz.hartrik.linecount.app;

import cz.hartrik.linecount.analyze.DataTypeCode;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.scene.Node;

/**
 * Panel s prvky uživatelského rozhraní.
 *
 * @version 2015-09-13
 * @author Patrik Harag
 */
public interface StagePanel {

    /**
     * Vrátí komponentu.
     *
     * @param rb resources
     * @return komponenta
     */
    Node getNode(ResourceBundle rb);

    /**
     * Povolí nebo zakáže úpravy. Ve výchozím stavu jsou úpravy povoleny. <p>
     *
     * Tato metoda je volána před zahájením déle trvající činnosti v jiném
     * vlákně a po jejím dokončení.
     *
     * @param enable povolit úpravy
     */
    default void enableEditing(boolean enable) { };

    /**
     * Zobrazí výsledky.
     *
     * @param results statistiky k jednotlivým typům souborů
     * @param log textový výstup
     */
    default void showResults(Collection<DataTypeCode> results, String log) { };

    /**
     * Odstraní předchozí výsledky.
     */
    default void clear() { };

}