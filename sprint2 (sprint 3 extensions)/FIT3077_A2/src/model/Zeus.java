package model;
//subclass for GodCard
public class Zeus extends GodCard {
    public Zeus() {
        //creating instance for the Zeus god card
        super("Zeus", "Your worker may build a block under itself.");
    }

    /**
     * Zeus allows building on the same space as the worker
     *
<<<<<<< Updated upstream:sprint2/FIT3077_A2/src/model/Zeus.java
     * @return true, meanig self-build is allowed
=======
     * @return true, meaning self-build is allowed
>>>>>>> Stashed changes:sprint2 (sprint 3 extensions)/FIT3077_A2/src/model/Zeus.java
     */
    @Override
    public boolean allowBuildUnderSelf() {
        return true;
    }
}
