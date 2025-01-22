package org.trylma.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.trylma.client.network.ClientNetworkManager;

import java.io.IOException;

public class ConnectionController {

    @FXML
    private TextField serverIpField;

    @FXML
    private TextField portField;

    private ClientNetworkManager networkManager;
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void handleConnect() {
        String serverIp = serverIpField.getText();
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Port", "Please enter a valid port number.");
            return;
        }

        try {
            networkManager = new ClientNetworkManager(serverIp, port);
            // Here you can transition to the HostSetupView or directly to GameView
            // For now, let's assume we go to HostSetupView
            loadHostSetupView();
        } catch (IOException e) {
            showAlert("Connection Failed", "Could not connect to the server: " + e.getMessage());
        }
    }

    private void loadHostSetupView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HostSetupView.fxml"));
        Parent hostSetupView = loader.load();

        HostSetupController hostSetupController = loader.getController();
        hostSetupController.setNetworkManager(networkManager);

        Scene hostSetupScene = new Scene(hostSetupView);
        primaryStage.setScene(hostSetupScene);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}