package org.trylma.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.trylma.client.controller.HostSetupController;
import org.trylma.client.controller.WaitingController;
import org.trylma.client.network.ClientNetworkManager;

import java.io.IOException;

public class Client extends Application {

    private ClientNetworkManager networkManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Trylma Game");

        // Replace "server-ip" with the actual server IP address and port
        networkManager = new ClientNetworkManager("127.0.0.1", 12345); // Example IP and port

        // Load the initial view based on the player's role
        if (networkManager.isHost()) {
            loadHostSetupView(primaryStage);
        } else {
            loadWaitingView(primaryStage);
        }
    }

    private void loadHostSetupView(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HostSetupView.fxml"));
        Parent root = loader.load();
        HostSetupController controller = loader.getController();
        controller.setNetworkManager(networkManager);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void loadWaitingView(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/WaitingView.fxml"));
        Parent root = loader.load();
        WaitingController controller = loader.getController();
        controller.setNetworkManager(networkManager);
        controller.setPrimaryStage(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}