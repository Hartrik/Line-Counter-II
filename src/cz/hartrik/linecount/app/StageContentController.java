
package cz.hartrik.linecount.app;

import cz.hartrik.common.ui.javafx.DragAndDropInitializer;
import cz.hartrik.common.ui.javafx.SimpleFXMLLoader;
import cz.hartrik.common.ui.javafx.TableInitializer;
import cz.hartrik.linecount.analyze.DataTypeCode;
import cz.hartrik.linecount.analyze.LineCountProvider;
import cz.hartrik.linecount.analyze.SimpleStringConsumer;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Controller
 *
 * @version 2015-09-02
 * @author Patrik Harag
 */
public class StageContentController implements Initializable {

    public static final String PATH_PANEL_SUMMARY = "StagePanelSummary.fxml";
    public static final String PATH_PANEL_ABOUT   = "StagePanelAbout.fxml";

    @FXML private ToggleGroup toggleGroup;
    @FXML private HBox box;
     @FXML private TableView<DataTypeCode> table; // 0
           private Node chartPanel;               // 1
     @FXML private TextArea logArea;              // 2
           private Node infoPanel;                // 3

    @FXML private TextArea inputArea;

    protected StagePanelSummaryController chartPanelController;

    protected FilterManager filterManager = new FilterManager();
    protected CustomOutputManager outputManager = new CustomOutputManager();
    protected FileChooserManager fileChooserManager = new FileChooserManager();

    // --- inicializace

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SimpleFXMLLoader loader1 = new SimpleFXMLLoader(PATH_PANEL_SUMMARY, getClass());
        chartPanelController = loader1.getController();
        chartPanel = loader1.getRoot();

        SimpleFXMLLoader loader2 = new SimpleFXMLLoader(PATH_PANEL_ABOUT, getClass());
        infoPanel = loader2.getRoot();

        DragAndDropInitializer.initFileDragAndDrop(inputArea, this::addPaths);

        initTable();
        initToggle();
        updatePanel();
    }

    protected void initTable() {
        // inicializace sloupců
        new TableInitializer<>(table)
            .addStringColumn("Typ", v -> v.getFileType().getName())
            .addIntegerColumn("Soubory", v -> v.getFiles())
            .addLongColumn("Velikost v\nbajtech", v -> v.getSizeTotal())

            .addInnerColumn("Řádky")
                .addIntegerColumn("kód", v -> v.getFileType().isSourceCode()
                        ? v.getLinesTotal() - v.getLinesComment() - v.getLinesEmpty()
                        : 0)
                .addIntegerColumn("komentáře", v -> v.getLinesComment())
                .addIntegerColumn("prázdné", v -> v.getLinesEmpty())
                .addIntegerColumn("celkem", v -> v.getLinesTotal())

            .addInnerColumn("Znaky")
                .addIntegerColumn("komentáře", v -> v.getCharsComment())
                .addIntegerColumn("odsazení", v -> v.getCharsIndent())
                .addIntegerColumn("whitespace", v -> v.getCharsWhitespace())
                .addIntegerColumn("celkem", v -> v.getCharsTotal());

        // text při prázdné tabulce
        table.setPlaceholder(new Text("Žádné položky"));
    }

    protected void initToggle() {
        toggleGroup.selectedToggleProperty().addListener(
                (ov, oldToggle, newToggle) -> {
                    if (newToggle == null) oldToggle.setSelected(true);
                    else updatePanel();
        });

        ObservableList<Toggle> toggles = toggleGroup.getToggles();
        toggles.get(0).setUserData(table);
        toggles.get(1).setUserData(chartPanel);
        toggles.get(2).setUserData(logArea);
        toggles.get(3).setUserData(infoPanel);
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
                inputArea.getScene().getWindow(), table.getItems());
    }

    @FXML protected void showFileChooser() {
        addFiles(fileChooserManager.showDialog(inputArea.getScene().getWindow()));
    }

    @FXML protected void updatePanel() {
        box.getChildren().clear();
        box.getChildren().add((Node) toggleGroup.getSelectedToggle().getUserData());
    }

    @FXML protected void count() {
        List<Path> paths = getPaths().stream().map(path -> Paths.get(path))
                .collect(Collectors.toList());

        if (paths.isEmpty()) {
            table.getItems().clear();
            logArea.setText("");
            chartPanelController.update(null);

        } else {
            final long startTime = System.currentTimeMillis();
            SimpleStringConsumer stringConsumer = new SimpleStringConsumer();
            LineCountProvider lineCountProvider = new LineCountProvider(
                    filterManager.getPredicate(), stringConsumer);
            lineCountProvider.analyze(paths);
            final long endTime = System.currentTimeMillis();

            stringConsumer.accept(
                    "--- výsledný čas: " + (endTime - startTime) + " ms");

            Collection<DataTypeCode> values = lineCountProvider.getStats().values();
            List<DataTypeCode> list = values.stream()
                    .sorted((t1, t2) -> t1.getFileType().getName()
                            .compareTo(t2.getFileType().getName()))
                    .collect(Collectors.toList());

            table.getItems().setAll(list);
            logArea.setText(stringConsumer.toString());
            chartPanelController.update(list);
        }
    }

    public List<String> getPaths() {
        String current = inputArea.getText();
        if (current.trim().isEmpty())
            return new ArrayList<>();

        List<String> list = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(current, "\n");
        while (tokenizer.hasMoreElements()) {
            String nextToken = tokenizer.nextToken().trim();
            if (!nextToken.isEmpty())
                list.add(nextToken);
        }

        return list;
    }

    public void addPaths(Collection<Path> files) {
        addFiles(files.stream().map(Path::toFile).collect(Collectors.toList()));
    }

    public void addFiles(Collection<File> files) {
        if (files == null || files.isEmpty()) return;
        final StringBuilder builder = new StringBuilder();

        for (String path : getPaths())
            builder.append(path).append("\n");

        for (File file : files)
            builder.append(file.getAbsolutePath()).append("\n");

        inputArea.setText(builder.toString());
    }

}