package com.uml.filehandler;

import com.uml.App;
import com.uml.ClassUML;
import com.uml.MainController;
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
                parseRelationships();
            }
        }
    }

    private ClassUML getClassByName(String name) {
        for (ClassUML currentClass : this.diagramClasses) {
            if (currentClass.getView().getId().equals(name)) {
                return currentClass;
            }
        }

        return null;
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

        // operations tags
        parseOperations(list);
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

    private String visibility2SingleChar(String visibility) {
        String visibilitySingleChar = null;

        switch (visibility) {
            case "UNSPECIFIED":
                visibilitySingleChar = "";
                break;
            case "PUBLIC":
                visibilitySingleChar = "+";
                break;
            case "PRIVATE":
                visibilitySingleChar = "-";
                break;
            case "PROTECTED":
                visibilitySingleChar = "#";
                break;
            case "PACKAGE":
                visibilitySingleChar = "~";
                break;
            default:
                break;
        }

        return visibilitySingleChar;
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
        visibility = visibility2SingleChar(visibility);

        if (visibility == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        /* value tag */
        node = list.item(idx);

        value = parseValueTag(node);

        if (value == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        ClassUML cls = this.diagramClasses.get(this.diagramClasses.size() - 1);
        this.controller.addAttribute(cls, attrValue, dataType, visibility, value);
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

        if (retDataType == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        /* visibility tag */
        node = list.item(idx);
        idx += 2;

        visibility = parseVisibilityTag(node);
        visibility = visibility2SingleChar(visibility);

        if (visibility == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        /* attributes tags */
        parseOperationAttributesTags(list, idx, attrValue, retDataType, visibility);
    }

    private void parseOperationAttributesTags(NodeList list,
                                              int idx,
                                              String operName,
                                              String operRetDataType,
                                              String operVisibility)
            throws CustomException.IllegalFileFormat {
        Node node;

        List<Pair<String, String>> attrList = new ArrayList<Pair<String, String>>();
        StringBuilder params = new StringBuilder();

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
                    params.append(attrValue + " : " + dataType + ", ");
                }
            }

            idx += 2;
        }

        params.setLength(params.length() - 2); // delete last ", "

        ClassUML cls = this.diagramClasses.get(this.diagramClasses.size() - 1);
        this.controller.addOperation(cls, operName, params.toString(), operRetDataType, operVisibility);
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
        Node node;

        while (this.firstLevelOrder < this.firstLevelList.getLength()) {
            node = this.firstLevelList.item(this.firstLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("relationship")) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                }

                /*
                String attrValue = parseXmlAttribute(node, "id");

                if (attrValue == null) {
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
                }
                */

                if (node.hasChildNodes()) {
                    parseRelationshipChildren(node);
                }
            }

            this.firstLevelOrder += 2;
        }
    }

    private void parseRelationshipChildren(Node relNode) throws CustomException.IllegalFileFormat {
        NodeList list = relNode.getChildNodes();
        Node node;
        String from = null;
        String to = null;
        this.secondLevelOrder = 1;

        /* From tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("from")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                from = node.getTextContent();
            }
        }

        if (from == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }


        /* to tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("to")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                to = node.getTextContent();
            }
        }

        if (to == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (node.getNodeName().equals("classLevel")) {
                parseClassLevel(node, from, to);
            } else if(node.getNodeName().equals("instanceLevel")) {
                parseInstanceLevel(node, from, to);
            } else {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            }
        }
    }

    private void parseClassLevel(Node classLevel, String from, String to) throws CustomException.IllegalFileFormat {
        NodeList list = classLevel.getChildNodes();
        int expectedListLen = 3;

        if (list.getLength() != expectedListLen ||
                list.item(1).getNodeName().equals("instance")) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        } else {
            parseInheritanceTag(from, to);
        }
    }

    private void parseInheritanceTag(String from, String to) throws CustomException.IllegalFileFormat {
        ClassUML clsFrom = getClassByName(from);
        ClassUML clsTo = getClassByName(to);

        if (clsFrom == null || clsTo == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        this.controller.createAndAddRelationship(clsFrom, clsTo, "inheritance");
    }

    private String multiplicityStrToChars(String multiplicity) {
        String multiplicityChars = null;

        switch (multiplicity) {
            case "UNSPECIFIED":
                multiplicityChars = "";
                break;
            case "ZERO":
                multiplicityChars = "0";
                break;
            case "ZERO_OR_ONE":
                multiplicityChars = "0..1";
                break;
            case "ONE":
                multiplicityChars = "1";
                break;
            case "ZERO_OR_MANY":
                multiplicityChars = "0..*";
                break;
            case "ONE_OR_MANY":
                multiplicityChars = "1..*";
                break;
            default:
                break;
        }

        return multiplicityChars;
    }

    private void parseInstanceLevel(Node instanceLevel, String from, String to) throws CustomException.IllegalFileFormat {
        NodeList list = instanceLevel.getChildNodes();
        Node node;
        String fromMultiplicity = null;
        String toMultiplicity = null;
        int idx = 1;
        //final int instLevelTagsCnt = 5;

        //if (list.getLength() != instLevelTagsCnt) {
        //    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        //} else {
            /* fromMultiplicity */
        node = list.item(idx);
        idx += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("fromMultiplicity")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                fromMultiplicity = node.getTextContent();
            }
        }

        if (fromMultiplicity == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        fromMultiplicity = multiplicityStrToChars(fromMultiplicity);

        if (fromMultiplicity == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        /* toMultiplicity */
        node = list.item(idx);
        idx += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("toMultiplicity")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                toMultiplicity = node.getTextContent();
            }
        }

        if (toMultiplicity == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        toMultiplicity = multiplicityStrToChars(toMultiplicity);

        if (toMultiplicity == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        /* fromRole */
        node = list.item(idx);
        idx += 2;
/*
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("fromRole")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                // TODO
            }
        }
*/
        /* toRole */
        node = list.item(idx);
        idx += 2;
/*
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("toRole")) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            } else {
                // TODO
            }
        }
*/

        ClassUML clsFrom = getClassByName(from);
        ClassUML clsTo = getClassByName(to);

        if (clsFrom == null || clsTo == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        /* association or aggregation or composition */
        node = list.item(idx);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            switch (node.getNodeName()) {
                case "association":
                    this.controller.createAndAddRelationship(clsFrom, clsTo,
                            fromMultiplicity, toMultiplicity, "association");
                    break;
                case "aggregation":
                    this.controller.createAndAddRelationship(clsFrom, clsTo,
                            fromMultiplicity, toMultiplicity, "aggregation");
                    break;
                case "composition":
                    this.controller.createAndAddRelationship(clsFrom, clsTo,
                            fromMultiplicity, toMultiplicity, "composition");
                    break;
                default:
                    throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            }
        }
        //}
    }
}
