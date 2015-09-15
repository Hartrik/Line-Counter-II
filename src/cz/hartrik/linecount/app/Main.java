
package cz.hartrik.linecount.app;

import cz.hartrik.common.ui.javafx.ExceptionDialog;
import cz.hartrik.linecount.analyze.supported.CommentStyles;
import cz.hartrik.linecount.analyze.supported.FileTypes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Vstupní třída
 *
 * @version 2015-09-15
 * @author Patrik Harag
 */
public class Main extends Application {

    public static final String APP_VERSION = "1.3";
    public static final String APP_VERSION_DATE = "2015-09-15";

    static final String FILE_FXML = "StageContent.fxml";
    static final String FILE_FRAME_ICON = "icon - text (32).png";

    public static final Path USER_DIR = Paths.get("");
    static final String FILE_USER_COMMENT_STYLES = "comment styles.xml";
    static final String FILE_USER_FILE_TYPES = "file types.xml";

    private static ResourceBundle resourceBundle;

    /**
     * Vstupní metoda
     *
     * @param args argumenty příkazové řádky
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        resourceBundle = ResourceBundle.getBundle(
                "cz.hartrik.linecount.app.strings", new Locale("cs", "CZ"));

        Thread thread = new Thread(() -> loadConfiguration());
        thread.start();

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource(FILE_FXML), resourceBundle);

        stage.setMinHeight(400);
        stage.setMinWidth(700);
        stage.setTitle(resourceBundle.getString("app-title"));

        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(
                getClass().getResourceAsStream(FILE_FRAME_ICON)));

        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);  // zastaví ostatní vlákna
        });

        stage.show();
    }

    private static void loadConfiguration() {
        CommentStyles.initDefaultStyles();
        initUserCommentStyles(USER_DIR.resolve(FILE_USER_COMMENT_STYLES));

        FileTypes.initDefaultFileTypes();
        initUserFileTypes(USER_DIR.resolve(FILE_USER_FILE_TYPES));
    }

    private static void initUserCommentStyles(Path file) {
        try {
            if (Files.isReadable(file))
                CommentStyles.initStyles(Files.newInputStream(file));

        } catch (Exception e) {
            showErrorDialog(resourceBundle, FILE_USER_COMMENT_STYLES, e);
        }
    }

    private static void initUserFileTypes(Path file) {
        try {
            if (Files.isReadable(file))
                FileTypes.initFileType(Files.newInputStream(file));

        } catch (Exception e) {
            showErrorDialog(resourceBundle, FILE_USER_FILE_TYPES, e);
        }
    }

    private static void showErrorDialog(ResourceBundle rb, String file, Exception e) {
        Platform.runLater(() -> {
            showErrorDialog(
                    rb.getString("dialog/config-err/title"),
                    rb.getString("dialog/config-err/header"),
                    String.format(rb.getString("dialog/config-err/content"), file),
                    e);
        });
    }

    private static void showErrorDialog(
            String title, String header, String content, Exception e) {

        ExceptionDialog dialog = new ExceptionDialog(e);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        dialog.getDialogPane().setPrefWidth(450);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(
                new Image(Main.class.getResourceAsStream(FILE_FRAME_ICON)));

        dialog.showAndWait();
    }

}