
package cz.hartrik.linecount.analyze;

/**
 * Objekt shromažďující data o zdrojových souborech napsaných v určitém
 * programovacím jazyce.
 *
 * @version 2015-09-04
 * @author Patrik Harag
 */
public class DataTypeCode extends DataTypeText {

    private int linesComment;
    private int linesEmpty;

    private int charsComment;
    private int charsIndent;

    public DataTypeCode(FileType fileType) {
        super(fileType);
    }

    // --- get

    public int getLinesComment() { return linesComment; }
    public int getLinesEmpty()   { return linesEmpty; }

    public int getCharsIndent()  { return charsIndent; }
    public int getCharsComment() { return charsComment; }

    // --- add

    public void addLinesEmpty(int lines)   { this.linesEmpty += lines; }
    public void addLinesComment(int lines) { this.linesComment += lines; }

    public void addCharsIndent(int chars)  { this.charsIndent += chars; }
    public void addCharsComment(int chars) { this.charsComment += chars; }

    // Object

    @Override
    public String toString() {
        return String.format(
                  "Data/Text[files=%d, size=%d, "
                + "lines=%d, lines/comment=%d, lines/empty=%d, "
                + "chars=%d, chars/ws=%d, chars/comment=%d, chars/indent=%d]",
                getFiles(), getSizeTotal(),
                getLinesTotal(), getLinesComment(), getLinesEmpty(),
                getCharsTotal(), getCharsWhitespace(), getCharsComment(), getCharsIndent());
    }

}