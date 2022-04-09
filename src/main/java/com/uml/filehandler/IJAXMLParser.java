package com.uml.filehandler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class IJAXMLParser {
    private final String filePath;

    public IJAXMLParser(String filePath) {
        this.filePath = filePath;
    }

    public void parse() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(this.filePath));

            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList list;

            /* Iterate through classes */
            list = doc.getElementsByTagName("class");

            for (int i = 0; i < list.getLength(); i++) {

            }

            /* Iterate through relationships */
            list = doc.getElementsByTagName("relationship");

            for (int i = 0; i < list.getLength(); i++) {

            }


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
