package org.trylma.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.trylma.client.network.ClientNetworkManager;

import java.io.IOException;

public class WaitingController {

    private ClientNetworkManager networkManager;
    private Stage primaryStage;

    public void setNetworkManager(ClientNetworkManager networkManager) {
        this.networkManager = networkManager;
        networkManager.setWaitingController(this);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void notifyGameStart() {
        Platform.runLater(() -> {
            try {
                loadGameView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadGameView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameView.fxml"));
        Parent gameView = loader.load();
        GameController gameController = loader.getController();
        gameController.setNetworkManager(networkManager);
        primaryStage.setScene(new Scene(gameView));
    }
}