package org.trylma.server.factory;

import org.trylma.common.model.GameConfig;
import org.trylma.common.model.Player;
import org.trylma.server.game.model.Board;
import org.trylma.server.game.model.Game;
import org.trylma.server.game.model.NormalBoard;
import org.trylma.server.game.model.DiamondBoard;
import org.trylma.server.game.network.ClientHandler;
import org.trylma.server.game.logic.GameStrategy;
import org.trylma.server.game.logic.NormalStrategy;
import org.trylma.server.game.logic.DiamondStrategy;
import org.trylma.common.enums.GameType;
import org.trylma.server.lobby.LobbyMediator;

import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class GameFactory {

    public static Game createGame(GameConfig config, List<Player> players, LobbyMediator lobbyMediator) {
        Board board = createBoard(config.getGameType(), config.getPlayerCount().getCount());
        GameStrategy strategy = createGameStrategy(config.getGameType());

        // Create ClientHandlers for each player (assuming you have a way to associate players with their network connections)
        List<ClientHandler> clientHandlers = players.stream()
                .map(player -> new ClientHandler(new Socket(), lobbyMediator)) // Replace with actual Socket creation
                .collect(Collectors.toList());

        return new Game(board, players, strategy, clientHandlers);
    }

    private static Board createBoard(GameType gameType, int playerCount) {
        // Adjust board size based on game type and player count if necessary
        int boardSize = (gameType == GameType.NORMAL) ? 17 : 13; // Example sizes
        if (gameType == GameType.NORMAL) {
            return new NormalBoard(boardSize);
        } else {
            return new DiamondBoard(boardSize);
        }
    }

    private static GameStrategy createGameStrategy(GameType gameType) {
        switch (gameType) {
            case NORMAL:
                return new NormalStrategy();
            case DIAMOND:
                return new DiamondStrategy();
            default:
                throw new IllegalArgumentException("Unknown game type: " + gameType);
        }
    }
}