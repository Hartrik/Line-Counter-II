package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.linecount.analyze.CommentStyle;
import cz.hartrik.linecount.analyze.FileType;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @version 2015-09-04
 * @author Patrik Harag
 */
public class FileTypesXMLParser {

    public static final String TAG_ROOT = "file-types";
    public static final String TAG_FILE_TYPE = "file-type";
    public static final String ARGUMENT_FT_NAME = "name";
    public static final String ARGUMENT_FT_TYPE = "type";
    public static final String ARGUMENT_FT_COMMENT_STYLE = "comment-style";

    public static final String TAG_FILE_TYPE_FILTER = "filter";
    public static final String ARGUMENT_FT_FILTER_REGEX = "regex";

    private final Function<String, CommentStyle> commentStylesSupplier;

    public FileTypesXMLParser(Function<String, CommentStyle> csSupplier) {
        this.commentStylesSupplier = csSupplier;
    }

    /**
     * Načte typy souborů z XML. Předpokládá správně formátovaný XML soubor.
     *
     * @param inputStream vstupní proud
     * @return typy souborů
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public List<FileType> parse(InputStream inputStream)
            throws IOException, SAXException, ParserConfigurationException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(inputStream);
        document.getDocumentElement().normalize();

        Element documentElement = document.getDocumentElement();
        NodeList list = documentElement.getElementsByTagName(TAG_FILE_TYPE);

        return createStream(list)
                .map(this::parseFileType)
                .collect(Collectors.toList());
    }

    protected FileType parseFileType(Element element) {
        String aName = element.getAttribute(ARGUMENT_FT_NAME);
        String aType = element.getAttribute(ARGUMENT_FT_TYPE);
        String aCS = element.getAttribute(ARGUMENT_FT_COMMENT_STYLE);

        DataType type = parseDataType(aType);
        CommentStyle commentStyle = parseCommentStyle(aCS);

        NodeList filterNodes = element.getElementsByTagName(TAG_FILE_TYPE_FILTER);

        List<Predicate<String>> filters = createStream(filterNodes)
                .map(this::parseFilter)
                .collect(Collectors.toList());

        return new FileTypeImpl(aName, type, commentStyle, filters);
    }

    protected DataType parseDataType(String string) {
        if (string.equalsIgnoreCase(DataType.SOURCE.name()))
            return DataType.SOURCE;
        else if (string.equalsIgnoreCase(DataType.TEXT.name()))
            return DataType.TEXT;
        else if (string.equalsIgnoreCase(DataType.UNKNOWN.name()))
            return DataType.UNKNOWN;
        else
            throw new IllegalArgumentException("Illegal DataType - " + string);
    }

    protected CommentStyle parseCommentStyle(String string) {
        CommentStyle commentStyle = commentStylesSupplier.apply(string);

        if (commentStyle != null)
            return commentStyle;
        else
            throw new IllegalArgumentException("Comment style not found - " + string);
    }

    protected Predicate<String> parseFilter(Element element) {
        String regex = element.getAttribute(ARGUMENT_FT_FILTER_REGEX);

        Pattern pattern = Pattern.compile(regex);
        return (fileName) -> pattern.matcher(fileName).matches();
    }

    // pomocné metody

    private Stream<Element> createStream(NodeList elements) {
        return IntStream.range(0, elements.getLength())
                .mapToObj(i -> (Element) elements.item(i));
    }

}