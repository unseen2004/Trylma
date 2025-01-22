package org.trylma.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.trylma.client.controller.ConnectionController;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the primary stage
        primaryStage.setTitle("Trylma Game");

        // Load the initial view (ConnectionView)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ConnectionView.fxml"));
        Parent root = loader.load();
        System.out.println(getClass().getResource("/view/ConnectionView.fxml"));

        // Get the controller and set the primary stage
        ConnectionController connectionController = loader.getController();
        connectionController.setPrimaryStage(primaryStage);

        // Create and show the scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}