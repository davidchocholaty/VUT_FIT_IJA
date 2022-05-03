package com.uml;

import com.uml.classdiagram.UMLClass;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Optional;

public class SequenceController extends Parent {

    public boolean sequenceAct = false;
    public boolean sequenceCreateAct = false;
    public boolean syncMessageAct = false;
    public ToggleButton sequenceButton;
    public ToggleButton sequenceCreateButton;
    static int count = 0;
    public Pane rPane;
    public static SequenceUML tmpNode;
    public ToggleButton syncMessageButton;
    private String messageID;


    public void sequenceActive(MouseEvent mouseEvent) {
        if (sequenceButton.isSelected()) {
            sequenceAct = true;
            sequenceCreateAct = false;
            syncMessageAct = false;
            count = 0;
        }else if (sequenceCreateButton.isSelected()) {
            sequenceAct = false;
            sequenceCreateAct = true;
            syncMessageAct = false;
            count = 0;
        }else if (syncMessageButton.isSelected()) {
            sequenceAct = false;
            sequenceCreateAct = false;
            syncMessageAct = true;
            messageID = "sync";
            count = 0;
        }else{
            sequenceAct = false;
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
            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("class name");
            dialog.setHeaderText("Enter class name:");
            dialog.setContentText("Class name:");

            Optional<String> name = dialog.showAndWait();

            name.ifPresent(s -> {
                try {
                    rPane.getChildren().add(createElement(mouseEvent, s));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private SequenceUML createElement(MouseEvent mouseEvent, String name) throws IOException {
        SequenceUML sq = new SequenceUML(mouseEvent.getX(), mouseEvent.getY(), name, rPane);

        sq.getView().setOnMouseDragged(e -> drag(e, sq));
        sq.getView().setOnMouseClicked(e -> sequenceClicked(e, sq));
        sq.getClickLine().setOnMouseClicked(e -> timeLineClicked(e, sq));
        return sq;
    }

    private void sequenceClicked(MouseEvent e, SequenceUML sq) {
        tmpNode = sq;
    }

    private Node createAndAddMessage(MouseEvent e,SequenceUML tmpNode, SequenceUML sq, String messageID) {
        Message message = new Message(tmpNode.getView().getLayoutX(), e.getY(), sq.getView().getLayoutX(), e.getY(), "add", messageID);
        message.x1Property().bind(tmpNode.getView().layoutXProperty());
        message.x2Property().bind(sq.getView().layoutXProperty());

        tmpNode.edges.add(message);
        sq.edges.add(message);

        return message;
    }

    private void timeLineClicked(MouseEvent e, SequenceUML sq) {
        if (syncMessageAct) {
            System.out.println("click");
            if (count == 0) {
                tmpNode = sq;
                count++;
            } else if (count == 1) {
                rPane.getChildren().add(createAndAddMessage(e, tmpNode, sq, messageID));
                count = 0;
            }
        }
    }

    private void drag(MouseEvent e, SequenceUML sq) {
        double x = sq.getTranslateX() + e.getX() + sq.getLayoutX();
        sq.setLayoutX(x);
    }
}
