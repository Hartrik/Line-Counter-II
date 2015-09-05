package cz.hartrik.linecount.analyze.supported;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Obsahuje společné metody pro XML parsery.
 *
 * @version 2015-09-05
 * @author Patrik Harag
 */
public abstract class XMLParserBase {

    protected Document loadXMLDocument(InputStream inputStream)
            throws IOException, SAXException, ParserConfigurationException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(inputStream);
        document.getDocumentElement().normalize();

        return document;
    }

    public static void validateXMLSchema(URL xsdUrl, Node node)
            throws JAXBException, SAXException, IOException{

        SchemaFactory factory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schema = factory.newSchema(xsdUrl);
        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(node));
    }

    protected static Stream<Element> createStream(NodeList elements) {
        return IntStream.range(0, elements.getLength())
                .mapToObj(i -> (Element) elements.item(i));
    }

}