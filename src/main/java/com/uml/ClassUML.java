/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing UML class on frontend of application.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;


import java.io.IOException;

import com.uml.classdiagram.UMLClass;

/**
 * Class represents UML class in class diagram on frontend.
 */
public class ClassUML extends Button{
    public Node view;
    public ObservableList<Arrow> edges = FXCollections.observableArrayList();
    private final ClassController controller;

    public ClassUML(double x, double y, MainController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("class_uml.fxml"));
        Button loaded = loader.load();
        setView(loaded);
        getView().setLayoutX(x);
        getView().setLayoutY(y);
        getView().translateXProperty().bind(loaded.widthProperty().divide(-2));
        getView().translateYProperty().bind(loaded.heightProperty().divide(-2));

        this.controller = loader.getController();
        this.controller.setParentController(controller);

        UMLClass cls = controller.diagram.createDefaultClass();
        cls.setXCoordinate(x);
        cls.setYCoordinate(y);

        loaded.setId(cls.getName());

    }

    /**
     * Creates and instance of ClassUML for current main controller.
     *
     * @param x Class x coordinate.
     * @param y Class y coordinate.
     * @param name Class name.
     * @param controller Current main controller.
     * @throws IOException FXMLLoader error.
     */
    public ClassUML(double x, double y, String name, MainController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("class_uml.fxml"));
        Button loaded = loader.load();
        setView(loaded);
        getView().setLayoutX(x);
        getView().setLayoutY(y);
        getView().translateXProperty().bind(loaded.widthProperty().divide(-2));
        getView().translateYProperty().bind(loaded.heightProperty().divide(-2));

        this.controller = loader.getController();
        this.controller.setParentController(controller);

        UMLClass cls = controller.diagram.createClass(name);
        cls.setXCoordinate(x);
        cls.setYCoordinate(y);

        loaded.setId(name);
    }

    /**
     * Get class controller.
     *
     * @return Class controller.
     */
    public ClassController getController(){
        return this.controller;
    }

    /**
     * Get view of class.
     *
     * @return Returns view.
     */
    public Node getView() {
        return view;
    }

    /**
     * Set view of class.
     *
     * @param view New view.
     */
    public void setView(Node view) {
        this.view = view;
    }

    /**
     * Get current node.
     *
     * @return Returns current node.
     */
    public Node getThis(){
        return (Node) this.getChildren();
    }
}
