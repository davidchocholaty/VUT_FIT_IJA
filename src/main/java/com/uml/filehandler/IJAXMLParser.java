package com.uml.filehandler;

import com.uml.App;
import com.uml.ClassUML;
import com.uml.MainController;
import com.uml.classdiagram.UMLOperation;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/ */
public class IJAXMLParser {
    private final String filePath;
    private Document doc;
    private int firstLevelOrder;
    private int secondLevelOrder;
    private List<ClassUML> diagramClasses;
    private MainController controller;
    private NodeList firstLevelList;

    public IJAXMLParser(String filePath) {
        this.filePath = filePath;
        this.doc = null;
        this.firstLevelOrder = 1;
        this.secondLevelOrder = 1;
        this.diagramClasses = new ArrayList<ClassUML>();
        this.controller = null;
        this.firstLevelList = null;
        this.currentClass = null;
    }

    private ClassUML getClassByName(String name) {
        for (ClassUML currentClass : this.diagramClasses) {
            if (currentClass.getView().getId().equals(name)) {
                return currentClass;
            }
        }

        return null;
    }

    public void parse() throws ParserConfigurationException, SAXException,
            IOException, CustomException.IllegalFileExtension, CustomException.IllegalFileFormat,
            NullPointerException, NumberFormatException {
        String ext = getFileExtension();

        /* Wrong file extension test */
        if (!ext.equals("ijaxml") && !ext.equals("ixl")) {
            throw new CustomException.IllegalFileExtension("Unsupported file format.");
        }
        else {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            this.doc = db.parse(new File(this.filePath));

            /* http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work */
            this.doc.getDocumentElement().normalize();

            /* Create frontend app */
            //App.main(null);

            App app = new App();
            app.start(new Stage());
            this.controller = app.getController();

            if (this.doc.hasChildNodes()) {
                NodeList list = this.doc.getChildNodes();
                /*
                if (list.getLength() != 1 || !list.item(0).getNodeName().equals("classDiagram")) {

                } else {

                }
                */

                this.firstLevelList = list.item(0).getChildNodes();

                parseClasses();
            //    parseRelationships();
            }
        }
    }

    /* https://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java */
    private String getFileExtension() {
        String ext = "";

        int i = this.filePath.lastIndexOf('.');
        int p = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));

        if (i > p) {
            ext = filePath.substring(i+1);
        }

        return ext;
    }

    private String parseXmlAttribute(Node node, String attrName) throws CustomException.IllegalFileFormat {
        String attrValue = null;

        if (node.hasAttributes()) {
            NamedNodeMap nodeMap = node.getAttributes();

            if (nodeMap.getLength() != 1 ||
                    !nodeMap.item(0).getNodeName().equals(attrName)) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            }
             /* TODO attribute tag attribute name */
            attrValue = nodeMap.item(0).getNodeValue();
        }

        return attrValue;
    }

    private void parseClasses() throws CustomException.IllegalFileFormat,
            NullPointerException, NumberFormatException, IOException {
        /* Iterate through classes */
        Node node;

        while (this.firstLevelOrder < this.firstLevelList.getLength()) {
            node = this.firstLevelList.item(this.firstLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("class")) {
                    break;
                }

                String attrValue = parseXmlAttribute(node, "name");

                if (attrValue == null) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                }

                if (node.hasChildNodes()) {
                    parseClassChildren(node, attrValue);
                }
            }

            this.firstLevelOrder += 2;
        }
    }

    private void parseClassChildren(Node classNode, String attrValue) throws CustomException.IllegalFileFormat,
            NullPointerException, NumberFormatException, IOException {
        NodeList list = classNode.getChildNodes();

        Node node;
        double x, y;
        this.secondLevelOrder = 1;

        x = 0.0;
        y = 0.0;

        /* Abstract tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

/*
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("abstract")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                // TODO abstract tag
            }
        }
*/

        /* xCoordinate tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("xCoordinate")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                x = Double.parseDouble(node.getTextContent());
            }
        }

        /* yCoordinate tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("yCoordinate")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                y = Double.parseDouble(node.getTextContent());
            }
        }

        /* Call frontend method for creating class element */
        ClassUML el = this.controller.createElement(x, y, attrValue);
        this.diagramClasses.add(el);
        this.controller.addElement(el);



        /* visibility tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;
        /*
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("visibility")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                // TODO visibility tag
            }
        }
        */
        // attributes tags
        parseAttributes(list);
        /*
        // operations tags
        parseOperations(list);
        */
    }

    private void parseAttributes(NodeList list) throws CustomException.IllegalFileFormat {
        Node node;

        while (this.secondLevelOrder < list.getLength()) {
            node = list.item(this.secondLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("attribute")) {
                    break;
                }

                String attrValue = parseXmlAttribute(node, "name");

                if (attrValue == null) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                }

                if (node.hasChildNodes()) {
                    parseAttributeChildren(node, attrValue);
                }
            }

            this.secondLevelOrder += 2;
        }
    }

    private String parseDataTypeTag(Node node) throws CustomException.IllegalFileFormat {
        String dataType = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("dataType")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                dataType = node.getTextContent();
            }
        }

        return dataType;
    }

    private String parseVisibilityTag(Node node) throws CustomException.IllegalFileFormat {
        String visibility = null;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("visibility")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                visibility = node.getTextContent();
            }
        }

        return visibility;
    }

    private String parseValueTag(Node node) throws CustomException.IllegalFileFormat {
        String value = null;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("value")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                value = node.getTextContent();
            }
        }

        return value;
    }

    private void parseAttributeChildren(Node attrNode, String attrValue) throws CustomException.IllegalFileFormat {
        NodeList list = attrNode.getChildNodes();
        Node node;
        String dataType, visibility, value;
        int idx = 1;

        /* dataType tag */
        node = list.item(idx);
        idx += 2;

        dataType = parseDataTypeTag(node);

        if (dataType == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        /* visibility tag */
        node = list.item(idx);
        idx += 2;

        visibility = parseVisibilityTag(node);

        if (visibility == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        /* value tag */
        node = list.item(idx);

        value = parseValueTag(node);

        if (value == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        // TODO volani metody
    }

    private void parseOperations(NodeList list) throws CustomException.IllegalFileFormat {
        Node node;

        while (this.secondLevelOrder < list.getLength()) {
            node = list.item(this.secondLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("operation")) {
                    break;
                }

                String attrValue = parseXmlAttribute(node, "name");

                if (attrValue == null) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                }

                if (node.hasChildNodes()) {
                    parseOperationChildren(node, attrValue);
                }
            }

            this.secondLevelOrder += 2;
        }
    }

    private void parseOperationChildren(Node operNode, String attrValue) throws CustomException.IllegalFileFormat {
        NodeList list = operNode.getChildNodes();
        Node node;
        String retDataType, visibility;
        int idx = 1;

        /* dataType tag */
        node = list.item(idx);
        idx += 2;

        retDataType = parseDataTypeTag(node);

        /* visibility tag */
        node = list.item(idx);
        idx += 2;

        visibility = parseVisibilityTag(node);

        /* attributes tags */
        parseOperationAttributesTags(list, idx, retDataType, visibility);
    }

    private void parseOperationAttributesTags(NodeList list,
                                              int idx,
                                              String operRetDataType,
                                              String operVisibility)
            throws CustomException.IllegalFileFormat {
        Node node;

        //List<Pair<String, String>> =

        while (idx < list.getLength()) {
            node = list.item(idx);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("attribute")) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                }

                String attrValue = parseXmlAttribute(node, "name");

                if (attrValue == null) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                }

                if (node.hasChildNodes()) {
                    String dataType = parseOperationArgument(node);
                    /* TODO mam nazev parametru a jeho datovy typ */

                }
            }

            idx += 2;
        }

        /* TODO volani metody */
    }

    private String parseOperationArgument(Node attrNode) throws CustomException.IllegalFileFormat {
        NodeList list = attrNode.getChildNodes();
        Node node;
        String dataType;
        int idx = 1;

        /* dataType tag */
        node = list.item(idx);

        dataType = parseDataTypeTag(node);

        if (dataType == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        return dataType;
    }

    /*--------------------------------------------------------------------------*/

    private void parseRelationships() throws CustomException.IllegalFileFormat {
        /* Iterate through relationships */
        NodeList list = this.doc.getChildNodes();
        Node node;

        while (this.firstLevelOrder < list.getLength()) {
            node = list.item(this.firstLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("relationship")) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                }

                String attrValue = parseXmlAttribute(node, "id");

                if (attrValue == null) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                }

                // TODO prace s value

                if (node.hasChildNodes()) {
                    parseRelationshipChildren(node);
                }
            }

            this.firstLevelOrder++;
        }
    }

    private void parseRelationshipChildren(Node relNode) throws CustomException.IllegalFileFormat {
        NodeList list = relNode.getChildNodes();
        Node node;
        this.secondLevelOrder = 1;

        /* From tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder++;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("from")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                /* TODO abstract tag */
            }
        }

        /* to tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder++;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("to")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                /* TODO xCoordinate tag */
            }
        }

        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder++;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (node.getNodeName().equals("classLevel")) {
                parseClassLevel(node);
            } else if(node.getNodeName().equals("instanceLevel")) {
                parseInstanceLevel(node);
            } else {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            }
        }
    }

    private void parseClassLevel(Node classLevel) throws CustomException.IllegalFileFormat {
        NodeList list = classLevel.getChildNodes();

        if (list.getLength() != 1 ||
                list.item(0).getNodeName().equals("instance")) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        } else {
            parseInheritanceTag(list.item(0));
        }
    }

    private void parseInheritanceTag(Node node) {
        // TODO prace s hodnotou
    }

    private void parseInstanceLevel(Node instanceLevel) throws CustomException.IllegalFileFormat {
        NodeList list = instanceLevel.getChildNodes();
        Node node;
        int idx = 1;
        final int instLevelTagsCnt = 5;

        if (list.getLength() != instLevelTagsCnt) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        } else {
            /* fromMultiplicity */
            node = list.item(idx);
            idx++;

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("fromMultiplicity")) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                } else {
                    /* TODO abstract tag */
                }
            }

            /* toMultiplicity */
            node = list.item(idx);
            idx++;

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("toMultiplicity")) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                } else {
                    /* TODO abstract tag */
                }
            }

            /* fromRole */
            node = list.item(idx);
            idx++;

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("fromRole")) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                } else {
                    /* TODO abstract tag */
                }
            }

            /* toRole */
            node = list.item(idx);
            idx++;

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("toRole")) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                } else {
                    /* TODO abstract tag */
                }
            }

            /* association or aggregation or composition */
            node = list.item(idx);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                switch (node.getNodeName()) {
                    case "association":
                        /* TODO */
                        break;
                    case "aggregation":
                        /* TODO */
                        break;
                    case "composition":
                        /* TODO */
                        break;
                    default:
                        throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                }
            }
        }
    }
}
