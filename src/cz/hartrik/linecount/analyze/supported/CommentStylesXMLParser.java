package cz.hartrik.linecount.analyze.supported;

import cz.hartrik.common.Pair;
import cz.hartrik.linecount.analyze.CommentStyle;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
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
public class CommentStylesXMLParser {

    public static final String TAG_ROOT = "comment-styles";
    public static final String TAG_COMMENT_STYLE = "comment-style";
    public static final String TAG_COMMENT = "comment";
    public static final String TAG_IGNORE = "ignore";

    public static final String ARGUMENT_STYLE_NAME = "name";
    public static final String ARGUMENT_COMMENT_START = "start";
    public static final String ARGUMENT_COMMENT_END = "end";

    public static final String ARGUMENT_COMMENT_END_DEF = "(?m)$";

    /**
     * Načte styly komentářů z XML souboru. Předpokládá správně formátovaný
     * XML soubor.
     *
     * @param inputStream vstupní proud
     * @return seznam stylů komentářů
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public List<CommentStyle> parse(InputStream inputStream)
            throws IOException, SAXException, ParserConfigurationException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(inputStream);
        document.getDocumentElement().normalize();

        Element documentElement = document.getDocumentElement();
        NodeList list = documentElement.getElementsByTagName(TAG_COMMENT_STYLE);

        return createStream(list)
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

        return new CommentStyleImpl(name, commentTypes, ignored);
    }

    protected Pair<Pattern, Pattern> parseRegexPair(Element element) {
        String start = element.getAttribute(ARGUMENT_COMMENT_START);
        String end = element.getAttribute(ARGUMENT_COMMENT_END);

        if (end.isEmpty())
            end = ARGUMENT_COMMENT_END_DEF;

        return Pair.of(Pattern.compile(start), Pattern.compile(end));
    }

    // pomocné metody

    private Stream<Element> createStream(NodeList elements) {
        return IntStream.range(0, elements.getLength())
                .mapToObj(i -> (Element) elements.item(i));
    }

}