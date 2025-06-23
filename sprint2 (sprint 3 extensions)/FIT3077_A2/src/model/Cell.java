package model;

import java.util.Stack;

/**
 * Represents a single cell on the board.
 * Each cell can have building components stacked (blocks and domes), and some might have workers on it.
 */
public class Cell {
    private final int x;
    private final int y;
    private final Stack<BuildingComponent> buildingComponents;
    private final int heightLimit;

    /**
     * Creates an empty Cell at the specified (x, y) coordinates.
     *
     * @param x x-coordinate (row)
     * @param y y-coordinate (column)
     */
    public Cell(int x, int y, int heightLimit) {
        this.x = x;
        this.y = y;
        this.heightLimit = heightLimit;
        this.buildingComponents = new Stack<>();
    }

    /**
     * Returns the x-coordinate of the cell.
     *
     * @return x-coordinate of the cell
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the y-coordinate of the cell.
     *
     * @return y-coordinate of the cell
     */
    public int getY() {
        return this.y;
    }

    /**
     * Returns the top block on the cell.
     *
     * @return the top block of a building, or null if empty
     */
    public BuildingComponent getTopBlock() {
        if (buildingComponents.isEmpty()) {
            return null;
        }
        return buildingComponents.peek();
    }

    /**
     * Adds a building component (block or dome) on the cell.
     *
     * @param block BuildingComponent to add
     */
    public void addBlock(BuildingComponent block) {
        if (this.getHeight() == this.heightLimit) {
            return;
        }
        buildingComponents.push(block);
    }

    /**
     * Returns the current height of the cell (number of building components).
     *
     * @return height/number of components
     */
    public int getHeight() {
        return buildingComponents.size();
    }

    /**
     * Returns the height limit of the cell.
     *
     * @return height limit of the cell
     */
    public int getHeightLimit() {
        return heightLimit;
    }

    public void removeTopBlock() {
        if (!buildingComponents.isEmpty()) {
            buildingComponents.pop();
        }
    }
}
