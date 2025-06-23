package model;

import javax.swing.ImageIcon;

/**
 * Abstract base class representing a building component (block/dome) on the board.
 */
public abstract class BuildingComponent {
    private String name;
    private boolean isOccupiable;
    private boolean allowBuildOnTop;
    private ImageIcon image;

    /**
     * Creates a BuildingComponent instance.
     *
     * @param name              name of the component (block or dome)
     * @param isOccupiable      true if workers can stand on it, false if not
     * @param allowBuildOnTop   true if another building can be built on top of it
     * @param image             image icon representing the component
     */
    public BuildingComponent(String name, boolean isOccupiable, boolean allowBuildOnTop, ImageIcon image) {
        this.name = name;
        this.isOccupiable = isOccupiable;
        this.allowBuildOnTop = allowBuildOnTop;
        this.image = image;
    }

    /**
     * Returns the name of the building component.
     *
     * @return the component name (block or dome)
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether the component can be occupied by a worker.
     *
     * @return true if occupiable, false if not
     */
    public boolean getIsOccupiable() {
        return isOccupiable;
    }

    /**
     * Returns whether another building can be built on top of this component.
     *
     * @return true if buildable on top, false if not
     */
    public boolean getAllowBuildOnTop() {
        return allowBuildOnTop;
    }

    /**
     * Returns the image icon representing the component.
     *
     * @return the image icon
     */
    public ImageIcon getImage() {
        return image;
    }
}
