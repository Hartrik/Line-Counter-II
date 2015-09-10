package cz.hartrik.linecount.app;

import cz.hartrik.linecount.analyze.DataTypeCode;
import java.util.Collection;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Panel zobrazující logy.
 *
 * @version 2015-09-10
 * @author Patrik Harag
 */
public class StagePanelLog implements StagePanel {

    private final TextArea textArea;

    public StagePanelLog() {
        this.textArea = new TextArea();

        textArea.setEditable(false);

        HBox.setHgrow(textArea, Priority.ALWAYS);
        HBox.setMargin(textArea, new Insets(10, 10, 10, 10));
    }

    @Override
    public Node getNode() {
        return textArea;
    }

    @Override
    public void showResults(Collection<DataTypeCode> results, String log) {
        setText(log);
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    @Override
    public void clear() {
        textArea.clear();
    }

}