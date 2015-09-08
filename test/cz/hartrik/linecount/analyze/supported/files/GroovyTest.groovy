package cz.hartrik.linecount.analyze.supported.files;

/** comment1 */
public class GroovyTest {

    public static void main(String[] args) {
        method();
    }

    def method() {
        // comment 2
        int number = 1 + 2; /*co
mm

ent*/

        // comment 4
        String text1 = "some // /* this is not comment"; // comment 5
        String text2 = 'some // /* this is not comment'; // comment 6
        String text3 = """some // /* this is not comment"""; // comment 7
        String text4 = '''some // /* this is not comment'''; // comment 8

        String multiline =""" string \n
            string \n        // this is not comment
        """; // comment 9

        String text5 = "escape \" ' " + /*comment 10*/'"'/*comment 11*/
                + '\"' + /*comment 12*/ "\""; /*comment 13*/
    }

}