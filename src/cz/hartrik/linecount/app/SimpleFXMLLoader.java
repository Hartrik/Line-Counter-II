
package cz.hartrik.linecount.app;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * Třída pro jednodušší načítání FXML.
 * 
 * @version 2014-08-14
 * @author Patrik Harag
 */
public class SimpleFXMLLoader {

    protected final Object root;
    protected final Object controller;
    
    public SimpleFXMLLoader(String resource) {
        URL url = getClass().getResource(resource);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        
        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        controller = fxmlLoader.getController();
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getController() {
        return (T) controller;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Node> T getRoot() {
        return (T) root;
    }
    
}