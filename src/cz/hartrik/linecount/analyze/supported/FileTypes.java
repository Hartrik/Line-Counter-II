package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.common.Exceptions;
import cz.hartrik.linecount.analyze.FileType;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

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

                                                      // ordered
    private static final Set<FileType> fileTypes = new LinkedHashSet<>();

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

            parser.parse(in).stream().forEach(fileTypes::add);
        });
    }

    public static Optional<FileType> find(String fileName) {
        return fileTypes.stream()
                .filter(type -> type.matches(fileName))
                .findFirst();
    }

    /**
     * Vrátí neměnnou množinu všech typů souborů.
     *
     * @return typy souborů
     */
    public static Set<FileType> getFileTypes() {
        return Collections.unmodifiableSet(fileTypes);
    }

}