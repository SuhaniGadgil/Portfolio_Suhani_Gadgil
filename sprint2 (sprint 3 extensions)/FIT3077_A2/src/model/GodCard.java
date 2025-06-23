package model;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Abstract base class representing a God Card in the game.
 */
public abstract class GodCard {
    private String name;
    private String description;
    private ImageIcon image;

    /**
     * Creates a GodCard instance.
     * 
     * @param name          the name of the God Card
     * @param description   the description of a God Card's power
     */
    public GodCard(String name, String description) {
        this.name = name;
        this.description = description;
        this.image = loadCardImage();
    }

    /**
     * Returns the name of a God Card
     * 
     * @return name of a God Card
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of a God Card
     * 
     * @return description of a God Card's power
     */
    public String getDescription() {
        return description;
    }

     /**
     * Manages the allowed move count per turn for a God Card.
     * One move count is set as default..
     *
     * @return The number of allowed moves (e.g., 1)
     */
    public int allowedMoveCount() {
        return 1;
    }

    /**
     * Manages the allowed build count per turn for a God Card.
     * One build count is set as default.
     *
     * @return The number of allowed builds (e.g., 1)
     */
    public int allowedBuildCount() {
        return 1;
    }

    /**
     * Determines if a God Card allows a worker to move back to its initial space
     *
     * @return true if moving back to the initial space is permitted, false otherwise
     */
    public boolean allowMoveToInitial() {
        return false;
    }

    /**
     * Determines if a God Card allows a worker to build on the same cell multiple times during their turn
     *
     * @return true if moving back to the initial space is permitted, false otherwise
     */
    public boolean allowBuildSamePlace() {
        return false;
    }

    /**
     * Loads the image for the god card.
     *
     * @return ImageIcon representing the god
     */
    private ImageIcon loadCardImage() {
        String path = "/resources/" + name.toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage();
        
        // Scale  image
        int width = 100;
        int height = (int) (img.getHeight(null) * (100.0 / img.getWidth(null))); 
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    /**
     * Returns the image icon representing the card.
     *
     * @return the image icon
     */
    public ImageIcon getImage() {
        return image;
    }

    /**
     * Finds out if a worker can build under itself - Zeus card
     * @return
     */
    public boolean allowBuildUnderSelf() {
        return false; //defaults to not allowed
    }
}