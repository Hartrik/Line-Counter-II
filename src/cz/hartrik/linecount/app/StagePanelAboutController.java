
package cz.hartrik.linecount.app;

import cz.hartrik.util.io.SimpleStringLoader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @version 2014-08-14
 * @author Patrik Harag
 */
public class StagePanelAboutController implements Initializable {

    public static final String PATH_ABOUT = "/cz/hartrik/linecount/app/about.html";
    
    @FXML protected WebView webView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webView.getEngine().loadContent(
                SimpleStringLoader.fromClassPath(PATH_ABOUT));
    }    
    
}