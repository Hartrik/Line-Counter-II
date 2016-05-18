
package cz.hartrik.linecount.app;

import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.stage.Window;

/**
 * Vyvolává dialog pro filtrování, spravuje nastavení.
 *
 * @version 2016-05-18
 * @author Patrik Harag
 */
public class FilterManager {

    protected FilterDialog filterDialog = null;
    protected Predicate<Path> predicate = path -> true;

    private final ResourceBundle resourceBundle;

    public FilterManager(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public void showDialog(Window window) {
        if (filterDialog == null) {
            filterDialog = new FilterDialog(resourceBundle, window);
        }

        filterDialog.showAndWait().ifPresent(p -> this.predicate = p);
    }

    public Predicate<Path> getPredicate() {
        return predicate;
    }

}