
package cz.hartrik.linecount.app;

import cz.hartrik.code.analyze.linecount.DataTypeCode;
import cz.hartrik.dialog.custom.script.ScriptDialog;
import cz.hartrik.util.io.SimpleStringLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 * @version 2014-08-11
 * @author Patrik Harag
 */
public class CustomOutputManager {
    
    protected static final String SCRIPT_PATH = "/cz/hartrik/linecount/app/"
                                              + "CustomOutputScript.js";
    
    protected static final String TITLE = "Výstup";
    protected static final String DEFAULT_ENGINE = "js";
    
    protected ScriptDialog scriptDialog = null;
    
    public void showOutputDialog(Window window, Collection<DataTypeCode> data) {
        if (scriptDialog == null) {
            String code = SimpleStringLoader.fromClassPath(SCRIPT_PATH);
            
            scriptDialog = new ScriptDialog(window, null, DEFAULT_ENGINE, code);
            scriptDialog.setTitle(TITLE);
            scriptDialog.initModality(Modality.APPLICATION_MODAL);
        }
        
        List<DataTypeCode> list = new ArrayList<>(data);
        scriptDialog.setBindings(Collections.singletonMap("data", list));
        scriptDialog.showAndWait();
    }
    
}