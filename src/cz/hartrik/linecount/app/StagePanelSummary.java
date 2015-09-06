package cz.hartrik.linecount.app;

import cz.hartrik.common.ui.javafx.SimpleFXMLLoader;
import cz.hartrik.linecount.analyze.DataTypeCode;
import java.util.Collection;
import javafx.scene.Node;

/**
 * Spravuje panel s grafy.
 *
 * @version 2015-09-06
 * @author Patrik Harag
 */
public class StagePanelSummary implements StagePanel {

    static final String PANEL_SUMMARY_FN = "StagePanelSummary.fxml";

    private Node chartPanel;
    private StagePanelSummaryController controller;

    @Override
    public synchronized Node getNode() {
        if (chartPanel == null)
            load();

        return chartPanel;
    }

    public synchronized void update(Collection<DataTypeCode> data) {
        if (chartPanel == null)
            load();

        controller.update(data);
    }

    private void load() {
        SimpleFXMLLoader loader = new SimpleFXMLLoader(PANEL_SUMMARY_FN, getClass());
        controller = loader.getController();
        chartPanel = loader.getRoot();
    }

}