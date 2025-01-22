package org.trylma.server.lobby;

import org.trylma.common.model.GameConfig;
import org.trylma.common.model.Player;
import org.trylma.server.game.model.Game;
import org.trylma.server.factory.GameFactory;
import org.trylma.server.game.network.ClientHandler;

import java.util.ArrayList;
import java.util.List;

public class GameLobby {
    private final GameConfig gameConfig;
    private final List<Player> players;
    private final List<ClientHandler> clientHandlers;
    private final LobbyMediator lobbyMediator;
    private Game game;
    private int connectedPlayers;

    public GameLobby(GameConfig gameConfig, LobbyMediator lobbyMediator) {
        this.gameConfig = gameConfig;
        this.players = new ArrayList<>();
        this.clientHandlers = new ArrayList<>();
        this.lobbyMediator = lobbyMediator;
        this.connectedPlayers = 0;
    }

    public synchronized boolean addPlayer(Player player, ClientHandler clientHandler) {
        if (connectedPlayers < gameConfig.getPlayerCount().getCount()) {
            players.add(player);
            clientHandlers.add(clientHandler);
            connectedPlayers++;
            if (connectedPlayers == gameConfig.getPlayerCount().getCount()) {
                startGame();
            }
            return true;
        } else {
            return false;
        }
    }

    public synchronized void removePlayer(ClientHandler clientHandler) {
        int index = clientHandlers.indexOf(clientHandler);
        if (index != -1) {
            players.remove(index);
            clientHandlers.remove(index);
            connectedPlayers--;
            // Handle game state change, e.g., if a player leaves mid-game
        }
    }

    private void startGame() {
        game = GameFactory.createGame(gameConfig, players, lobbyMediator);
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.setGame(game);
            clientHandler.notifyGameStart();
        }
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Game getGame() {
        return game;
    }

    public synchronized boolean isFull() {
        return connectedPlayers == gameConfig.getPlayerCount().getCount();
    }
}