package cz.hartrik.linecount.app;

import cz.hartrik.linecount.analyze.DataTypeCode;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

/**
 * FXML Controller class
 *
 * @version 2015-09-10
 * @author Patrik Harag
 */
public class StagePanelSummaryController implements Initializable {

    @FXML private PieChart chartFiles;
    @FXML private PieChart chartLines;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    public void update(Collection<DataTypeCode> data) {
        if (data == null || data.isEmpty()) {
            chartFiles.getData().clear();
            chartLines.getData().clear();

        } else {
            updateChartLines(chartLines, data);
            updateChartFiles(chartFiles, data);
        }
    }

    protected void updateChartLines(PieChart chart, Collection<DataTypeCode> coll) {
        chart.setData(coll.stream()
                .filter((data) -> data.getLinesTotal() > 0)
                .sorted((d1, d2) -> Integer.compare(
                        d2.getLinesTotal(), d1.getLinesTotal()))
                .map((data) -> new PieChart.Data(
                        data.getFileType().getName(), data.getLinesTotal()))
                .collect(Collectors.toCollection(()
                        -> FXCollections.observableArrayList())));
    }

    protected void updateChartFiles(PieChart chart, Collection<DataTypeCode> coll) {
        chart.setData(coll.stream()
                .filter((data) -> data.getFiles() > 0)
                .sorted((d1, d2) -> Integer.compare(d2.getFiles(), d1.getFiles()))
                .map((data) -> new PieChart.Data(
                        data.getFileType().getName(), data.getFiles()))
                .collect(Collectors.toCollection(()
                        -> FXCollections.observableArrayList())));
    }

}