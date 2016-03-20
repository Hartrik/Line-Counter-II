package cz.hartrik.linecount.app;

import cz.hartrik.linecount.analyze.DataTypeCode;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Panel zobrazující logy.
 *
 * @version 2016-03-20
 * @author Patrik Harag
 */
public class StagePanelLog implements StagePanel {

    private final TextArea textArea = new TextArea();
    private boolean initialized = false;

    @Override
    public Node getNode(ResourceBundle rb) {
        if (!initialized) {
            initialized = true;

            textArea.setPromptText(rb.getString("log/prompt-text"));
            textArea.setEditable(false);

            HBox.setHgrow(textArea, Priority.ALWAYS);
            HBox.setMargin(textArea, new Insets(10, 10, 10, 10));
        }

        return textArea;
    }

    @Override
    public void showResults(Collection<DataTypeCode> results, String log) {
        textArea.setText(log);
    }

    @Override
    public void clear() {
        textArea.clear();
    }

}