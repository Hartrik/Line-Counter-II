package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.linecount.analyze.DataTypeCode;
import cz.hartrik.linecount.analyze.SourceCodeAnalyzer;
import cz.hartrik.linecount.analyze.load.TextLoaders;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Testuje jednotlivé podporované jazyky. <p>
 * Testovací zdrojové kódy mají komentáře o délce 10 znaků a odsazení dlouhé
 * 2 nebo 4 znaky.
 *
 * @version 2015-09-08
 * @author Patrik Harag
 */
public class FileTypesTest {

    private static Path folder;

    @BeforeClass
    public static void init() {
        CommentStyles.initDefaultStyles();
        FileTypes.initDefaultFileTypes();

        folder = Paths.get("")
                .resolve("test/cz/hartrik/linecount/analyze/supported/files/");
    }

    private static DataTypeCode analyze(String fileName, String lang) {
        SourceCodeAnalyzer analyzer = new SourceCodeAnalyzer();

        return analyzer.analyze(
                folder.resolve(fileName),
                FileTypes.getByName(lang),
                TextLoaders.standardTextLoader("UTF-8"));
    }

    @Test
    public void testJava() throws IOException {
        DataTypeCode data = analyze("JavaTest.java", "Java");

        assertThat(data.getLinesTotal(), equalTo(26));
        assertThat(data.getLinesEmpty(), equalTo(6));
        assertThat(data.getLinesComment(), equalTo(5));

        assertThat(data.getCharsIndent(), equalTo(4 * 24));
        assertThat(data.getCharsComment(), equalTo(10 * 9));
    }

    @Test
    public void testPython() throws IOException {
        DataTypeCode data = analyze("Python test.py", "Python");

        assertThat(data.getLinesTotal(), equalTo(25));
        assertThat(data.getLinesEmpty(), equalTo(5));
        assertThat(data.getLinesComment(), equalTo(1));

        assertThat(data.getCharsIndent(), equalTo(4 * 20));
        assertThat(data.getCharsComment(), equalTo(10 * 6));
    }

    @Test
    public void testCSS() throws IOException {
        DataTypeCode data = analyze("CSS test.css", "CSS");

        assertThat(data.getLinesTotal(), equalTo(15));
        assertThat(data.getLinesEmpty(), equalTo(3));
        assertThat(data.getLinesComment(), equalTo(3));

        assertThat(data.getCharsIndent(), equalTo(5 * 4));
        assertThat(data.getCharsComment(), equalTo(10 * 4));
    }

    @Test
    public void testJS() throws IOException {
        DataTypeCode data = analyze("JavaScript test.js", "JavaScript");

        assertThat(data.getLinesTotal(), equalTo(25));
        assertThat(data.getLinesEmpty(), equalTo(7));
        assertThat(data.getLinesComment(), equalTo(3));

        assertThat(data.getCharsIndent(), equalTo(6 * 4));
        assertThat(data.getCharsComment(), equalTo(10 * 10));
    }

    @Test
    public void testPHP() throws IOException {
        DataTypeCode data = analyze("PHP test.php", "PHP");

        assertThat(data.getLinesTotal(), equalTo(22));
        assertThat(data.getLinesEmpty(), equalTo(6));
        assertThat(data.getLinesComment(), equalTo(4));

        assertThat(data.getCharsIndent(), equalTo(12 * 4));
        assertThat(data.getCharsComment(), equalTo(13 * 10));
    }

    @Test
    public void testPascal() throws IOException {
        DataTypeCode data = analyze("Pascal test.pas", "Pascal");

        assertThat(data.getLinesTotal(), equalTo(25));
        assertThat(data.getLinesEmpty(), equalTo(8));
        assertThat(data.getLinesComment(), equalTo(4));

        assertThat(data.getCharsIndent(), equalTo(10 * 4));
        assertThat(data.getCharsComment(), equalTo(8 * 10));
    }

    @Test
    public void testVBScript() throws IOException {
        DataTypeCode data = analyze("VBScript test.vbs", "VBScript");

        assertThat(data.getLinesTotal(), equalTo(26));
        assertThat(data.getLinesEmpty(), equalTo(10));
        assertThat(data.getLinesComment(), equalTo(8));

        assertThat(data.getCharsIndent(), equalTo(7 * 4));
        assertThat(data.getCharsComment(), equalTo(10 * 10));
    }

    @Test
    public void testCSharp() throws IOException {
        DataTypeCode data = analyze("C Sharp test.cs", "C#");

        assertThat(data.getLinesTotal(), equalTo(22));
        assertThat(data.getLinesEmpty(), equalTo(6));
        assertThat(data.getLinesComment(), equalTo(4));

        assertThat(data.getCharsIndent(), equalTo(13 * 4));
        assertThat(data.getCharsComment(), equalTo(11 * 10));
    }

    @Test
    public void testGroovy() throws IOException {
        DataTypeCode data = analyze("GroovyTest.groovy", "Groovy");

        assertThat(data.getLinesTotal(), equalTo(31));
        assertThat(data.getLinesEmpty(), equalTo(8));
        assertThat(data.getLinesComment(), equalTo(5));

        assertThat(data.getCharsIndent(), equalTo(33 * 4));
        assertThat(data.getCharsComment(), equalTo(13 * 10));
    }

    @Test
    public void testClojure() throws IOException {
        DataTypeCode data = analyze("Clojure test.clj", "Clojure");

        assertThat(data.getLinesTotal(), equalTo(19));
        assertThat(data.getLinesEmpty(), equalTo(4));
        assertThat(data.getLinesComment(), equalTo(2));

        assertThat(data.getCharsIndent(), equalTo(19 * 2));
        assertThat(data.getCharsComment(), equalTo(7 * 10));
    }

    @Test
    public void testLua() throws IOException {
        DataTypeCode data = analyze("Lua test.lua", "Lua");

        assertThat(data.getLinesTotal(), equalTo(21));
        assertThat(data.getLinesEmpty(), equalTo(6));
        assertThat(data.getLinesComment(), equalTo(4));

        assertThat(data.getCharsIndent(), equalTo(3 * 4));
        assertThat(data.getCharsComment(), equalTo(6 * 10));
    }

}