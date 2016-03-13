package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.common.Pair;
import cz.hartrik.linecount.analyze.CommentStyle;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parsuje dokument se styly komentářů.
 *
 * @version 2016-02-28
 * @author Patrik Harag
 */
public class CommentStylesXMLParser extends XMLParserBase {

    /**
     * Schéma definující strukturu dokumentu.
     * Slouží k validaci.
     */
    public static final URL XSD_URL =
            CommentStylesXMLParser.class.getResource("comment styles.xsd");

    public static final String TAG_ROOT = "comment-styles";
    public static final String TAG_COMMENT_STYLE = "comment-style";
    public static final String TAG_COMMENT = "comment";
    public static final String TAG_IGNORE = "ignore";

    public static final String ARGUMENT_STYLE_NAME = "name";

    public static final String ARGUMENT_REGEX_START = "start";
    public static final String ARGUMENT_REGEX_END = "end";
    public static final String ARGUMENT_REGEX_END_DEF = "(?m)$";

    /**
     * Načte styly komentářů z XML souboru. Předpokládá správně formátovaný
     * XML soubor.
     *
     * @param inputStream vstupní proud
     * @return seznam stylů komentářů
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws JAXBException
     */
    public List<CommentStyle> parse(InputStream inputStream)
            throws IOException, SAXException, JAXBException,
                   ParserConfigurationException {

        Element dcElement = loadXMLDocument(inputStream).getDocumentElement();
        validateXMLSchema(XSD_URL, dcElement);

        return createStream(dcElement.getElementsByTagName(TAG_COMMENT_STYLE))
                .map(this::parseCommentStyle)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected CommentStyle parseCommentStyle(Element element) {
        String name = element.getAttribute(ARGUMENT_STYLE_NAME);

        NodeList commentNodes = element.getElementsByTagName(TAG_COMMENT);
        NodeList ignoreNodes = element.getElementsByTagName(TAG_IGNORE);

        List<Pair<Pattern, Pattern>> commentTypes = createStream(commentNodes)
                .map(this::parseRegexPair)
                .collect(Collectors.toList());

        List<Pair<Pattern, Pattern>> ignored = createStream(ignoreNodes)
                .map(this::parseRegexPair)
                .collect(Collectors.toList());

        return CommentStyle.of(name, commentTypes, ignored);
    }

    protected Pair<Pattern, Pattern> parseRegexPair(Element element) {
        String start = element.getAttribute(ARGUMENT_REGEX_START);
        String end = element.getAttribute(ARGUMENT_REGEX_END);

        if (end.isEmpty())
            end = ARGUMENT_REGEX_END_DEF;

        return Pair.of(Pattern.compile(start), Pattern.compile(end));
    }

}