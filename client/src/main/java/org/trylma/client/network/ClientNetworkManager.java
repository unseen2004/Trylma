package org.trylma.client.network;

import org.trylma.common.model.GameConfig;
import org.trylma.common.model.Move;
import org.trylma.client.controller.GameController;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class ClientNetworkManager {

    private final String serverAddress;
    private final int serverPort;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private GameController gameController;

    public ClientNetworkManager(String serverAddress, int serverPort) throws IOException {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        connect();
    }

    private void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        // Start listening for server messages in a separate thread
        new Thread(this::listenToServer).start();
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public CompletableFuture<Boolean> sendGameSetup(GameConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] data = MessageAdapter.serialize(config);
                out.writeObject(data);
                out.flush();
                // Wait for a confirmation or response from the server
                byte[] responseData = (byte[]) in.readObject();
                String response = (String) MessageAdapter.deserialize(responseData);

                // Process the response (e.g., check for success or failure)
                return "OK".equals(response); // Example response handling
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        });
    }


    public void sendMove(Move move) {
        try {
            byte[] data = MessageAdapter.serialize(move);
            out.writeObject(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error (e.g., server disconnected)
        }
    }

    private void listenToServer() {
        try {
            while (!socket.isClosed()) {
                byte[] receivedData = (byte[]) in.readObject();
                Object received = MessageAdapter.deserialize(receivedData);

                if (received instanceof Move) {
                    // Update the game state with the received move
                    Platform.runLater(() -> gameController.updateBoard((Move) received));
                } else if (received instanceof String) {
                    String message = (String) received;
                    // Process other messages from the server
                    // For example, you might receive messages about game start, turn changes, etc.
                    // Use Platform.runLater() to update the UI from these messages
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            if (!socket.isClosed()) {
                e.printStackTrace();
                // Handle error (e.g., server disconnected)
            }
        }
    }

    // Other methods for sending/receiving data, closing the connection, etc.
}