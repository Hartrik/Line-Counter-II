package cz.hartrik.linecount.app;

import cz.hartrik.code.analyze.linecount.DataTypeCode;
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
 * @version 2014-08-15
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

    protected void updateChartLines(PieChart chart,
            Collection<DataTypeCode> data) {

        chart.setData(data.stream()
                .filter((DataTypeCode t) -> t.getLinesTotal() > 0)
                .map((DataTypeCode t)
                        -> new PieChart.Data(
                                t.getFileType().getName(), t.getLinesTotal()))
                .collect(Collectors.toCollection(()
                        -> FXCollections.observableArrayList())));
    }

    protected void updateChartFiles(PieChart chart,
            Collection<DataTypeCode> data) {

        chart.setData(data.stream()
                .filter((DataTypeCode t) -> t.getFiles() > 0)
                .map((DataTypeCode t)
                        -> new PieChart.Data(
                                t.getFileType().getName(), t.getFiles()))
                .collect(Collectors.toCollection(()
                        -> FXCollections.observableArrayList())));
    }

}