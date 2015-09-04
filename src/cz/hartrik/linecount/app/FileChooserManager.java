
package cz.hartrik.linecount.app;

import cz.hartrik.common.io.NioUtil;
import java.io.File;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * Nastavuje a vyvolává file chooser.
 *
 * @version 2015-09-04
 * @author Patrik Harag
 */
public class FileChooserManager {

    static final String TITLE = "Výběr souborů";
    static final String FILTER_ALL = "Všechny soubory";
    static final String FILTER_SUPPORTED = "Všechny podporované soubory";

    protected final FileChooser chooser;

    public FileChooserManager() {
        chooser = new FileChooser();
        chooser.setTitle(TITLE);
        chooser.setInitialDirectory(NioUtil.workingDirectory().toFile());
    }

    public List<File> showDialog(Window window) {
        List<File> files = chooser.showOpenMultipleDialog(window);
        updateInitalDirectory(files);
        return files;
    }

    /**
     * Nastaví výchozí adresář podle posledního vybraného soubou. Uživatel se
     * tak nebude muset vícekrát proklikávat na stejné místo.
     *
     * @param lastSelected poslední vybramé soubory
     */
    protected void updateInitalDirectory(List<File> lastSelected) {
        if (lastSelected == null || lastSelected.isEmpty()) return;

        File lastDir = lastSelected.get(lastSelected.size() - 1).getParentFile();
        chooser.setInitialDirectory(lastDir);
    }

}