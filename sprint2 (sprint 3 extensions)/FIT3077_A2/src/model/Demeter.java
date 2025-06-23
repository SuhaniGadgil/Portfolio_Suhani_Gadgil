package model;

/**
 * A subclass for Demeter God Card.
 */
public class Demeter extends GodCard{

    /**
     * Creates an instance of the Demeter God Card.
     */
    public Demeter() {
        super("Demeter", "Your worker may build one additional time, but not on the same space.");
    }

    /**
     * Overrides the default build count.
     * Player has the option to build once or twice.
     *
     * @return the number of allowed builds
     */
    @Override
    public int allowedBuildCount() {
        return 2;
    }

    /**
     * Overrides the default value regarding building on the same space.
     *
     * @return false, indicating that a player has to build on a different space
     */
    @Override
    public boolean allowBuildSamePlace() {
        return false;
    }
}
