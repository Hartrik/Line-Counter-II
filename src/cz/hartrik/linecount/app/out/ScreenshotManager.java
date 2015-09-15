package cz.hartrik.linecount.app.out;

import cz.hartrik.common.Exceptions;
import java.io.File;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.Window;
import javax.imageio.ImageIO;

/**
 * Spravuje ukládání snímků uživatelského rozhraní.
 *
 * @version 2015-09-13
 * @author Patrik Harag
 */
public class ScreenshotManager {

    private final SaveFileChooser saveFileChooser;

    public ScreenshotManager(SaveFileChooser chooser) {
        this.saveFileChooser = chooser;
    }

    public void save(Node node, Window owner) {
        WritableImage image = node.snapshot(new SnapshotParameters(), null);

        saveFileChooser.setExtensionFilter("PNG", "*.png");
        File file = saveFileChooser.showDialog(owner);

        if (file == null)
            return;

        Exceptions.unchecked(() -> {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        });
    }

}