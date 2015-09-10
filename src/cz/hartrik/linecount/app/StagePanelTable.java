package cz.hartrik.linecount.app;

import cz.hartrik.common.ui.javafx.TableInitializer;
import cz.hartrik.linecount.analyze.DataTypeCode;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
 * @version 2015-09-10
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

        initTable();
    }

    private void initTable() {
        // inicializace sloupců

        Function<Number, String> format = (n) -> String.format("%,d ", n);

        TableInitializer.of(table)
            .addStringColumn("Typ", v -> " " + v.getFileType().getName())
                    .init(c -> c.setStyle("-fx-alignment: center-left;"))
            .addObjectColumn("Soubory", DataTypeCode::getFiles, format)
            .addObjectColumn("Velikost\nv bajtech", DataTypeCode::getSizeTotal, format)
            .addInnerColumn("Řádky")
                .addObjectColumn("kód", DataTypeCode::getLinesCode, format)
                .addObjectColumn("komentáře", DataTypeCode::getLinesComment, format)
                .addObjectColumn("prázdné", DataTypeCode::getLinesEmpty, format)
                .addObjectColumn("celkem", DataTypeCode::getLinesTotal, format)
                        .init(c -> c.setSortType(TableColumn.SortType.DESCENDING))
                        .init(c -> table.getSortOrder().add(0, c))
            .addInnerColumn("Znaky")
                .addObjectColumn("komentáře", DataTypeCode::getCharsComment, format)
                .addObjectColumn("odsazení", DataTypeCode::getCharsIndent, format)
                .addObjectColumn("whitespace", DataTypeCode::getCharsWhitespace, format)
                .addObjectColumn("celkem", DataTypeCode::getCharsTotal, format);

        // text při prázdné tabulce
        table.setPlaceholder(new Text("Žádné položky"));
    }

    @Override
    public Node getNode() {
        return box;
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

}