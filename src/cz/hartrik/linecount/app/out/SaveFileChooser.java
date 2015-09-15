package cz.hartrik.linecount.app.out;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * @version 2015-09-13
 * @author Patrik Harag
 */
public class SaveFileChooser {

    private final FileChooser chooser;

    public SaveFileChooser(String title) {
        chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File("."));
    }

    public void setExtensionFilter(String name, String filter) {
        chooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter(name, filter));
    }

    public File showDialog(Window window) {
        File file = chooser.showSaveDialog(window);
        updateInitalDirectory(file);
        return file;
    }

    /**
     * Nastaví výchozí adresář podle posledního vybraného souboru. Uživatel se
     * tak nebude muset vícekrát proklikávat na stejné místo.
     *
     * @param lastSelected poslední vybrané soubory
     */
    protected void updateInitalDirectory(File lastSelected) {
        if (lastSelected == null) return;

        File lastDir = lastSelected.getParentFile();
        chooser.setInitialDirectory(lastDir);
    }

}