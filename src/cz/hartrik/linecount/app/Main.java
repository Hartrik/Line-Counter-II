
package cz.hartrik.linecount.app;

import cz.hartrik.common.ui.javafx.ExceptionDialog;
import cz.hartrik.linecount.analyze.supported.CommentStyles;
import cz.hartrik.linecount.analyze.supported.FileTypes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Vstupní třída
 *
 * @version 2015-09-07
 * @author Patrik Harag
 */
public class Main extends Application {

    public static final String FILE_FXML = "StageContent.fxml";
    public static final String FILE_FRAME_ICON = "icon - text (32).png";

    public static final Path USER_DIR = Paths.get("");
    public static final String FILE_USER_COMMENT_STYLES = "comment styles.xml";
    public static final String FILE_USER_FILE_TYPES = "file types.xml";

    /**
     * Vstupní metoda
     *
     * @param args argumenty příkazové řádky
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        // načtení konfigurace
        CommentStyles.initDefaultStyles();
        initUserCommentStyles(USER_DIR.resolve(FILE_USER_COMMENT_STYLES));

        FileTypes.initDefaultFileTypes();
        initUserFileTypes(USER_DIR.resolve(FILE_USER_FILE_TYPES));

        // GUI
        Parent root = FXMLLoader.load(getClass().getResource(FILE_FXML));
        Scene scene = new Scene(root);

        stage.setMinHeight(400);
        stage.setMinWidth(700);
        stage.setTitle("Počítač řádků II");

        stage.setScene(scene);
        stage.getIcons().add(new Image(
                getClass().getResourceAsStream(FILE_FRAME_ICON)));

        stage.show();
    }

    private static void initUserCommentStyles(Path file) throws IOException {
        try {
            if (Files.isReadable(file))
                CommentStyles.initStyles(Files.newInputStream(file));

        } catch (Exception e) {
            showErrorDialog(
                    "Chyba", "Chyba při načítání konfigurace",
                    "Došlo k chybě při načítání konfiguračního souboru: "
                            + FILE_USER_COMMENT_STYLES, e);
        }
    }

    private static void initUserFileTypes(Path file) throws IOException {
        try {
            if (Files.isReadable(file))
                FileTypes.initFileType(Files.newInputStream(file));

        } catch (Exception e) {
            showErrorDialog(
                    "Chyba", "Chyba při načítání konfigurace",
                    "Došlo k chybě při načítání konfiguračního souboru: "
                            + FILE_USER_FILE_TYPES, e);
        }
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