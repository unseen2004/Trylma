package org.trylma.server.game.logic;

import org.trylma.common.model.Move;
import org.trylma.common.model.Player;
import org.trylma.server.game.model.Board;

public interface GameStrategy {
    boolean isValidMove(Move move, Board board, Player player);
    void performMove(Move move, Board board);
}