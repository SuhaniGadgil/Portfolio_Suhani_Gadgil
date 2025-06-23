package model;

/**
 * records moves or builds for undo function
 */
public class MoveRecord {
    private final Worker worker;
    private final Cell fromCell;
    private final Cell toCell;
    private final boolean buildAction;
    private final Cell buildCell;
    private final BuildingComponent removedBlock;

    //Constructor for move
    public MoveRecord(Worker worker, Cell fromCell, Cell toCell) {
        this.worker = worker;
        this.fromCell = fromCell;
        this.toCell = toCell;
        this.buildAction = false; // for moves not builds
        this.buildCell = null;
        this.removedBlock = null;
    }

    //constructor for build
    public MoveRecord(Worker worker, Cell buildCell, BuildingComponent removedBlock) {
        this.worker = worker;
        this.fromCell = null;
        this.toCell = null;
        this.buildAction = true; //it's a build
        this.buildCell = buildCell;
        this.removedBlock = removedBlock;
    }

    //Getter method
    public boolean isBuildAction() {
        return buildAction;
    }

    public Worker getWorker() {
        return worker;
    }

    public Cell getFromCell() {
        return fromCell;
    }

    public Cell getToCell() {
        return toCell;
    }

    public Cell getBuildCell() {
        return buildCell;
    }

    public BuildingComponent getRemovedBlock() {
        return removedBlock;
    }
}
