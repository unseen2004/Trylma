package org.trylma.server.game.network;

import org.trylma.common.model.GameConfig;
import org.trylma.common.model.Move;
import org.trylma.common.model.Player;
import org.trylma.server.lobby.LobbyMediator;
import org.trylma.server.game.model.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final LobbyMediator lobbyMediator;
    private Player player;
    private Game game;
    private boolean isHost;

    public ClientHandler(Socket clientSocket, LobbyMediator lobbyMediator, boolean isHost) {
        this.clientSocket = clientSocket;
        this.lobbyMediator = lobbyMediator;
        this.isHost = isHost;
        try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error creating client handler: " + e.getMessage());
            closeConnection();
        }
    }

    @Override
    public void run() {
        try {
            out.writeBoolean(isHost);
            out.flush();
            handleClientConnection();
            while (!clientSocket.isClosed()) {
                Object input = in.readObject();
                if (input instanceof Move move) {
                    game.makeMove(move, player);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Client disconnected or error: " + e.getMessage());
        } finally {
            if (game != null) {
                game.removePlayer(this);
            }
            closeConnection();
        }
    }

    private void handleClientConnection() throws IOException, ClassNotFoundException {
        Object playerName = in.readObject();
        if (!(playerName instanceof String)) {
            throw new IllegalArgumentException("Expected player name as String");
        }
        this.player = new Player((String) playerName);

        Object gameConfig = in.readObject();
        if (!(gameConfig instanceof GameConfig)) {
            throw new IllegalArgumentException("Expected GameConfig object");
        }
        lobbyMediator.registerPlayer(player, (GameConfig) gameConfig, this);
    }

    public void notifyGameFull() {
        try {
            out.writeObject("Game is full or game has already started. Try again later.");
            out.flush();
        } catch (IOException e) {
            System.err.println("Error notifying game full: " + e.getMessage());
        }
    }

    public void notifyGameStart() {
        try {
            out.writeObject("Game start");
            out.flush();
        } catch (IOException e) {
            System.err.println("Error notifying game start: " + e.getMessage());
        }
    }

    public void sendMove(Move move) {
        try {
            out.writeObject(move);
            out.flush();
        } catch (IOException e) {
            System.err.println("Error sending move: " + e.getMessage());
        }
    }

    public void notifyPlayerTurn() {
        try {
            out.writeObject("Your turn");
            out.flush();
        } catch (IOException e) {
            System.err.println("Error notifying player turn: " + e.getMessage());
        }
    }

    public void notifyInvalidMove() {
        try {
            out.writeObject("Invalid move");
            out.flush();
        } catch (IOException e) {
            System.err.println("Error notifying invalid move: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }
}