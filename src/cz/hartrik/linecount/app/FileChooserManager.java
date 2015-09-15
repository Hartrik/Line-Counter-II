
package cz.hartrik.linecount.app;

import cz.hartrik.common.io.NioUtil;
import java.io.File;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * Nastavuje a vyvolává file chooser.
 *
 * @version 2015-09-13
 * @author Patrik Harag
 */
public class FileChooserManager {

    private final FileChooser chooser;

    public FileChooserManager(String title) {
        chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(NioUtil.workingDirectory().toFile());
    }

    public List<File> showDialog(Window window) {
        List<File> files = chooser.showOpenMultipleDialog(window);
        updateInitalDirectory(files);
        return files;
    }

    /**
     * Nastaví výchozí adresář podle posledního vybraného souboru. Uživatel se
     * tak nebude muset vícekrát proklikávat na stejné místo.
     *
     * @param lastSelected poslední vybrané soubory
     */
    protected void updateInitalDirectory(List<File> lastSelected) {
        if (lastSelected == null || lastSelected.isEmpty()) return;

        File lastDir = lastSelected.get(lastSelected.size() - 1).getParentFile();
        chooser.setInitialDirectory(lastDir);
    }

}