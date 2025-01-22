package org.trylma.client.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.trylma.client.network.ClientNetworkManager;
import org.trylma.common.model.Move;

public class GameController {

    @FXML
    private Canvas gameBoardCanvas;

    private ClientNetworkManager networkManager;

    public void setNetworkManager(ClientNetworkManager networkManager) {
        this.networkManager = networkManager;
        networkManager.setGameController(this);
        // Initialize the game view, register listeners, etc.
    }

    @FXML
    public void initialize() {
        // Initialize the canvas, draw the initial board, etc.
        drawBoard();
    }

    private void drawBoard() {
        GraphicsContext gc = gameBoardCanvas.getGraphicsContext2D();
        // Example: draw a simple board
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, gameBoardCanvas.getWidth(), gameBoardCanvas.getHeight());
        // Draw cells, pieces, etc.
    }

    // Method to handle move requests from the UI
    public void handleMove(int fromX, int fromY, int toX, int toY) {
        Move move = new Move(fromX, fromY, toX, toY);
        networkManager.sendMove(move);
    }

    // Method to update the game board based on server updates
    public void updateBoard(Move move) {
        // Update the board state based on the move
        // Redraw the board
        drawBoard();
    }

    // Other methods for game logic, handling server messages, etc.
}