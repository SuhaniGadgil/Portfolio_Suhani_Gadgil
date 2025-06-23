package model;

import javax.swing.ImageIcon;

/**
 * Represents a dome that is built on top of a tower.
 */
public class Dome extends BuildingComponent {

    /**
     * Creates a Dome component.
     */
    public Dome() {
        super("Dome", false, false, loadDomeImage()); // a dome is not occupiable, hence false
    }

    /**
     * Loads the image for the dome.
     *
     * @return ImageIcon representing the dome
     */
    private static ImageIcon loadDomeImage() {
        String path = "/resources/dome.png";
        return new ImageIcon(Dome.class.getResource(path));
    }

    /**
     * Returns the string representation of the dome.
     */
    @Override
    public String toString() {
        return "Dome";
    }
}
