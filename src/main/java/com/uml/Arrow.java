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

    public Arrow(double x1, double y1, double x2, double y2, String arrowID, ClassUML from, ClassUML to){
        this.x1.set(x1);
        this.x2.set(x2);
        this.y1.set(y1);
        this.y2.set(y2);

        relA.getStyleClass().add("relLabel");
        relB.getStyleClass().add("relLabel");

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add relationship");
        ButtonType loginButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField relA = new TextField();
        relA.setPromptText("Relationship A");
        TextField relB = new TextField();
        relB.setPromptText("Relationship B");

        grid.add(new Label("Relationship A:"), 0, 0);
        grid.add(relA, 1, 0);
        grid.add(new Label("Relationship B:"), 2, 0);
        grid.add(relB, 3, 0);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                this.relA.setText(relA.getText());
                this.relB.setText(relB.getText());
                return new Pair<>(relA.getText(), relB.getText());
            }
            return null;
        });

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();

        UMLClass classFrom = MainController.diagram.findClass(from.getView().getId());
        UMLClass classTo = MainController.diagram.findClass(to.getView().getId());

        switch (arrowID){
            case "arrow":
                getChildren().addAll(mainLine, headAS, this.relA, this.relB);
                UMLAssociation association = MainController.diagram.createAssociationRelationship(classFrom, classTo);
                setMultiplicityTypes(association, relA.getText(), relB.getText());
                break;

            case "realization":
                /* TODO Adam - nebudou multiplicity, zmena na plnou caru */
                getChildren().addAll(dashedLine, headR, this.relA, this.relB);
                UMLInheritance inheritance = MainController.diagram.createInheritanceRelationship(classFrom, classTo);
                break;

            case "aggregation":
                getChildren().addAll(mainLine, headAG1, headAG2, this.relA, this.relB);
                UMLAggregation aggregation = MainController.diagram.createAggregationRelationship(classFrom, classTo);
                setMultiplicityTypes(aggregation, relA.getText(), relB.getText());
                break;

            case "composition":
                getChildren().addAll(mainLine, headAS, headC3, headC1, headC2, this.relA, this.relB);
                UMLComposition composition = MainController.diagram.createCompositionRelationship(classFrom, classTo);
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

        relA.setTranslateX(x1);
        relA.setTranslateY(y1);


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

        relB.setTranslateX(x1);
        relB.setTranslateY(y1);
    }

    private double[] scaleRel(double x2, double y2, double x1, double y1) {
        double theta = Math.atan2(y2-y1, x2-x1);
        return new double[]{
                x1 + Math.cos(theta) * REL_SCALER,
                y1 + Math.sin(theta) * REL_SCALER
        };
    }

    private double[] scale(double x1, double y1, double x2, double y2){
        double theta = Math.atan2(y2-y1, x2-x1);
        return new double[]{
              x1 + Math.cos(theta) * ARROW_SCALER,
              y1 + Math.sin(theta) * ARROW_SCALER
        };
    }

    public double getX1() {
        return x1.get();
    }

    public SimpleDoubleProperty x1Property() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1.set(x1);
    }

    public double getY1() {
        return y1.get();
    }

    public SimpleDoubleProperty y1Property() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1.set(y1);
    }

    public double getX2() {
        return x2.get();
    }

    public SimpleDoubleProperty x2Property() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2.set(x2);
    }

    public double getY2() {
        return y2.get();
    }

    public SimpleDoubleProperty y2Property() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2.set(y2);
    }
}
