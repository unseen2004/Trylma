package org.trylma.client.network;

import org.trylma.common.model.GameConfig;
import org.trylma.common.model.Move;
import org.trylma.client.controller.GameController;
import org.trylma.client.controller.WaitingController;
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
    private WaitingController waitingController;
    private boolean isHost;

    public ClientNetworkManager(String serverAddress, int serverPort) throws IOException {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        connect();
    }

    private void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        // Determine if this client is the host
        isHost = in.readBoolean();

        // Start listening for server messages in a separate thread
        new Thread(this::listenToServer).start();
    }

    public boolean isHost() {
        return isHost;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setWaitingController(WaitingController waitingController) {
        this.waitingController = waitingController;
    }

    public CompletableFuture<Boolean> sendGameSetup(GameConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                out.writeObject(config);
                out.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public void sendMove(Move move) {
        try {
            out.writeObject(move);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenToServer() {
        try {
            while (!socket.isClosed()) {
                Object received = in.readObject();
                if (received instanceof Move) {
                    Platform.runLater(() -> gameController.updateBoard((Move) received));
                } else if (received instanceof String) {
                    String message = (String) received;
                    if ("Game start".equals(message)) {
                        Platform.runLater(waitingController::notifyGameStart);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}