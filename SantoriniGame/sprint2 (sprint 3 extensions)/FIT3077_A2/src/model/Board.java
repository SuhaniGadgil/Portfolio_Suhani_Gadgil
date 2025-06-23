package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the board in the game.
 * Manages a dynamic grid of cells based on the specified board size.
 */
public class Board {
    private List<List<Cell>> grid;

    /**
     * Creates a Board instance with the specified size.
     *
     * @param size the size of the board (e.g., 5 for a 5x5 board)
     */
    public Board(int size, int cellHeightLimit) {
        grid = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            List<Cell> row = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                row.add(new Cell(i, j, cellHeightLimit));
            }
            grid.add(row);
        }
    }

    /**
     * Returns the Cell at the specified coordinates.
     *
     * @param x x-coordinate (row)
     * @param y y-coordinate (column)
     * 
     * @return the coordinate of the Cell.
     */
    public Cell getCell(int x, int y) {
        return grid.get(x).get(y);
    }

    /**
     * Returns a list of cells adjacent to the current cell.
     *
     * @param cell current cell
     * 
     * @return a list of adjacent Cells
     */
    public List<Cell> getAdjacentCells(Cell cell) {
        List<Cell> adjacentCells = new ArrayList<>();

        int x = cell.getX();
        int y = cell.getY();

        int size = grid.size();

        // Loop over the cell's surrounding area
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // Skip the cell itself
                if (dx == 0 && dy == 0) {
                    continue;
                }

                int newX = x + dx;
                int newY = y + dy;

                // Check boundaries
                if (newX >= 0 && newX < size && newY >= 0 && newY < size) {
                    adjacentCells.add(getCell(newX, newY));
                }
            }
        }

        return adjacentCells;
    }

    /** 
     * Returns the size of the board.
     * 
     * @return the size of the board
    */
    public int getSize() {
        return grid.size();
    }

    /**
     * Returns the grid of cells.
     *
     * @return the grid of cells
     */
    public List<List<Cell>> getGrid() {
        return grid;
    }
}
