
package cz.hartrik.linecount.app;

import cz.hartrik.code.analyze.linecount.DataTypeCode;
import cz.hartrik.common.io.Resources;
import cz.hartrik.jfxeditor.CodeEditor;
import cz.hartrik.jfxeditor.build.CodeMirrorBuilder;
import cz.hartrik.jfxeditor.build.Template;
import cz.hartrik.jfxeditor.codemirror.CMResources;
import cz.hartrik.jfxeditor.dialog.ScriptDialog;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 * @version 2015-09-02
 * @author Patrik Harag
 */
public class CustomOutputManager {

    protected static final String SCRIPT_FILE = "CustomOutputScript.js";

    protected static final String TITLE = "Výstup";
    protected static final String DEFAULT_ENGINE = "js";

    protected ScriptDialog scriptDialog = null;

    public void showOutputDialog(Window window, Collection<DataTypeCode> data) {
        if (scriptDialog == null) {
            scriptDialog = createDialog(window, data);
            scriptDialog.initModality(Modality.APPLICATION_MODAL);
        }

        scriptDialog.showAndWait();
    }

    private ScriptDialog createDialog(Window window, Collection<DataTypeCode> data) {
        String template = new CodeMirrorBuilder(Template.load())
                .addTheme(CMResources.themeDefault())
                .addTheme(CMResources.themeEclipse())
                .addScript(CMResources.scriptBase())
                .addScript(CMResources.modeJavaScript())
                .addScript(CMResources.addonActiveLine())
                .addScript(CMResources.addonMatchBrackets())
                .selectMode("text/javascript")
                .selectTheme("eclipse")
                .build();

        CodeEditor cd = new CodeEditor("text", template);
        Map<String, Supplier<?>> map = Collections.singletonMap("data", () -> data);
        String def = Resources.text(SCRIPT_FILE, getClass());

        ScriptDialog dialog = new ScriptDialog(cd, window, map, DEFAULT_ENGINE, def);
        dialog.setWidth(700);
        dialog.setMinHeight(500);
        dialog.setTitle(TITLE);

        return dialog;
    }

}