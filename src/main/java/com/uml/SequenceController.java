package com.uml;

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
    public ToggleButton sequenceButton;
    public ToggleButton sequenceCreateButton;
    static int count = 0;
    public Pane rPane;
    public static SequenceUML tmpNode;
    public ToggleButton syncMessageButton;
    public ToggleButton asyncMessageButton;
    private String messageID;
    private static SequenceUML tmpNode2;
    static ToggleGroup group = new ToggleGroup();

    public void initialize() {
        group.getToggles().addAll(sequenceButton, sequenceCreateButton, syncMessageButton, asyncMessageButton);
    }

    public void sequenceActive(MouseEvent mouseEvent) {
        if (sequenceButton.isSelected()) {
            sequenceAct = true;
            sequenceCreateAct = false;
            syncMessageAct = false;
            asyncMessageAct = false;
            tmpNode = null;
            tmpNode2 = null;
            count = 0;
        }else if (sequenceCreateButton.isSelected()) {
            sequenceAct = false;
            sequenceCreateAct = true;
            syncMessageAct = false;
            asyncMessageAct = false;
            tmpNode = null;
            tmpNode2 = null;
            messageID = "Create";
            count = 0;
        }else if (syncMessageButton.isSelected()) {
            sequenceAct = false;
            sequenceCreateAct = false;
            syncMessageAct = true;
            asyncMessageAct = false;
            tmpNode = null;
            tmpNode2 = null;
            messageID = "sync";
            count = 0;
        }else if (asyncMessageButton.isSelected()) {
            sequenceAct = false;
            sequenceCreateAct = false;
            syncMessageAct = false;
            asyncMessageAct = true;
            tmpNode = null;
            tmpNode2 = null;
            messageID = "async";
            count = 0;
        }else{
            sequenceAct = false;

            tmpNode = null;
            tmpNode2 = null;
        }
    }

    public void handleKeyEvents(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE && tmpNode.getView() != null) {
            for (Message a : tmpNode.edges) {
                rPane.getChildren().remove(a);
            }
            rPane.getChildren().remove(tmpNode);
        }
    }

    public void addElement(MouseEvent mouseEvent) throws IOException {
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
        SequenceUML sq = new SequenceUML(mouseEvent.getX(), DEFAULT_SEQUENCE_HEIGHT, nm[0], rPane);

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
        SequenceUML sq = new SequenceUML(mouseEvent.getX(), mouseEvent.getY(), nm[0], rPane);
        rPane.getChildren().add(sq);

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
        return sq;
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

    private Node createMessage(MouseEvent e,SequenceUML tmpNode, SequenceUML sq, String messageID) {
        Message message = new Message(tmpNode.getView().getLayoutX(), e.getY(), sq.getView().getLayoutX(), e.getY(),  messageID);

        message.x1Property().bind(tmpNode.getView().layoutXProperty());
        message.x2Property().bind(sq.getView().layoutXProperty());

        tmpNode.edges.add(message);
        sq.edges.add(message);

        return message;
    }

    private Node createAndAddMessage(MouseDragEvent e, SequenceUML tmpNode, SequenceUML sq, String messageID) {
        Message message = new Message(tmpNode.getView().getLayoutX(), e.getY(), sq.getView().getLayoutX(), e.getY(), messageID);
        message.x1Property().bind(tmpNode.getView().layoutXProperty());
        message.x2Property().bind(sq.getView().layoutXProperty());

        rPane.getChildren().add(message);

        tmpNode.edges.add(message);
        sq.edges.add(message);

        return message;
    }

    private void timeLineClicked(MouseEvent e, SequenceUML sq) {
        if (syncMessageAct || asyncMessageAct) {
            System.out.println("click");
            if (count == 0) {
                tmpNode = sq;
                count++;
            } else if (count == 1) {
                if (tmpNode == (Node)sq){
                    if(syncMessageAct){
                        rPane.getChildren().add(createMessage(e, tmpNode, sq, "syncSelf"));
                    }else{
                        rPane.getChildren().add(createMessage(e, tmpNode, sq, "asyncSelf"));
                    }
                }else {
                    rPane.getChildren().add(createMessage(e, tmpNode, sq, messageID));
                }
                count = 0;
            }
        }
    }

    private void drag(MouseEvent e, SequenceUML sq) {
        double x = sq.getView().getTranslateX() + e.getX() + sq.getView().getLayoutX();
        sq.getView().setLayoutX(x);
    }
}
