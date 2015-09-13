
package cz.hartrik.linecount.app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @version 2015-09-13
 * @author Patrik Harag
 */
public class StagePanelAboutController implements Initializable {

    static final String PATH_ABOUT = "/cz/hartrik/linecount/app/about.html";

    @FXML private Label labelVersion;
    @FXML private Label labelVersionDate;
    @FXML private WebView webView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelVersion.setText(Main.APP_VERSION);
        labelVersionDate.setText(Main.APP_VERSION_DATE);

        String contentUrl = getClass().getResource(PATH_ABOUT).toExternalForm();
        webView.getEngine().load(contentUrl);
    }

}