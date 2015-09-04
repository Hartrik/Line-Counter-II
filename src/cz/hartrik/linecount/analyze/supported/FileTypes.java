package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.common.Exceptions;
import cz.hartrik.linecount.analyze.FileType;
import java.io.InputStream;
import java.util.*;

/**
 * Spravuje podporované typy souborů.
 *
 * @version 2015-09-04
 * @author Patrik Harag
 */
public class FileTypes {

    private FileTypes() { }

    static final String DEFAULT_FILE_TYPES_NAME = "file types.xml";

    public static final FileType OTHER = new FileTypeImpl(
            "<?>", DataType.UNKNOWN, CommentStyles.NONE,
            Collections.singletonList((s) -> true));

    private static final Map<String, FileType> fileTypes = new LinkedHashMap<>();

    /**
     * Načte výchozí, vestavěné typy souborů.
     */
    public static void initDefaultFileTypes() {
        initFileType(FileTypes.class.getResourceAsStream(DEFAULT_FILE_TYPES_NAME));
    }

    public static void initFileType(InputStream in) {
        Exceptions.unchecked(() -> {
            FileTypesXMLParser parser = new FileTypesXMLParser(
                    CommentStyles::getByName);

            parser.parse(in).stream()
                    .forEach(f -> fileTypes.put(f.getName(), f));
        });
    }

    public static Optional<FileType> find(String fileName) {
        return fileTypes.values().stream()
                .filter(type -> type.matches(fileName))
                .findFirst();
    }

    public static FileType getByName(String fileName) {
        return fileTypes.get(fileName);
    }

    /**
     * Vrátí neměnný slovník všech typů souborů.
     * Klíčem ve slovníku je název.
     *
     * @return typy souborů
     */
    public static Map<String, FileType> getFileTypes() {
        return Collections.unmodifiableMap(fileTypes);
    }

}