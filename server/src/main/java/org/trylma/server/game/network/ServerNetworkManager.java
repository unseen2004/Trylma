package org.trylma.server.game.network;

import org.trylma.server.lobby.LobbyMediator;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetworkManager {
    private final int port;
    private ServerSocket serverSocket;
    private final ExecutorService clientExecutor;
    private final LobbyMediator lobbyMediator;
    private boolean isFirstClient = true;

    public ServerNetworkManager(int port) {
        this.port = port;
        this.clientExecutor = Executors.newCachedThreadPool();
        this.lobbyMediator = new LobbyMediator();
    }

    public void start() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Server started on port " + port);

                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket);
                    boolean isHost = isFirstClient;
                    isFirstClient = false;
                    clientExecutor.execute(new ClientHandler(clientSocket, lobbyMediator, isHost));
                }
            } catch (IOException e) {
                System.err.println("Server exception: " + e.getMessage());
                stop();
            }
        }).start();
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
        clientExecutor.shutdown();
    }
}