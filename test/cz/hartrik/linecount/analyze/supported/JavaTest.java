package cz.hartrik.linecount.analyze.supported;

/** comment1 */
public class JavaTest {

    public static void main(String[] args) {
        method();
    }

    private static void method() {
        // comment 2
        int number = 1 + 2; /*co
m
ment3*/

        // comment 4
        String text1 = "komentáře po 10 znacích";
        String text2 = "some // this is not comment";
        String text3 = "some /* this is not comment */";   // comment 5

        String text4 = "escape \" and literal " + /* comment6 */'"'/* comment7 */
                + '\"' + /* comment8 */ "\""; /* comment9 */
    }

}