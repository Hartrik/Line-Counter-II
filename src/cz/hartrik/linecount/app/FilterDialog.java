
package cz.hartrik.linecount.app;

import cz.hartrik.common.Exceptions;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Dialog pro tvorbu filtrů souborů.
 * Tento dialog je připraven na opakované použití.
 *
 * @version 2016-02-29
 * @author Patrik Harag
 */
public class FilterDialog extends Dialog<Predicate<Path>> {

    private static final String PACKAGE = "/cz/hartrik/linecount/app/";
    public static final String PATH_ICON = PACKAGE + "icon - filter (16).png";

    private TextField tfRegex;
    private ToggleGroup toggleGroup;
    private RadioButton rbAbsolutePath;
    private RadioButton rbFileName;

    private String tempRegex;
    private Toggle tempToggle;

    public FilterDialog(ResourceBundle rb, Window owner) {
        setTitle(rb.getString("dialog/filter/title"));
        initModality(Modality.APPLICATION_MODAL);
        getDialogPane().setContent(createContent(rb));
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        initOwner(owner);  // musí být nastaven před nastavením ikony
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().setAll(new Image(PATH_ICON));

        setOnShowing((e) -> commit());

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK)
                return getPredicate();

            rollback();
            return null;
        });
    }

    private Node createContent(ResourceBundle rb) {
        Label labelHeader = new Label(rb.getString("dialog/filter/header"));
        labelHeader.setStyle("-fx-font-weight: bold;");

        tfRegex = new TextField(".*");
        tfRegex.setFont(Font.font("Monospaced", 12));

        Label labelApply = new Label(rb.getString("dialog/filter/apply"));
        rbAbsolutePath = new RadioButton(rb.getString("dialog/filter/apply/path"));
        rbFileName = new RadioButton(rb.getString("dialog/filter/apply/file-name"));

        // nastavení RadioButton
        rbFileName.selectedProperty().set(true);
        toggleGroup = new ToggleGroup();
        rbAbsolutePath.setToggleGroup(toggleGroup);
        rbFileName.setToggleGroup(toggleGroup);

        // vložení do lyoutů
        VBox boxRB = new VBox(10, rbFileName, rbAbsolutePath);
        HBox boxApply = new HBox(20, labelApply, boxRB);
        VBox boxMain = new VBox(10, labelHeader, tfRegex, boxApply);
        boxMain.setPadding(new Insets(20));
        boxMain.setPrefWidth(400);
        return boxMain;
    }

    private Predicate<Path> getPredicate() {
        final String expression = tfRegex.getText();
        final boolean isAbsolute = rbAbsolutePath.isSelected();

        return path -> {
            String strPath = (isAbsolute ? path : path.getFileName()).toString();
            return Exceptions.call(strPath::matches, expression).orElse(false);
        };
    }

    private void commit() {
        tempRegex = tfRegex.getText();
        tempToggle = toggleGroup.getSelectedToggle();
    }

    private void rollback() {
        tfRegex.setText(tempRegex);
        toggleGroup.selectToggle(tempToggle);
    }

}