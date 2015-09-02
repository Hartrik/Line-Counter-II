
package cz.hartrik.linecount.app;

import java.util.function.Function;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

/**
 * Pomocná třída pro pohodlnější inicializaci tabulky. Podporuje pouze jednu
 * úroveň vnořených sloupců.
 * 
 * @version 2014-08-02
 * @author Patrik Harag
 * @param <T> content type
 */
public class TableInitializer<T> {
    
    private final TableView<T> table;
    private TableColumn<T, TableColumn<T, ?>> inner;

    public TableInitializer(TableView<T> table) {
        this.table = table;
    }
    
    // add column
    
    public TableInitializer<T> addColumn(TableColumn<T, ?> column) {
        if (inner == null)
            table.getColumns().add(column);
        else
            inner.getColumns().add(column);
        
        return this;
    }
    
    // add string column
    
    public TableInitializer<T> addStringColumn(
            String name, Function<T, Object> function) {
        
        return addStringColumn(0, Integer.MAX_VALUE, name, function);
    }
    
    public TableInitializer<T> addStringColumn(int minWidth, int maxWidth,
            String name, Function<T, Object> function) {
        
        TableColumn<T, String> column = new TableColumn<>(name);
        column.setMinWidth(minWidth);
        column.setMaxWidth(maxWidth);
        
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setCellValueFactory(p -> new SimpleStringProperty(
                function.apply(p.getValue()).toString()));
        
        return addColumn(column);
    }
    
    // add int column
    
    public TableInitializer<T> addIntegerColumn(String name,
            Function<T, Integer> function) {
        
        return addIntegerColumn(0, Integer.MAX_VALUE, name, function);
    }
    
    public TableInitializer<T> addIntegerColumn(int minWidth, int maxWidth,
            String name, Function<T, Integer> function) {
        
        TableColumn<T, Integer> column = new TableColumn<>(name);
        column.setMinWidth(minWidth);
        column.setMaxWidth(maxWidth);
        
        column.setCellFactory(TextFieldTableCell.forTableColumn(
                new StringConverter<Integer>() {
                    
            @Override public String toString(Integer object) {
                return object == null ? "0" : object.toString();
            }

            @Override public Integer fromString(String string) {
                return string == null ? 0 : Integer.parseInt(string);
            }
        }));

        column.setCellValueFactory(p ->
                new SimpleObjectProperty<>(function.apply(p.getValue())));
        
        return addColumn(column);
    }
    
    // add long column
    
    public TableInitializer<T> addLongColumn(String name,
            Function<T, Long> function) {
        
        return addLongColumn(0, Integer.MAX_VALUE, name, function);
    }
    
    public TableInitializer<T> addLongColumn(int minWidth, int maxWidth,
            String name, Function<T, Long> function) {
        
        TableColumn<T, Long> column = new TableColumn<>(name);
        column.setMinWidth(minWidth);
        column.setMaxWidth(maxWidth);
        
        column.setCellFactory(TextFieldTableCell.forTableColumn(
                new StringConverter<Long>() {
                    
            @Override public String toString(Long object) {
                return object == null ? "0" : object.toString();
            }

            @Override public Long fromString(String string) {
                return string == null ? 0L : Long.parseLong(string);
            }
        }));

        column.setCellValueFactory(p ->
                new SimpleObjectProperty<>(function.apply(p.getValue())));
        
        return addColumn(column);
    }
    
    // add inner column
    
    public TableInitializer<T> addInnerColumn(String name) {
        return addInnerColumn(0, Integer.MAX_VALUE, name);
    }
    
    public TableInitializer<T> addInnerColumn(int minWidth, int maxWidth,
            String name) {
        
        inner = new TableColumn<>(name);
        inner.setMinWidth(minWidth);
        inner.setMaxWidth(maxWidth);
        
        table.getColumns().add(inner);
        return this;
    }
    
    public TableInitializer<T> canceInner() {
        inner = null;
        
        return this;
    }
    
}