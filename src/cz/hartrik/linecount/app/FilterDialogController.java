
package cz.hartrik.linecount.app;

import cz.hartrik.common.Exceptions;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @version 2015-09-02
 * @author Patrik Harag
 */
public class FilterDialogController implements Initializable {

    @FXML protected TextField tfRegex;
    @FXML protected RadioButton rbAbsolutePath;

    protected boolean closed;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    protected void okButton() {
        closed = false;
        getDialog().close();
    }

    @FXML
    protected void cancelButton() {
        closed = true;
        getDialog().close();
    }

    private FilterDialog getDialog() {
        return (FilterDialog) tfRegex.getScene().getWindow();
    }

    protected void setRegex(String regex) {
        tfRegex.setText(regex);
    }

    protected Predicate<Path> createPredicate() {
        final String expression = tfRegex.getText();
        final boolean isAbsolute = rbAbsolutePath.isSelected();

        return path -> {
            String strPath = (isAbsolute ? path : path.getFileName()).toString();
            return Exceptions.call(strPath::matches, expression).orElse(false);
        };
    }

}