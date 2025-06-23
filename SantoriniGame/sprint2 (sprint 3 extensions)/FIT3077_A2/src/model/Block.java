package model;

import javax.swing.ImageIcon;

/**
 * Represents a block on the board that a worker can stand on.
 */
public class Block extends BuildingComponent {

    /**
     * Creates a new Block instance.
     *
     * @param level the level of the block (1 to 3)
     */
    public Block(int level) {
        super("Level" + level, true, true, loadBlockImage(level));
    }

    /**
     * Loads the image for the block based on its level.
     *
     * @param level the level of the block (1 to 3)
     * 
     * @return ImageIcon representing the block
     */
    private static ImageIcon loadBlockImage(int level) {
        String path = "/resources/level_" + level + ".png";
        return new ImageIcon(Block.class.getResource(path));
    }

    /**
     * Returns the string representation of the block.
     */
    @Override
    public String toString() {
        return "L";
    }
}
