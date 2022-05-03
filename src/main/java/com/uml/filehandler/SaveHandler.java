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
import com.uml.sequencediagram.*;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing handler for saving diagram into *.ijaxml or *.ixl file in xml format.
 * <p>
 *     The main goal of this class is save diagram into output file.
 * </p>
 */
public class SaveHandler {
    private final ClassDiagram classDiagram;
    private List<SequenceDiagram> sequenceDiagrams;
    private Document doc;

    /**
     * Creates a new instance of SaveHandler.
     *
     * @param classDiagram Class diagram.
     * @param sequenceDiagrams Sequence diagrams.
     */
    public SaveHandler(ClassDiagram classDiagram, SequenceDiagram... sequenceDiagrams) {
        this.classDiagram = classDiagram;
        this.sequenceDiagrams = new ArrayList<SequenceDiagram>();
        doc = null;

        Collections.addAll(this.sequenceDiagrams, sequenceDiagrams);
    }

    /**
     * Main method for diagrams saving.
     *
     * @param destPath Output file destination path.
     * @throws ParserConfigurationException Parser configuration error.
     * @throws FileNotFoundException File was not found (FileOutputStream).
     * @throws TransformerException Transformer error.
     */
    public void save(String destPath) throws ParserConfigurationException, FileNotFoundException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        this.doc = docBuilder.newDocument();

        Element rootElement = this.doc.createElement("ijaUml");
        this.doc.appendChild(rootElement);

        this.saveClassDiagram(rootElement);
        this.saveSequenceDiagrams(rootElement);

        FileOutputStream output = new FileOutputStream(destPath);
        writeXml(this.doc, output);
    }

    /*-------------------------------------------------------------------------------------------------*/
    /*                                          CLASS DIAGRAM                                          */
    /*-------------------------------------------------------------------------------------------------*/

    /**
     * Main method for class diagram saving.
     *
     * @param rootElement Root element.
     */
    private void saveClassDiagram(Element rootElement) {
        Element classDiagramElement = this.doc.createElement("classDiagram");
        rootElement.appendChild(classDiagramElement);
        classDiagramElement.setAttribute("name", this.classDiagram.getName());

        List<UMLClass> diagramClasses = this.classDiagram.getClasses();
        List<UMLRelationship> diagramRelationships = this.classDiagram.getRelationships();

        addClassTags(classDiagramElement, diagramClasses);
        addRelationshipsTags(classDiagramElement, diagramRelationships);
    }

    /**
     * Add all diagram classes tags.
     *
     * @param classDiagramElement Class diagram element.
     * @param diagramClasses Classes of diagram.
     */
    private void addClassTags(Element classDiagramElement, List<UMLClass> diagramClasses) {
        /* Iterate through classes */
        for (UMLClass currentClass : diagramClasses) {
            addClassTag(classDiagramElement, currentClass);
        }
    }

    /**
     * Add all diagram relationships tags.
     *
     * @param classDiagramElement Class diagram element.
     * @param diagramRelationships Relationships of diagram.
     */
    private void addRelationshipsTags(Element classDiagramElement, List<UMLRelationship> diagramRelationships) {
        /* Iterate through relationships */
        for (UMLRelationship currentRel : diagramRelationships) {
            addRelationshipTag(classDiagramElement, currentRel);
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
     * @param classDiagramElement Class diagram element.
     * @param currentClass Current class to be saved.
     */
    private void addClassTag(Element classDiagramElement, UMLClass currentClass) {
        Element diagramClass;

        /* Create class tag */
        diagramClass = this.doc.createElement("class");
        classDiagramElement.appendChild(diagramClass);
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
     * @param classDiagramElement Class diagram element.
     * @param currentRel Current relationship.
     */
    private void addRelationshipTag(Element classDiagramElement, UMLRelationship currentRel) {
        Element diagramRel;

        /* Create relationship tag */
        diagramRel = this.doc.createElement("relationship");
        classDiagramElement.appendChild(diagramRel);
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


    /*-------------------------------------------------------------------------------------------------*/
    /*                                        SEQUENCE DIAGRAM                                         */
    /*-------------------------------------------------------------------------------------------------*/

    /**
     * Main method for sequence diagrams saving.
     *
     * @param rootElement Root element.
     */
    private void saveSequenceDiagrams(Element rootElement) {
        Element sequenceDiagramElement;

        for (SequenceDiagram currentSequenceDiagram : this.sequenceDiagrams) {
            sequenceDiagramElement = this.doc.createElement("sequenceDiagram");
            rootElement.appendChild(sequenceDiagramElement);
            sequenceDiagramElement.setAttribute("name", currentSequenceDiagram.getName());

            List<UMLLifeline> diagramLifelines = currentSequenceDiagram.getLifelines();
            List<UMLMessage> diagramMessages = currentSequenceDiagram.getMessages();

            addLifelinesTags(sequenceDiagramElement, diagramLifelines);
            addMessagesTags(sequenceDiagramElement, diagramMessages);
        }
    }

    /**
     * Add all sequence diagram lifelines tags.
     *
     * @param sequenceDiagramElement Sequence diagram element.
     * @param diagramLifelines Sequence diagram lifelines.
     */
    private void addLifelinesTags(Element sequenceDiagramElement,
                                  List<UMLLifeline> diagramLifelines) {
        for (UMLLifeline currentLifeline : diagramLifelines) {
            addLifelineTag(sequenceDiagramElement, currentLifeline);
        }
    }

    /**
     * Add lifeline tag.
     *
     * @param sequenceDiagramElement Sequence diagram element.
     * @param currentLifeline Current lifeline to be saved.
     */
    private void addLifelineTag(Element sequenceDiagramElement, UMLLifeline currentLifeline) {
        Element diagramLifeline;

        /* Create lifeline tag */
        diagramLifeline = this.doc.createElement("lifeline");
        sequenceDiagramElement.appendChild(diagramLifeline);
        diagramLifeline.setAttribute("name", currentLifeline.getObjectClass().getName());
        diagramLifeline.setAttribute("id", Long.toString(currentLifeline.getId()));

        addHeightTag(diagramLifeline, currentLifeline);
        addXCoordinateTag(diagramLifeline, currentLifeline);
    }

    /**
     * Add height tag.
     *
     * @param diagramLifeline Lifeline element.
     * @param currentLifeline Current lifeline to be saved.
     */
    private void addHeightTag(Element diagramLifeline, UMLLifeline currentLifeline) {
        Element height;

        /* height tag */
        height = this.doc.createElement("height");
        diagramLifeline.appendChild(height);
        height.setTextContent(Double.toString(currentLifeline.getHeight()));
    }

    /**
     * Add x coordinate tag.
     * <p>
     *     This method is overridden method of the class diagram method version for sequence diagram.
     * </p>
     *
     * @param diagramLifeline Lifeline element.
     * @param currentLifeline Current lifeline to be saved.
     */
    private void addXCoordinateTag(Element diagramLifeline, UMLLifeline currentLifeline) {
        Element xCoordinate;

        /* xCoordinate tag */
        xCoordinate = this.doc.createElement("xCoordinate");
        diagramLifeline.appendChild(xCoordinate);
        xCoordinate.setTextContent(Double.toString(currentLifeline.getXCoordinate()));
    }

    /**
     * Add all diagram messages tags.
     *
     * @param sequenceDiagramElement Sequence diagram element.
     * @param diagramMessages Messages of diagram.
     */
    private void addMessagesTags(Element sequenceDiagramElement,
                                 List<UMLMessage> diagramMessages) {
        for (UMLMessage currentMessage : diagramMessages) {
            addMessageTag(sequenceDiagramElement, currentMessage);
        }
    }

    /**
     * Add message tag.
     *
     * @param sequenceDiagramElement Sequence diagram element.
     * @param currentMessage Current message to be saved.
     */
    private void addMessageTag(Element sequenceDiagramElement, UMLMessage currentMessage) {
        Element diagramMessage;

        /* Create message tag */
        diagramMessage = this.doc.createElement("message");
        sequenceDiagramElement.appendChild(diagramMessage);
        diagramMessage.setAttribute("name", currentMessage.getLabel());
        diagramMessage.setAttribute("id", Long.toString(currentMessage.getId()));

        addFromTag(diagramMessage, currentMessage);
        addToTag(diagramMessage, currentMessage);
        addYCoordinateTag(diagramMessage, currentMessage);

        if (currentMessage instanceof UMLMessageLabelType) {
            addLabelTypeTag(diagramMessage, currentMessage);
        } else {
            addOperationTypeTag(diagramMessage, currentMessage);
        }
    }

    /**
     * Add from tag.
     * <p>
     *     This method is overridden method of the class diagram method version for sequence diagram.
     * </p>
     *
     * @param diagramMessage Message element.
     * @param currentMessage Current message.
     */
    private void addFromTag(Element diagramMessage, UMLMessage currentMessage) {
        Element from;

        /* from tag */
        from = this.doc.createElement("from");
        diagramMessage.appendChild(from);
        from.setTextContent(currentMessage.getFromLifeline().getObjectClass().getName());
    }

    /**
     * Add to tag.
     * <p>
     *     This method is overridden method of the class diagram method version for sequence diagram.
     * </p>
     *
     * @param diagramMessage Message element.
     * @param currentMessage Current message.
     */
    private void addToTag(Element diagramMessage, UMLMessage currentMessage) {
        Element to;

        /* to tag */
        to = this.doc.createElement("to");
        diagramMessage.appendChild(to);
        to.setTextContent(currentMessage.getFromLifeline().getObjectClass().getName());
    }

    /**
     * Add y coordinate tag.
     * <p>
     *     This method is overridden method of the class diagram method version for sequence diagram.
     * </p>
     *
     * @param diagramMessage Message element.
     * @param currentMessage Current message.
     */
    private void addYCoordinateTag(Element diagramMessage, UMLMessage currentMessage) {
        Element yCoordinate;

        /* yCoordinate tag */
        yCoordinate = this.doc.createElement("yCoordinate");
        diagramMessage.appendChild(yCoordinate);
        yCoordinate.setTextContent(Double.toString(currentMessage.getYCoordinate()));
    }

    /**
     * Add label type tag.
     *
     * @param diagramMessage Message element.
     * @param currentMessage Current message.
     */
    private void addLabelTypeTag(Element diagramMessage, UMLMessage currentMessage) {
        Element labelType;

        /* labelType tag */
        labelType = this.doc.createElement("labelType");
        diagramMessage.appendChild(labelType);

        if (currentMessage instanceof UMLReturnMessage) {
            addReturnMessageTag(labelType);
        } else if (currentMessage instanceof UMLDestroyMessage) {
            addDestroyMessageTag(labelType);
        }
    }

    /**
     * Add return message tag.
     *
     * @param labelType Label type element.
     */
    private void addReturnMessageTag(Element labelType) {
        Element returnMessage;

        /* returnMessage tag */
        returnMessage = this.doc.createElement("returnMessage");
        labelType.appendChild(returnMessage);
    }

    /**
     * Add destroy message tag.
     *
     * @param labelType Label type element.
     */
    private void addDestroyMessageTag(Element labelType) {
        Element destroyMessage;

        /* destroyMessage tag */
        destroyMessage = this.doc.createElement("destroyMessage");
        labelType.appendChild(destroyMessage);
    }

    /**
     * Add operation type tag.
     *
     * @param diagramMessage Message element.
     * @param currentMessage Current message.
     */
    private void addOperationTypeTag(Element diagramMessage, UMLMessage currentMessage) {
        Element operationType;

        /* operationType tag */
        operationType = this.doc.createElement("operationType");
        diagramMessage.appendChild(operationType);

        if (currentMessage instanceof UMLSynchronousMessage) {
            addSynchronousMessageTag(operationType);
        } else if (currentMessage instanceof UMLAsynchronousMessage) {
            addAsynchronousMessageTag(operationType);
        } else if (currentMessage instanceof UMLSelfMessage) {
            addSelfMessageTag(operationType);
        } else if (currentMessage instanceof UMLCreateMessage) {
            addCreateMessageTag(operationType);
        }
    }

    /**
     * Add synchronous message tag.
     *
     * @param operationType Label type element.
     */
    private void addSynchronousMessageTag(Element operationType) {
        Element synchronousMessage;

        /* synchronousMessage tag */
        synchronousMessage = this.doc.createElement("synchronousMessage");
        operationType.appendChild(synchronousMessage);
    }

    /**
     * Add asynchronous message tag.
     *
     * @param operationType Label type element.
     */
    private void addAsynchronousMessageTag(Element operationType) {
        Element asynchronousMessage;

        /* asynchronousMessage tag */
        asynchronousMessage = this.doc.createElement("asynchronousMessage");
        operationType.appendChild(asynchronousMessage);
    }

    /**
     * Add self message tag.
     *
     * @param operationType Label type element.
     */
    private void addSelfMessageTag(Element operationType) {
        Element selfMessage;

        /* selfMessage tag */
        selfMessage = this.doc.createElement("selfMessage");
        operationType.appendChild(selfMessage);
    }

    /**
     * Add create message tag.
     *
     * @param operationType Label type element.
     */
    private void addCreateMessageTag(Element operationType) {
        Element createMessage;

        /* createMessage tag */
        createMessage = this.doc.createElement("createMessage");
        operationType.appendChild(createMessage);
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
