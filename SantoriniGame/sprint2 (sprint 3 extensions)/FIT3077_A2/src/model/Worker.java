package model;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Represents a worker that can move and build on the board.
 * Each worker is identified by a worker number and occupies a position on the board.
 */
public class Worker {
    private int workerNo;
    private Cell position;
    private Player owner;
    private ImageIcon icon;

    /**
     * Creates a new Worker instance.
     *
     * @param workerNo worker's unique number (1 or 2)
     * @param position the position of the worker
     */
    public Worker(Player owner, int workerNo, Cell position) {
        this.workerNo = workerNo;
        this.position = position;
        this.owner = owner;

        // Load the worker icon based on the player's number
        ImageIcon workerIcon;
        if (owner.getPlayerNo() == 1) {
            workerIcon = new ImageIcon(getClass().getResource("/resources/worker_pink.png"));
        } 
        else {
            workerIcon = new ImageIcon(getClass().getResource("/resources/worker_blue.png"));
        }

        Image scaledImage = workerIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        this.icon = new ImageIcon(scaledImage);
    }

    /**
     * Returns the owner of the worker.
     *
     * @return the player who owns the worker
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Returns the worker's number.
     *
     * @return the number of the worker
     */
    public int getWorkerNo() {
        return this.workerNo;
    }

    /**
     * Returns the worker's position.
     *
     * @return the position of the worker
     */
    public Cell getPosition() {
        return this.position;
    }

    /**
     * Moves the worker to a new position on the board.
     *
     * @param newPosition the position to move to
     */
    public void move(Cell newPosition) {
        this.position = newPosition;
    }
    
    /**
     * Performs a build adjacent to the worker's current position.
     *
     * @param cell the cell where the worker wants to build at
     */
    public void build(Cell cell, BuildingComponent buildingComponent) {
        cell.addBlock(buildingComponent);
    }

    /**
     * Returns the icon representing the worker.
     *
     * @return the icon of the worker
     */
    public ImageIcon getIcon() {
        return icon;
    }
}
