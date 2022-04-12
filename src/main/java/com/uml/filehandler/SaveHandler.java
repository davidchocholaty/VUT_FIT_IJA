/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing handler for saving.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.filehandler;

import com.uml.classdiagram.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Class representing handler for saving diagram into *.ijaxml or *.ixl file in xml format.
 * <p>
 *     The main goal of this class is save diagram into output file.
 * </p>
 */
public class SaveHandler {
    private final ClassDiagram diagram;
    private Document doc;

    /**
     * Creates a new instance of SaveHandler.
     *
     * @param diagram Class diagram.
     */
    public SaveHandler(ClassDiagram diagram) {
        this.diagram = diagram;
        doc = null;
    }

    /**
     * Main method for class diagram saving.
     *
     * @param destPath Output file destination path.
     * @throws ParserConfigurationException Parser configuration error.
     * @throws FileNotFoundException File wasn't found (FileOutputStream).
     * @throws TransformerException Transformer error.
     */
    public void saveClassDiagram(String destPath) throws ParserConfigurationException,
            FileNotFoundException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        this.doc = docBuilder.newDocument();

        Element rootElement = this.doc.createElement("classDiagram");
        this.doc.appendChild(rootElement);
        rootElement.setAttribute("name", this.diagram.getName());

        List<UMLClass> diagramClasses = this.diagram.getClasses();
        List<UMLRelationship> diagramRelationships = this.diagram.getRelationships();

        addClassTags(rootElement, diagramClasses);
        addRelationshipsTags(rootElement, diagramRelationships);

        FileOutputStream output = new FileOutputStream(destPath);
        writeXml(this.doc, output);
    }

    /**
     * Add all diagram classes tags.
     *
     * @param rootElement Root element.
     * @param diagramClasses Classes of diagram.
     */
    private void addClassTags(Element rootElement, List<UMLClass> diagramClasses) {
        /* Iterate through classes */
        for (UMLClass currentClass : diagramClasses) {
            addClassTag(rootElement, currentClass);
        }
    }

    /**
     * Add all diagram relationships tags.
     *
     * @param rootElement Root element.
     * @param diagramRelationships Relationships of diagram.
     */
    private void addRelationshipsTags(Element rootElement, List<UMLRelationship> diagramRelationships) {
        /* Iterate through relationships */
        for (UMLRelationship currentRel : diagramRelationships) {
            addRelationshipTag(rootElement, currentRel);
        }
    }

    /*-----------------------------------------------------------------------------*/
    /*                              Class tag members                              */
    /*-----------------------------------------------------------------------------*/

    /**
     * Add attributes tags.
     *
     * @param parent Parent element.
     * @param attributes List of attributes.
     */
    private void addAttributesTags(Element parent, List<UMLAttribute> attributes) {
        for (UMLAttribute currentAttr : attributes) {
            addAttributeTag(parent, currentAttr);
        }
    }

    /**
     * Add operations tags.
     *
     * @param diagramClass Class element.
     * @param operations List of operations.
     */
    private void addOperationsTags(Element diagramClass, List<UMLOperation> operations) {
        for (UMLOperation currentOper : operations) {
            addOperationTag(diagramClass, currentOper);
        }
    }

    /**
     * Add class tag.
     *
     * @param rootElement Root element.
     * @param currentClass Current class to be saved.
     */
    private void addClassTag(Element rootElement, UMLClass currentClass) {
        Element diagramClass;

        /* Create class tag */
        diagramClass = this.doc.createElement("class");
        rootElement.appendChild(diagramClass);
        diagramClass.setAttribute("name", currentClass.getName());

        addAbstractTag(diagramClass, currentClass);
        addXCoordinateTag(diagramClass, currentClass);
        addYCoordinateTag(diagramClass, currentClass);
        addVisibilityTag(diagramClass, currentClass);

        /* Add attributes tags */
        List<UMLAttribute> classAttributes = currentClass.getAttributes();
        addAttributesTags(diagramClass, classAttributes);

        /* Add operations tags */
        List <UMLOperation> classOperations = currentClass.getOperations();
        addOperationsTags(diagramClass, classOperations);
    }

    /**
     * Add abstract tag.
     *
     * @param diagramClass Class element.
     * @param currentClass Current class to be abstract.
     */
    private void addAbstractTag(Element diagramClass, UMLClass currentClass) {
        Element abstractTag;

        /* Abstract tag */
        abstractTag = this.doc.createElement("abstract");
        diagramClass.appendChild(abstractTag);

        if (currentClass.isAbstract()) {
            abstractTag.setTextContent("true");
        } else {
            abstractTag.setTextContent("false");
        }
    }

    /**
     * Add x coordinate tag.
     *
     * @param diagramClass Class element.
     * @param currentClass Current class.
     */
    private void addXCoordinateTag(Element diagramClass, UMLClass currentClass) {
        Element xCoordinate;

        /* xCoordinate tag */
        xCoordinate = this.doc.createElement("xCoordinate");
        diagramClass.appendChild(xCoordinate);
        xCoordinate.setTextContent(Double.toString(currentClass.getXCoordinate()));
    }

    /**
     * Add y coordinate tag.
     *
     * @param diagramClass Class element.
     * @param currentClass Current class.
     */
    private void addYCoordinateTag(Element diagramClass, UMLClass currentClass) {
        Element yCoordinate;

        /* yCoordinate tag */
        yCoordinate = this.doc.createElement("yCoordinate");
        diagramClass.appendChild(yCoordinate);
        yCoordinate.setTextContent(Double.toString(currentClass.getYCoordinate()));
    }

    /**
     * Add visibility tag.
     *
     * @param parent Parent element.
     * @param current Current element.
     */
    private void addVisibilityTag(Element parent, com.uml.classdiagram.Element current) {
        Element visibility;

        /* visibility tag */
        visibility = this.doc.createElement("visibility");
        parent.appendChild(visibility);

        if (current instanceof UMLClass) {
            visibility.setTextContent(((UMLClass)current).getVisibility().name());
        } else {
            visibility.setTextContent(((UMLAttribute)current).getVisibility().name());
        }
    }

    /**
     * Add attribute tag.
     *
     * @param parent Parent element.
     * @param currentAttr Current attribute.
     */
    private void addAttributeTag(Element parent, UMLAttribute currentAttr) {
        Element attr;

        /* Create attribute tag */
        attr = this.doc.createElement("attribute");
        parent.appendChild(attr);
        attr.setAttribute("name", currentAttr.getName());

        addDataTypeTag(attr, currentAttr);
        addVisibilityTag(attr, currentAttr);
        addValueTag(attr, currentAttr);
    }

    /**
     * Add data type tag.
     *
     * @param parent Parent element.
     * @param currentAttr Current attribute.
     */
    private void addDataTypeTag(Element parent, UMLAttribute currentAttr) {
        Element dataType;

        /* dataType tag */
        dataType = this.doc.createElement("dataType");
        parent.appendChild(dataType);
        dataType.setTextContent(currentAttr.getType().toString());
    }

    /**
     * Add value tag.
     *
     * @param attr Attribute element.
     * @param currentAttr Current attribute.
     */
    private void addValueTag(Element attr, UMLAttribute currentAttr) {
        Element value;

        /* value tag */
        value = this.doc.createElement("value");
        attr.appendChild(value);

        value.setTextContent(currentAttr.getDefaultValue());
    }

    /**
     * Add operation tag.
     *
     * @param diagramClass Class element.
     * @param currentOper Current operation.
     */
    private void addOperationTag(Element diagramClass, UMLOperation currentOper) {
        Element oper;

        /* Create operation tag */
        oper = this.doc.createElement("operation");
        diagramClass.appendChild(oper);
        oper.setAttribute("name", currentOper.getName());

        addDataTypeTag(oper, currentOper);
        addVisibilityTag(oper, currentOper);

        /* Iterate through operation arguments */
        List<UMLAttribute> operArgs = currentOper.getArguments();
        addAttributesTags(oper, operArgs);
    }

    /*-----------------------------------------------------------------------------*/
    /*                           Relationship tag members                          */
    /*-----------------------------------------------------------------------------*/

    /**
     * Add relationship tag.
     *
     * @param rootElement Root element.
     * @param currentRel Current relationship.
     */
    private void addRelationshipTag(Element rootElement, UMLRelationship currentRel) {
        Element diagramRel;

        /* Create relationship tag */
        diagramRel = this.doc.createElement("relationship");
        rootElement.appendChild(diagramRel);
        diagramRel.setAttribute("id", Long.toString(currentRel.getId()));

        addFromTag(diagramRel, currentRel);
        addToTag(diagramRel, currentRel);

        if (currentRel instanceof UMLInheritance) {
            addClassLevelTag(diagramRel);
        } else {
            addInstanceLevelTag(diagramRel, currentRel);
        }
    }

    /**
     * Add from tag.
     *
     * @param diagramRel Relationship element.
     * @param currentRel Current relationship.
     */
    private void addFromTag(Element diagramRel, UMLRelationship currentRel) {
        Element from;

        /* from tag */
        from = this.doc.createElement("from");
        diagramRel.appendChild(from);
        from.setTextContent(currentRel.getFrom().getName());
    }

    /**
     * Add to tag.
     *
     * @param diagramRel Relationship element.
     * @param currentRel Current relationship.
     */
    private void addToTag(Element diagramRel, UMLRelationship currentRel) {
        Element to;

        /* to tag */
        to = this.doc.createElement("to");
        diagramRel.appendChild(to);
        to.setTextContent(currentRel.getTo().getName());
    }

    /**
     * Add class level tag.
     *
     * @param diagramRel Relationship element.
     */
    private void addClassLevelTag(Element diagramRel) {
        Element classLevel;

        /* classLevel tag */
        classLevel = this.doc.createElement("classLevel");
        diagramRel.appendChild(classLevel);

        addInheritanceTag(classLevel);
    }

    /**
     * Add inheritance tag.
     *
     * @param classLevel Class level element.
     */
    private void addInheritanceTag(Element classLevel) {
        Element inheritance;

        /* inheritance tag */
        inheritance = this.doc.createElement("inheritance");
        classLevel.appendChild(inheritance);
    }

    /**
     * Add instance level tag.
     *
     * @param diagramRel Relationship element.
     * @param currentRel Current relationship.
     */
    private void addInstanceLevelTag(Element diagramRel, UMLRelationship currentRel) {
        Element instanceLevel;

        /* instanceLevel tag */
        instanceLevel = this.doc.createElement("instanceLevel");
        diagramRel.appendChild(instanceLevel);

        addFromMultiplicityTag(instanceLevel, currentRel);
        addToMultiplicityTag(instanceLevel, currentRel);
        addFromRoleTag(instanceLevel, currentRel);
        addToRoleTag(instanceLevel, currentRel);

        if (currentRel instanceof UMLAssociation) {
            addAssociationTag(instanceLevel);
        } else if (currentRel instanceof UMLAggregation) {
            addAggregationTag(instanceLevel);
        } else if (currentRel instanceof UMLComposition) {
            addCompositionTag(instanceLevel);
        }
    }

    /**
     * Add from multiplicity.
     *
     * @param instanceLevel Instance level element.
     * @param currentRel Current relationship.
     */
    private void addFromMultiplicityTag(Element instanceLevel, UMLRelationship currentRel) {
        Element fromMultiplicity;

        /* fromMultiplicity tag */
        fromMultiplicity = this.doc.createElement("fromMultiplicity");
        instanceLevel.appendChild(fromMultiplicity);
        fromMultiplicity.setTextContent(((UMLInstanceLevel)currentRel).getFromMultiplicity().name());
    }

    /**
     * Add to multiplicity.
     *
     * @param instanceLevel Instance level element.
     * @param currentRel Current relationship.
     */
    private void addToMultiplicityTag(Element instanceLevel, UMLRelationship currentRel) {
        Element toMultiplicity;

        /* toMultiplicity tag */
        toMultiplicity = this.doc.createElement("toMultiplicity");
        instanceLevel.appendChild(toMultiplicity);
        toMultiplicity.setTextContent(((UMLInstanceLevel)currentRel).getToMultiplicity().name());
    }

    /**
     * Add from role tag.
     *
     * @param instanceLevel Instance level element.
     * @param currentRel Current relationship.
     */
    private void addFromRoleTag(Element instanceLevel, UMLRelationship currentRel) {
        Element fromRole;

        /* fromRole tag */
        fromRole = this.doc.createElement("fromRole");
        instanceLevel.appendChild(fromRole);
        fromRole.setTextContent(((UMLInstanceLevel)currentRel).getFromRole());
    }

    /**
     * Add to role tag.
     *
     * @param instanceLevel Instance level element.
     * @param currentRel Current relationship.
     */
    private void addToRoleTag(Element instanceLevel, UMLRelationship currentRel) {
        Element toRole;

        /* toRole tag */
        toRole = this.doc.createElement("toRole");
        instanceLevel.appendChild(toRole);
        toRole.setTextContent(((UMLInstanceLevel)currentRel).getToRole());
    }

    /**
     * Add association tag.
     *
     * @param instanceLevel Instance level element.
     */
    private void addAssociationTag(Element instanceLevel) {
        Element association;

        /* association tag */
        association = this.doc.createElement("association");
        instanceLevel.appendChild(association);
    }

    /**
     * Add aggregation tag.
     *
     * @param instanceLevel Instance level element.
     */
    private void addAggregationTag(Element instanceLevel) {
        Element aggregation;

        /* aggregation tag*/
        aggregation = this.doc.createElement("aggregation");
        instanceLevel.appendChild(aggregation);
    }

    /**
     * Add composition tag.
     *
     * @param instanceLevel Instance level element.
     */
    private void addCompositionTag(Element instanceLevel) {
        Element composition;

        /* composition tag */
        composition = this.doc.createElement("composition");
        instanceLevel.appendChild(composition);
    }


    /*-----------------------------------------------------------------------------*/
    /*                                  Write xml                                  */
    /*-----------------------------------------------------------------------------*/

    /**
     * Write xml to output file.
     *
     * @param doc Document.
     * @param output Output stream.
     * @throws TransformerException Transformer error.
     */
    private static void writeXml(Document doc,
                                 OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }
}
