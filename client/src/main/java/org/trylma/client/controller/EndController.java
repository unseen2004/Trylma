package org.trylma.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EndController {

    @FXML
    private Label gameResultLabel;

    // Method to set the game result message
    public void setGameResult(String result) {
        gameResultLabel.setText(result);
    }

    @FXML
    private void handleBackToMainMenu() {
        // Logic to go back to the main menu
    }

    @FXML
    private void handleExitGame() {
        // Logic to exit the game
    }
}