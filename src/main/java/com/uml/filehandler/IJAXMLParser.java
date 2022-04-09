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
    private int secondLevelOrder;

    public IJAXMLParser(String filePath) {
        this.filePath = filePath;
        this.doc = null;
        this.firstLevelOrder = 0;
        this.secondLevelOrder = 0;
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

                if (node.hasAttributes()) {
                    NamedNodeMap nodeMap = node.getAttributes();

                    if (nodeMap.getLength() != 1 ||
                            !nodeMap.item(0).getNodeName().equals("name")) {
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
        this.secondLevelOrder = 0;

        /* Abstract tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder++;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("abstract")) {
                // TODO error
            } else {
                /* TODO abstract tag */
            }
        }

        /* xCoordinate tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder++;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("xCoordinate")) {
                // TODO error
            } else {
                /* TODO xCoordinate tag */
            }
        }

        /* yCoordinate tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder++;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("yCoordinate")) {
                // TODO error
            } else {
                /* TODO yCoordinate tag */
            }
        }

        /* visibility tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder++;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("visibility")) {
                // TODO error
            } else {
                /* TODO visibility tag */
            }
        }

        /* attributes tags */
        parseAttributes(list);

        /* operations tags */
        parseOperations(list);
    }

    private void parseAttributes(NodeList list) {
        Node node;

        while (this.secondLevelOrder < list.getLength()) {
            node = list.item(this.secondLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("attribute")) {
                    break;
                }

                if (node.hasAttributes()) {
                    NamedNodeMap nodeMap = node.getAttributes();

                    if (nodeMap.getLength() != 1 ||
                            !nodeMap.item(0).getNodeName().equals("name")) {
                        // TODO error
                    } else {
                        /* TODO attribute tag attribute name */
                    }
                }

                if (node.hasChildNodes()) {
                    parseAttributeChildren(node);
                }
            }

            this.secondLevelOrder++;
        }
    }

    private void parseDataTypeTag(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("dataType")) {
                // TODO error
            } else {
                /* TODO abstract tag */
            }
        }
    }

    private void parseVisibilityTag(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("visibility")) {
                // TODO error
            } else {
                /* TODO xCoordinate tag */
            }
        }
    }

    private void parseValueTag(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("value")) {
                // TODO error
            } else {
                /* TODO yCoordinate tag */
            }
        }
    }

    private int parseClassElement(NodeList list) {
        Node node;
        int idx = 0;

        /* dataType tag */
        node = list.item(idx);
        idx++;

        parseDataTypeTag(node);

        /* visibility tag */
        node = list.item(idx);
        idx++;

        parseVisibilityTag(node);

        return idx;
    }

    private void parseAttributeChildren(Node attrNode) {
        NodeList list = attrNode.getChildNodes();
        Node node;
        int idx;

        idx = parseClassElement(list);

        /* value tag */
        node = list.item(idx);

        parseValueTag(node);
    }

    private void parseOperations(NodeList list) {
        Node node;

        while (this.secondLevelOrder < list.getLength()) {
            node = list.item(this.secondLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("operation")) {
                    break;
                }

                if (node.hasAttributes()) {
                    NamedNodeMap nodeMap = node.getAttributes();

                    if (nodeMap.getLength() != 1 ||
                            !nodeMap.item(0).getNodeName().equals("name")) {
                        // TODO error
                    } else {
                        /* TODO attribute tag attribute name */
                    }
                }

                if (node.hasChildNodes()) {
                    parseAttributeChildren(node);
                }
            }

            this.secondLevelOrder++;
        }
    }

    private void parseOperationChildren(Node operNode) {
        NodeList list = operNode.getChildNodes();
        int idx;

        idx = parseClassElement(list);

        /* attributes tags */
        parseOperationAttributesTags(list, idx);
    }

    private void parseOperationAttributesTags(NodeList list, int idx) {
        Node node;

        while (idx < list.getLength()) {
            node = list.item(idx);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("attribute")) {
                    // TODO error
                }

                if (node.hasAttributes()) {
                    NamedNodeMap nodeMap = node.getAttributes();

                    if (nodeMap.getLength() != 1 ||
                            !nodeMap.item(0).getNodeName().equals("name")) {
                        // TODO error
                    } else {
                        /* TODO attribute tag attribute name */
                    }
                }

                if (node.hasChildNodes()) {
                    parseAttributeChildren(node);
                }
            }

            idx++;
        }
    }

    private void parseRelationships() {
        /* Iterate through relationships */
        NodeList list = this.doc.getChildNodes();
        Node node;

        while (this.firstLevelOrder < list.getLength()) {
            node = list.item(this.firstLevelOrder);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (!node.getNodeName().equals("relationship")) {
                    // TODO error
                }

                if (node.hasAttributes()) {
                    NamedNodeMap nodeMap = node.getAttributes();

                    if (nodeMap.getLength() != 1 ||
                            !nodeMap.item(0).getNodeName().equals("id")) {
                        // TODO error
                    } else {
                        /* TODO class tag attribute name */
                    }
                }

                if (node.hasChildNodes()) {
                    parseRelationshipChildren(node);
                }
            }

            this.firstLevelOrder++;
        }
    }

    private void parseRelationshipChildren(Node relNode) {
        NodeList list = relNode.getChildNodes();
        Node node;
        this.secondLevelOrder = 0;

        /* From tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder++;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("from")) {
                // TODO error
            } else {
                /* TODO abstract tag */
            }
        }

        /* to tag */
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder++;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (!node.getNodeName().equals("to")) {
                // TODO error
            } else {
                /* TODO xCoordinate tag */
            }
        }

        // TODO classLevel, instanceLevel
        node = list.item(this.secondLevelOrder);
        this.secondLevelOrder++;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (node.getNodeName().equals("classLevel")) {
                parseClassLevel(node);
            } else if(node.getNodeName().equals("instanceLevel")) {
                parseInstanceLevel(node);
            } else {
                // TODO error
            }
        }
    }

    private void parseClassLevel(Node classLevel) {
        NodeList list = classLevel.getChildNodes();

        if (list.getLength() != 1 ||
                list.item(0).getNodeName().equals("instance")) {
            // TODO error
        } else {
            parseInheritanceTag(list.item(0));
        }
    }

    private void parseInheritanceTag(Node node) {
        // TODO
    }

    private void parseInstanceLevel(Node instanceLevel) {
        NodeList list = instanceLevel.getChildNodes();

        /* fromMultiplicity */
        /* toMultiplicity */
        /* fromRole */
        /* toRole */

        /* association nebo aggregation nebo composition */
    }
}
