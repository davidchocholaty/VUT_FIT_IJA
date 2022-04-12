/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing input file parser.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

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

/**
 * Class representing parser for input *.ijaxml or *.ixl files in xml format.
 * <p>
 *     The main goal of this class is parse input file and specific methods
 *     for recreate frontend and backend structure saved in input file.
 * </p>
 */
public class IJAXMLParser {
    private final String filePath;
    private Document doc;
    private int firstLevelOrder;
    private int secondLevelOrder;
    private List<ClassUML> diagramClasses;
    private MainController controller;
    private NodeList firstLevelList;

    /**
     * Creates a new instance of IJAXMLParser.
     *
     * @param filePath Path to file to be parsed.
     */
    public IJAXMLParser(String filePath) {
        this.filePath = filePath;
        this.doc = null;
        this.firstLevelOrder = 1;
        this.secondLevelOrder = 1;
        this.diagramClasses = new ArrayList<ClassUML>();
        this.controller = null;
        this.firstLevelList = null;
    }

    /**
     * Main method for input file parsing.
     * <p>
     *     Method creates new app and prepare inner structure for following file parsing.
     * </p>
     *
     * @throws ParserConfigurationException Parser configuration error.
     * @throws SAXException Parser configuration error.
     * @throws IOException Invalid input file path.
     * @throws CustomException.IllegalFileExtension Invalid file extension.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     * @throws NullPointerException Null pointer based on invalid file format.
     * @throws NumberFormatException Invalid number format based on invalid file format.
     */
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

    /**
     * Function returns an instance of ClassUML with the specified name.
     *
     * @param name Name of class.
     * @return Found ClassUML instance. Null otherwise.
     */
    private ClassUML getClassByName(String name) {
        for (ClassUML currentClass : this.diagramClasses) {
            if (currentClass.getView().getId().equals(name)) {
                return currentClass;
            }
        }

        return null;
    }

    /*
     * This method is based on code from following source.
     *
     * Source: https://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java
     * Author: EboMike
     * Editor: Tombart
     */
    /**
     * Get file extension from input file path.
     *
     * @return File extension.
     */
    private String getFileExtension() {
        String ext = "";

        int i = this.filePath.lastIndexOf('.');
        int p = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));

        if (i > p) {
            ext = filePath.substring(i+1);
        }

        return ext;
    }

    /**
     * Parse attribute of xml tag.
     *
     * @param node Input node to be parsed.
     * @param attrName Expected xml tag attribute name.
     * @return Value of xml tag attribute. Null otherwise.
     * @throws CustomException.IllegalFileFormat Invalid xml tag attribute.
     */
    private String parseXmlAttribute(Node node, String attrName) throws CustomException.IllegalFileFormat {
        String attrValue = null;

        if (node.hasAttributes()) {
            NamedNodeMap nodeMap = node.getAttributes();

            if (nodeMap.getLength() != 1 ||
                    !nodeMap.item(0).getNodeName().equals(attrName)) {
                throw new CustomException.IllegalFileFormat("Invalid file syntax.");
            }

            attrValue = nodeMap.item(0).getNodeValue();
        }

        return attrValue;
    }

    /**
     * Parse saved diagram classes.
     *
     * @throws CustomException.IllegalFileFormat Invalid input file format.
     * @throws NullPointerException Null pointer based on invalid file format.
     * @throws NumberFormatException Invalid number format based on invalid file format.
     * @throws IOException Error during writing on frontend.
     */
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

    /**
     * Parse class children.
     * <p>
     *     As class children are parsed x and y coordinates, attributes and operations.
     * </p>
     *
     * @param classNode Node of class to be parsed.
     * @param attrValue Value of class tag name attribute.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     * @throws NullPointerException Null pointer based on invalid file format.
     * @throws NumberFormatException Invalid number format based on invalid file format.
     * @throws IOException Error during writing on frontend.
     */
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

            }
        }
        */

        /* Attributes tags */
        parseAttributes(list);

        /* Operations tags */
        parseOperations(list);
    }

    /**
     * Parse class attributes.
     *
     * @param list List of class attributes.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
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

    /**
     * Parse data type tag.
     *
     * @param node Data type node.
     * @return Parsed data type. Null otherwise.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
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

    /**
     * Parse visibility tag.
     *
     * @param node Visibility node.
     * @return Parset visibility. Null otherwise.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
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

    /**
     * Parse value tag (default value).
     *
     * @param node Value node.
     * @return Attribute default value. Null otherwise.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
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

    /**
     * Converts inner string representation of visibility to convention format.
     * <p>
     *     Possible converts are: "UNSPECIFIED" -> "", "PUBLIC" -> "+", "PRIVATE", -> "-",
     *     "PROTECTED" -> "#", "PACKAGE" -> "~".
     * </p>
     *
     * @param visibility Input inner representation string of visibility.
     * @return Convention version of visibility type. Null otherwise.
     */
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

    /**
     * Parse attribute children.
     * <p>
     *     As attribute children is parsed data type, visibility and default value.
     * </p>
     *
     * @param attrNode Attribute node.
     * @param attrValue Attribute name.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
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

    /**
     * Parse class operation.
     *
     * @param list List of class operations.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
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

    /**
     * Parse operation children.
     * <p>
     *     As operation children is parsed return data type and visibility.
     * </p>
     *
     * @param operNode Operation node.
     * @param attrValue Operation tag name attribute value.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
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

    /**
     * Parse operation arguments.
     *
     * @param list List of operation attributes.
     * @param idx Index in list of operation children.
     * @param operName Operation name.
     * @param operRetDataType Operation return data type.
     * @param operVisibility Operation visibility.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
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

    /**
     * Parse operation argument.
     *
     * @param attrNode Attribute node.
     * @return Data type of an attribute.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
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
    /*                            RELATIONSHIPS                                 */
    /*--------------------------------------------------------------------------*/

    /**
     * Parse saved diagram relationships.
     *
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
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

    /**
     * Parse relationship children.
     * <p>
     *     As relationship children are parsed from and to classes and
     *     type of relationship (class level or instance level).
     * </p>
     *
     * @param relNode Relationship node.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
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

    /**
     * Parse class level tag children.
     *
     * @param classLevel Class level node.
     * @param from From class.
     * @param to To class.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
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

    /**
     * Parse inheritance tag.
     * <p>
     *     This method create specific method that creates inheritance relationship on frontend and in backend too.
     * </p>
     *
     * @param from From class.
     * @param to To class.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
    private void parseInheritanceTag(String from, String to) throws CustomException.IllegalFileFormat {
        ClassUML clsFrom = getClassByName(from);
        ClassUML clsTo = getClassByName(to);

        if (clsFrom == null || clsTo == null) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        }

        this.controller.createAndAddRelationship(clsFrom, clsTo, "inheritance");
    }

    /**
     * Converts inner string representation of multiplicity to convention string type.
     * <p>
     *     Possible conversion are: "UNSPECIFIED" -> "", "ZERO" -> "0", "ZERO_OR_ONE" -> "0..1",
     *     "ONE" -> "1", "ZERO_OR_MANY" -> "0..*", "ONE_OR_MANY" -> "1..*".
     * </p>
     *
     * @param multiplicity Inner string representation of multiplicity.
     * @return Convention string type of multiplicity. Null otherwise.
     */
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

    /**
     * Parse all instance level relationships (association, aggregation and composition).
     *
     * @param instanceLevel Instance level node.
     * @param from From class.
     * @param to To class.
     * @throws CustomException.IllegalFileFormat Invalid format of input file.
     */
    private void parseInstanceLevel(Node instanceLevel, String from, String to) throws CustomException.IllegalFileFormat {
        NodeList list = instanceLevel.getChildNodes();
        Node node;
        String fromMultiplicity = null;
        String toMultiplicity = null;
        int idx = 1;

        /*
        final int instLevelTagsCnt = 5;

        if (list.getLength() != instLevelTagsCnt) {
            throw new CustomException.IllegalFileFormat("Invalid file syntax.");
        } else {
        */

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
    }
}
