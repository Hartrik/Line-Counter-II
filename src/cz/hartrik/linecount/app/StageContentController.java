
package cz.hartrik.linecount.app;

import cz.hartrik.common.reflect.StopWatch;
import cz.hartrik.common.ui.javafx.DragAndDropInitializer;
import cz.hartrik.linecount.analyze.DataTypeCode;
import cz.hartrik.linecount.analyze.LineCountProvider;
import cz.hartrik.linecount.analyze.SimpleStringConsumer;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Controller
 *
 * @version 2015-09-06
 * @author Patrik Harag
 */
public class StageContentController implements Initializable {

    @FXML private ToggleGroup toggleGroup;
    @FXML private HBox box;

    @FXML private TextArea inputArea;

    private final StagePanelTable stagePanelTable = new StagePanelTable();
    private final StagePanelSummary stagePanelSummary = new StagePanelSummary();
    private final StagePanelLog stagePanelLog = new StagePanelLog();
    private final StagePanelAbout stagePanelAbout = new StagePanelAbout();

    private final FilterManager filterManager = new FilterManager();
    private final CustomOutputManager outputManager = new CustomOutputManager();
    private final FileChooserManager fileChooserManager = new FileChooserManager();

    // --- inicializace

    @Override
    public void initialize(java.net.URL url, ResourceBundle rb) {
        DragAndDropInitializer.initFileDragAndDrop(inputArea, this::addPaths);

        initToggle();
        updatePanel();
    }

    protected void initToggle() {
        toggleGroup.selectedToggleProperty().addListener((o, oldTgg, newTgg) -> {
            if (newTgg == null)
                oldTgg.setSelected(true);
            else
                updatePanel();
        });

        ObservableList<Toggle> toggles = toggleGroup.getToggles();
        toggles.get(0).setUserData(stagePanelTable);
        toggles.get(1).setUserData(stagePanelSummary);
        toggles.get(2).setUserData(stagePanelLog);
        toggles.get(3).setUserData(stagePanelAbout);
    }

    // --- metody

    @FXML protected void showFilterDialog() {
        filterManager.showDialog(inputArea.getScene().getWindow());
    }

    @FXML protected void clearInput() {
        inputArea.clear();
    }

    @FXML protected void showScriptDialog() {
        outputManager.showOutputDialog(
                inputArea.getScene().getWindow(), stagePanelTable.getData());
    }

    @FXML protected void showFileChooser() {
        addFiles(fileChooserManager.showDialog(inputArea.getScene().getWindow()));
    }

    @FXML protected void updatePanel() {
        box.getChildren().clear();

        Object data = toggleGroup.getSelectedToggle().getUserData();
        Node node = ((StagePanel) data).getNode();

        box.getChildren().add(node);
    }

    @FXML protected void count() {
        List<Path> paths = getPaths()
                .map(Paths::get).collect(Collectors.toList());

        if (paths.isEmpty()) {
            stagePanelTable.clear();
            stagePanelSummary.update(null);
            stagePanelLog.clear();

        } else {
            SimpleStringConsumer stringConsumer = new SimpleStringConsumer();
            LineCountProvider lineCountProvider = new LineCountProvider(
                    filterManager.getPredicate(), stringConsumer);

            StopWatch stopWatch = StopWatch.measure(() -> {
                lineCountProvider.analyze(paths);
            });

            stringConsumer.accept(
                    "--- výsledný čas: " + stopWatch.getMillis() + " ms");

            Collection<DataTypeCode> values = lineCountProvider.getStats().values();
            List<DataTypeCode> list = values.stream()
                    .sorted((t1, t2) -> t1.getFileType().getName()
                            .compareTo(t2.getFileType().getName()))
                    .collect(Collectors.toList());

            stagePanelTable.setData(list);
            stagePanelSummary.update(list);
            stagePanelLog.setText(stringConsumer.toString());
        }
    }

    public Stream<String> getPaths() {
        String paths = inputArea.getText();

        return Arrays.stream(paths.split("\\s*\n\\s*"))
                .filter(line -> !line.isEmpty());
    }

    public void addPaths(Collection<Path> files) {
        addFiles(files.stream().map(Path::toFile).collect(Collectors.toList()));
    }

    public void addFiles(Collection<File> files) {
        if (files == null || files.isEmpty()) return;

        inputArea.setText(Stream
                .concat(getPaths(), files.stream().map(File::getAbsolutePath))
                .collect(Collectors.joining("\n")));
    }

}