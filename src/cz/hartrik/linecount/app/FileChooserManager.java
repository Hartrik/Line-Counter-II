
package cz.hartrik.linecount.app;

import cz.hartrik.common.io.NioUtil;
import cz.hartrik.linecount.analyze.supported.FileTypes;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

/**
 * Nastavuje a vyvolává file chooser.
 *
 * @version 2014-08-14
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

        List<String> exts = Arrays.stream(FileTypes.getAllFileExtensions())
                .filter(ext -> !ext.equals("*"))
                .map(ext -> "*.".concat(ext))
                .collect(Collectors.toList());

        ExtensionFilter supFilter = new ExtensionFilter(FILTER_SUPPORTED, exts);
        ExtensionFilter allFilter = new ExtensionFilter(FILTER_ALL, "*.*");

        chooser.getExtensionFilters().addAll(supFilter, allFilter);
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