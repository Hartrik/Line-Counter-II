package cz.hartrik.linecount.app;

import cz.hartrik.common.ui.javafx.SimpleFXMLLoader;
import javafx.scene.Node;

/**
 * Spravuje panel s informacemi o programu.
 *
 * @version 2015-09-06
 * @author Patrik Harag
 */
public class StagePanelAbout implements StagePanel {

    public static final String PANEL_ABOUT_FN = "StagePanelAbout.fxml";

    private Node infoPanel;

    @Override
    public synchronized Node getNode() {
        if (infoPanel == null)
            load();

        return infoPanel;
    }

    private void load() {
        SimpleFXMLLoader loader = new SimpleFXMLLoader(PANEL_ABOUT_FN, getClass());
        infoPanel = loader.getRoot();
    }

}