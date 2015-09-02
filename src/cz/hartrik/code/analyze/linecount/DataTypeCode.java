
package cz.hartrik.code.analyze.linecount;

import cz.hartrik.code.analyze.FileType;

/**
 * Objekt shromažďující data o zdrojových souborech napsaných v určitém
 * programovacím jazyce.
 *
 * @version 2014-08-05
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

    public int getLinesComment() { return linesComment; }
    public int getLinesEmpty()   { return linesEmpty; }

    public int getCharsIndent()  { return charsIndent; }
    public int getCharsComment() { return charsComment; }

    // -- add

    public void addLinesEmpty(int lines)   { this.linesEmpty += lines; }
    public void addLinesComment(int lines) { this.linesComment += lines; }

    public void addCharsIndent(int chars)  { this.charsIndent += chars; }
    public void addCharsComment(int chars) { this.charsComment += chars; }

}