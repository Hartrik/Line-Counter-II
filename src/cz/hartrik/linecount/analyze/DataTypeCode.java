
package cz.hartrik.linecount.analyze;

/**
 * Objekt shromažďující data o zdrojových souborech napsaných v určitém
 * programovacím jazyce.
 *
 * @version 2015-09-06
 * @author Patrik Harag
 */
public class DataTypeCode extends DataTypeText {

    private int linesCode;
    private int linesComment;
    private int linesEmpty;

    private int charsComment;
    private int charsIndent;

    public DataTypeCode(FileType fileType) {
        super(fileType);
    }

    // --- get

    public int getLinesCode()    { return linesCode; }
    public int getLinesComment() { return linesComment; }
    public int getLinesEmpty()   { return linesEmpty; }

    public int getCharsIndent()  { return charsIndent; }
    public int getCharsComment() { return charsComment; }

    // --- add

    public void addLinesCode(int lines)    { this.linesCode += lines; }
    public void addLinesEmpty(int lines)   { this.linesEmpty += lines; }
    public void addLinesComment(int lines) { this.linesComment += lines; }

    public void addCharsIndent(int chars)  { this.charsIndent += chars; }
    public void addCharsComment(int chars) { this.charsComment += chars; }

    // Object

    @Override
    public String toString() {
        return String.format(
                  "Data/Text[files=%d, size=%d, "
                + "lines=%d, lines/code=%d, lines/comment=%d, lines/empty=%d, "
                + "chars=%d, chars/ws=%d, chars/comment=%d, chars/indent=%d]",
                getFiles(), getSizeTotal(),
                getLinesTotal(), getLinesCode(), getLinesComment(), getLinesEmpty(),
                getCharsTotal(), getCharsWhitespace(), getCharsComment(), getCharsIndent());
    }

}