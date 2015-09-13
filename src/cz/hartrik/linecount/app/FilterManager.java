
package cz.hartrik.linecount.app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.stage.Window;

/**
 * Vyvolává dialog pro filtrování, spravuje nastavení.
 *
 * @version 2015-09-13
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
            filterDialog = new FilterDialog(resourceBundle);
            filterDialog.initOwner(window);
        }

        filterDialog.showAndWait();
        predicate = filterDialog.getPredicate();
    }

    public Predicate<Path> getPredicate() {
        return (path) -> {
            return !Files.isDirectory(path) && predicate.test(path);
        };
    }

}