/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing input file parser.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.filehandler;

import com.uml.*;
import com.uml.customexception.*;
import com.uml.sequencediagram.UMLLifeline;
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
    private int zeroLevelOrder;
    private int firstLevelOrder;
    private int secondLevelOrder;
    private List<ClassUML> diagramClasses;
    private List<SequenceUML> diagramLifelines;
    private MainController controller;
    private SequenceController sequenceController;
    private NodeList zeroLevelList;
    private NodeList firstLevelList;

    /**
     * Creates a new instance of IJAXMLParser.
     *
     * @param filePath Path to file to be parsed.
     */
    public IJAXMLParser(String filePath) {
        this.filePath = filePath;
        this.doc = null;
        this.zeroLevelOrder = 1;
        this.firstLevelOrder = 1;
        this.secondLevelOrder = 1;
        this.diagramClasses = new ArrayList<ClassUML>();
        this.diagramLifelines = new ArrayList<SequenceUML>();
        this.controller = null;
        this.zeroLevelList = null;
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
     * @throws IllegalFileExtension Invalid file extension.
     * @throws IllegalFileFormat Invalid format of input file.
     * @throws NullPointerException Null pointer based on invalid file format.
     * @throws NumberFormatException Invalid number format based on invalid file format.
     */
    public void parse() throws ParserConfigurationException, SAXException,
            IOException, IllegalFileExtension, IllegalFileFormat,
            NullPointerException, NumberFormatException {
        String ext = getFileExtension();

        /* Wrong file extension test */
        if (!ext.equals("ijaxml") && !ext.equals("ixl")) {
            throw new IllegalFileExtension("Unsupported file format.");
        }
        else {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            this.doc = db.parse(new File(this.filePath));

            this.doc.getDocumentElement().normalize();

            /* If the root element is invalid. */
            if (!doc.getDocumentElement().getNodeName().equals("ijaUml")) {
                throw new IllegalFileFormat("Invalid root element.");
            }

            /* Create frontend app */
            App app = new App();
            app.start(new Stage());
            this.controller = app.getController();

            if (this.doc.hasChildNodes()) {
                NodeList list = this.doc.getChildNodes();

                this.zeroLevelList = list.item(0).getChildNodes();

                /* Iterate through diagrams */
                Node node;

                /* Class diagram*/
                while (this.zeroLevelOrder < this.zeroLevelList.getLength()) {
                    node = this.zeroLevelList.item(this.zeroLevelOrder);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        if (!node.getNodeName().equals("classDiagram")) {
                            break;
                        }

                        if (this.zeroLevelOrder > 1) {
                            throw new IllegalFileFormat("Multiple class diagram occurence.");
                        }

                        String attrValue = parseXmlAttribute(node, "name");

                        if (attrValue == null) {
                            throw new IllegalFileFormat("Missing class diagram name.");
                        }

                        if (node.hasChildNodes()) {
                            this.firstLevelList = node.getChildNodes();
                            this.firstLevelOrder = 1;

                            parseClasses();
                            parseRelationships();
                        }
                    }

                    this.zeroLevelOrder += 2;
                }

                /* Sequence diagrams */
                while (this.zeroLevelOrder < this.zeroLevelList.getLength()) {
                    node = this.zeroLevelList.item(this.zeroLevelOrder);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        if (!node.getNodeName().equals("sequenceDiagram")) {
                            throw new IllegalFileFormat("Invalid diagram tag.");
                        }

                        String attrValue = parseXmlAttribute(node, "name");

                        if (attrValue == null) {
                            throw new IllegalFileFormat("Missing sequence diagram name.");
                        }

                        /* Create new sequence diagram */
                        this.sequenceController = controller.addTabLoad(attrValue);

                        if (node.hasChildNodes()) {
                            this.firstLevelList = node.getChildNodes();
                            this.firstLevelOrder = 1;

                            parseLifelines();
                            parseMessages();
                            parseDestroys();
                            parseActivations();
                        }
                    }

                    this.zeroLevelOrder += 2;
                }
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

    private SequenceUML getLifelineByNameAndId(String name, long id) {
        for (SequenceUML currentLifeline : this.diagramLifelines) {
            if (currentLifeline.lifeline.getObjectClass().getName().equals(name) &&
                    currentLifeline.lifeline.getId() == id) {
                return currentLifeline;
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
     * @throws IllegalFileFormat Invalid xml tag attribute.
     */
    private String parseXmlAttribute(Node node, String attrName) throws IllegalFileFormat {
        String attrValue = null;

        if (node.hasAttributes()) {
            NamedNodeMap nodeMap = node.getAttributes();

            if (nodeMap.getLength() != 1 ||
                    !nodeMap.item(0).getNodeName().equals(attrName)) {
                throw new IllegalFileFormat("Invalid file syntax.");
            }

            attrValue = nodeMap.item(0).getNodeValue();
        }

        return attrValue;
    }

    /*-------------------------------------------------------------------------------------------------*/
    /*                                          CLASS DIAGRAM                                          */
    /*-------------------------------------------------------------------------------------------------*/

    /**
     * Parse saved diagram classes.
     *
     * @throws IllegalFileFormat Invalid input file format.
     * @throws NullPointerException Null pointer based on invalid file format.
     * @throws NumberFormatException Invalid number format based on invalid file format.
     * @throws IOException Error during writing on frontend.
     */
    private void parseClasses() throws IllegalFileFormat,
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
                    throw new IllegalFileFormat("Invalid file syntax.");
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
     * @throws IllegalFileFormat Invalid format of input file.
     * @throws NullPointerException Null pointer based on invalid file format.
     * @throws NumberFormatException Invalid number format based on invalid file format.
     * @throws IOException Error during writing on frontend.
     */
    private void parseClassChildren(Node classNode, String attrValue) throws IllegalFileFormat,
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
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {

            }
        }
        */

        /* xCoordinate tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("xCoordinate")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                x = Double.parseDouble(node.getTextContent());
            }
        }

        /* yCoordinate tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("yCoordinate")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                y = Double.parseDouble(node.getTextContent());
            }
        }

        /* Call frontend method for creating class element */
        ClassUML el = this.controller.createElement(x, y, attrValue);
        this.diagramClasses.add(el);
        this.controller.addElement(el);

        /* Attributes tags */
        parseAttributes(list);

        /* Operations tags */
        parseOperations(list);
    }

    /**
     * Parse class attributes.
     *
     * @param list List of class attributes.
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseAttributes(NodeList list) throws IllegalFileFormat {
        Node node;

        while (this.secondLevelOrder < list.getLength()) {
            node = list.item(this.secondLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("attribute")) {
                    break;
                }

                String attrValue = parseXmlAttribute(node, "name");

                if (attrValue == null) {
                    throw new IllegalFileFormat("Invalid file syntax.");
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
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private String parseDataTypeTag(Node node) throws IllegalFileFormat {
        String dataType = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("dataType")) {
                throw new IllegalFileFormat("Invalid file syntax.");
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
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private String parseVisibilityTag(Node node) throws IllegalFileFormat {
        String visibility = null;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("visibility")) {
                throw new IllegalFileFormat("Invalid file syntax.");
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
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private String parseValueTag(Node node) throws IllegalFileFormat {
        String value = null;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("value")) {
                throw new IllegalFileFormat("Invalid file syntax.");
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
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseAttributeChildren(Node attrNode, String attrValue) throws IllegalFileFormat {
        NodeList list = attrNode.getChildNodes();
        Node node;
        String dataType, visibility, value;
        int idx = 1;

        /* dataType tag */
        node = list.item(idx);
        idx += 2;

        dataType = parseDataTypeTag(node);

        if (dataType == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }

        /* visibility tag */
        node = list.item(idx);
        idx += 2;

        visibility = parseVisibilityTag(node);
        visibility = visibility2SingleChar(visibility);

        if (visibility == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }

        /* value tag */
        node = list.item(idx);

        value = parseValueTag(node);

        if (value == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }

        ClassUML cls = this.diagramClasses.get(this.diagramClasses.size() - 1);
        this.controller.addAttribute(cls, attrValue, dataType, visibility, value);
    }

    /**
     * Parse class operation.
     *
     * @param list List of class operations.
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseOperations(NodeList list) throws IllegalFileFormat {
        Node node;

        while (this.secondLevelOrder < list.getLength()) {
            node = list.item(this.secondLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("operation")) {
                    break;
                }

                String attrValue = parseXmlAttribute(node, "name");

                if (attrValue == null) {
                    throw new IllegalFileFormat("Invalid file syntax.");
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
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseOperationChildren(Node operNode, String attrValue) throws IllegalFileFormat {
        NodeList list = operNode.getChildNodes();
        Node node;
        String retDataType, visibility;
        int idx = 1;

        /* dataType tag */
        node = list.item(idx);
        idx += 2;

        retDataType = parseDataTypeTag(node);

        if (retDataType == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }

        /* visibility tag */
        node = list.item(idx);
        idx += 2;

        visibility = parseVisibilityTag(node);
        visibility = visibility2SingleChar(visibility);

        if (visibility == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
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
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseOperationAttributesTags(NodeList list,
                                              int idx,
                                              String operName,
                                              String operRetDataType,
                                              String operVisibility)
            throws IllegalFileFormat {
        Node node;

        List<Pair<String, String>> attrList = new ArrayList<Pair<String, String>>();
        StringBuilder params = new StringBuilder();

        while (idx < list.getLength()) {
            node = list.item(idx);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("attribute")) {
                    throw new IllegalFileFormat("Invalid file syntax.");
                }

                String attrValue = parseXmlAttribute(node, "name");

                if (attrValue == null) {
                    throw new IllegalFileFormat("Invalid file syntax.");
                }

                if (node.hasChildNodes()) {
                    String dataType = parseOperationArgument(node);
                    params.append(attrValue).append(" : ").append(dataType).append(", ");
                }
            }

            idx += 2;
        }

        params.setLength(params.length() - 2); /* delete last ", " */

        ClassUML cls = this.diagramClasses.get(this.diagramClasses.size() - 1);
        this.controller.addOperation(cls, operName, params.toString(), operRetDataType, operVisibility);
    }

    /**
     * Parse operation argument.
     *
     * @param attrNode Attribute node.
     * @return Data type of an attribute.
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private String parseOperationArgument(Node attrNode) throws IllegalFileFormat {
        NodeList list = attrNode.getChildNodes();
        Node node;
        String dataType;
        int idx = 1;

        /* dataType tag */
        node = list.item(idx);

        dataType = parseDataTypeTag(node);

        if (dataType == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }

        return dataType;
    }

    /*--------------------------------------------------------------------------*/
    /*                            RELATIONSHIPS                                 */
    /*--------------------------------------------------------------------------*/

    /**
     * Parse saved diagram relationships.
     *
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseRelationships() throws IllegalFileFormat {
        /* Iterate through relationships */
        Node node;

        while (this.firstLevelOrder < this.firstLevelList.getLength()) {
            node = this.firstLevelList.item(this.firstLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("relationship")) {
                    throw new IllegalFileFormat("Invalid file syntax.");
                }

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
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseRelationshipChildren(Node relNode) throws IllegalFileFormat {
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
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                from = node.getTextContent();
            }
        }

        if (from == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }


        /* to tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("to")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                to = node.getTextContent();
            }
        }

        if (to == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }

        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (node.getNodeName().equals("classLevel")) {
                parseClassLevel(node, from, to);
            } else if(node.getNodeName().equals("instanceLevel")) {
                parseInstanceLevel(node, from, to);
            } else {
                throw new IllegalFileFormat("Invalid file syntax.");
            }
        }
    }

    /**
     * Parse class level tag child.
     *
     * @param classLevel Class level node.
     * @param from From class.
     * @param to To class.
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseClassLevel(Node classLevel, String from, String to) throws IllegalFileFormat {
        NodeList list = classLevel.getChildNodes();
        int expectedListLen = 3;

        if (list.getLength() != expectedListLen ||
                !list.item(1).getNodeName().equals("inheritance")) {
            throw new IllegalFileFormat("Invalid file syntax.");
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
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseInheritanceTag(String from, String to) throws IllegalFileFormat {
        ClassUML clsFrom = getClassByName(from);
        ClassUML clsTo = getClassByName(to);

        if (clsFrom == null || clsTo == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
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
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseInstanceLevel(Node instanceLevel, String from, String to) throws IllegalFileFormat {
        NodeList list = instanceLevel.getChildNodes();
        Node node;
        String fromMultiplicity = null;
        String toMultiplicity = null;
        int idx = 1;

        /*
        final int instLevelTagsCnt = 5;

        if (list.getLength() != instLevelTagsCnt) {
            throw new IllegalFileFormat("Invalid file syntax.");
        } else {
        */

        /* fromMultiplicity */
        node = list.item(idx);
        idx += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("fromMultiplicity")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                fromMultiplicity = node.getTextContent();
            }
        }

        if (fromMultiplicity == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }

        fromMultiplicity = multiplicityStrToChars(fromMultiplicity);

        if (fromMultiplicity == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }

        /* toMultiplicity */
        node = list.item(idx);
        idx += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("toMultiplicity")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                toMultiplicity = node.getTextContent();
            }
        }

        if (toMultiplicity == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }

        toMultiplicity = multiplicityStrToChars(toMultiplicity);

        if (toMultiplicity == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }

        ClassUML clsFrom = getClassByName(from);
        ClassUML clsTo = getClassByName(to);

        if (clsFrom == null || clsTo == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
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
                    throw new IllegalFileFormat("Invalid file syntax.");
            }
        }
    }

    /*-------------------------------------------------------------------------------------------------*/
    /*                                        SEQUENCE DIAGRAM                                         */
    /*-------------------------------------------------------------------------------------------------*/

    /**
     * Parse saved sequence diagram lifelines.
     *
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseLifelines() throws IllegalFileFormat, NullPointerException, NumberFormatException {
        Node node;

        while (this.firstLevelOrder < this.firstLevelList.getLength()) {
            node = this.firstLevelList.item(this.firstLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("lifeline")) {
                    break;
                }

                String attrValue = parseXmlAttribute(node, "name");

                if (attrValue == null) {
                    throw new IllegalFileFormat("Invalid file syntax.");
                }

                if (node.hasChildNodes()) {
                    parseLifelineChildren(node, attrValue);
                }
            }

            this.firstLevelOrder += 2;
        }
    }

    /**
     * Parse lifeline children.
     * <p>
     *     As a lifeline children are parsed height and yCoordinate tags.
     * </p>
     *
     * @param lifelineNode Node of lifeline to be parsed.
     * @param attrValue Value of lifeline tag name attribute.
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseLifelineChildren(Node lifelineNode, String attrValue) throws IllegalFileFormat,
            NullPointerException, NumberFormatException {
        NodeList list = lifelineNode.getChildNodes();

        Node node;
        double height;
        double x, y;
        int order;

        order = 1;

        height = 0.0;
        x = 0.0;
        y = 0.0;

        /* Height tag */
        node = list.item(order);
        order += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("height")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                height = Double.parseDouble(node.getTextContent());
            }
        }

        /* xCoordinate tag */
        node = list.item(order);
        order += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("xCoordinate")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                x = Double.parseDouble(node.getTextContent());
            }
        }

        /* yCoordinate tag */
        node = list.item(order);
        order += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("yCoordinate")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                y = Double.parseDouble(node.getTextContent());
            }
        }

        /* Call frontend method for creating lifeline element */
        try {
            System.out.println("creating lifeline ...");
            SequenceUML lifeline = this.sequenceController.addElementLoaded(attrValue, height, x, y);
            this.diagramLifelines.add(lifeline);
        } catch (IOException e) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }
    }

    /**
     * Parse saved sequence diagram messages.
     *
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseMessages() throws IllegalFileFormat, NullPointerException, NumberFormatException {
        /* Iterate through messages */
        Node node;

        while (this.firstLevelOrder < this.firstLevelList.getLength()) {
            node = this.firstLevelList.item(this.firstLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("message")) {
                    break;
                }

                String attrValue = parseXmlAttribute(node, "label");

                if (attrValue == null) {
                    throw new IllegalFileFormat("Invalid file syntax.");
                }

                if (node.hasChildNodes()) {
                    parseMessageChildren(node);
                }
            }

            this.firstLevelOrder += 2;
        }
    }

    /**
     * Parse message children.
     * <p>
     *     As message children are parsed from ant to lifelines,
     *     type of message and concrete message.
     * </p>
     *
     * @param messageNode Message node.
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseMessageChildren(Node messageNode) throws IllegalFileFormat, NullPointerException, NumberFormatException {
        NodeList list = messageNode.getChildNodes();
        Node node;
        String label = null;
        String from = null;
        String to = null;
        String fromIdStr;
        String toIdStr;
        long fromId;
        long toId;
        double y;

        this.secondLevelOrder = 1;
        y = 0.0;

        /* Label tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("label")) {
                throw new IllegalFileFormat("Invalid file syntax");
            } else {
                label = node.getTextContent();
            }
        }

        /* From tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("from")) {
                throw new IllegalFileFormat("Invalid file syntax");
            } else {
                from = node.getTextContent();
            }
        }

        if (from == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        } else {
            fromIdStr = parseXmlAttribute(node, "id");

            if (fromIdStr == null || fromIdStr.equals("")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                fromId = Long.parseLong(fromIdStr);
            }
        }

        /* To tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("to")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                to = node.getTextContent();
            }
        }

        if (to == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        } else {
            toIdStr = parseXmlAttribute(node, "id");

            if (toIdStr == null || toIdStr.equals("")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                toId = Long.parseLong(toIdStr);
            }
        }

        /* yCoordinate tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("yCoordinate")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                y = Double.parseDouble(node.getTextContent());
            }
        }

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (node.getNodeName().equals("labelType")) {
                parseLabelType(node, label, from, to, fromId, toId, y);
            } else if (node.getNodeName().equals("operationType")) {
                parseOperationType(node, label, from, to, fromId, toId, y);
            } else {
                throw new IllegalFileFormat("Invalid file syntax");
            }
        }
    }

    /**
     * Parse label type tag child.
     *
     * @param labelType Label type node.
     * @param from From lifeline.
     * @param to To lifeline.
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseLabelType(Node labelType,
                                String label,
                                String from,
                                String to,
                                long fromId,
                                long toId,
                                double y) throws IllegalFileFormat {
        NodeList list = labelType.getChildNodes();
        int expectedListLen = 3;

        if (list.getLength() != expectedListLen) {
            throw new IllegalFileFormat("Invalid file syntax.");
        } else {
            if (list.item(1).getNodeName().equals("returnMessage")) {
                SequenceUML fromLifeline = getLifelineByNameAndId(from, fromId);
                SequenceUML toLifeline = getLifelineByNameAndId(to, toId);

                if (fromLifeline == null || toLifeline == null) {
                    throw new IllegalFileFormat("Invalid file syntax.");
                }

                this.sequenceController.createMessageLoaded(fromLifeline, toLifeline, y, label, "return");
            } else {
                throw new IllegalFileFormat("Invalid file syntax.");
            }
        }
    }

    /**
     * Parse operation type tag child.
     *
     * @param operationType Operation type tag.
     * @param from From lifeline.
     * @param to To lifeline.
     * @throws IllegalFileFormat Invalid format of input file.
     */
    private void parseOperationType(Node operationType,
                                    String label,
                                    String from,
                                    String to,
                                    long fromId,
                                    long toId,
                                    double y) throws IllegalFileFormat {
        NodeList list = operationType.getChildNodes();
        int expectedListLen = 3;

        if (list.getLength() != expectedListLen) {
            throw new IllegalFileFormat("Invalid file syntax.");
        } else {
            SequenceUML fromLifeline = getLifelineByNameAndId(from, fromId);
            SequenceUML toLifeline = getLifelineByNameAndId(to, toId);

            switch (list.item(1).getNodeName()) {
                case "synchronousMessage":
                    this.sequenceController.createMessageLoaded(fromLifeline, toLifeline, y, label, "sync");
                    break;
                case "asynchronousMessage":
                    this.sequenceController.createMessageLoaded(fromLifeline, toLifeline, y, label, "async");
                    break;
                case "synchronousSelfMessage":
                    this.sequenceController.createMessageLoaded(fromLifeline, toLifeline, y, label, "syncSelf");
                    break;
                case "asynchronousSelfMessage":
                    this.sequenceController.createMessageLoaded(fromLifeline, toLifeline, y, label, "asyncSelf");
                    break;
                case "returnSelfMessage":
                    this.sequenceController.createMessageLoaded(fromLifeline, toLifeline, y, label, "returnSelf");
                    break;
                case "createMessage":
                    this.sequenceController.createMessageLoaded(fromLifeline, toLifeline, y, label, "create");
                    break;
                default:
                    throw new IllegalFileFormat("Invalid file syntax.");
            }
        }
    }

    private void parseDestroys() throws IllegalFileFormat, NullPointerException, NumberFormatException {
        Node node;

        while (this.firstLevelOrder < this.firstLevelList.getLength()) {
            node = this.firstLevelList.item(this.firstLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("destroy")) {
                    break;
                }

                String attrValue = parseXmlAttribute(node, "name");

                if (attrValue == null) {
                    throw new IllegalFileFormat("Invalid file syntax.");
                }

                if (node.hasChildNodes()) {
                    parseDestroyChildren(node, attrValue);
                }
            }

            this.firstLevelOrder += 2;
        }
    }

    private void parseDestroyChildren(Node destroyNode, String attrValue) throws IllegalFileFormat,
            NullPointerException, NumberFormatException {
        NodeList list = destroyNode.getChildNodes();
        Node node;
        int order;
        double y;
        String lifeline = null;
        String idStr = null;
        long id;

        order = 1;
        y = 0.0;

        /* destroyLifeline tag */
        node = list.item(order);
        order += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("lifelineReference")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                lifeline = node.getTextContent();
            }
        }

        if (lifeline == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        } else {
            idStr = parseXmlAttribute(node, "id");

            if (idStr == null || idStr.equals("")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                id = Long.parseLong(idStr);
            }
        }

        /* yCoordinate tag */
        node = list.item(order);
        order += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("yCoordinate")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                y = Double.parseDouble(node.getTextContent());
            }
        }



        /* Call frontend method for creating destroy element */
        SequenceUML frontLifeline = getLifelineByNameAndId(lifeline, id);

        if (frontLifeline == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }

        this.sequenceController.createDestroy(frontLifeline, y);
    }

    private void parseActivations() throws IllegalFileFormat, NullPointerException, NumberFormatException {
        Node node;

        while (this.firstLevelOrder < this.firstLevelList.getLength()) {
            node = this.firstLevelList.item(this.firstLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("activation")) {
                    break;
                }

                String attrValue = parseXmlAttribute(node, "name");

                if (attrValue == null) {
                    throw new IllegalFileFormat("Invalid file syntax.");
                }

                if (node.hasChildNodes()) {
                    parseActivationChildren(node, attrValue);
                }
            }

            this.firstLevelOrder += 2;
        }
    }

    private void parseActivationChildren(Node activationNode, String attrValue) throws IllegalFileFormat,
            NullPointerException, NumberFormatException {
        NodeList list = activationNode.getChildNodes();
        Node node;
        int order;
        double yStart, yEnd;
        String lifeline = null;
        String idStr = null;
        String orderAttr = null;
        long id;

        yStart = yEnd = 0.0;
        order = 1;

        /* activationLifeline tag */
        node = list.item(order);
        order += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("lifelineReference")) {
                throw new IllegalFileFormat("Invalid file syntax");
            } else {
                lifeline = node.getTextContent();
            }
        }

        if (lifeline == null) {
            throw new IllegalFileFormat("Invalid file syntax");
        } else {
            idStr = parseXmlAttribute(node, "id");

            if (idStr == null || idStr.equals("")) {
                throw new IllegalFileFormat("Invalid file syntax.");
            } else {
                id = Long.parseLong(idStr);
            }
        }

        /* yCoordinate tags */
        node = list.item(order);
        order += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("yCoordinate")) {
                throw new IllegalFileFormat("Invalid file syntax");
            } else {
                orderAttr = parseXmlAttribute(node, "order");

                if (!orderAttr.equals("1")) {
                    throw new IllegalFileFormat("Invalid file syntax.");
                }

                yStart = Double.parseDouble(node.getTextContent());
            }
        }

        node = list.item(order);
        order += 2;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("yCoordinate")) {
                throw new IllegalFileFormat("Invalid file syntax");
            } else {
                orderAttr = parseXmlAttribute(node, "order");

                if (!orderAttr.equals("2")) {
                    throw new IllegalFileFormat("Invalid file syntax.");
                }

                yEnd = Double.parseDouble(node.getTextContent());
            }
        }

        /* Call frontend method for creating activation element */
        SequenceUML frontLifeline = getLifelineByNameAndId(lifeline, id);

        if (frontLifeline == null) {
            throw new IllegalFileFormat("Invalid file syntax.");
        }

        this.sequenceController.createActivationLoaded(frontLifeline, yStart, yEnd);
    }

}
