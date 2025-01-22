package org.trylma.client.strategy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.trylma.server.game.model.Board;
import org.trylma.server.game.model.HexCell;

public class UIRenderer {

    private final GraphicsContext gc;
    private final Board board;
    private final double cellWidth;
    private final double cellHeight;

    public UIRenderer(GraphicsContext gc, Board board, double cellWidth, double cellHeight) {
        this.gc = gc;
        this.board = board;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    public void render() {
        // Clear the canvas
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        // Render each cell
        for (HexCell cell : board.getCells()) {
            renderCell(cell);
        }
    }

    private void renderCell(HexCell cell) {
        // Calculate the center position of the cell
        double centerX = cell.getX() * cellWidth + cellWidth / 2;
        double centerY = cell.getY() * cellHeight + cellHeight / 2;

        // Choose color based on cell state
        switch (cell.getState()) {
            case EMPTY:
                gc.setFill(Color.LIGHTGRAY);
                break;
            case OCCUPIED:
                gc.setFill(Color.DARKGRAY); // Placeholder, you'll likely differentiate by player
                break;
            default:
                gc.setFill(Color.BLACK); // Error state
        }

        // Draw the cell (e.g., as a circle)
        gc.fillOval(centerX - cellWidth / 4, centerY - cellHeight / 4, cellWidth / 2, cellHeight / 2);
    }
}