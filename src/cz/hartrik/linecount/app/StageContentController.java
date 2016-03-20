
package cz.hartrik.linecount.app;

import cz.hartrik.common.reflect.StopWatch;
import cz.hartrik.linecount.analyze.DataTypeCode;
import cz.hartrik.linecount.analyze.FileFilter;
import cz.hartrik.linecount.analyze.LineCountProvider;
import cz.hartrik.linecount.analyze.SimpleStringConsumer;
import cz.hartrik.linecount.app.out.OutputManager;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Controller
 *
 * @version 2016-03-13
 * @author Patrik Harag
 */
public class StageContentController implements Initializable {

    @FXML private Button buttonStart;
    @FXML private MenuButton buttonOut;

    @FXML private ToggleGroup toggleGroup;
    @FXML private HBox mainBox;
    @FXML private VBox bottomBox;

    private final ProgressBar progressBar = new ProgressBar();
    { progressBar.setPrefWidth(Integer.MAX_VALUE); }

    private OutputManager outputManager;

    private ResourceBundle rb;

    // panely

    private final StagePanelTable stagePanelTable = new StagePanelTable();
    private final StagePanelSummary stagePanelSummary = new StagePanelSummary();
    private final StagePanelLog stagePanelLog = new StagePanelLog();
    private final StagePanelAbout stagePanelAbout = new StagePanelAbout();

    private final StagePanelInput stagePanelInput = new StagePanelInput();

    private final StagePanel[] panels = {
            stagePanelTable, stagePanelSummary, stagePanelLog, stagePanelAbout,
            stagePanelInput
    };

    // --- inicializace

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        this.rb = resourceBundle;
        this.outputManager = new OutputManager(
                resourceBundle, () -> mainBox.getScene().getWindow());

        initToggle();
        updatePanel();  // zobrazení výchozího panelu

        // zobrazení panelu se vstupem
        bottomBox.getChildren().add(stagePanelInput.getNode(resourceBundle));
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

    @FXML protected void showScriptDialog() {
        outputManager.showScriptDialog(stagePanelTable.getData());
    }

    @FXML protected void showOptions() {
        ConfigDialog dialog = new ConfigDialog(rb);
        dialog.initOwner(mainBox.getScene().getWindow());
        dialog.showAndWait();
    }

    @FXML protected void saveTableAsImage() {
        outputManager.saveAsImage(stagePanelTable.getNode(rb));
    }

    @FXML protected void saveTableAsHTML() {
        outputManager.saveAsHTML(stagePanelTable.getTable());
    }

    @FXML protected void updatePanel() {
        mainBox.getChildren().clear();

        Object data = toggleGroup.getSelectedToggle().getUserData();
        Node node = ((StagePanel) data).getNode(rb);

        mainBox.getChildren().add(node);
    }

    @FXML protected void count() {
        List<Path> paths = stagePanelInput.getPaths()
                .map(Paths::get).collect(Collectors.toList());

        clear();

        if (!paths.isEmpty()) {
            SimpleStringConsumer stringConsumer = new SimpleStringConsumer();

            final String format = rb.getString("log/not-exists");
            FileFilter fileFilter = new FileFilter(
                    stagePanelInput.getFilter(),
                    (p, e) -> stringConsumer.accept(String.format(format, p)));

            Collection<Path> filtered = fileFilter.filter(paths);

            if (filtered.isEmpty()) {
                showResults(Collections.emptyList(), stringConsumer.toString());

            } else {
                Thread thread = new Thread(() -> process(filtered, stringConsumer));
                thread.start();
            }
        }
    }

    private void process(Collection<Path> paths, Consumer<String> consumer) {
        LineCountProvider lineCountProvider = new LineCountProvider(consumer, rb);

        Platform.runLater(() -> {
            enableEditing(false);
            showProgressBar();
        });

        StopWatch stopWatch = StopWatch.measure(() -> {
            int i = 0;
            for (Path path : paths) {
                lineCountProvider.analyze(path);

                final double progress = (double) ++i / paths.size();
                Platform.runLater(() -> progressBar.setProgress(progress));
            }
        });

        final String format = rb.getString("log/time");
        consumer.accept(String.format(format, stopWatch.getMillis()));

        Platform.runLater(() -> {
            hideProgressBar();
            showResults(lineCountProvider.getStats().values(), consumer.toString());
            enableEditing(true);
        });
    }

    private void showProgressBar() {
        progressBar.setProgress(0);
        bottomBox.getChildren().add(0, progressBar);
    }

    private void hideProgressBar() {
        bottomBox.getChildren().remove(0);
    }

    private void enableEditing(boolean enable) {
        buttonStart.setDisable(!enable);
        buttonOut.setDisable(!enable);

        Arrays.stream(panels).forEach(p -> p.enableEditing(enable));
    }

    private void showResults(Collection<DataTypeCode> results, String log) {
        Arrays.stream(panels).forEach(p -> p.showResults(results, log));
    }

    private void clear() {
        Arrays.stream(panels).forEach(StagePanel::clear);
    }

}