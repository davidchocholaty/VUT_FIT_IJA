package com.uml;

import com.uml.classdiagram.ClassDiagram;
import com.uml.classdiagram.UMLClass;
import com.uml.customexception.InvalidOperationLabel;
import com.uml.sequencediagram.SequenceDiagram;
import com.uml.sequencediagram.UMLLifeline;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Optional;

public class SequenceController extends Parent {

    private static final double DEFAULT_SEQUENCE_HEIGHT = 40;
    public boolean sequenceAct = false;
    public boolean sequenceCreateAct = false;
    public boolean syncMessageAct = false;
    public boolean asyncMessageAct = false;
    public boolean returnMessageAct = false;
    public boolean destroyMessageAct = false;
    public boolean actiovationAct = false;
    public ToggleButton sequenceButton;
    public ToggleButton sequenceCreateButton;
    public ToggleButton returnMessageButton;
    public ToggleButton destroyMessageButton;
    public ToggleButton activationButton;
    static int count = 0;
    public Pane rPane;
    public static SequenceUML tmpNode;
    public ToggleButton syncMessageButton;
    public ToggleButton asyncMessageButton;
    private String messageID;
    private Double activation;
    private static SequenceUML tmpNode2;
    static ToggleGroup group = new ToggleGroup();

    public SequenceDiagram sequenceDiagram;
    public ClassDiagram classDiagram;

    public void initialize() {
        group.getToggles().addAll(sequenceButton, sequenceCreateButton, syncMessageButton, asyncMessageButton, returnMessageButton, destroyMessageButton, activationButton);
    }

    public void sequenceActive(MouseEvent mouseEvent) {
        if (sequenceButton.isSelected()) {
            sequenceAct = true;
            sequenceCreateAct = false;
            syncMessageAct = false;
            asyncMessageAct = false;
            returnMessageAct = false;
            destroyMessageAct = false;
            actiovationAct = false;
            tmpNode = null;
            tmpNode2 = null;
            count = 0;
        }else if (sequenceCreateButton.isSelected()) {
            sequenceAct = false;
            sequenceCreateAct = true;
            syncMessageAct = false;
            asyncMessageAct = false;
            returnMessageAct = false;
            destroyMessageAct = false;
            actiovationAct = false;
            tmpNode = null;
            tmpNode2 = null;
            messageID = "Create";
            count = 0;
        }else if (syncMessageButton.isSelected()) {
            sequenceAct = false;
            sequenceCreateAct = false;
            syncMessageAct = true;
            asyncMessageAct = false;
            returnMessageAct = false;
            destroyMessageAct = false;
            actiovationAct = false;
            tmpNode = null;
            tmpNode2 = null;
            messageID = "sync";
            count = 0;
        }else if (asyncMessageButton.isSelected()) {
            sequenceAct = false;
            sequenceCreateAct = false;
            syncMessageAct = false;
            asyncMessageAct = true;
            returnMessageAct = false;
            destroyMessageAct = false;
            actiovationAct = false;
            tmpNode = null;
            tmpNode2 = null;
            messageID = "async";
            count = 0;
        }else if (returnMessageButton.isSelected()) {
            sequenceAct = false;
            sequenceCreateAct = false;
            syncMessageAct = false;
            asyncMessageAct = false;
            returnMessageAct = true;
            destroyMessageAct = false;
            actiovationAct = false;
            tmpNode = null;
            tmpNode2 = null;
            messageID = "return";
            count = 0;
        }else if (destroyMessageButton.isSelected()) {
            sequenceAct = false;
            sequenceCreateAct = false;
            syncMessageAct = false;
            asyncMessageAct = false;
            returnMessageAct = false;
            destroyMessageAct = true;
            actiovationAct = false;
            tmpNode = null;
            tmpNode2 = null;
            messageID = "return";
            count = 0;
        }else if (activationButton.isSelected()) {
            sequenceAct = false;
            sequenceCreateAct = false;
            syncMessageAct = false;
            asyncMessageAct = false;
            returnMessageAct = false;
            destroyMessageAct = false;
            actiovationAct = true;
            tmpNode = null;
            tmpNode2 = null;
            count = 0;
        }else{
            sequenceAct = false;
            tmpNode = null;
            tmpNode2 = null;
        }
    }

    public void handleKeyEvents(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE && tmpNode.getView() != null) {
            for (Node a : tmpNode.edges) {
                rPane.getChildren().remove(a);
            }
            rPane.getChildren().remove(tmpNode);
        }
    }

    public void addElement(MouseEvent mouseEvent) throws IOException {
        /* Lifeline */
        if(sequenceAct) {
            rPane.getChildren().add(createElement(mouseEvent));
        }
    }

    private SequenceUML createElement(MouseEvent mouseEvent) throws IOException {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("class name");
        dialog.setHeaderText("Enter class name:");
        dialog.setContentText("Class name:");

        Optional<String> name = dialog.showAndWait();
        String[] nm = {""};

        name.ifPresent(s -> {
            nm[0] = name.get();
        });

        UMLClass cls = this.classDiagram.findClass(nm[0]);

        // TODO height
        UMLLifeline lifeline = this.sequenceDiagram.createLifeline(cls, 0.0);
        lifeline.setXCoordinate(mouseEvent.getX());

        SequenceUML sq = new SequenceUML(mouseEvent.getX(), DEFAULT_SEQUENCE_HEIGHT, cls, rPane);

        sq.lifeline = lifeline;

        sq.getView().setOnMouseDragged(e -> drag(e, sq));
        sq.getView().setOnMouseClicked(e -> sequenceClicked(e, sq));
        sq.getClickLine().setOnMouseClicked(e -> timeLineClicked(e, sq));
        sq.getClickLine().setOnDragDetected(e -> timeLineDragDetected(e, sq));
        rPane.setOnMouseDragReleased(e -> {
            try {
                timeLineDragDone(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sq.getView().setOnContextMenuRequested(e -> changeHeight(e, sq));
        return sq;
    }

    private void timeLineDragDone(MouseDragEvent e) throws IOException {
        System.out.println();
        if(tmpNode != null){
            tmpNode2 = createAndAddElement(e);
            Node message = createAndAddMessage(e, tmpNode, tmpNode2, messageID);
            tmpNode = null;
            tmpNode2 = null;
        }
    }

    private SequenceUML createAndAddElement(MouseDragEvent mouseEvent) throws IOException {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("class name");
        dialog.setHeaderText("Enter class name:");
        dialog.setContentText("Class name:");

        Optional<String> name = dialog.showAndWait();
        String[] nm = {""};

        name.ifPresent(s -> {
            nm[0] = name.get();
        });

        UMLClass cls = this.classDiagram.findClass(nm[0]);

        // TODO height
        UMLLifeline lifeline = this.sequenceDiagram.createLifeline(cls, 0.0);
        lifeline.setXCoordinate(mouseEvent.getX());

        SequenceUML sq = new SequenceUML(mouseEvent.getX(), mouseEvent.getY(), cls, rPane);
        rPane.getChildren().add(sq);

        sq.lifeline = lifeline;

        sq.getView().setOnMouseDragged(e -> drag(e, sq));
        sq.getView().setOnMouseClicked(e -> sequenceClicked(e, sq));
        sq.getClickLine().setOnMouseClicked(e -> timeLineClicked(e, sq));
        sq.getClickLine().setOnDragDetected(e -> timeLineDragDetected(e, sq));
        rPane.setOnMouseDragReleased(e -> {
            try {
                timeLineDragDone(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sq.getView().setOnContextMenuRequested(e -> changeHeight(e, sq));
        return sq;
    }

    private void changeHeight(ContextMenuEvent e, SequenceUML sq) {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Height");
        dialog.setHeaderText("Enter lifeLine height:");
        dialog.setContentText("height:");

        Optional<String> name = dialog.showAndWait();
        String[] nm = {""};

        name.ifPresent(s -> {
            nm[0] = name.get();
        });
        sq.setY2(Integer.parseInt(nm[0])+80);
    }

    private void timeLineDragDetected(MouseEvent e, SequenceUML sq){
        if (sequenceCreateAct){
            sq.getClickLine().startFullDrag();
            tmpNode = sq;
        }
    }

    private void sequenceClicked(MouseEvent e, SequenceUML sq) {
        tmpNode = sq;
    }

    private Node createMessage(MouseEvent e,SequenceUML fromNode, SequenceUML sq, String messageID) {
        String messageText = getMessage(messageID);


        // TODO
        String operation = "operation";
        String[] tmp = {"t", "m", "p"};
        String label = "label";
/*
        try {
            switch (messageID) {
                case "sync":
                    this.sequenceDiagram.createSynchronousMessage(fromNode.lifeline, sq.lifeline, operation, tmp);
                    break;
                case "async":
                    this.sequenceDiagram.createAsynchronousMessage(fromNode.lifeline, sq.lifeline, operation, tmp);
                    break;
                case "return":
                    this.sequenceDiagram.createReturnMessage(fromNode.lifeline, sq.lifeline, label);
                    break;
                case "syncSelf":
                    this.sequenceDiagram.createSynchronousSelfMessage(fromNode.lifeline, operation, tmp);
                    break;
                case "asyncSelf":
                    this.sequenceDiagram.createAsynchronousSelfMessage(fromNode.lifeline, operation, tmp);
                    break;
                case "returnSelf":
                    this.sequenceDiagram.createReturnSelfMessage(fromNode.lifeline, operation, tmp);
                    break;
                default:
                    break;
            }
        } catch (InvalidOperationLabel err) {
            // TODO
            err.printStackTrace();
        }

 */
        Message message = new Message(fromNode.getView().getLayoutX(), e.getY(), sq.getView().getLayoutX(), e.getY(), messageID, messageText);

        message.x1Property().bind(fromNode.getView().layoutXProperty());
        message.x2Property().bind(sq.getView().layoutXProperty());

        fromNode.edges.add(message);
        sq.edges.add(message);
        return message;
    }

    private String getMessage(String messageID) {
        TextInputDialog dialog;
        if (messageID.equals("return")){
            dialog = new TextInputDialog();
            dialog.setTitle(messageID+" Message");
            dialog.setHeaderText("Enter return value");
            dialog.setContentText("value:");
        }else if(messageID.equals("Create")){
            dialog = new TextInputDialog();
            dialog.setTitle(messageID+" Message");
            dialog.setHeaderText("Enter create message <<Create>>(parameters)");
            dialog.setContentText("Message:");
        }else{
            dialog = new TextInputDialog();
            dialog.setTitle(messageID+" Message");
            dialog.setHeaderText("Enter create message Operation(parameters)");
            dialog.setContentText("Message:");
        }

        Optional<String> name = dialog.showAndWait();
        final String[] message = new String[1];

        name.ifPresent(s -> {
            message[0] = s;
        });
        return message[0];
    }

    private Node createAndAddMessage(MouseDragEvent e, SequenceUML fromNode, SequenceUML sq, String messageID) {
        String messageText = getMessage(messageID);

        // TODO
        String[] tmp = {"t", "m", "p"};

        /*try {
            this.sequenceDiagram.createCreateMessage(fromNode.lifeline, sq.lifeline, tmp);
        } catch (InvalidOperationLabel err) {
            // TODO
            err.printStackTrace();
        }*/
        Message message = new Message(fromNode.getView().getLayoutX(), e.getY(), sq.getView().getLayoutX(), e.getY(), messageID, messageText);
        message.x1Property().bind(fromNode.getView().layoutXProperty());
        message.x2Property().bind(sq.getView().layoutXProperty());

        rPane.getChildren().add(message);

        fromNode.edges.add(message);
        sq.edges.add(message);
        return message;
    }

    private void timeLineClicked(MouseEvent e, SequenceUML sq) {
        if (syncMessageAct || asyncMessageAct || returnMessageAct) {
            System.out.println("click");
            if (count == 0) {
                tmpNode = sq;
                count++;
            } else if (count == 1) {
                if (tmpNode == (Node)sq){
                    if(syncMessageAct){
                        rPane.getChildren().add(createMessage(e, tmpNode, sq, "syncSelf"));
                    }else if(asyncMessageAct){
                        rPane.getChildren().add(createMessage(e, tmpNode, sq, "asyncSelf"));
                    }else if(returnMessageAct){
                        rPane.getChildren().add(createMessage(e, tmpNode, sq, "returnSelf"));
                    }
                }else {
                    rPane.getChildren().add(createMessage(e, tmpNode, sq, messageID));
                }
                count = 0;
            }
        }else if(actiovationAct){
            if (count == 0) {
                activation = e.getY();
                count++;
            }else if(count == 1){
                rPane.getChildren().add(createActivation(e, activation, sq));
                count = 0;
                activation = 0.0;
            }
        }else if(destroyMessageAct){
            rPane.getChildren().add(createDelete(e, sq));
        }
    }

    private Node createDelete(MouseEvent e, SequenceUML sq) {
        Cross cross = new Cross(sq.getView().getLayoutX(), e.getY());
        cross.x1Property().bind(sq.getView().layoutXProperty());
        sq.setY2(e.getY() + 40);
        sq.edges.add(cross);
        return cross;
    }

    private Node createActivation(MouseEvent e, Double activation, SequenceUML sq){
        ActivationSequenceUML activ = new ActivationSequenceUML(activation, sq.getView().getLayoutX(), e.getY(), sq);
        activ.x1Property().bind(sq.getView().layoutXProperty());

        sq.edges.add(activ);
        return activ;
    }

    private void drag(MouseEvent e, SequenceUML sq) {
        double x = sq.getView().getTranslateX() + e.getX() + sq.getView().getLayoutX();
        sq.getView().setLayoutX(x);
    }
}
