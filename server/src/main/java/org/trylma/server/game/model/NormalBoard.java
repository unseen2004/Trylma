package org.trylma.server.game.model;

public class NormalBoard extends Board {
    public NormalBoard(int boardSize) {
        super(boardSize);
    }

    @Override
    protected void initializeBoard(int boardSize) {
        // Initialize cells for a normal game
        // Example for a simple 4x4 board (you'll need to adjust this based on the actual board size and shape)
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                HexCell.State initialState = (i + j) % 2 == 0 ? HexCell.State.EMPTY : HexCell.State.OCCUPIED;
                cells.add(new HexCell(i, j, initialState));
            }
        }
    }
}