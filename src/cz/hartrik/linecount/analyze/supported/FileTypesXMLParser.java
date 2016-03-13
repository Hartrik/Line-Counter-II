package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.linecount.analyze.CommentStyle;
import cz.hartrik.linecount.analyze.FileType;
import cz.hartrik.linecount.analyze.FileType.DataType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Slouží k parsování podporovaných typů souborů z XML dokumentu.
 *
 * @version 2016-02-28
 * @author Patrik Harag
 */
public class FileTypesXMLParser extends XMLParserBase {

    public static final URL XSD_URL =
            CommentStylesXMLParser.class.getResource("file types.xsd");

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
     * @throws JAXBException
     * @throws ParserConfigurationException
     */
    public List<FileType> parse(InputStream inputStream)
            throws IOException, SAXException, JAXBException,
                   ParserConfigurationException {

        Element docElement = loadXMLDocument(inputStream).getDocumentElement();
        validateXMLSchema(XSD_URL, docElement);

        return createStream(docElement.getElementsByTagName(TAG_FILE_TYPE))
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

        return FileType.of(aName, type, commentStyle, filters);
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

        final Pattern pattern = Pattern.compile(regex);
        return new Filter(pattern);
    }

    private static class Filter implements Predicate<String> {
        private final Pattern pattern;

        private Filter(Pattern pattern) {
            this.pattern = pattern;
        }

        @Override
        public boolean test(String text) {
            return pattern.matcher(text).matches();
        }

        @Override
        public String toString() {
            return pattern.pattern();
        }
    }

}