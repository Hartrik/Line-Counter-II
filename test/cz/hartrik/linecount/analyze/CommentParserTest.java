
package cz.hartrik.linecount.analyze;

import cz.hartrik.linecount.analyze.supported.CommentStyles;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @version 2015-09-03
 * @author Patrik Harag
 */
public class CommentParserTest {

    @BeforeClass
    public static void initStyles() {
        CommentStyles.initDefaultStyles();
    }

    private final CommentStyle javaCS = CommentStyles.getByName("JAVA");
    private final CommentParser parser = new CommentParser(javaCS);

    // testy metody pro hledání nejbližšího začátku komentáře nebo
    //   ignorované oblasti

    @Test
    public void textNextComment_Simple() {
        CommentParser.SearchResult r = parser.getNextStart(
                new StringBuilder("01234//789"), javaCS.getCommentPatterns(), 0);

        assertThat(r.start, equalTo(5));
        assertThat(r.end, equalTo(7));
    }

    @Test
    public void textNextComment_Repeat() {
        CommentParser.SearchResult r = parser.getNextStart(
                new StringBuilder("01234//789//cd"), javaCS.getCommentPatterns(), 0);

        assertThat(r.start, equalTo(5));
        assertThat(r.end, equalTo(7));
    }

    @Test
    public void textNextComment_Multiple() {
        CommentParser.SearchResult r = parser.getNextStart(
                new StringBuilder("0123//*67/*cd"), javaCS.getCommentPatterns(), 0);

        assertThat(r.start, equalTo(4));
        assertThat(r.end, equalTo(6));
    }

    @Test
    public void textNextComment_Null() {
        CommentParser.SearchResult r = parser.getNextStart(
                new StringBuilder("0123456789"), javaCS.getCommentPatterns(), 0);

        assertThat(r, nullValue());
    }

    // testy parsování komentářů

    @Test
    public void testLineComment_1() {
        List<String> out = parser.analyze(" . . . .. . . . //ahoj");

        assertTrue(!parser.isContinue());
        assertTrue(out.size() == 1);
        assertEquals("ahoj", out.get(0));
    }

    @Test
    public void testLineComment_2() {
        List<String> out = parser.analyze(" . .. .. //ahoj\n" + "/// světe");

        assertTrue(!parser.isContinue());
        assertEquals("ahoj", out.get(0));
        assertEquals("/ světe", out.get(1));
        assertTrue(out.size() == 2);
    }

    @Test
    public void testLineComment_3() {
        List<String> out = parser.analyze(" . . . ..   //*ahoj \n světe*/");

        assertTrue(!parser.isContinue());
        assertTrue(out.size() == 1);
        assertEquals("*ahoj ", out.get(0));
    }

    @Test
    public void testBlockComment_1() {
        List<String> out = parser.analyze("/*ahoj*/ . .");

        assertTrue(!parser.isContinue());
        assertTrue(out.size() == 1);
        assertEquals("ahoj", out.get(0));
    }

    @Test
    public void testBlockComment_2() {
        List<String> out = parser.analyze("/*ahoj");

        assertTrue(parser.isContinue()); // !
        assertTrue(out.size() == 1);
        assertEquals("ahoj", out.get(0));
    }

    @Test
    public void testBlockComment_3() {
        List<String> out = parser.analyze(" . .  /*ahoj*//* světe*/ ... . .");

        assertTrue(!parser.isContinue());
        assertTrue(out.size() == 2);
        assertEquals("ahoj", out.get(0));
        assertEquals(" světe", out.get(1));
    }

    @Test
    public void testBlockComment_4() {
        List<String> out = parser.analyze(" . . .. /*ahoj \n světe*/ ... . .");

        assertTrue(!parser.isContinue());
        assertTrue(out.size() == 1);
        assertEquals("ahoj \n světe", out.get(0));
    }

    @Test
    public void testJavaDocComent() {
        List<String> out = parser.analyze(" . . /**ahoj*/");

        assertTrue(!parser.isContinue());
        assertTrue(out.size() == 1);
        assertEquals("ahoj", out.get(0));
    }

    @Test
    public void testIgnore_1() {
        List<String> out = parser.analyze("String text = \" // !comment  \"");

        assertTrue(out.isEmpty());
    }

    @Test
    public void testIgnore_2() {
        List<String> out = parser.analyze("String text = \" /* !comment */ \"");

        assertTrue(out.isEmpty());
    }

    @Test
    public void testIgnore_3() {
        List<String> out = parser.analyze("/* 1 */ \"/* !comment */\"// ahoj");

        assertEquals(" 1 ", out.get(0));
        assertEquals(" ahoj", out.get(1));
        assertTrue(out.size() == 2);
    }

    @Test
    public void testIgnore_4() {
        List<String> out = parser.analyze("/*\" // \"*/");

        assertTrue(out.size() == 1);
        assertEquals("\" // \"", out.get(0));
    }

    @Test
    public void testIgnore_5() {
        List<String> out = parser.analyze(" '\"'// char literal");
        // '"'

        assertEquals(" char literal", out.get(0));
        assertTrue(out.size() == 1);
    }

    @Test
    public void testIgnore_6() {
        List<String> out = parser.analyze(" '\\\"'// char literal");
        // '\"'

        assertEquals(" char literal", out.get(0));
        assertTrue(out.size() == 1);
    }

}