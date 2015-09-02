
package cz.hartrik.linecount.app;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.PatternSyntaxException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 * 
 * @version 2014-08-14
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
            try {
                return (isAbsolute
                        ? path.toString()
                        : path.getFileName().toString())
                                .matches(expression);
            } catch (PatternSyntaxException e) {
                return false;
            }
        };
    }
    
}