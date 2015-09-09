package cz.hartrik.linecount.app;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Panel zobrazující logy.
 *
 * @version 2015-09-09
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

    public void setText(String text) {
        textArea.setText(text);
    }

    @Override
    public void clear() {
        textArea.clear();
    }

}