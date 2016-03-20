
package cz.hartrik.linecount.app;

import cz.hartrik.common.io.NioUtil;
import java.io.File;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

/**
 * Nastavuje a vyvolává file chooser.
 *
 * @version 2016-03-20
 * @author Patrik Harag
 */
public class FileChooserManager {

    private final DirectoryChooser chooser;

    public FileChooserManager(String title) {
        chooser = new DirectoryChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(NioUtil.workingDirectory().toFile());
    }

    public File showDialog(Window window) {
        File file = chooser.showDialog(window);
        updateInitalDirectory(file);
        return file;
    }

    /**
     * Nastaví výchozí adresář podle posledního vybraného souboru. Uživatel se
     * tak nebude muset vícekrát proklikávat na stejné místo.
     *
     * @param lastSelected poslední vybraný soubor nebo složka
     */
    protected void updateInitalDirectory(File lastSelected) {
        if (lastSelected == null) return;

        File parent = lastSelected.getParentFile();
        chooser.setInitialDirectory(parent);
    }

}