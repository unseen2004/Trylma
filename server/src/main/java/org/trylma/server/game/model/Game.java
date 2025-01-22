package org.trylma.server.game.model;

import org.trylma.common.model.Move;
import org.trylma.common.model.Player;
import org.trylma.server.game.logic.GameStrategy;
import org.trylma.server.game.network.ClientHandler;

import java.util.List;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final GameStrategy gameStrategy;
    private final List<ClientHandler> clientHandlers;
    private int currentPlayerIndex;

    public Game(Board board, List<Player> players, GameStrategy gameStrategy, List<ClientHandler> clientHandlers) {
        this.board = board;
        this.players = players;
        this.gameStrategy = gameStrategy;
        this.clientHandlers = clientHandlers;
        this.currentPlayerIndex = 0; // Start with the first player
    }

    public void startGame() {
        // Initialize the game, set up the board, etc.
        clientHandlers.forEach(ClientHandler::notifyGameStart);
        clientHandlers.get(currentPlayerIndex).notifyPlayerTurn();
    }

    public void makeMove(Move move, Player player) {
        if (!isPlayerTurn(player)) {
            // Handle out-of-turn move
            return;
        }

        if (gameStrategy.isValidMove(move, board, player)) {
            gameStrategy.performMove(move, board);
            // Notify all clients about the move
            clientHandlers.forEach(ch -> ch.sendMove(move));

            advanceTurn();
        } else {
            // Handle invalid move
            clientHandlers.get(players.indexOf(player)).notifyInvalidMove();
        }
    }

    private boolean isPlayerTurn(Player player) {
        return players.indexOf(player) == currentPlayerIndex;
    }

    private void advanceTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        clientHandlers.get(currentPlayerIndex).notifyPlayerTurn();
    }

    public void removePlayer(ClientHandler clientHandler) {
        int index = clientHandlers.indexOf(clientHandler);
        if (index != -1) {
            clientHandlers.remove(index);
            players.remove(index);
            // Adjust currentPlayerIndex if necessary
            if (currentPlayerIndex >= players.size()) {
                currentPlayerIndex = 0;
            }
        }
    }
}