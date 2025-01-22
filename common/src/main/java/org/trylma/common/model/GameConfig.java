package org.trylma.common.model;

import org.trylma.common.enums.GameType;
import org.trylma.common.enums.PlayerCount;

import java.io.Serializable;

public class GameConfig implements Serializable {
    private GameType gameType;
    private PlayerCount playerCount;

    public GameConfig(GameType gameType, PlayerCount playerCount) {
        this.gameType = gameType;
        this.playerCount = playerCount;
    }

    public GameType getGameType() {
        return gameType;
    }

    public PlayerCount getPlayerCount() {
        return playerCount;
    }

    // Add equals and hashCode methods for proper comparison in collections (like in LobbyMediator)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameConfig that = (GameConfig) o;

        if (gameType != that.gameType) return false;
        return playerCount == that.playerCount;
    }

    @Override
    public int hashCode() {
        int result = gameType.hashCode();
        result = 31 * result + playerCount.hashCode();
        return result;
    }
}