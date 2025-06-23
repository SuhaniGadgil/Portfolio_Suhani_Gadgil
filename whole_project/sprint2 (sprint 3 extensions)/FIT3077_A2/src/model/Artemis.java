package model;

/**
 * A subclass for Artemis God Card.
 */
public class Artemis extends GodCard{

    /**
     * Creates an instance of the Artemis God Card.
     */
    public Artemis() {
        super("Artemis", "Your worker may move one additional time, but not back to its initial space.");
    }

    /**
     * Overrides the default move count.
     * Player has the option to move once or twice.
     *
     * @return the number of allowed moves
     */
    @Override
    public int allowedMoveCount() {
        return 2; // allows one or two moves
    }

    /**
     * Overrides the default value regarding moving back to the initial space.
     *
     * @return false, indictating that a player has to move to a different space.
     */
    @Override
    public boolean allowMoveToInitial() {
        return false;
    }
}
