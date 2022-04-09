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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/* https://mkyong.com/java/how-to-create-xml-file-in-java-dom/ */
public class SaveHandler {
    private final ClassDiagram diagram;

    public SaveHandler(ClassDiagram diagram) {
        this.diagram = diagram;
    }

    public void saveClassDiagram(String destPath) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement("classDiagram");
        doc.appendChild(rootElement);
        rootElement.setAttribute("name", this.diagram.getName());

        List<UMLClass> diagramClasses = this.diagram.getClasses();
        List<UMLRelationship> diagramRelationships = this.diagram.getRelationships();

        Element diagramClass;
        Element abstractTag;
        Element xCoordinate;
        Element yCoordinate;
        Element visibility;
        Element attr;
        Element dataType;
        Element value;
        Element oper;
        Element diagramRel;
        Element from;
        Element to;
        Element classLevel;
        Element instanceLevel;
        Element inheritance;
        Element fromMultiplicity;
        Element toMultiplicity;
        Element fromRole;
        Element toRole;
        Element association;
        Element aggregation;
        Element composition;

        /* Iterate through classes */
        for (UMLClass currentClass : diagramClasses) {
            /* Create class tag */
            diagramClass = doc.createElement("class");
            rootElement.appendChild(diagramClass);
            diagramClass.setAttribute("name", currentClass.getName());

            /* Abstract tag */
            abstractTag = doc.createElement("abstract");
            diagramClass.appendChild(abstractTag);

            if (currentClass.isAbstract()) {
                abstractTag.setTextContent("true");
            } else {
                abstractTag.setTextContent("false");
            }

            /* xCoordinate tag */
            xCoordinate = doc.createElement("xCoordinate");
            diagramClass.appendChild(xCoordinate);
            xCoordinate.setTextContent(Double.toString(currentClass.getXCoordinate()));

            /* yCoordinate tag */
            yCoordinate = doc.createElement("yCoordinate");
            diagramClass.appendChild(yCoordinate);
            yCoordinate.setTextContent(Double.toString(currentClass.getYCoordinate()));

            /* visibility tag */
            visibility = doc.createElement("visibility");
            diagramClass.appendChild(visibility);
            visibility.setTextContent(currentClass.getVisibility().name());

            /* Iterate through attributes */
            List<UMLAttribute> classAttributes = currentClass.getAttributes();

            for (UMLAttribute currentAttr : classAttributes) {
                /* Create attribute tag */
                attr = doc.createElement("attribute");
                diagramClass.appendChild(attr);
                attr.setAttribute("name", currentAttr.getName());

                /* dataType tag */
                dataType = doc.createElement("dataType");
                attr.appendChild(dataType);
                dataType.setTextContent(currentAttr.getType().toString());

                /* visibility tag */
                visibility = doc.createElement("visibility");
                attr.appendChild(visibility);
                visibility.setTextContent(currentAttr.getVisibility().name());

                /* value tag */
                value = doc.createElement("value");
                attr.appendChild(value);
                value.setTextContent(currentAttr.getDefaultValue());
            }

            /* Iterate through operations */
            List <UMLOperation> classOperations = currentClass.getOperations();

            for (UMLOperation currentOper : classOperations) {
                /* Create operation tag */
                oper = doc.createElement("operation");
                diagramClass.appendChild(oper);
                oper.setAttribute("name", currentOper.getName());

                /* dataType tag */
                dataType = doc.createElement("dataType");
                oper.appendChild(dataType);
                dataType.setTextContent(currentOper.getType().toString());

                /* visibility tag */
                visibility = doc.createElement("visibility");
                oper.appendChild(visibility);
                visibility.setTextContent(currentOper.getVisibility().name());

                /* Iterate through operation arguments */
                List<UMLAttribute> operArgs = currentOper.getArguments();

                for (UMLAttribute currentArg : operArgs) {
                    /* Create attribute tag */
                    attr = doc.createElement("attribute");
                    oper.appendChild(attr);
                    attr.setAttribute("name", currentArg.getName());

                    /* dataType tag */
                    dataType = doc.createElement("dataType");
                    attr.appendChild(dataType);
                    dataType.setTextContent(currentArg.getType().toString());

                    /* visibility tag */
                    visibility = doc.createElement("visibility");
                    attr.appendChild(visibility);
                    visibility.setTextContent(currentArg.getVisibility().name());

                    /* value tag */
                    value = doc.createElement("value");
                    attr.appendChild(value);
                    value.setTextContent(currentArg.getDefaultValue());
                }
            }
        }

        /* Iterate through relationships */
        for (UMLRelationship currentRel : diagramRelationships) {
            /* Create relationship tag */
            diagramRel = doc.createElement("relationship");
            rootElement.appendChild(diagramRel);
            diagramRel.setAttribute("id", Long.toString(currentRel.getId()));

            /* from tag */
            from = doc.createElement("from");
            diagramRel.appendChild(from);
            from.setTextContent(currentRel.getFrom().getName());

            /* to tag */
            to = doc.createElement("to");
            diagramRel.appendChild(to);
            to.setTextContent(currentRel.getTo().getName());

            if (currentRel instanceof UMLInheritance) {
                /* classLevel tag */
                classLevel = doc.createElement("classLevel");
                diagramRel.appendChild(classLevel);

                /* inheritance tag */
                inheritance = doc.createElement("inheritance");
                classLevel.appendChild(inheritance);
            } else {
                /* instanceLevel tag */
                instanceLevel = doc.createElement("instanceLevel");
                diagramRel.appendChild(instanceLevel);

                /* fromMultiplicity tag */
                fromMultiplicity = doc.createElement("fromMultiplicity");
                instanceLevel.appendChild(fromMultiplicity);
                fromMultiplicity.setTextContent(((UMLInstanceLevel)currentRel).getFromMultiplicity().name());

                /* toMultiplicity tag */
                toMultiplicity = doc.createElement("toMultiplicity");
                instanceLevel.appendChild(toMultiplicity);
                toMultiplicity.setTextContent(((UMLInstanceLevel)currentRel).getToMultiplicity().name());

                /* fromRole tag */
                fromRole = doc.createElement("fromRole");
                instanceLevel.appendChild(fromRole);
                fromRole.setTextContent(((UMLInstanceLevel)currentRel).getFromRole());

                /* toRole tag */
                toRole = doc.createElement("toRole");
                instanceLevel.appendChild(toRole);
                toRole.setTextContent(((UMLInstanceLevel)currentRel).getToRole());

                if (currentRel instanceof UMLAssociation) {
                    /* association tag */
                    association = doc.createElement("association");
                    instanceLevel.appendChild(association);

                } else if (currentRel instanceof UMLAggregation) {
                    /* aggregation tag*/
                    aggregation = doc.createElement("aggregation");
                    instanceLevel.appendChild(aggregation);

                } else if (currentRel instanceof UMLComposition) {
                    /* composition tag */
                    composition = doc.createElement("composition");
                    instanceLevel.appendChild(composition);

                }
            }
        }

        try (FileOutputStream output = new FileOutputStream(destPath)) {
            writeXml(doc, output);
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

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
