
package cz.hartrik.code.analyze.linecount;

import cz.hartrik.code.analyze.FileType;

/**
 * Objekt shromažďující data o typu souboru.
 *
 * @version 2014-08-05
 * @author Patrik Harag
 */
public class DataTypeFile {

    private final FileType fileType;

    private int files;
    private long size;

    public DataTypeFile(FileType fileType) {
        this.fileType = fileType;
    }

    public FileType getFileType() {
        return fileType;
    }

    public long getSizeTotal() { return size; }
    public int  getFiles()     { return files; }

    public void addSizeTotal(long size) { this.size += size; }
    public void addFiles(int files)     { this.files += files; }

}