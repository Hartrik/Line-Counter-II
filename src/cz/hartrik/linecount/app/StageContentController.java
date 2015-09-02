
package cz.hartrik.linecount.app;

import cz.hartrik.code.analyze.SimpleStringConsumer;
import cz.hartrik.code.analyze.linecount.DataTypeSource;
import cz.hartrik.code.analyze.linecount.LineCountStats;
import cz.hartrik.util.io.SimpleStringLoader;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

/**
 * Controller
 * 
 * @version 2014-08-06
 * @author Patrik Harag
 */
public class StageContentController implements Initializable {

    @FXML private ToggleGroup toggleGroup;
    @FXML private HBox box;
     @FXML private TableView<DataTypeSource> table; // 0
     @FXML private TextArea logArea;                // 1
     @FXML private VBox infoPanel;                  // 2
      @FXML private WebView webView;

    @FXML private TextArea inputArea;

    // --- inicializace

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDragAndDrop();
        initTable();
        initToggle();
        updatePanel();
        
        webView.getEngine().loadContent(SimpleStringLoader
                .fromClassPath("/cz/hartrik/linecount/app/info.html"));
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
                .addIntegerColumn("ws - odsazení", v -> v.getCharsIndent())
                .addIntegerColumn("ws - celkem", v -> v.getCharsWhitespace())
                .addIntegerColumn("celkem", v -> v.getCharsTotal());

        // text při prázdné tabulce
        table.setPlaceholder(new Text("Žádné položky"));
    }

    protected void initDragAndDrop() {
        inputArea.setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasFiles())
                event.acceptTransferModes(TransferMode.LINK);
            else
                event.consume();
        });
        
        inputArea.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                addFiles(db.getFiles());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
    
    protected void initToggle() {
        toggleGroup.selectedToggleProperty().addListener(
                (ov, oldToggle, newToggle) -> {
                    if (newToggle == null) oldToggle.setSelected(true);
                    else updatePanel();
        });
        
        ObservableList<Toggle> toggles = toggleGroup.getToggles();
        toggles.get(0).setUserData(table);
        toggles.get(1).setUserData(logArea);
        toggles.get(2).setUserData(infoPanel);
    }
    
    // --- metody
    
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
            
        } else {
            final long startTime = System.currentTimeMillis();
            SimpleStringConsumer stringConsumer = new SimpleStringConsumer();
            LineCountStats lineCountStats = new LineCountStats(stringConsumer);
            lineCountStats.analyze(paths);
            final long endTime = System.currentTimeMillis();
            
            stringConsumer.accept(
                    "--- výsledný čas: " + (endTime - startTime) + " ms");
            
            table.getItems().setAll(lineCountStats.getStats().values());
            logArea.setText(stringConsumer.toString());
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
    
    public void addFiles(Collection<File> files) {
        StringBuilder builder = new StringBuilder();

        for (String path : getPaths())
            builder.append(path).append("\n");

        for (File file : files)
            builder.append(file.getAbsolutePath()).append("\n");

        inputArea.setText(builder.toString());
    }
    
}