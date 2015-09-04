
package cz.hartrik.linecount.analyze;

/**
 * Objekt shromažďující data o souboru textového typu.
 *
 * @version 2015-09-04
 * @author Patrik Harag
 */
public class DataTypeText extends DataTypeFile {

    private int linesTotal;

    private int charsTotal;
    private int charsWS;

    public DataTypeText(FileType fileType) {
        super(fileType);
    }

    // -- get

    public int getLinesTotal() { return linesTotal; }

    public int getCharsTotal()      { return charsTotal; }
    public int getCharsWhitespace() { return charsWS; }

    // -- add

    public void addLinesTotal(int lines) { this.linesTotal += lines; }

    public void addCharsTotal(int chars)      { this.charsTotal += chars; }
    public void addCharsWhitespace(int chars) { this.charsWS += chars; }

    // Object

    @Override
    public String toString() {
        return String.format("Data/Text[files=%d, size=%d, lines=%d, chars=%d, ws=%d]",
                getFiles(), getSizeTotal(), linesTotal, charsTotal, charsWS);
    }

}