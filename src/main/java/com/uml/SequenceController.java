/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class represents main controller of sequence frontend diagram.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml;

import com.uml.classdiagram.ClassDiagram;
import com.uml.classdiagram.UMLClass;
import com.uml.customexception.InvalidOperationLabel;
import com.uml.customexception.OperationNotExists;
import com.uml.sequencediagram.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import java.io.IOException;
import java.util.Optional;

/**
 * Class represents controller for sequence diagram.
 */
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

    /**
     * Sequence controller initialization.
     */
    public void initialize() {
        group.getToggles().addAll(sequenceButton, sequenceCreateButton, syncMessageButton, asyncMessageButton, returnMessageButton, destroyMessageButton, activationButton);
    }

    /**
     * Sequence controller activation.
     *
     * @param mouseEvent Event of mouse.
     */
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

    /**
     * Method for deleting lifeline.
     *
     * @param keyEvent Key event.
     */
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

    /**
     * Add lifeline when loading diagram from file.
     *
     * @param name Lifeline name.
     * @param height Lifeline height.
     * @param x Lifeline x coordinate.
     * @param y Lifeline y coordinate indent.
     * @return New lifeline.
     * @throws IOException Error occurrence while setting lifeline name.
     */
    public SequenceUML addElementLoaded(String name, String preamble, double height, double x, double y) throws IOException {
        UMLClass cls = this.classDiagram.findClass(name);

        SequenceUML sq;

        if(y == 0.0){
            sq = new SequenceUML(x, DEFAULT_SEQUENCE_HEIGHT, height, rPane, preamble, name);
        }else{
            sq = new SequenceUML(x, y, height, rPane, preamble, name);
        }

        if (cls != null) {
            sq.nameProperty().bind(cls.getClassName());
        }

        if (cls == null){
            sq.getView().getStyleClass().add("CollisionButton");
            sq.dashedLine.getStyleClass().add("Collision");
            cls = new UMLClass(name);
        }

        UMLLifeline lifeline = this.sequenceDiagram.createLifeline(cls, height);
        lifeline.setXCoordinate(x);
        lifeline.setYCoordinate(y);
        lifeline.setHeight(height);

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

    /**
     * Add lifeline element.
     *
     * @param mouseEvent Event of mous.
     * @throws IOException Input handling error.
     */
    public void addElement(MouseEvent mouseEvent) throws IOException {
        /* Lifeline */
        if(sequenceAct) {
            SequenceUML sequenceUML = createElement(mouseEvent);
            if (sequenceUML != null){
                rPane.getChildren().add(sequenceUML);
            }
        }
    }

    /**
     * Creates new lifeline.
     * <p>
     *     The new lifeline is created on frontend scene and in backend too.
     * </p>
     *
     * @param mouseEvent Event of mouse.
     * @return New SequenceUML lifeline.
     * @throws IOException Input handling error.
     */
    private SequenceUML createElement(MouseEvent mouseEvent) throws IOException {
        final String[] clsName = new String[1];
        final String[] nameinst = new String[1];
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Method");
        ButtonType acceptButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(acceptButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField instanceName = new TextField();
        instanceName.setPromptText("instance name");
        ComboBox className = new ComboBox();
        className.getItems().addAll(this.classDiagram.getClassesNames());

        grid.add(new Label("Instance name :"), 0, 0);
        grid.add(instanceName, 0, 1);
        grid.add(new Label("Class name :"), 1, 0);
        grid.add(className, 1, 1);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == acceptButtonType){
                nameinst[0] = instanceName.getText();
                clsName[0] = className.getValue().toString();
            }
            return null;
        });

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();

        if(clsName[0] != null && clsName[0].equals("")){
            return null;
        }

        UMLClass cls = this.classDiagram.findClass(clsName[0]);

        UMLLifeline lifeline = this.sequenceDiagram.createLifeline(cls, 0.0);
        lifeline.setXCoordinate(mouseEvent.getX());
        lifeline.setPreamble(nameinst[0]);

        SequenceUML sq = new SequenceUML(mouseEvent.getX(), DEFAULT_SEQUENCE_HEIGHT,0.0, rPane, nameinst[0], clsName[0]);

        sq.nameProperty().bind(cls.getClassName());

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

    /**
     * Done timeline drag.
     *
     * @param e Mouse drag event.
     * @throws IOException Input output error.
     */
    private void timeLineDragDone(MouseDragEvent e) throws IOException {
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

    /**
     * Create lifeline used in an UML create message.
     * <p>
     *     The special difference from a classic lifeline is that this lifeline has set y coordinate indent.
     * </p>
     *
     * @param mouseEvent Event of mouse.
     * @return New SequenceUML lifeline.
     * @throws IOException Input handling error.
     */
    private SequenceUML createAndAddElement(MouseDragEvent mouseEvent) throws IOException {
        final String[] clsName = new String[1];
        final String[] nameinst = new String[1];
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Method");
        ButtonType acceptButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(acceptButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField instanceName = new TextField();
        instanceName.setPromptText("instance name");
        ComboBox className = new ComboBox();
        className.getItems().addAll(this.classDiagram.getClassesNames());

        grid.add(new Label("Instance name :"), 0, 0);
        grid.add(instanceName, 0, 1);
        grid.add(new Label("Class name :"), 1, 0);
        grid.add(className, 1, 1);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == acceptButtonType){
                nameinst[0] = instanceName.getText();
                clsName[0] = className.getValue().toString();
            }
            return null;
        });

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();

        if(clsName[0] != null && clsName[0].equals("")){
            return null;
        }

        UMLClass cls = this.classDiagram.findClass(clsName[0]);

        UMLLifeline lifeline = this.sequenceDiagram.createLifeline(cls, 0.0);
        lifeline.setXCoordinate(mouseEvent.getX());
        lifeline.setYCoordinate(mouseEvent.getY());
        lifeline.setPreamble(nameinst[0]);

        SequenceUML sq = new SequenceUML(mouseEvent.getX(), mouseEvent.getY(),0.0, rPane, nameinst[0], clsName[0]);
        sq.nameProperty().bind(cls.getClassName());
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

    /**
     * Changes height of lifeline on frontend scene.
     * <p>
     *     For change is created a window for the user into which it can be set new height value.
     * </p>
     *
     * @param e Event.
     * @param sq Frontend lifeline.
     */
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
        boolean isNumber;
        try {
            Double.parseDouble(nm[0]);
            isNumber = true;
        } catch(NumberFormatException ex){
            isNumber =  false;
        }
        if (isNumber) {
            sq.setY2(Integer.parseInt(nm[0]) + 80);
            sq.lifeline.setHeight(Integer.parseInt(nm[0]) + 80);
        }
    }

    /**
     * Method for detecting of timeline drag.
     *
     * @param e Mouse event.
     * @param sq Frontend lifeline.
     */
    private void timeLineDragDetected(MouseEvent e, SequenceUML sq){
        if (sequenceCreateAct){
            sq.getClickLine().startFullDrag();
            tmpNode = sq;
        }
    }

    /**
     * Method for set frontend lifeline clicked.
     *
     * @param e Event of mouse.
     * @param sq Clicked frontend lifeline.
     */
    private void sequenceClicked(MouseEvent e, SequenceUML sq) {
        tmpNode = sq;
    }

    /**
     * Creates a new message when loading sequence diagram from a file.
     * <p>
     *     This method creates all message types. Possible types of message are: synchronous, asynchronous,
     *     synchronous self, asynchronous self, return self, create and return.
     * </p>
     *
     * @param from Lifeline where the message starts.
     * @param to Lifeline where the message ends.
     * @param yCoordinate Y coordinate lifeline indent.
     * @param messageText Message label.
     * @param messageID Type of message.
     */
    public void createMessageLoaded(SequenceUML from, SequenceUML to, double yCoordinate, String messageText, String messageID) {
        Pair<String, String[]> operation;
        String[] createArguments;

        Message message = new Message(from.getView().getLayoutX(), yCoordinate, to.getView().getLayoutX(), yCoordinate, messageID, messageText);
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
                    UMLReturnSelfMessage returnSelf = this.sequenceDiagram.createReturnSelfMessage(from.lifeline, messageText);
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
            getWarning("invalid Message text");
            return;
        } catch (OperationNotExists e) {
            for(Node node : message.getChildrenUnmodifiable()){
                node.getStyleClass().add("Collision");
            }

            switch (messageID) {
                case "sync":
                    try {
                        operation = parseOperationLabel(messageText);
                        UMLSynchronousMessage sync = this.sequenceDiagram.createSynchronousMessageNotExists(from.lifeline, to.lifeline, operation.getKey(), operation.getValue());
                        sync.setYCoordinate(yCoordinate);
                    } catch (InvalidOperationLabel ex) {
                        ex.printStackTrace();
                    }

                    break;
                case "async":
                    try {
                        operation = parseOperationLabel(messageText);
                        UMLAsynchronousMessage async = this.sequenceDiagram.createAsynchronousMessageNotExists(from.lifeline, to.lifeline, operation.getKey(), operation.getValue());
                        async.setYCoordinate(yCoordinate);
                    } catch (InvalidOperationLabel ex) {
                        ex.printStackTrace();
                    }

                    break;
                case "syncSelf":
                    try {
                        operation = parseOperationLabel(messageText);
                        UMLSynchronousSelfMessage syncSelf = this.sequenceDiagram.createSynchronousSelfMessageNotExists(from.lifeline, operation.getKey(), operation.getValue());
                        syncSelf.setYCoordinate(yCoordinate);
                    } catch (InvalidOperationLabel ex) {
                        ex.printStackTrace();
                    }

                    break;
                case "asyncSelf":
                    try {
                        operation = parseOperationLabel(messageText);
                        UMLAsynchronousSelfMessage asyncSelf = this.sequenceDiagram.createAsynchronousSelfMessageNotExists(from.lifeline, operation.getKey(), operation.getValue());
                        asyncSelf.setYCoordinate(yCoordinate);
                    } catch (InvalidOperationLabel ex) {
                        ex.printStackTrace();
                    }

                    break;
                default:
                    break;
            }
        }
        message.x1Property().bind(from.getView().layoutXProperty());
        message.x2Property().bind(to.getView().layoutXProperty());

        from.edges.add(message);
        to.edges.add(message);
        rPane.getChildren().add(message);
    }


    /**
     * Method creates an operation label from string constant.
     * <p>
     *     Method split string constant into operation name and operation arguments for further work.
     * </p>
     *
     * @param operationLabel Label string constant.
     * @return Pair with operation name and operation arguments.
     * @throws InvalidOperationLabel Invalid syntax of operation label.
     */
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

    /**
     * Method creates a create label arguments from string constant.
     *
     * @param createLabel Label string constant.
     * @return Create label arguments.
     * @throws InvalidOperationLabel Invalid syntax of create label arguments.
     */
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

    /**
     * Creates new message.
     * <p>
     *     The new message is created on frontend scene (see getNode() method) and in backend too.
     * </p>
     *
     * @param e Event of mouse.
     * @param fromNode Lifeline where message starts.
     * @param sq Lifeline where message ends.
     * @param messageID Type of message.
     * @return New message.
     */
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
                    UMLReturnSelfMessage returnSelf = this.sequenceDiagram.createReturnSelfMessage(fromNode.lifeline, messageText);
                    returnSelf.setYCoordinate(e.getY());
                    break;
                case "return":
                    UMLReturnMessage returnMessage = this.sequenceDiagram.createReturnMessage(fromNode.lifeline, sq.lifeline, messageText);
                    returnMessage.setYCoordinate(e.getY());
                    break;
                default:
                    break;
            }
            return getNode(e, fromNode, sq, messageID, messageText);
        } catch (InvalidOperationLabel err) {
            getWarning("invalid Message text");
            return null;
        } catch (OperationNotExists err) {
            if (getYesNoWindow()) {
                switch (messageID) {
                    case "sync":
                        try {
                            operation = parseOperationLabel(messageText);
                        } catch (InvalidOperationLabel ex) {
                            throw new RuntimeException(ex);
                        }
                        UMLSynchronousMessage sync = this.sequenceDiagram.createSynchronousMessageNotExists(fromNode.lifeline, sq.lifeline, operation.getKey(), operation.getValue());
                        sync.setYCoordinate(e.getY());
                        break;
                    case "async":
                        try {
                            operation = parseOperationLabel(messageText);
                        } catch (InvalidOperationLabel ex) {
                            throw new RuntimeException(ex);
                        }
                        UMLAsynchronousMessage async = this.sequenceDiagram.createAsynchronousMessageNotExists(fromNode.lifeline, sq.lifeline, operation.getKey(), operation.getValue());
                        async.setYCoordinate(e.getY());
                        break;
                    case "syncSelf":
                        try {
                            operation = parseOperationLabel(messageText);
                        } catch (InvalidOperationLabel ex) {
                            throw new RuntimeException(ex);
                        }
                        UMLSynchronousSelfMessage syncSelf = this.sequenceDiagram.createSynchronousSelfMessageNotExists(fromNode.lifeline, operation.getKey(), operation.getValue());
                        syncSelf.setYCoordinate(e.getY());
                        break;
                    case "asyncSelf":
                        try {
                            operation = parseOperationLabel(messageText);
                        } catch (InvalidOperationLabel ex) {
                            throw new RuntimeException(ex);
                        }
                        UMLAsynchronousSelfMessage asyncSelf = this.sequenceDiagram.createAsynchronousSelfMessageNotExists(fromNode.lifeline, operation.getKey(), operation.getValue());
                        asyncSelf.setYCoordinate(e.getY());
                        break;
                    default:
                        break;
                }
                return getNode(e, fromNode, sq, messageID, messageText);
            }
        }
        return null;
    }

    /**
     * Creates new message on frontend scene.
     *
     * @param e Event of mouse.
     * @param fromNode Lifeline where message starts.
     * @param sq Lifeline where message starts.
     * @param messageID Lifeline where message ends.
     * @param messageText Message text.
     * @return New frontend message.
     */
    private Node getNode(MouseEvent e, SequenceUML fromNode, SequenceUML sq, String messageID, String messageText) {
        Message message = new Message(fromNode.getView().getLayoutX(), e.getY(), sq.getView().getLayoutX(), e.getY(), messageID, messageText);

        message.x1Property().bind(fromNode.getView().layoutXProperty());
        message.x2Property().bind(sq.getView().layoutXProperty());

        fromNode.edges.add(message);
        sq.edges.add(message);
        return message;
    }

    /**
     * Returns obtained message label.
     *
     * @param messageID Message type.
     * @return Obtained message label.
     */
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

    /**
     * Creates new UML create message.
     * <p>
     *     New create message is created on frontend scene and in backend too.
     * </p>
     *
     * @param e Event of mouse drag.
     * @param fromNode Lifeline where message starts.
     * @param sq New created lifeline (where message ends).
     * @param messageID Type of message.
     * @return New create message.
     */
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

    /**
     * Show warning window.
     *
     * @param warning_message Warning text message.
     */
    private void getWarning(String warning_message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(warning_message);

        alert.showAndWait();
    }

    /**
     * Show yes or now window.
     * <p>
     *     This window is used if user wants to create operation that is correct by syntax,
     *     but does not exist in class from which instance (lifeline) the message starts.
     * </p>
     *
     * @return The option which is chosen by the user. (YES -> true, NO -> false).
     */
    private boolean getYesNoWindow() {
        Boolean status = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Current operation does not exist in the class");
        alert.setContentText("Add operation anyway");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(noButton) == okButton){
            status = true;
        }
        return status;
    }

    /**
     * Choose clicked timeline.
     *
     * @param e Mouse event.
     * @param sq Clicked lifeline.
     */
    private void timeLineClicked(MouseEvent e, SequenceUML sq) {
        if (syncMessageAct || asyncMessageAct || returnMessageAct) {
            if (count == 0) {
                tmpNode = sq;
                count++;
            } else if (count == 1) {
                if (tmpNode == (Node)sq){
                    if(syncMessageAct){
                        Node message = createMessage(e, tmpNode, sq, "syncSelf");
                        if (message != null){
                            rPane.getChildren().add(message);
                        }
                    }else if(asyncMessageAct){
                        Node message = createMessage(e, tmpNode, sq, "asyncSelf");
                        if (message != null){
                            rPane.getChildren().add(message);
                        }
                    }else if(returnMessageAct){
                        Node message = createMessage(e, tmpNode, sq, "returnSelf");
                        if (message != null){
                            rPane.getChildren().add(message);
                        }
                    }
                }else {
                    Node message = createMessage(e, tmpNode, sq, messageID);
                    if (message != null){
                        rPane.getChildren().add(message);
                    }
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


    /**
     * Creates destroy symbol.
     * <p>
     *     The destroy symbol is created on frontend scene and in backend too.
     *
     * </p>
     * @param sq Lifeline on which will be destroy symbol created.
     * @param y Y coordinate of destroy on the lifeline.
     */
    public void createDestroy(SequenceUML sq, double y){
        Cross cross = new Cross(sq.getView().getLayoutX(), y);
        cross.x1Property().bind(sq.getView().layoutXProperty());
        sq.setY2(y + 40);
        sq.edges.add(cross);
        rPane.getChildren().add(cross);

        this.sequenceDiagram.createDestroy(sq.lifeline, y);
    }

    /**
     * Creates destroy symbol.
     * <p>
     *     The destroy symbol is created on frontend scene and in backend too.
     *
     * </p>
     * @param e Event of mouse.
     * @param sq Lifeline on which will be destroy symbol created.
     */
    private Node createDelete(MouseEvent e, SequenceUML sq) {
        Cross cross = new Cross(sq.getView().getLayoutX(), e.getY());
        cross.x1Property().bind(sq.getView().layoutXProperty());
        sq.setY2(e.getY() + 40);
        sq.edges.add(cross);

        this.sequenceDiagram.createDestroy(sq.lifeline, e.getY());

        return cross;
    }

    /**
     * Creates an activation while loading sequence diagram from file.
     *
     * @param sq Lifeline on which will be activation created.
     * @param y1 First y coordinate where lifeline starts on the lifeline.
     * @param y2 Second y coordinate where lifeline ends on the lifeline.
     */
    public void createActivationLoaded(SequenceUML sq, double y1, double y2){
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

    /**
     * Creates the new activation.
     *
     * @param e Event of mouse.
     * @param activation Activation coordinate.
     * @param sq Lifeline on which will be activation created.
     * @return New activation.
     */
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

    /**
     * Drag with a lifeline on frontend scene.
     *
     * @param e Event of mouse.
     * @param sq Dragged lifeline.
     */
    private void drag(MouseEvent e, SequenceUML sq) {
        double x = sq.getView().getTranslateX() + e.getX() + sq.getView().getLayoutX();
        sq.getView().setLayoutX(x);

        sq.lifeline.setXCoordinate(x);
    }
}
