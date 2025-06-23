package model;

import java.util.List;

/**
 * Validates game moves, builds and win conditions.
 */
public class GameRuleChecker {
    private Board board;
    private List<Player> players;

    /**
     * Creates a GameRuleChecker instance.
     *
     * @param board     the game board
     * @param players   the players in the game
     */
    public GameRuleChecker(Board board, List<Player> players) {
        this.board = board;
        this.players = players;
    }

    /**
     * Checks if a worker can move to a sepecific cell.
     *
     * @param worker        the worker attempting to move
     * @param targetCell    the target cell
     * @param player        the owner of the worker
     * 
     * @throws RuntimeException if the move is invalid
     */
    public void isValidMove(Worker worker, Cell targetCell, Player player) {
        // Check if a cell is occupied
        for (Player p: players) {
            for (Worker w: p.getWorkers()){
                if (w.getPosition() == targetCell){
                    throw new RuntimeException("Cell is occupied by another worker");
                }
            }
        }
        
        // Make sure the worker only moves up one level
        if (targetCell.getHeight() > worker.getPosition().getHeight() + 1) {
            throw new RuntimeException("Worker cannot move up more than one level");
        }

        // Check if cell has a 'dome' or any other inoccupiable object
        if (targetCell.getTopBlock() != null && targetCell.getTopBlock().getIsOccupiable() == false) {
            throw new RuntimeException("Cell is occupied by a dome or other inoccupiable object");
        }

        // Check if cell is adjacent
        List<Cell> adjacentCells = board.getAdjacentCells(worker.getPosition());
        if (!adjacentCells.contains(targetCell)) {
            throw new RuntimeException("Cell is not adjacent to the worker's current position");
        }
    }

    /**
     * Checks if a worker can build at a specific cell.
     *
     * @param worker        the worker attempting to build
     * @param targetCell    the cell to build on
     * @param player        the owner of the worker
     * 
     * @throws RuntimeException if the build is invalid
     */
    public void isValidBuild(Worker worker, Cell targetCell, Player player) {
        // Check if a cell is occupied
        boolean occupiedByOtherWorker = false;

        for (Player p : players) {
            for (Worker w : p.getWorkers()) {
                if (w.getPosition() == targetCell && w != worker) {
                    occupiedByOtherWorker = true;
                    break;
                }
            }
        }

        if (occupiedByOtherWorker) {
            throw new RuntimeException("Cell is occupied by another worker");
        }

        // Allow self-build only if Zeus allows it
        if (worker.getPosition() == targetCell && !worker.getOwner().getGodCard().allowBuildUnderSelf()) {
            throw new RuntimeException("Cannot build under self unless your god allows it");
        }

        // Check if cell can be built upon
        if (targetCell.getTopBlock() != null && targetCell.getTopBlock().getAllowBuildOnTop() == false) {
            throw new RuntimeException("Cell is occupied by a dome or other inoccupiable object");
        } 
        else if (targetCell.getHeight() == targetCell.getHeightLimit()) {
            throw new RuntimeException("Cell is already at maximum height");
        }

        //[MODIFIED for ZEUS] Allow Zeus to build under self (not just adjacent)
        List<Cell> adjacentCells = board.getAdjacentCells(worker.getPosition());
        if (!adjacentCells.contains(targetCell) && targetCell != worker.getPosition()) {
            throw new RuntimeException("Cell is not adjacent to the worker's current position");
        }

        // Check if cell is adjacent
//        List<Cell> adjacentCells = board.getAdjacentCells(worker.getPosition());
//        if (!adjacentCells.contains(targetCell)) {
//            throw new RuntimeException("Cell is not adjacent to the worker's current position");
//        }
    }

    /**
     * Checks if the worker has won (reached the third level).
     *
     * @return      true if the worker is on level 3
     */
    public Player checkWinner() {
        for (Player p : this.players) {
            boolean isWinner = false;
            for (Worker w : p.getWorkers()) {
                if (w.getPosition().getHeight() == w.getPosition().getHeightLimit()) {
                    isWinner = true;
                }
            }
            if (isWinner) {
                return p;
            }
        }
        return null;
    }
}
