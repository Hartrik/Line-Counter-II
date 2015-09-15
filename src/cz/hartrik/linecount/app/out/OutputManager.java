package cz.hartrik.linecount.app.out;

import cz.hartrik.linecount.analyze.DataTypeCode;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.stage.Window;

/**
 * @version 2015-09-13
 * @author Patrik Harag
 */
public class OutputManager {

    private final ResourceBundle resourceBundle;
    private final Supplier<Window> windowSupplier;

    private final SaveFileChooser fileChooser;

    private final CustomOutputManager customOutputManager;
    private final ScreenshotManager screenshotManager;
    private final HTMLExportManager htmlExportManager;

    public OutputManager(ResourceBundle rb, Supplier<Window> windowSupplier) {

        this.resourceBundle = rb;
        this.windowSupplier = windowSupplier;

        this.fileChooser = new SaveFileChooser(rb.getString("dialog/fc/save/title"));
        this.customOutputManager = new CustomOutputManager(rb);
        this.screenshotManager = new ScreenshotManager(fileChooser);
        this.htmlExportManager = new HTMLExportManager(fileChooser);
    }

    public void showScriptDialog(Collection<DataTypeCode> data) {
        customOutputManager.showOutputDialog(windowSupplier.get(), data);
    }

    public void saveAsImage(Node node) {
        screenshotManager.save(node, windowSupplier.get());
    }

    public void saveAsHTML(TableView<DataTypeCode> tableView) {
        htmlExportManager.save(tableView, windowSupplier.get());
    }

}