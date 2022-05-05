package com.uml;

import com.uml.classdiagram.ClassDiagram;
import com.uml.classdiagram.UMLClass;
import com.uml.customexception.InvalidOperationLabel;
import com.uml.customexception.OperationNotExists;
import com.uml.sequencediagram.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
            messageID = "create";
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
            sequenceCreateAct = false;
            syncMessageAct = false;
            asyncMessageAct = false;
            returnMessageAct = false;
            destroyMessageAct = false;
            actiovationAct = false;
            tmpNode = null;
            tmpNode2 = null;
            count = 0;
        }
    }

    public void handleKeyEvents(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE && tmpNode.getView() != null) {
            for (Node a : tmpNode.edges) {
                rPane.getChildren().remove(a);
            }

            this.sequenceDiagram.deleteAllLifelineDependencies(tmpNode.lifeline);
            this.sequenceDiagram.deleteLifeline(tmpNode.lifeline);

            rPane.getChildren().remove(tmpNode);
        }
    }

    public SequenceUML addElementLoaded(String name, double height, double x, double y) throws IOException {
        UMLClass cls = this.classDiagram.findClass(name);

        if (cls == null) {
            // Inconsistence
            // TODO barevne odliseni lifeline
            // TODO nastaveni jmena lifeline
        }

        UMLLifeline lifeline = this.sequenceDiagram.createLifeline(cls, height);
        lifeline.setXCoordinate(x);
        lifeline.setYCoordinate(y);
        lifeline.setHeight(height);

        SequenceUML sq;
        if(y == 0.0){
            sq = new SequenceUML(x, DEFAULT_SEQUENCE_HEIGHT, height, rPane, name);
        }else{
            sq = new SequenceUML(x, y, height, rPane, name);
        }

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
        rPane.getChildren().add(sq);
        return sq;
    }

    public void addElement(MouseEvent mouseEvent) throws IOException {
        /* Lifeline */
        if(sequenceAct) {
            SequenceUML sequenceUML = createElement(mouseEvent);
            if (sequenceUML != null){
                rPane.getChildren().add(sequenceUML);
            }
        }
    }

    private SequenceUML createElement(MouseEvent mouseEvent) throws IOException {
        ChoiceDialog dialog = new ChoiceDialog();
        dialog.setTitle("Class Name");
        dialog.setHeaderText("Choice class name from list");
        dialog.setContentText("Class list");
        dialog.getItems().addAll(this.classDiagram.getClassesNames());

        Optional<String> name = dialog.showAndWait();
        String[] nm = {""};

        name.ifPresent(s -> {
            nm[0] = name.get();
        });

        if(nm[0].equals("")){
            return null;
        }

        UMLClass cls = this.classDiagram.findClass(nm[0]);

        UMLLifeline lifeline = this.sequenceDiagram.createLifeline(cls, 0.0);
        lifeline.setXCoordinate(mouseEvent.getX());

        SequenceUML sq = new SequenceUML(mouseEvent.getX(), DEFAULT_SEQUENCE_HEIGHT,0.0, rPane, nm[0]);

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
            if (tmpNode2 == null){
                return;
            }
            Node message = createAndAddMessage(e, tmpNode, tmpNode2, messageID);
            tmpNode = null;
            tmpNode2 = null;
        }
    }

    private SequenceUML createAndAddElement(MouseDragEvent mouseEvent) throws IOException {
        ChoiceDialog dialog = new ChoiceDialog();
        dialog.setTitle("Class Name");
        dialog.setHeaderText("Choice class name from list");
        dialog.setContentText("Class list");
        dialog.getItems().addAll(this.classDiagram.getClassesNames());

        Optional<String> name = dialog.showAndWait();
        String[] nm = {""};

        name.ifPresent(s -> {
            nm[0] = name.get();
        });

        if(nm[0].equals("")){
            return null;
        }

        UMLClass cls = this.classDiagram.findClass(nm[0]);

        UMLLifeline lifeline = this.sequenceDiagram.createLifeline(cls, 0.0);
        lifeline.setXCoordinate(mouseEvent.getX());
        lifeline.setYCoordinate(mouseEvent.getY());

        SequenceUML sq = new SequenceUML(mouseEvent.getX(), mouseEvent.getY(),0.0, rPane, nm[0]);
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
                getWarning("Non exist file");
            }
        });
        sq.getView().setOnContextMenuRequested(e -> changeHeight(e, sq));
        return sq;
    }

    // TODO int number exception
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

        sq.lifeline.setHeight(Integer.parseInt(nm[0])+80);
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

    public void createMessageLoaded(SequenceUML from, SequenceUML to, double yCoordinate, String messageText, String messageID) {
        Pair<String, String[]> operation;
        String[] createArguments;

        if (from == null || to == null) {
            // Inconsistence
            // TODO barevne odliseni message
        } else {
            try {
                switch (messageID) {
                    case "sync":
                        operation = parseOperationLabel(messageText);
                        UMLSynchronousMessage sync = this.sequenceDiagram.createSynchronousMessage(from.lifeline, to.lifeline, operation.getKey(), operation.getValue());
                        sync.setYCoordinate(yCoordinate);
                        break;
                    case "async":
                        operation = parseOperationLabel(messageText);
                        UMLAsynchronousMessage async = this.sequenceDiagram.createAsynchronousMessage(from.lifeline, to.lifeline, operation.getKey(), operation.getValue());
                        async.setYCoordinate(yCoordinate);
                        break;
                    case "syncSelf":
                        operation = parseOperationLabel(messageText);
                        UMLSynchronousSelfMessage syncSelf = this.sequenceDiagram.createSynchronousSelfMessage(from.lifeline, operation.getKey(), operation.getValue());
                        syncSelf.setYCoordinate(yCoordinate);
                        break;
                    case "asyncSelf":
                        operation = parseOperationLabel(messageText);
                        UMLAsynchronousSelfMessage asyncSelf = this.sequenceDiagram.createAsynchronousSelfMessage(from.lifeline, operation.getKey(), operation.getValue());
                        asyncSelf.setYCoordinate(yCoordinate);
                        break;
                    case "returnSelf":
                        operation = parseOperationLabel(messageText);
                        UMLReturnSelfMessage returnSelf = this.sequenceDiagram.createReturnSelfMessage(from.lifeline, operation.getKey(), operation.getValue());
                        returnSelf.setYCoordinate(yCoordinate);
                        break;
                    case "create":
                        createArguments = parseCreateLabel(messageText);
                        UMLCreateMessage createMessage = this.sequenceDiagram.createCreateMessage(from.lifeline, to.lifeline, createArguments);
                        createMessage.setYCoordinate(yCoordinate);
                        break;
                    case "return":
                        UMLReturnMessage returnMessage = this.sequenceDiagram.createReturnMessage(from.lifeline, to.lifeline, messageText);
                        returnMessage.setYCoordinate(yCoordinate);
                        break;
                    default:
                        break;
                }
            } catch (InvalidOperationLabel err) {
                // TODO
                err.printStackTrace();
            } catch (OperationNotExists e) {
                // Inconsistence
                // TODO barevne odliseni message
                e.printStackTrace();
            }
        }

        Message message = new Message(from.getView().getLayoutX(), yCoordinate, to.getView().getLayoutX(), yCoordinate, messageID, messageText);
        message.x1Property().bind(from.getView().layoutXProperty());
        message.x2Property().bind(to.getView().layoutXProperty());

        from.edges.add(message);
        to.edges.add(message);
        rPane.getChildren().add(message);
    }


    private Pair<String, String[]> parseOperationLabel(String operationLabel) throws InvalidOperationLabel {
        if (operationLabel == null) {
            throw new InvalidOperationLabel("Invalid message label.");
        }

        /* Remove all whitespaces and non-visible characters */
        operationLabel = operationLabel.replaceAll("\\s+","");

        int idx = operationLabel.indexOf('(');

        if (idx < 0 || idx >= operationLabel.length()-1) {
            throw new InvalidOperationLabel("Invalid message label.");
        }

        String operationName = operationLabel.substring(0, idx);
        if (operationLabel.charAt(operationLabel.length()-1) != ')') {
            throw new InvalidOperationLabel("Invalid message label.");
        }

        operationLabel = operationLabel.substring(idx+1, operationLabel.length()-1);

        String[] splitedOperationLabel = operationLabel.split(",");

        return new Pair<String, String[]>(operationName, splitedOperationLabel);
    }

    private String[] parseCreateLabel(String createLabel) throws InvalidOperationLabel {
        if (createLabel == null) {
            throw new InvalidOperationLabel("Invalid message label.");
        }

        /* Remove all whitespaces and non-visible characters */
        createLabel = createLabel.replaceAll("\\s+","");

        int idx = createLabel.indexOf('(');

        if (idx < 0 || idx >= createLabel.length()-1) {
            throw new InvalidOperationLabel("Invalid message label.");
        }

        String createName = createLabel.substring(0, idx);
        if (createLabel.charAt(createLabel.length()-1) != ')') {
            throw new InvalidOperationLabel("Invalid message label.");
        }

        if (!createName.equals("<<create>>")) {
            throw new InvalidOperationLabel("Invalid message label.");
        }

        createLabel = createLabel.substring(idx+1, createLabel.length()-1);

        return createLabel.split(",");
    }

    private Node createMessage(MouseEvent e,SequenceUML fromNode, SequenceUML sq, String messageID) {
        Pair<String, String[]> operation;
        String messageText = getMessage(messageID);

        try {
            switch (messageID) {
                case "sync":
                    operation = parseOperationLabel(messageText);
                    UMLSynchronousMessage sync = this.sequenceDiagram.createSynchronousMessage(fromNode.lifeline, sq.lifeline, operation.getKey(), operation.getValue());
                    sync.setYCoordinate(e.getY());
                    break;
                case "async":
                    operation = parseOperationLabel(messageText);
                    UMLAsynchronousMessage async = this.sequenceDiagram.createAsynchronousMessage(fromNode.lifeline, sq.lifeline, operation.getKey(), operation.getValue());
                    async.setYCoordinate(e.getY());
                    break;
                case "syncSelf":
                    operation = parseOperationLabel(messageText);
                    UMLSynchronousSelfMessage syncSelf = this.sequenceDiagram.createSynchronousSelfMessage(fromNode.lifeline, operation.getKey(), operation.getValue());
                    syncSelf.setYCoordinate(e.getY());
                    break;
                case "asyncSelf":
                    operation = parseOperationLabel(messageText);
                    UMLAsynchronousSelfMessage asyncSelf = this.sequenceDiagram.createAsynchronousSelfMessage(fromNode.lifeline, operation.getKey(), operation.getValue());
                    asyncSelf.setYCoordinate(e.getY());
                    break;
                case "returnSelf":
                    operation = parseOperationLabel(messageText);
                    UMLReturnSelfMessage returnSelf = this.sequenceDiagram.createReturnSelfMessage(fromNode.lifeline, operation.getKey(), operation.getValue());
                    returnSelf.setYCoordinate(e.getY());
                    break;
                case "return":
                    UMLReturnMessage returnMessage = this.sequenceDiagram.createReturnMessage(fromNode.lifeline, sq.lifeline, messageText);
                    returnMessage.setYCoordinate(e.getY());
                    break;
                default:
                    break;
            }
        } catch (InvalidOperationLabel err) {
            getWarning("invalid Message text");
            return null;
        } catch (OperationNotExists err) {
            getYesNoWindow();
            /*
             * TODO, jedna se o zaslani zpravy, ktera neexistuje
             *  -> zda skutecne provest -> ANO / NE
             *  -> pokud ano -> nasledujici usek kodu a vytvoreni na frontendu
             *  -> pokud ne  -> nic se na backendu ani frontendu neprovede
             */

            /*
            switch (messageID) {
                case "sync":
                    UMLSynchronousMessage sync = this.sequenceDiagram.createSynchronousMessageNotExists(fromNode.lifeline, sq.lifeline, operation.getKey(), operation.getValue());
                    sync.setYCoordinate(e.getY());
                    break;
                case "async":
                    UMLAsynchronousMessage async = this.sequenceDiagram.createAsynchronousMessageNotExists(fromNode.lifeline, sq.lifeline, operation.getKey(), operation.getValue());
                    async.setYCoordinate(e.getY());
                    break;
                case "syncSelf":
                    UMLSynchronousSelfMessage syncSelf = this.sequenceDiagram.createSynchronousSelfMessageNotExists(fromNode.lifeline, operation.getKey(), operation.getValue());
                    syncSelf.setYCoordinate(e.getY());
                    break;
                case "asyncSelf":
                    UMLAsynchronousSelfMessage asyncSelf = this.sequenceDiagram.createAsynchronousSelfMessageNotExists(fromNode.lifeline, operation.getKey(), operation.getValue());
                    asyncSelf.setYCoordinate(e.getY());
                    break;
                case "returnSelf":
                    UMLReturnSelfMessage returnSelf = this.sequenceDiagram.createReturnSelfMessageNotExists(fromNode.lifeline, operation.getKey(), operation.getValue());
                    returnSelf.setYCoordinate(e.getY());
                    break;
                case "return":
                    UMLReturnMessage returnMessage = this.sequenceDiagram.createReturnMessage(fromNode.lifeline, sq.lifeline, messageText);
                    returnMessage.setYCoordinate(e.getY());
                    break;
                default:
                    break;
            }

            */
        }


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
        }else if(messageID.equals("create")){
            dialog = new TextInputDialog();
            dialog.setTitle(messageID+" Message");
            dialog.setHeaderText("Enter create message <<Create>>(parameters)");
            dialog.setContentText("Message:");
        }else{
            dialog = new TextInputDialog();
            dialog.setTitle(messageID+" Message");
            dialog.setHeaderText("Enter message Operation(parameters)");
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
        Pair<String, String[]> operation;
        String[] createArguments;
        String messageText = getMessage(messageID);

        try {
            createArguments = parseCreateLabel(messageText);
            UMLCreateMessage createMessage = this.sequenceDiagram.createCreateMessage(fromNode.lifeline, sq.lifeline, createArguments);
            createMessage.setYCoordinate(e.getY());
        } catch (InvalidOperationLabel | OperationNotExists err) {
            getWarning("Message have invalid operation label");
            return null;
        }

        Message message = new Message(fromNode.getView().getLayoutX(), e.getY(), sq.getView().getLayoutX(), e.getY(), messageID, messageText);
        message.x1Property().bind(fromNode.getView().layoutXProperty());
        message.x2Property().bind(sq.getView().layoutXProperty());

        rPane.getChildren().add(message);

        fromNode.edges.add(message);
        sq.edges.add(message);
        return message;
    }

    private void getWarning(String warning_message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(warning_message);

        alert.showAndWait();
    }

    private boolean getYesNoWindow() {
        AtomicBoolean status = new AtomicBoolean(false);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Current operation does not exist in the class");
        alert.setContentText("Add operation anyway");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.OK) {
                status.set(true);
            }
        });

        return status.get();
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

    public void createDestroy(SequenceUML sq, double y){
        if (sq == null) {
            // Inconsistence
            // TODO barevne odliseni
        } else {
            Cross cross = new Cross(sq.getView().getLayoutX(), y);
            cross.x1Property().bind(sq.getView().layoutXProperty());
            sq.setY2(y + 40);
            sq.edges.add(cross);
            rPane.getChildren().add(cross);

            this.sequenceDiagram.createDestroy(sq.lifeline, y);
        }
    }

    private Node createDelete(MouseEvent e, SequenceUML sq) {
        Cross cross = new Cross(sq.getView().getLayoutX(), e.getY());
        cross.x1Property().bind(sq.getView().layoutXProperty());
        sq.setY2(e.getY() + 40);
        sq.edges.add(cross);

        this.sequenceDiagram.createDestroy(sq.lifeline, e.getY());

        return cross;
    }

    public void createActivationLoaded(SequenceUML sq, double y1, double y2){
        if (sq == null) {
            // Inconsistence
            // TODO barevne odliseni
        } else {
            ActivationSequenceUML activ = new ActivationSequenceUML(y1, sq.getView().getLayoutX(), y2);
            activ.x1Property().bind(sq.getView().layoutXProperty());

            sq.edges.add(activ);

            activ.getRectangle().setOnMouseClicked(ev -> timeLineClicked(ev, sq));
            activ.getRectangle().setOnDragDetected(ev -> timeLineDragDetected(ev, sq));
            rPane.setOnMouseDragReleased(ev -> {
                try {
                    timeLineDragDone(ev);
                } catch (IOException ex) {
                    getWarning("Non exist file");
                }
            });
            rPane.getChildren().add(activ);

            this.sequenceDiagram.createActivation(sq.lifeline, y1, y2);
        }
    }

    private Node createActivation(MouseEvent e, Double activation, SequenceUML sq){
        ActivationSequenceUML activ = new ActivationSequenceUML(activation, sq.getView().getLayoutX(), e.getY());
        activ.x1Property().bind(sq.getView().layoutXProperty());

        sq.edges.add(activ);

        activ.getRectangle().setOnMouseClicked(ev -> timeLineClicked(ev, sq));
        activ.getRectangle().setOnDragDetected(ev -> timeLineDragDetected(ev, sq));
        rPane.setOnMouseDragReleased(ev -> {
            try {
                timeLineDragDone(ev);
            } catch (IOException ex) {
                getWarning("non exist file");
            }
        });


        this.sequenceDiagram.createActivation(sq.lifeline, activation, e.getY());
        return activ;
    }

    private void drag(MouseEvent e, SequenceUML sq) {
        double x = sq.getView().getTranslateX() + e.getX() + sq.getView().getLayoutX();
        sq.getView().setLayoutX(x);

        sq.lifeline.setXCoordinate(x);
    }
}
