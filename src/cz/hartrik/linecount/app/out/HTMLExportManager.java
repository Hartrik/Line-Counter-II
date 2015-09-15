package cz.hartrik.linecount.app.out;

import cz.hartrik.common.Exceptions;
import cz.hartrik.common.io.Resources;
import cz.hartrik.linecount.analyze.DataTypeCode;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Window;

/**
 * Spravuje ukládání snímků uživatelského rozhraní.
 *
 * @version 2015-09-15
 * @author Patrik Harag
 */
public class HTMLExportManager {

    static final String FILE_TEMPLATE = "template.html";
    static final String INDENT = "                ";

    private final SaveFileChooser saveFileChooser;

    public HTMLExportManager(SaveFileChooser chooser) {
        this.saveFileChooser = chooser;
    }

    public void save(TableView<DataTypeCode> tableView, Window owner) {
        saveFileChooser.setExtensionFilter("HTML", "*.html");
        File file = saveFileChooser.showDialog(owner);

        if (file == null)
            return;

        Path path = file.toPath();
        Charset charset = StandardCharsets.UTF_8;

        Exceptions.unchecked(() -> {
            try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
                writer.write(createTable(tableView));
            }
        });
    }

    protected String createTable(TableView<DataTypeCode> tableView) {
        String template = Resources.text(
                FILE_TEMPLATE, getClass(), StandardCharsets.UTF_8);

        StringBuilder table = new StringBuilder();
        table.append(createTableHeader(tableView));
        table.append(createTableData(tableView));

        return template
//                .replace("${title}", "")
                .replace("${table}", table);
    }

    protected CharSequence createTableHeader(TableView<DataTypeCode> tableView) {
        StringBuilder firstRow = new StringBuilder();
        StringBuilder secondRow = new StringBuilder();

        tableView.getColumns().forEach((column) -> {
            if (column.getColumns().isEmpty()) {
                firstRow.append("<th rowspan=\"2\">")
                        .append(normalize(column.getText()))
                        .append("</th>");
            } else {
                firstRow.append("<th colspan=\"")
                        .append(column.getColumns().size())
                        .append("\">")
                        .append(normalize(column.getText()))
                        .append("</th>");

                column.getColumns().forEach(innerColumn -> {
                    secondRow.append("<th>")
                            .append(normalize(innerColumn.getText()))
                            .append("</th>");
                });
            }
        });

        StringBuilder header = new StringBuilder("\n");
        header.append(INDENT).append("<tr>").append(firstRow).append("</tr>\n");
        header.append(INDENT).append("<tr>").append(secondRow).append("</tr>\n");

        return header;
    }

    protected CharSequence createTableData(TableView<DataTypeCode> tableView) {

        // seznam sloupců s daty
        List<TableColumn<DataTypeCode, ?>> columns = tableView.getColumns()
                .stream()
                .flatMap(column -> (column.getColumns().isEmpty())
                        ? Stream.of(column)
                        : column.getColumns().stream())
                .collect(Collectors.toList());

        StringBuilder content = new StringBuilder();

        tableView.getItems().forEach((data) -> {
            content.append(INDENT).append("<tr>");

            for (TableColumn<DataTypeCode, ?> column : columns) {
                ObservableValue<?> val = column.getCellObservableValue(data);

                content.append("<td>")
                        .append(normalize(String.valueOf(val.getValue())))
                        .append("</td>");
            }

            content.append("</tr>\n");
        });

        return content;
    }

    private static String normalize(String string) {
        return escapeHTML(string.trim()).replace("\n", "<br>");
    }

    private static String escapeHTML(String string) {
        StringBuilder out = new StringBuilder(Math.max(16, string.length()));

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
                out.append("&#");
                out.append((int) c);
                out.append(';');
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

}