package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.linecount.analyze.CommentStyle;
import cz.hartrik.linecount.analyze.FileType;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @version 2015-09-04
 * @author Patrik Harag
 */
public class FileTypeImpl implements FileType {

    private final String name;
    private final DataType dataType;
    private final CommentStyle commentStyle;
    private final List<Predicate<String>> filters;

    public FileTypeImpl(String name, DataType dataType,
            CommentStyle commentStyle, List<Predicate<String>> filters) {

        this.name = name;
        this.dataType = dataType;
        this.commentStyle = commentStyle;
        this.filters = filters;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CommentStyle getCommentStyle() {
        return commentStyle;
    }

    @Override
    public List<Predicate<String>> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    @Override
    public boolean isSourceCode() {
        return dataType == DataType.SOURCE;
    }

    @Override
    public boolean isTextDocument() {
        return dataType == DataType.TEXT || isSourceCode();
    }

    @Override
    public boolean matches(String fileName) {
        return filters.stream()
                .filter(f -> f.test(fileName))
                .findAny()
                    .isPresent();
    }

}