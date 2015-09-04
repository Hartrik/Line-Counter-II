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
 * @version 2015-09-04
 * @author Patrik Harag
 */
public class FileTypesTest {

    private static Path folder;

    @BeforeClass
    public static void init() {
        CommentStyles.initDefaultStyles();
        FileTypes.initDefaultFileTypes();

        folder = Paths.get("")
                .resolve("test/cz/hartrik/linecount/analyze/supported/");
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
                folder.resolve("python test.py"), FileTypes.getByName("Python"));

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
                folder.resolve("css test.css"), FileTypes.getByName("CSS"));

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

        assertThat(data.getLinesTotal(), equalTo(20));
        assertThat(data.getLinesEmpty(), equalTo(5));
        assertThat(data.getLinesComment(), equalTo(3));

        assertThat(data.getCharsIndent(), equalTo(6 * 4));
        assertThat(data.getCharsComment(), equalTo(10 * 8));
    }

}