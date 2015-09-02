
package cz.hartrik.linecount.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Vstupní třída
 * 
 * @version 2014-08-05
 * @author Patrik Harag
 */
public class Main extends Application {
    
    public static final String FILE_FXML = "StageContent.fxml";
    public static final String FILE_FRAME_ICON = "icon - text (32).png";
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(FILE_FXML));
        Scene scene = new Scene(root);
        
        stage.setMinHeight(400);
        stage.setMinWidth(700);
        stage.setTitle("Počítač řádků II");
        
        stage.setScene(scene);
        stage.getIcons().add(new Image(
                this.getClass().getResourceAsStream(FILE_FRAME_ICON))); 
        
        stage.show();
    }

    /**
     * Vstupní metoda
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
