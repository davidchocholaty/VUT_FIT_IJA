package com.uml;
import com.uml.classdiagram.UMLClass;
import com.uml.classdiagram.UMLInheritance;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

public class Message extends Group {

    private final SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    private final Polyline mainLine = new Polyline();
    private final Polyline dashedLine = new Polyline();
    private final Polyline headAS = new Polyline();
    private final Polygon headR = new Polygon();
    private final Label messageText = new Label();
    private final double MESSAGEHEAD_ANGLE = Math.toRadians(30);
    private final double MESSAGEHEAD_LENGTH = 15;

    public Message(double X1, double X2, double Y1, double Y2, String message, String messageID){
        this.x1.set(X1);
        this.x2.set(Y1);
        this.y1.set(X2);
        this.y2.set(Y2);

        getChildren().addAll(mainLine, headR);

        for(SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1, this.x2, this.y1, this.y2}){
            s.addListener((l,o,n) -> update() );
        }

        update();
    }

    private void update(){
        double x1 = this.x1.get();
        double y1 = this.y1.get();
        double x2 = this.x2.get();
        double y2 = this.y2.get();

        mainLine.getPoints().setAll(x1, y1, x2, y2);
        dashedLine.getPoints().setAll(x1, y1, x2, y2);
        dashedLine.getStrokeDashArray().addAll(25d, 10d);

        double theta = Math.atan2(y2-y1, x2-x1);

        double x_1 = x2 - Math.cos(theta + MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        double y_1 = y2 - Math.sin(theta + MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        headAS.getPoints().setAll(x_1,y_1,x2,y2);
        double x_2 = x2 - Math.cos(theta - MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        double y_2= y2 - Math.sin(theta - MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        headAS.getPoints().addAll(x_2,y_2);

        headR.getPoints().setAll(x2, y2, x_1, y_1,  x_2, y_2);
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
