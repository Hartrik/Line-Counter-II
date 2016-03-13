package cz.hartrik.linecount.app;

import cz.hartrik.common.Pair;
import cz.hartrik.linecount.analyze.CommentStyle;
import cz.hartrik.linecount.analyze.FileType;
import cz.hartrik.linecount.analyze.supported.*;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

/**
 * Dialog, který zobrazí aktuální konfiguraci - read-only.
 *
 * @version 2016-03-13
 * @author Patrik Harag
 */
public class ConfigDialog extends Dialog<Void> {

    private final ResourceBundle rb;

    public ConfigDialog(ResourceBundle rb) {
        this.rb = rb;
        getDialogPane().getStylesheets().addAll(
                getClass().getResource("styles.css").toExternalForm());
        setTitle(rb.getString("dialog/config/title"));
        setResizable(true);

        getDialogPane().setContent(createContent());
        getDialogPane().getButtonTypes().add(ButtonType.OK);
    }

    private Node createContent() {
        TreeView<?> tree1 = createFileTypesTreeView();
        TreeView<?> tree2 = createCommentStylesTreeView();

        GridPane gridPane = new GridPane();

        Label label1 = new Label(rb.getString("dialog/config/title-left"));
              label1.getStyleClass().add("title");
        Label label2 = new Label(rb.getString("dialog/config/title-right"));
              label2.getStyleClass().add("title");
        Label info = new Label(rb.getString("dialog/config/info"));

        gridPane.addRow(0, label1, label2);
        gridPane.addRow(1, tree1, tree2);
        gridPane.addRow(2, info);

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        GridPane.setColumnSpan(info, 2);

        GridPane.setHgrow(tree1, Priority.ALWAYS);
        GridPane.setHgrow(tree2, Priority.ALWAYS);
        GridPane.setVgrow(tree1, Priority.ALWAYS);
        GridPane.setVgrow(tree2, Priority.ALWAYS);

        return gridPane;
    }

    private TreeView<?> createFileTypesTreeView() {
        TreeItem<Pair<String, String>> root = item(FileTypesXMLParser.TAG_ROOT);

        Map<String, FileType> fileTypes = FileTypes.getFileTypes();
        for (FileType type : fileTypes.values()) {
            TreeItem<Pair<String, String>> item = item(type.getName());

            item.getChildren().add(item(
                    FileTypesXMLParser.ARGUMENT_FT_TYPE,
                    type.getDataType()));

            item.getChildren().add(item(
                    FileTypesXMLParser.ARGUMENT_FT_COMMENT_STYLE,
                    type.getCommentStyle().getName()));

            for (Predicate<String> filter : type.getFilters()) {
                item.getChildren().add(
                        item(FileTypesXMLParser.TAG_FILE_TYPE_FILTER, filter));
            }

            root.getChildren().add(item);
        }

        TreeView<Pair<String, String>> treeView = new TreeView<>(root);
        treeView.setShowRoot(false);
        treeView.setCellFactory(tv -> new KeyValueCell());

        return treeView;
    }

    private TreeView<?> createCommentStylesTreeView() {
        TreeItem<Pair<String, String>> root = item(CommentStylesXMLParser.TAG_ROOT);

        Map<String, CommentStyle> styles = CommentStyles.getStyles();
        for (CommentStyle style : styles.values()) {
            TreeItem<Pair<String, String>> item = item(style.getName());

            for (Pair<Pattern, Pattern> pattern : style.getCommentPatterns()) {
                TreeItem<Pair<String, String>> itemComment = item(
                        CommentStylesXMLParser.TAG_COMMENT);

                itemComment.getChildren().add(item(
                        CommentStylesXMLParser.ARGUMENT_REGEX_START,
                        pattern.getFirst().pattern()));

                itemComment.getChildren().add(item(
                        CommentStylesXMLParser.ARGUMENT_REGEX_END,
                        pattern.getSecond().pattern()));

                item.getChildren().add(itemComment);
            }

            for (Pair<Pattern, Pattern> pattern : style.getIgnorePatterns()) {
                TreeItem<Pair<String, String>> itemIgnore = item(
                        CommentStylesXMLParser.TAG_IGNORE);

                itemIgnore.getChildren().add(item(
                        CommentStylesXMLParser.ARGUMENT_REGEX_START,
                        pattern.getFirst().pattern()));

                itemIgnore.getChildren().add(item(
                        CommentStylesXMLParser.ARGUMENT_REGEX_END,
                        pattern.getSecond().pattern()));

                item.getChildren().add(itemIgnore);
            }

            root.getChildren().add(item);
        }

        TreeView<Pair<String, String>> treeView = new TreeView<>(root);
        treeView.setShowRoot(false);
        treeView.setCellFactory(tv -> new KeyValueCell());

        return treeView;
    }

    private TreeItem<Pair<String, String>> item(Object key) {
        return new TreeItem<>(Pair.of(key.toString(), null));
    }

    private TreeItem<Pair<String, String>> item(Object key, Object value) {
        return new TreeItem<>(Pair.of(key.toString(), value.toString()));
    }

    /**
     * Speciální buňka, která zobrazuje data jako klíč = hodnota.
     * Hodnota může být {@code null}.
     */
    private class KeyValueCell extends TreeCell<Pair<String, String>> {

        @Override
        public void updateItem(Pair<String, String> item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText("");
                setGraphic(null);
            } else {
                if (item.getSecond() == null) {
                    // obsahuje pouze jednu hodnotu
                    setText(item.getFirst());
                    setGraphic(null);

                } else {
                    setText("");

                    Label key = new Label(item.getFirst());

                    Label separator = new Label("=");

                    Label value = new Label(item.getSecond());
                    value.setTextFill(Color.BLUE);
                    value.setStyle("-fx-font-family: monospace");

                    HBox hBox = new HBox(key, separator, value);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(hBox);
                }
            }
        }
    };

}