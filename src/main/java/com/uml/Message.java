/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class represents UML sequence diagram message on frontend.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

/**
 * Class represents UML sequence diagram message on frontend.
 */
public class Message extends Group {

    private static final double ARROW_SCALER = 10;
    private static final double ARROW_SCALER_CREATE = 30;
    private static final double SELF_SPACE = 30;
    private final SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    private final Polyline mainLine = new Polyline();
    private final Polyline mainLineSelf = new Polyline();
    private final Polyline dashedLine = new Polyline();
    private final Polyline dashedLineSelf = new Polyline();
    private final Polyline dashedLineCreate = new Polyline();
    private final Polyline headAS = new Polyline();
    private final Polyline headASSelf = new Polyline();
    private final Polyline headASCreate = new Polyline();
    private final Polygon headR = new Polygon();
    private final Polygon headRSelf = new Polygon();
    private final Label createText = new Label();
    private final Label messageText = new Label();
    private final Label messageTextSelf = new Label();
    private final double MESSAGEHEAD_ANGLE = Math.toRadians(30);
    private final double MESSAGEHEAD_LENGTH = 15;

    /**
     * Create new Message instance.
     *
     * @param X1 First x coordinate (start).
     * @param X2 Second x coordinate (end).
     * @param Y1 First y coordinate (start).
     * @param Y2 Second y coordinate (end).
     * @param messageID Type of message.
     * @param message Message label.
     */
    public Message(double X1, double X2, double Y1, double Y2, String messageID, String message){
        this.x1.set(X1);
        this.x2.set(Y1);
        this.y1.set(X2);
        this.y2.set(Y2);

        if(messageID.equals("create")){
            createText.setText(message);
            createText.translateXProperty().bind(createText.widthProperty().divide(-2));
            createText.translateYProperty().bind(createText.heightProperty().divide(-1));
            getChildren().addAll(dashedLineCreate, headASCreate, createText);
        }else if (messageID.equals("sync")){
            messageText.setText(message);
            getChildren().addAll(mainLine, headR, messageText);
        }else if (messageID.equals("async")){
            messageText.setText(message);
            getChildren().addAll(mainLine, headAS, messageText);
        }else if (messageID.equals("return")){
            messageText.setText(message);
            getChildren().addAll(dashedLine, headAS, messageText);
        }else if (messageID.equals("syncSelf")){
            messageTextSelf.setText(message);
            getChildren().addAll(mainLineSelf, headRSelf, messageTextSelf);
        }else if (messageID.equals("asyncSelf")){
            messageTextSelf.setText(message);
            getChildren().addAll(mainLineSelf, headASSelf, messageTextSelf);
        }else if (messageID.equals("returnSelf")){
            messageTextSelf.setText(message);
            getChildren().addAll(dashedLineSelf, headASSelf, messageTextSelf);
        }
        messageText.translateXProperty().bind(messageText.widthProperty().divide(-2));
        messageText.translateYProperty().bind(messageText.heightProperty().divide(-1));
        messageTextSelf.translateXProperty().bind(messageTextSelf.widthProperty().divide(-1));
        messageTextSelf.translateYProperty().bind(messageTextSelf.heightProperty().divide(-2));

        for(SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1, this.x2, this.y1, this.y2}){
            s.addListener((l,o,n) -> update() );
        }

        update();
    }

    /**
     * Change message x and y coordinates on update.
     */
    private void update(){
        double[] start = scale(x1.get(), y1.get(), x2.get(), y2.get());
        double[] end = scale(x2.get(), y2.get(), x1.get(), y1.get());
        double[] endCreate = scaleCreate(x2.get(), y2.get(), x1.get(), y1.get());

        double x1 = start[0];
        double y1 = start[1];
        double x2 = end[0];
        double y2 = end[1];

        mainLine.getPoints().setAll(x1, y1, x2, y2);
        dashedLine.getPoints().setAll(x1, y1, x2, y2);
        dashedLineCreate.getPoints().setAll(x1, y1, endCreate[0], endCreate[1]);
        createText.setLayoutX((x1+endCreate[0])/2);
        createText.setLayoutY((y1+endCreate[1])/2);
        messageText.setLayoutX((x1+x2)/2);
        messageText.setLayoutY((y1+y2)/2);
        dashedLineCreate.getStrokeDashArray().addAll(15d, 10d);
        dashedLine.getStrokeDashArray().addAll(15d, 10d);
        dashedLineSelf.getStrokeDashArray().addAll(15d, 10d);

        //self lines
        mainLineSelf.getPoints().setAll(this.x2.get(), this.y2.get(), this.x2.get() - SELF_SPACE, this.y2.get());
        dashedLineSelf.getPoints().setAll(this.x2.get(), this.y2.get(), this.x2.get() - SELF_SPACE, this.y2.get());
        mainLineSelf.getPoints().addAll(this.x2.get() - SELF_SPACE, this.y2.get() + SELF_SPACE);
        dashedLineSelf.getPoints().addAll(this.x2.get() - SELF_SPACE, this.y2.get() + SELF_SPACE);
        messageTextSelf.setLayoutX(this.x2.get() - SELF_SPACE);
        messageTextSelf.setLayoutY((this.y2.get() + this.y2.get() + SELF_SPACE)/2);
        mainLineSelf.getPoints().addAll(this.x2.get(), this.y2.get() + SELF_SPACE);
        dashedLineSelf.getPoints().addAll(this.x2.get(), this.y2.get() + SELF_SPACE);

        double thetaSelf = Math.atan2(y2-y1, x2-x1);
        double x_1Self = this.x2.get() - Math.cos(thetaSelf + MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        double y_1Self = this.y2.get() - Math.sin(thetaSelf + MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        headASSelf.getPoints().setAll(x_1Self,y_1Self + SELF_SPACE,this.x2.get(),this.y2.get() + SELF_SPACE);
        double x_2Self = this.x2.get() - Math.cos(thetaSelf - MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        double y_2Self= this.y2.get() - Math.sin(thetaSelf - MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        headASSelf.getPoints().addAll(x_2Self, y_2Self + SELF_SPACE);
        headRSelf.getPoints().setAll(this.x2.get(), this.y2.get() + SELF_SPACE, x_1Self, y_1Self + SELF_SPACE,  x_2Self, y_2Self + SELF_SPACE);


        //normal message
        double theta = Math.atan2(y2-y1, x2-x1);
        double thetaCreate = Math.atan2(endCreate[1]-y1, endCreate[0]-x1);

        double x_1 = x2 - Math.cos(theta + MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        double x_1Create = endCreate[0] - Math.cos(thetaCreate + MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        double y_1 = y2 - Math.sin(theta + MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        double y_1Create = endCreate[1] - Math.sin(thetaCreate + MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        headAS.getPoints().setAll(x_1,y_1,x2,y2);
        headASCreate.getPoints().setAll(x_1Create,y_1Create,endCreate[0],endCreate[1]);
        double x_2 = x2 - Math.cos(theta - MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        double x_2Create = endCreate[0] - Math.cos(thetaCreate - MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        double y_2= y2 - Math.sin(theta - MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        double y_2Create= endCreate[1] - Math.sin(thetaCreate - MESSAGEHEAD_ANGLE) * MESSAGEHEAD_LENGTH;
        headAS.getPoints().addAll(x_2,y_2);
        headASCreate.getPoints().addAll(x_2Create, y_2Create);

        headR.getPoints().setAll(x2, y2, x_1, y_1,  x_2, y_2);
    }

    /**
     * Calculate scale for message when is created.
     *
     * @param x1 First x coordinate.
     * @param y1 First y coordinate.
     * @param x2 Second x coordinate.
     * @param y2 Second y coordinate.
     * @return Calculated scale.
     */
    private double[] scaleCreate(double x1, double y1, double x2, double y2){
        double theta = Math.atan2(y2-y1, x2-x1);
        return new double[]{
                x1 + Math.cos(theta) * ARROW_SCALER_CREATE,
                y1 + Math.sin(theta) * ARROW_SCALER_CREATE
        };
    }

    /**
     * Calculate scale for message.
     *
     * @param x1 First x coordinate.
     * @param y1 First y coordinate.
     * @param x2 Second x coordinate.
     * @param y2 Second y coordinate.
     * @return Calculated scale.
     */
    private double[] scale(double x1, double y1, double x2, double y2){
        double theta = Math.atan2(y2-y1, x2-x1);
        return new double[]{
                x1 + Math.cos(theta) * ARROW_SCALER,
                y1 + Math.sin(theta) * ARROW_SCALER
        };
    }

    /**
     * Returns first x coordinate (start).
     *
     * @return Start x coordinate.
     */
    public double getX1() {
        return x1.get();
    }

    /**
     * Returns first x simple property coordinate.
     *
     * @return First x simple property coordinate.
     */
    public SimpleDoubleProperty x1Property() {
        return x1;
    }

    /**
     * Sets new x start coordinate.
     *
     * @param x1 New x coordinate.
     */
    public void setX1(double x1) {
        this.x1.set(x1);
    }

    /**
     * Returns first y coordinate (start).
     *
     * @return Start y coordinate.
     */
    public double getY1() {
        return y1.get();
    }

    /**
     * Returns first y simple property coordinate.
     *
     * @return First y simple property coordinate.
     */
    public SimpleDoubleProperty y1Property() {
        return y1;
    }

    /**
     * Sets new y start coordinate.
     *
     * @param y1 New y coordinate.
     */
    public void setY1(double y1) {
        this.y1.set(y1);
    }

    /**
     * Returns first x coordinate (end).
     *
     * @return End x coordinate.
     */
    public double getX2() {
        return x2.get();
    }

    /**
     * Returns second x simple property coordinate.
     *
     * @return Second x simple property coordinate.
     */
    public SimpleDoubleProperty x2Property() {
        return x2;
    }

    /**
     * Sets new x end coordinate.
     *
     * @param x2 New x coordinate.
     */
    public void setX2(double x2) {
        this.x2.set(x2);
    }

    /**
     * Returns second y coordinate (end).
     *
     * @return End y coordinate.
     */
    public double getY2() {
        return y2.get();
    }

    /**
     * Returns second y simple property coordinate.
     *
     * @return Second y simple property coordinate.
     */
    public SimpleDoubleProperty y2Property() {
        return y2;
    }

    /**
     * Sets new y end coordinate.
     *
     * @param y2 New x coordinate.
     */
    public void setY2(double y2) {
        this.y2.set(y2);
    }

}
