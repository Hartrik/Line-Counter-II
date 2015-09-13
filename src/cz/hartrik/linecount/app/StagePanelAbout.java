package cz.hartrik.linecount.app;

import cz.hartrik.common.ui.javafx.SimpleFXMLLoader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Node;

/**
 * Spravuje panel s informacemi o programu.
 *
 * @version 2015-09-13
 * @author Patrik Harag
 */
public class StagePanelAbout implements StagePanel {

    static final String PANEL_ABOUT_FN = "StagePanelAbout.fxml";

    private Node infoPanel;

    @Override
    public synchronized Node getNode(ResourceBundle resourceBundle) {
        if (infoPanel == null)
            load(resourceBundle);

        return infoPanel;
    }

    private void load(ResourceBundle rb) {
        URL url = getClass().getResource(PANEL_ABOUT_FN);
        SimpleFXMLLoader loader = new SimpleFXMLLoader(url, rb);
        infoPanel = loader.getRoot();
    }

}