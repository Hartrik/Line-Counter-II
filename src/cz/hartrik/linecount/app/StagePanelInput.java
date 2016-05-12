package cz.hartrik.linecount.app;

import cz.hartrik.common.io.Resources;
import cz.hartrik.common.ui.javafx.DragAndDropInitializer;
import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Panel se vstupním polem. Také se stará o filtry.
 *
 * @version 2015-03-20
 * @author Patrik Harag
 */
public class StagePanelInput implements StagePanel {

    public static final String ICON_FOLDER = "icon - folder (16).png";
    public static final String ICON_FILTER = "icon - filter (16).png";
    public static final String ICON_CLEAN = "icon - delete (16).png";

    private FilterManager filterManager;
    private FileChooserManager fileChooserManager;

    private HBox box;
    private TextArea inputArea;

    @Override
    public synchronized Node getNode(ResourceBundle resourceBundle) {
        if (box == null) {
            filterManager = new FilterManager(resourceBundle);
            fileChooserManager = new FileChooserManager(
                    resourceBundle.getString("dialog/fc/import/title"));

            box = createBox(resourceBundle);
            DragAndDropInitializer.initFileDragAndDrop(inputArea, this::addPaths);
        }

        return box;
    }

    private HBox createBox(ResourceBundle rb) {
        inputArea = new TextArea();
        inputArea.setPromptText(rb.getString("input/prompt-text"));

        Button buttonChoose = new Button();
        buttonChoose.setPrefHeight(24);
        buttonChoose.setOnAction((e) -> showFileChooser());
        buttonChoose.setGraphic(new ImageView(loadImage(ICON_FOLDER)));

        Button buttonFilter = new Button();
        buttonFilter.setPrefHeight(24);
        buttonFilter.setOnAction((e) -> showFilterDialog());
        buttonFilter.setGraphic(new ImageView(loadImage(ICON_FILTER)));

        Button buttonDel = new Button();
        buttonDel.setPrefHeight(24);
        buttonDel.setOnAction((e) -> clearInput());
        buttonDel.setGraphic(new ImageView(loadImage(ICON_CLEAN)));

        VBox buttons = new VBox(5, buttonChoose, buttonFilter, buttonDel);
        HBox hBox = new HBox(5, inputArea, buttons);

        HBox.setHgrow(inputArea, Priority.ALWAYS);
        VBox.setVgrow(hBox, Priority.ALWAYS);

        return hBox;
    }

    @Override
    public void enableEditing(boolean enable) {
        box.setDisable(!enable);
    }

    private Image loadImage(String fileName) {
        return Resources.image(fileName, getClass());
    }

    // veřejné metody

    public void showFilterDialog() {
        filterManager.showDialog(inputArea.getScene().getWindow());
    }

    public Predicate<Path> getFilter() {
        return filterManager.getPredicate();
    }

    public void clearInput() {
        inputArea.clear();
    }

    public void showFileChooser() {
        File file = fileChooserManager.showDialog(inputArea.getScene().getWindow());
        if (file != null)
            addFiles(Collections.singleton(file));
    }

    public Stream<String> getPaths() {
        String paths = inputArea.getText();

        return Arrays.stream(paths.split("\\s*\n\\s*"))
                .filter(line -> !line.isEmpty());
    }

    public void addPaths(Collection<Path> files) {
        addFiles(files.stream().map(Path::toFile).collect(Collectors.toList()));
    }

    public void addFiles(Collection<File> files) {
        if (files == null || files.isEmpty())
            return;

        Stream<String> oldPaths = getPaths();
        Stream<String> newPaths = files.stream().map(File::getAbsolutePath);

        String text = Stream.concat(oldPaths, newPaths)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));

        inputArea.setText(text);
    }

}