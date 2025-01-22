package org.trylma.server.lobby;

import org.trylma.common.model.GameConfig;
import org.trylma.common.model.Player;
import org.trylma.server.game.network.ClientHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyMediator {
    private final Map<GameConfig, GameLobby> lobbies = new HashMap<>();
    private final List<GameLobby> activeLobbies = new ArrayList<>();

    public synchronized void registerPlayer(Player player, GameConfig config, ClientHandler clientHandler) {
        GameLobby lobby = findOrCreateLobby(config);
        if (!lobby.addPlayer(player, clientHandler)) {
            clientHandler.notifyGameFull();
            clientHandler.closeConnection();
        }
    }

    private GameLobby findOrCreateLobby(GameConfig config) {
        for (GameLobby lobby : activeLobbies) {
            if (lobby.getGameConfig().equals(config) && !lobby.isFull()) {
                return lobby;
            }
        }
        GameLobby newLobby = new GameLobby(config, this);
        activeLobbies.add(newLobby);
        return newLobby;
    }

    public synchronized void removeLobby(GameLobby lobby) {
        activeLobbies.remove(lobby);
    }
}