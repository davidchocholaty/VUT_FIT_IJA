/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Class representing an application frontend.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class represents main application frontend. In this class with specific method
 * is frontend application created.
 */
public class App extends Application {
    private MainController controller;

    /**
     * Start method for creating frontend application scene.
     *
     * @param stage Input stage.
     * @throws IOException Error while loading fxml.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main_page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        this.controller = fxmlLoader.getController();
        System.out.println(scene.getStylesheets());
        stage.setTitle("IJA UML designer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Get current main controller for application instance.
     *
     * @return Current main controller.
     */
    public MainController getController() {
        return this.controller;
    }

    /**
     * Main method of class for launching frontend application.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}