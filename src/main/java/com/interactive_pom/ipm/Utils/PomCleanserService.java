package com.interactive_pom.ipm.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

@Component
@Slf4j
public class PomCleanserService {

    public void cleanPom(File inputFile) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputFile);
            document.getDocumentElement().normalize();

            // Remove empty tags
            removeEmptyTagsAndComments(document.getDocumentElement());

            // Save the updated XML back to the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Enable pretty printing
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(inputFile);
            transformer.transform(source, result);

            System.out.println("Empty tags and comments removed successfully!");
        } catch (Exception e) {
            log.error("Error while cleaning the pom.xml {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void removeEmptyTagsAndComments(Element element) {
        NodeList children = element.getChildNodes();

        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);

            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) child;

                // Recursively check and remove empty child elements
                removeEmptyTagsAndComments(childElement);

                // Remove the element if it is empty (including whitespace)
                if (!childElement.hasChildNodes() && childElement.getTextContent().trim().isEmpty()) {
                    element.removeChild(child);
                }
            } else if (child.getNodeType() == Node.COMMENT_NODE) {
                // Remove comments
                element.removeChild(child);
            } else if (child.getNodeType() == Node.TEXT_NODE) {
                // Remove whitespace-only text nodes
                if (child.getTextContent().trim().isEmpty()) {
                    element.removeChild(child);
                }
            }
        }
    }
}

