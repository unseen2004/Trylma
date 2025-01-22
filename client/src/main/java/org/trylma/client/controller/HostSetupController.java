package org.trylma.client.controller;

import org.trylma.common.enums.GameType;
import org.trylma.common.enums.PlayerCount;
import org.trylma.common.model.GameConfig;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.trylma.client.network.ClientNetworkManager;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class HostSetupController {

    @FXML
    private ComboBox<GameType> gameTypeComboBox;

    @FXML
    private ComboBox<PlayerCount> playerCountComboBox;

    @FXML
    private Button startGameButton;

    private ClientNetworkManager networkManager;

    public void setNetworkManager(ClientNetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @FXML
    public void initialize() {
        gameTypeComboBox.getItems().addAll(GameType.values());
        playerCountComboBox.getItems().addAll(PlayerCount.values());
        startGameButton.setOnAction(event -> startGame());
    }

    @FXML
    private void startGame() {
        GameType selectedGameType = gameTypeComboBox.getSelectionModel().getSelectedItem();
        PlayerCount selectedPlayerCount = playerCountComboBox.getSelectionModel().getSelectedItem();

        if (selectedGameType != null && selectedPlayerCount != null) {
            GameConfig gameConfig = new GameConfig(selectedGameType, selectedPlayerCount);
            CompletableFuture<Boolean> setupResult = networkManager.sendGameSetup(gameConfig);

            setupResult.thenAccept(success -> {
                if (success) {
                    Platform.runLater(() -> {
                        try {
                            loadWaitingView();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }
    }

    private void loadWaitingView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/WaitingView.fxml"));
        Parent waitingView = loader.load();
        WaitingController waitingController = loader.getController();
        waitingController.setNetworkManager(networkManager);
        Stage stage = (Stage) startGameButton.getScene().getWindow();
        stage.setScene(new Scene(waitingView));
    }
}