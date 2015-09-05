package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.linecount.analyze.DataTypeCode;
import cz.hartrik.linecount.analyze.SourceCodeAnalyzer;
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
 * 4 znaky.
 *
 * @version 2015-09-05
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

    @Test
    public void testJava() throws IOException {
        SourceCodeAnalyzer analyzer = new SourceCodeAnalyzer();
        DataTypeCode data = analyzer.analyze(
                folder.resolve("JavaTest.java"), FileTypes.getByName("Java"));

        assertThat(data.getLinesTotal(), equalTo(25));
        assertThat(data.getLinesEmpty(), equalTo(6));
        assertThat(data.getLinesComment(), equalTo(5));

        assertThat(data.getCharsIndent(), equalTo(4 * 24));
        assertThat(data.getCharsComment(), equalTo(10 * 9));
    }

    @Test
    public void testPython() throws IOException {
        SourceCodeAnalyzer analyzer = new SourceCodeAnalyzer();
        DataTypeCode data = analyzer.analyze(
                folder.resolve("Python test.py"), FileTypes.getByName("Python"));

        assertThat(data.getLinesTotal(), equalTo(25));
        assertThat(data.getLinesEmpty(), equalTo(5));
        assertThat(data.getLinesComment(), equalTo(1));

        assertThat(data.getCharsIndent(), equalTo(4 * 20));
        assertThat(data.getCharsComment(), equalTo(10 * 6));
    }

    @Test
    public void testCSS() throws IOException {
        SourceCodeAnalyzer analyzer = new SourceCodeAnalyzer();
        DataTypeCode data = analyzer.analyze(
                folder.resolve("CSS test.css"), FileTypes.getByName("CSS"));

        assertThat(data.getLinesTotal(), equalTo(15));
        assertThat(data.getLinesEmpty(), equalTo(3));
        assertThat(data.getLinesComment(), equalTo(3));

        assertThat(data.getCharsIndent(), equalTo(5 * 4));
        assertThat(data.getCharsComment(), equalTo(10 * 4));
    }

    @Test
    public void testJS() throws IOException {
        SourceCodeAnalyzer analyzer = new SourceCodeAnalyzer();
        DataTypeCode data = analyzer.analyze(
                folder.resolve("JavaScript test.js"),
                FileTypes.getByName("JavaScript"));

        assertThat(data.getLinesTotal(), equalTo(24));
        assertThat(data.getLinesEmpty(), equalTo(6));
        assertThat(data.getLinesComment(), equalTo(3));

        assertThat(data.getCharsIndent(), equalTo(6 * 4));
        assertThat(data.getCharsComment(), equalTo(10 * 10));
    }

    @Test
    public void testPHP() throws IOException {
        SourceCodeAnalyzer analyzer = new SourceCodeAnalyzer();
        DataTypeCode data = analyzer.analyze(
                folder.resolve("PHP test.php"), FileTypes.getByName("PHP"));

        assertThat(data.getLinesTotal(), equalTo(22));
        assertThat(data.getLinesEmpty(), equalTo(6));
        assertThat(data.getLinesComment(), equalTo(4));

        assertThat(data.getCharsIndent(), equalTo(12 * 4));
        assertThat(data.getCharsComment(), equalTo(13 * 10));
    }

    @Test
    public void testPascal() throws IOException {
        SourceCodeAnalyzer analyzer = new SourceCodeAnalyzer();
        DataTypeCode data = analyzer.analyze(
                folder.resolve("Pascal test.pas"), FileTypes.getByName("Pascal"));

        assertThat(data.getLinesTotal(), equalTo(25));
        assertThat(data.getLinesEmpty(), equalTo(8));
        assertThat(data.getLinesComment(), equalTo(4));

        assertThat(data.getCharsIndent(), equalTo(10 * 4));
        assertThat(data.getCharsComment(), equalTo(8 * 10));
    }

    @Test
    public void testVBScript() throws IOException {
        SourceCodeAnalyzer analyzer = new SourceCodeAnalyzer();
        DataTypeCode data = analyzer.analyze(
                folder.resolve("VBScript test.vbs"),
                FileTypes.getByName("VBScript"));

        assertThat(data.getLinesTotal(), equalTo(26));
        assertThat(data.getLinesEmpty(), equalTo(10));
        assertThat(data.getLinesComment(), equalTo(8));

        assertThat(data.getCharsIndent(), equalTo(7 * 4));
        assertThat(data.getCharsComment(), equalTo(10 * 10));
    }

    @Test
    public void testCSharp() throws IOException {
        SourceCodeAnalyzer analyzer = new SourceCodeAnalyzer();
        DataTypeCode data = analyzer.analyze(
                folder.resolve("C Sharp test.cs"), FileTypes.getByName("C#"));

        assertThat(data.getLinesTotal(), equalTo(22));
        assertThat(data.getLinesEmpty(), equalTo(6));
        assertThat(data.getLinesComment(), equalTo(4));

        assertThat(data.getCharsIndent(), equalTo(13 * 4));
        assertThat(data.getCharsComment(), equalTo(11 * 10));
    }

}