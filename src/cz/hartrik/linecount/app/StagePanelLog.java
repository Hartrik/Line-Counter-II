package cz.hartrik.linecount.app;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @version 2015-09-06
 * @author Patrik Harag
 */
public class StagePanelLog implements StagePanel {

    private final TextArea textArea;
    private final HBox box;

    public StagePanelLog() {
        this.textArea = new TextArea();
        this.box = new HBox(textArea);

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

    public void clear() {
        textArea.clear();
    }

}