package org.trylma.server.game.model;

public class DiamondBoard extends Board {
    public DiamondBoard(int boardSize) {
        super(boardSize);
    }

    @Override
    protected void initializeBoard(int boardSize) {
        // Initialize cells for a diamond game
        // Example for a simple diamond-shaped board
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if ((i + j) >= boardSize / 2 && (i + j) <= boardSize + boardSize / 2 - 1) {
                    HexCell.State initialState = (i + j) % 2 == 0 ? HexCell.State.EMPTY : HexCell.State.OCCUPIED;
                    cells.add(new HexCell(i, j, initialState));
                }
            }
        }
    }
}