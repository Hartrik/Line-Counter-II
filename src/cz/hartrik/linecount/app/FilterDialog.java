
package cz.hartrik.linecount.app;

import cz.hartrik.common.ui.javafx.FXMLControlledStage;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.scene.image.Image;
import javafx.stage.Modality;

/**
 * Dialog pro tvorbu filtrů souborů.
 *
 * @version 2015-09-13
 * @author Patrik Harag
 */
public class FilterDialog
        extends FXMLControlledStage<FilterDialogController> {

    private static final String PACKAGE = "/cz/hartrik/linecount/app/";
    public static final String PATH_FXML = PACKAGE + "FilterDialog.fxml";
    public static final String PATH_ICON = PACKAGE + "icon - filter (16).png";

    public FilterDialog(ResourceBundle resourceBundle) {
        super(FilterDialog.class.getResource(PATH_FXML), resourceBundle);

        setTitle(resourceBundle.getString("dialog/filter/title"));
        setResizable(false);
        getIcons().add(new Image(PATH_ICON));
        initModality(Modality.APPLICATION_MODAL);
    }

    @Override
    public void showAndWait() {
        String temp = getRegex();

        super.showAndWait();

        if (controller.closed) {
            setRegex(temp);
        }
    }

    // gettery

    public void setRegex(String regex) {
        controller.setRegex(regex);
    }

    public String getRegex() {
        return controller.tfRegex.getText();
    }

    public Predicate<Path> getPredicate() {
        return controller.createPredicate();
    }

}