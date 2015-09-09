
package cz.hartrik.linecount.app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import javafx.stage.Window;

/**
 * Vyvolává dialog pro filtrování, spravuje nastavení.
 *
 * @version 2015-09-09
 * @author Patrik Harag
 */
public class FilterManager {

    protected FilterDialog filterDialog = null;
    protected Predicate<Path> predicate = path -> true;

    public void showDialog(Window window) {
        if (filterDialog == null) {
            filterDialog = new FilterDialog(filterDialog);
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