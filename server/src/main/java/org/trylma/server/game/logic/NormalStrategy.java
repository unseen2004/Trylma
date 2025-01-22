package org.trylma.server.game.logic;

import org.trylma.common.model.Move;
import org.trylma.common.model.Player;
import org.trylma.server.game.model.Board;
import org.trylma.server.game.model.HexCell;

public class NormalStrategy implements GameStrategy {
    @Override
    public boolean isValidMove(Move move, Board board, Player player) {
        // Get the cell at the starting position of the move
        HexCell startCell = board.getCell(move.getFromX(), move.getFromY());

        // Check if the start cell is occupied (should always be true in a valid move)
        if (startCell == null || startCell.getState() != HexCell.State.OCCUPIED) {
            return false;
        }

        // Get the cell at the ending position of the move
        HexCell endCell = board.getCell(move.getToX(), move.getToY());

        // Check if the end cell is within the board boundaries and is empty
        if (endCell == null || endCell.getState() != HexCell.State.EMPTY) {
            return false;
        }

        // Check if the move is a valid distance (adjacent cell or a valid jump)
        if (!isValidDistance(move, board)) {
            return false;
        }

        return true;
    }

    private boolean isValidDistance(Move move, Board board) {
        int dx = Math.abs(move.getToX() - move.getFromX());
        int dy = Math.abs(move.getToY() - move.getFromY());

        // Adjacent move
        if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 1 && dy == 1)) {
            return true;
        }

        return false;
    }


    @Override
    public void performMove(Move move, Board board) {
        // Set the starting cell to empty
        board.updateCellState(move.getFromX(), move.getFromY(), HexCell.State.EMPTY);

        // Set the ending cell to occupied
        board.updateCellState(move.getToX(), move.getToY(), HexCell.State.OCCUPIED);
    }
}