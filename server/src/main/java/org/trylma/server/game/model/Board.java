package org.trylma.server.game.model;

import java.util.List;
import java.util.ArrayList;

public abstract class Board {
    protected List<HexCell> cells;
    protected int boardSize; // Add a field for board size

    public Board(int boardSize) {
        this.boardSize = boardSize;
        this.cells = new ArrayList<>();
        initializeBoard(boardSize);
    }

    protected abstract void initializeBoard(int boardSize);

    // Method to get a specific cell
    public HexCell getCell(int x, int y) {
        for (HexCell cell : cells) {
            if (cell.getX() == x && cell.getY() == y) {
                return cell;
            }
        }
        return null; // Or throw an exception if the cell is not found
    }

    // Method to update the state of a cell
    public void updateCellState(int x, int y, HexCell.State newState) {
        HexCell cell = getCell(x, y);
        if (cell != null) {
            cell.setState(newState);
        }
    }

    public int getBoardSize() {
        return boardSize;
    }

    public List<HexCell> getCells(){
        return cells;
    }
}