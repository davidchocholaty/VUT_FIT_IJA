/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing relationship on frontend.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml;

import com.uml.classdiagram.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.util.Pair;

/**
 * Class represents arrow representing relationship in UML class diagram.
 */
public class Arrow extends Group {
    @FXML
    private final Polyline mainLine = new Polyline();
    private final Polyline dashedLine = new Polyline();
    private final Polyline headAS = new Polyline();
    private final Polygon headR = new Polygon();
    private final Polygon headAG1 = new Polygon();
    private final Polygon headAG2 = new Polygon();
    private final Polygon headC1 = new Polygon();
    private final Polygon headC2 = new Polygon();
    private final Polyline headC3 = new Polyline();
    private final Label relA = new Label();
    private final Label relB = new Label();
    private final SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    private final double ARROW_SCALER = 60;
    private final double REL_SCALER = 27;
    private final double ARROWHEAD_ANGLE = Math.toRadians(30);
    private final double ARROWHEAD_LENGTH = 15;

    /**
     * Creates new Arrow instance.
     *
     * @param x1 From x coordinate.
     * @param y1 From y coordinate.
     * @param x2 To x coordinate.
     * @param y2 To y coordinate.
     * @param arrowID Identifier of arrow.
     * @param from From class.
     * @param to To class.
     * @param controller Current main controller.
     */
    public Arrow(double x1, double y1, double x2, double y2, String arrowID, ClassUML from, ClassUML to, MainController controller){
        this.x1.set(x1);
        this.x2.set(x2);
        this.y1.set(y1);
        this.y2.set(y2);

        relA.getStyleClass().add("relLabel");
        relB.getStyleClass().add("relLabel");
        relA.translateXProperty().bind(relA.heightProperty().divide(-2));
        relA.translateYProperty().bind(relA.heightProperty().divide(-1));
        relB.translateXProperty().bind(relB.heightProperty().divide(-2));
        relB.translateYProperty().bind(relB.heightProperty().divide(-1));

        UMLClass classFrom = controller.diagram.findClass(from.getView().getId());
        UMLClass classTo = controller.diagram.findClass(to.getView().getId());

        if(arrowID.equals("realization")){
            getChildren().addAll(mainLine, headR);
            UMLInheritance inheritance = controller.diagram.createInheritanceRelationship(classFrom, classTo);
        }else {

            Dialog dialog = new Dialog();
            dialog.setTitle("Add relationship");
            ButtonType loginButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            ComboBox relA = new ComboBox();
            relA.getItems().addAll("", "0", "0..1", "1", "0..*", "1..*");
            relA.getSelectionModel().selectFirst();
            ComboBox relB = new ComboBox();
            relB.getItems().addAll("", "0", "0..1", "1", "0..*", "1..*");
            relB.getSelectionModel().selectFirst();

            grid.add(new Label("Relationship A:"), 0, 0);
            grid.add(relA, 1, 0);
            grid.add(new Label("Relationship B:"), 2, 0);
            grid.add(relB, 3, 0);

            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    this.relA.setText(relA.getValue().toString());
                    this.relB.setText(relB.getValue().toString());
                    return new Pair<>(relA.getValue().toString(), relB.getValue().toString());
                }
                return null;
            });

            dialog.getDialogPane().setContent(grid);

            dialog.showAndWait();

            switch (arrowID) {
                case "arrow":
                    getChildren().addAll(mainLine, headAS, this.relA, this.relB);
                    UMLAssociation association = controller.diagram.createAssociationRelationship(classFrom, classTo);
                    setMultiplicityTypes(association, this.relA.getText(), this.relB.getText());
                    break;

                case "aggregation":
                    getChildren().addAll(mainLine, headAG1, headAG2, this.relA, this.relB);
                    UMLAggregation aggregation = controller.diagram.createAggregationRelationship(classFrom, classTo);
                    setMultiplicityTypes(aggregation, this.relA.getText(), this.relB.getText());
                    break;

                case "composition":
                    getChildren().addAll(mainLine, headAS, headC3, headC1, headC2, this.relA, this.relB);
                    UMLComposition composition = controller.diagram.createCompositionRelationship(classFrom, classTo);
                    setMultiplicityTypes(composition, this.relA.getText(), this.relB.getText());
                    break;

                default:
                    break;
            }
        }

        for(SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1, this.x2, this.y1, this.y2}){
            s.addListener((l,o,n) -> update() );
        }

        setUpClassStyle();

        update();
    }

    /**
     * Creates new Arrow instance.
     * <p>
     *     This constructor is used for creating instance level relationships while loading saved class diagram.
     * </p>
     *
     * @param from From class.
     * @param to To class.
     * @param controller Current main controler.
     * @param fromText From multiplicity.
     * @param toText To multiplicity.
     * @param relType Relationship type (association, aggregation, composition).
     */
    public Arrow(ClassUML from, ClassUML to, MainController controller, String fromText, String toText, String relType){
        this.x1.set(from.getView().getLayoutX());
        this.x2.set(to.getView().getLayoutX());
        this.y1.set(from.getView().getLayoutY());
        this.y2.set(to.getView().getLayoutY());

        relA.getStyleClass().add("relLabel");
        relB.getStyleClass().add("relLabel");

        UMLClass classFrom = controller.diagram.findClass(from.getView().getId());
        UMLClass classTo = controller.diagram.findClass(to.getView().getId());

        relA.setText(fromText);
        relB.setText(toText);
        relA.translateXProperty().bind(relA.heightProperty().divide(-2));
        relA.translateYProperty().bind(relA.heightProperty().divide(-1));
        relB.translateXProperty().bind(relB.heightProperty().divide(-2));
        relB.translateYProperty().bind(relB.heightProperty().divide(-1));

        switch (relType){
            case "association":
                getChildren().addAll(mainLine, headAS, this.relA, this.relB);
                UMLAssociation association = controller.diagram.createAssociationRelationship(classFrom, classTo);
                setMultiplicityTypes(association, relA.getText(), relB.getText());
                break;
            case "aggregation":
                getChildren().addAll(mainLine, headAG1, headAG2, this.relA, this.relB);
                UMLAggregation aggregation = controller.diagram.createAggregationRelationship(classFrom, classTo);
                setMultiplicityTypes(aggregation, relA.getText(), relB.getText());
                break;
            case "composition":
                getChildren().addAll(mainLine, headAS, headC3, headC1, headC2, this.relA, this.relB);
                UMLComposition composition = controller.diagram.createCompositionRelationship(classFrom, classTo);
                setMultiplicityTypes(composition, relA.getText(), relB.getText());
                break;
            default:
                break;
        }

        for(SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1, this.x2, this.y1, this.y2}){
            s.addListener((l,o,n) -> update() );
        }

        setUpClassStyle();

        update();
    }

    /**
     * Creates new Arrow instance.
     * <p>
     *     This constructor is used for creating class level relationships (inheritance)
     *     while loading saved class diagram.
     * </p>
     * @param from From class.
     * @param to To class.
     * @param controller Current main controller.
     * @param relType Relationship type (inheritance).
     */
    public Arrow(ClassUML from, ClassUML to, MainController controller, String relType){
        this.x1.set(from.getView().getLayoutX());
        this.x2.set(to.getView().getLayoutX());
        this.y1.set(from.getView().getLayoutY());
        this.y2.set(to.getView().getLayoutY());

        UMLClass classFrom = controller.diagram.findClass(from.getView().getId());
        UMLClass classTo = controller.diagram.findClass(to.getView().getId());

        relA.getStyleClass().add("relLabel");
        relB.getStyleClass().add("relLabel");

        relA.translateXProperty().bind(relA.heightProperty().divide(-2));
        relA.translateYProperty().bind(relA.heightProperty().divide(-1));
        relB.translateXProperty().bind(relB.heightProperty().divide(-2));
        relB.translateYProperty().bind(relB.heightProperty().divide(-1));

        if(relType.equals("inheritance")){
            getChildren().addAll(mainLine, headR);
            UMLInheritance inheritance = controller.diagram.createInheritanceRelationship(classFrom, classTo);
        }

        for(SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1, this.x2, this.y1, this.y2}){
            s.addListener((l,o,n) -> update() );
        }

        setUpClassStyle();

        update();
    }

    /**
     * Set multiplicity type from string.
     * <p>
     *     Possible conversions are: null -> UMLMultiplicityType.UNSPECIFIED, "0" -> UMLMultiplicityType.ZERO,
     *     "0..1" -> UMLMultiplicityType.ZERO_OR_ONE, "1" -> UMLMultiplicityType.ONE,
     *     "0..*" -> UMLMultiplicityType.ZERO_OR_MANY, "1..*" -> UMLMultiplicityType.ONE_OR_MANY.
     * </p>
     *
     * @param relationship UML instance level relationship.
     * @param fromMultiplicity From multiplicity String value to convert.
     * @param toMultiplicity To multiplicity String value to convert.
     */
    private void setMultiplicityTypes(UMLInstanceLevel relationship, String fromMultiplicity, String toMultiplicity) {
        /* From multiplicity */
        if (fromMultiplicity == null) {
            relationship.setFromMultiplicity(UMLMultiplicityType.UNSPECIFIED);
        } else {
            switch (fromMultiplicity) {
                case "0":
                    relationship.setFromMultiplicity(UMLMultiplicityType.ZERO);
                    break;
                case "0..1":
                    relationship.setFromMultiplicity(UMLMultiplicityType.ZERO_OR_ONE);
                    break;
                case "1":
                    relationship.setFromMultiplicity(UMLMultiplicityType.ONE);
                    break;
                case "0..*":
                    relationship.setFromMultiplicity(UMLMultiplicityType.ZERO_OR_MANY);
                    break;
                case "1..*":
                    relationship.setFromMultiplicity(UMLMultiplicityType.ONE_OR_MANY);
                    break;
            }
        }

        /* To multiplicity */
        if (toMultiplicity == null) {
            relationship.setToMultiplicity(UMLMultiplicityType.UNSPECIFIED);
        } else {
            switch (toMultiplicity) {
                case "0":
                    relationship.setToMultiplicity(UMLMultiplicityType.ZERO);
                    break;
                case "0..1":
                    relationship.setToMultiplicity(UMLMultiplicityType.ZERO_OR_ONE);
                    break;
                case "1":
                    relationship.setToMultiplicity(UMLMultiplicityType.ONE);
                    break;
                case "0..*":
                    relationship.setToMultiplicity(UMLMultiplicityType.ZERO_OR_MANY);
                    break;
                case "1..*":
                    relationship.setToMultiplicity(UMLMultiplicityType.ONE_OR_MANY);
                    break;
            }
        }
    }

    /**
     * Set arrow style for line and for end.
     */
    private void setUpClassStyle() {
        mainLine.getStyleClass().setAll("arrow");
        dashedLine.getStyleClass().setAll("arrow");
        headAS.getStyleClass().setAll("arrow");
        headR.getStyleClass().setAll("arrowR");
        headAG1.getStyleClass().setAll("arrowAG");
        headAG2.getStyleClass().setAll("arrowAG");
        headC1.getStyleClass().setAll("arrowC");
        headC2.getStyleClass().setAll("arrowC");
        headC3.getStyleClass().setAll("arrow");
    }

    /**
     * Update arrow position.
     */
    private void update(){
        double[] start = scale(x1.get(), y1.get(), x2.get(), y2.get());
        double[] end = scale(x2.get(), y2.get(), x1.get(), y1.get());

        double x1 = start[0];
        double y1 = start[1];
        double x2 = end[0];
        double y2 = end[1];

        mainLine.getPoints().setAll(x1, y1, x2, y2);
        dashedLine.getPoints().setAll(x1, y1, x2, y2);
        dashedLine.getStrokeDashArray().addAll(25d, 10d);

        double theta = Math.atan2(y2-y1, x2-x1);

        double x_1 = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
        double y_1 = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
        headAS.getPoints().setAll(x_1,y_1,x2,y2);
        double x_2 = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
        double y_2= y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
        headAS.getPoints().addAll(x_2,y_2);

        headR.getPoints().setAll(x2, y2, x_1, y_1,  x_2, y_2);
        headAG1.getPoints().setAll(x2, y2, x_1, y_1,  x_2, y_2);
        headC1.getPoints().setAll(x2, y2, x_1, y_1,  x_2, y_2);

        relA.setLayoutX(x1);
        relA.setLayoutY(y1);


        start = scaleRel(x1,y1,x2,y2);
        end = scaleRel(x2,y2,x1,y1);

        x1 = start[0];
        y1 = start[1];
        x2 = end[0];
        y2 = end[1];

        headAG2.getPoints().setAll( x_1, y_1,  x_2, y_2, x1, y1);
        headC2.getPoints().setAll(x_1, y_1,  x_2, y_2, x1, y1);
        headC3.getPoints().setAll(x_1,y_1,x1,y1);
        headC3.getPoints().addAll(x_2, y_2);

        relB.setLayoutX(x1);
        relB.setLayoutY(y1);
    }

    /**
     * Calculate scale for relationship for visible indent.
     *
     * @param x2 x2 coordinate.
     * @param y2 y2 coordinate.
     * @param x1 x1 coordinate.
     * @param y1 y1 coordinate.
     * @return Returns an array with x and y scale.
     */
    private double[] scaleRel(double x2, double y2, double x1, double y1) {
        double theta = Math.atan2(y2-y1, x2-x1);
        return new double[]{
                x1 + Math.cos(theta) * REL_SCALER,
                y1 + Math.sin(theta) * REL_SCALER
        };
    }

    /**
     * Calculate scale for arrow to show indent of class block.
     *
     * @param x1 x1 coordinate.
     * @param y1 y1 coordinate.
     * @param x2 x2 coordinate.
     * @param y2 y2 coordinate.
     * @return Returns an array with x and y scale.
     */
    private double[] scale(double x1, double y1, double x2, double y2){
        double theta = Math.atan2(y2-y1, x2-x1);
        return new double[]{
              x1 + Math.cos(theta) * ARROW_SCALER,
              y1 + Math.sin(theta) * ARROW_SCALER
        };
    }

    /**
     * Get x1 coordinate.
     *
     * @return x1 coordinate.
     */
    public double getX1() {
        return x1.get();
    }

    /**
     * Get x1 coordinate property.
     *
     * @return x1 property.
     */
    public SimpleDoubleProperty x1Property() {
        return x1;
    }

    /**
     * Set x1 coordinate.
     *
     * @param x1 New value to x1 coordinate.
     */
    public void setX1(double x1) {
        this.x1.set(x1);
    }

    /**
     * Get y1 coordinate.
     *
     * @return y1 coordinate.
     */
    public double getY1() {
        return y1.get();
    }

    /**
     * Get y1 coordinate property.
     *
     * @return y1 property.
     */
    public SimpleDoubleProperty y1Property() {
        return y1;
    }

    /**
     * Set y1 coordinate.
     *
     * @param y1 New value to y1 coordinate.
     */
    public void setY1(double y1) {
        this.y1.set(y1);
    }

    /**
     * Get x2 coordinate.
     *
     * @return x2 coordinate.
     */
    public double getX2() {
        return x2.get();
    }

    /**
     * Get x2 coordinate property.
     *
     * @return x2 property.
     */
    public SimpleDoubleProperty x2Property() {
        return x2;
    }

    /**
     * Set x2 coordinate.
     *
     * @param x2 New value to x2 coordinate.
     */
    public void setX2(double x2) {
        this.x2.set(x2);
    }

    /**
     * Get y2 coordinate.
     *
     * @return y2 coordinate.
     */
    public double getY2() {
        return y2.get();
    }

    /**
     * Get y2 coordinate property.
     *
     * @return y2 property.
     */
    public SimpleDoubleProperty y2Property() {
        return y2;
    }

    /**
     * Set y2 coordinate.
     *
     * @param y2 New value to y2 coordinate.
     */
    public void setY2(double y2) {
        this.y2.set(y2);
    }
}
