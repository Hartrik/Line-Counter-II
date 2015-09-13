package cz.hartrik.linecount.app;

import cz.hartrik.common.ui.javafx.SimpleFXMLLoader;
import cz.hartrik.linecount.analyze.DataTypeCode;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.scene.Node;

/**
 * Spravuje panel s grafy.
 *
 * @version 2015-09-13
 * @author Patrik Harag
 */
public class StagePanelSummary implements StagePanel {

    static final String PANEL_SUMMARY_FN = "StagePanelSummary.fxml";

    private Node chartPanel;
    private StagePanelSummaryController controller;

    // pro dočasné uložení výsledků, pokud ještě nebyl panel vytvořen
    //   (nebyla volána metoda getNode)
    private Collection<DataTypeCode> temp;

    @Override
    public synchronized Node getNode(ResourceBundle resourceBundle) {
        if (chartPanel == null)
            load(resourceBundle);

        return chartPanel;
    }

    @Override
    public void clear() {
        showResults(null, "");
    }

    @Override
    public synchronized void showResults(Collection<DataTypeCode> data, String log) {
        if (chartPanel == null)
            this.temp = data;
        else
            controller.update(data);
    }

    private void load(ResourceBundle rb) {
        URL url = getClass().getResource(PANEL_SUMMARY_FN);
        SimpleFXMLLoader loader = new SimpleFXMLLoader(url, rb);
        controller = loader.getController();
        chartPanel = loader.getRoot();

        controller.update(temp);
        temp = null;
    }

}