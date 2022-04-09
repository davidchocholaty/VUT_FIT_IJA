package com.uml.filehandler;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/* https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/ */
public class IJAXMLParser {
    private final String filePath;
    private Document doc;
    private int firstLevelOrder;

    public IJAXMLParser(String filePath) {
        this.filePath = filePath;
        this.doc = null;
        this.firstLevelOrder = 0;
    }

    public void parse() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            this.doc = db.parse(new File(this.filePath));

            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            this.doc.getDocumentElement().normalize();

            if (this.doc.hasChildNodes()) {
                parseClasses();
                parseRelationships();
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void parseClasses() {
        /* Iterate through classes */
        NodeList list = this.doc.getChildNodes();
        Node node;

        while (this.firstLevelOrder < list.getLength()) {
            node = list.item(this.firstLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("class")) {
                    break;
                }

                /* TODO class tag */

                if (node.hasAttributes()) {
                    NamedNodeMap nodeMap = node.getAttributes();

                    if (nodeMap.getLength() != 1 || !node.getNodeName().equals("name")) {
                        // TODO error
                    } else {
                        /* TODO class tag attribute name */
                    }
                }

                if (node.hasChildNodes()) {
                    parseClassChildren(node);
                }
            }

            this.firstLevelOrder++;
        }
    }

    private void parseClassChildren(Node classNode) {
        NodeList list = classNode.getChildNodes();
        Node node;
        int idx = 0;

        /* Abstract tag */
        node = list.item(idx);
        idx++;

        if (!node.getNodeName().equals("abstract")) {
            // TODO error
        } else {
            /* TODO abstract tag */
        }

        /* xCoordinate tag */
        node = list.item(idx);
        idx++;

        if (!node.getNodeName().equals("xCoordinate")) {
            // TODO error
        } else {
            /* TODO xCoordinate tag */
        }

        /* yCoordinate tag */
        node = list.item(idx);
        idx++;

        if (!node.getNodeName().equals("yCoordinate")) {
            // TODO error
        } else {
            /* TODO yCoordinate tag */
        }

        /* visibility tag */
        node = list.item(idx);
        idx++;

        if (!node.getNodeName().equals("visibility")) {
            // TODO error
        } else {
            /* TODO visibility tag */
        }

        /* attributes tags */
        parseAttributes(classNode, idx);

        /* operations tags */
        parseOperations(classNode, idx);
    }

    private void parseAttributes(Node parentNode, int idx) {

    }

    private void parseOperations(Node classNode, int idx) {

    }

    private void parseRelationships() {
        /* Iterate through relationships */
        NodeList relList = this.doc.getElementsByTagName("relationship");

        for (int i = 0; i < relList.getLength(); i++) {

        }
    }
}
