package cz.hartrik.linecount.app;

import cz.hartrik.common.ui.javafx.TableInitializer;
import cz.hartrik.linecount.analyze.DataTypeCode;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

/**
 * Spravuje panel s tabulkou.
 *
 * @version 2015-09-13
 * @author Patrik Harag
 */
public class StagePanelTable implements StagePanel {

    private final TableView<DataTypeCode> table;
    private final HBox box;

    public StagePanelTable() {
        this.table = new TableView<>();
        this.box = new HBox(table);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(Integer.MAX_VALUE);

        HBox.setHgrow(table, Priority.ALWAYS);
        HBox.setMargin(table, new Insets(-1, -2, -1, -1));
    }

    @Override
    public Node getNode(ResourceBundle resourceBundle) {
        if (table.getColumns().isEmpty())
            initTable(resourceBundle);

        return box;
    }

    private void initTable(ResourceBundle rb) {
        // inicializace sloupců

        Function<String, String> n = (key) -> rb.getString("table/column/" + key);
        Function<Number, String> format = (num) -> String.format("%,d ", num);

        TableInitializer.of(table)
            .addStringColumn(n.apply("type"), v -> " " + v.getFileType().getName())
                    .init(c -> c.setStyle("-fx-alignment: center-left;"))
            .addObjectColumn(n.apply("files"), DataTypeCode::getFiles, format)
            .addObjectColumn(n.apply("size"), DataTypeCode::getSizeTotal, format)
            .addInnerColumn(n.apply("lines"))
                .addObjectColumn(n.apply("l-code"), DataTypeCode::getLinesCode, format)
                .addObjectColumn(n.apply("l-comments"), DataTypeCode::getLinesComment, format)
                .addObjectColumn(n.apply("l-empty"), DataTypeCode::getLinesEmpty, format)
                .addObjectColumn(n.apply("l-total"), DataTypeCode::getLinesTotal, format)
                        .init(c -> c.setSortType(TableColumn.SortType.DESCENDING))
                        .init(c -> table.getSortOrder().add(0, c))
            .addInnerColumn(n.apply("chars"))
                .addObjectColumn(n.apply("c-comments"), DataTypeCode::getCharsComment, format)
                .addObjectColumn(n.apply("c-indent"), DataTypeCode::getCharsIndent, format)
                .addObjectColumn(n.apply("c-ws"), DataTypeCode::getCharsWhitespace, format)
                .addObjectColumn(n.apply("c-total"), DataTypeCode::getCharsTotal, format);

        // text při prázdné tabulce
        table.setPlaceholder(new Text(rb.getString("table/empty")));
    }

    @Override
    public void clear() {
        table.getItems().clear();
    }

    @Override
    public void showResults(Collection<DataTypeCode> results, String log) {
        table.getItems().setAll(results);
        table.sort();  // seřazení dat podle nastavení uživatele
    }

    public List<DataTypeCode> getData() {
        return Collections.unmodifiableList(table.getItems());
    }

    protected TableView<DataTypeCode> getTable() {
        return table;
    }

}