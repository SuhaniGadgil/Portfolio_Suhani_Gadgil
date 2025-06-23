package controller;

import model.Cell;
import model.Game;
import model.GodCard;
import model.Worker;

/**
 * Manages the turns of the game.
 */
public class TurnManager {
    private Game game;
    private Worker selectedWorker;
    private boolean awaitingBuild;
    private Cell previousMovePosition;
    private Cell previousBuildPosition;
    private int movesMadeThisTurn;
    private int buildsMadeThisTurn;

    /**
     * Cretaes a new TurnManager instance.
     * 
     * @param game game instance
     */
    public TurnManager(Game game) {
        this.game = game;
        this.selectedWorker = null;
        this.awaitingBuild = false;
        this.previousMovePosition = null;
        this.previousBuildPosition = null;
        this.movesMadeThisTurn = 0;
        this.buildsMadeThisTurn = 0;
    }

    /**
     * Check if there is a selected worker.
     * 
     * @return true if a worker is selected, false otherwise
     */
    public boolean hasSelectedWorker() {
        return selectedWorker != null;
    }

    /**
     * Get the currently selected worker.
     * 
     * @return the selected worker
     */
    public Worker getSelectedWorker() {
        return selectedWorker;
    }

    /**
     * Selects a worker or switches worker selection if another worker of the same player is selected.
     *
     * @param worker    The worker to select
     * @return          True if this is a new worker selection or a worker switch, false otherwise
     */
    public boolean selectWorker(Worker worker) {
        if (worker.getOwner() == game.getCurrentPlayer()) {
            // If in the middle of a move (awaiting build), don't allow switching workers
            if (awaitingBuild) {
                return false;
            }

            // If this is the same worker already selected, do nothing
            if (worker == selectedWorker) {
                return false;
            }

            // If a worker is already selected, deselect it
            selectedWorker = worker;
            previousMovePosition = worker.getPosition();

            movesMadeThisTurn = 0;
            buildsMadeThisTurn = 0;
            awaitingBuild = false;
            previousBuildPosition = null;
            return true;
        }
        return false;
    }

    /**
     * Deselects the currently selected worker.
     */
    public void deselectWorker() {
        if (!awaitingBuild) {
            selectedWorker = null;
            previousMovePosition = null;
        }

        movesMadeThisTurn = 0;
        buildsMadeThisTurn = 0;
        awaitingBuild = false;
        previousBuildPosition = null;
    }

    /**
     * Check if the player is awaiting a build action.
     * 
     * @return true if awaiting build, false otherwise
     */
    public boolean isAwaitingBuild() {
        return awaitingBuild;
    }

    /**
     * Check player's move action.
     * 
     * @param targetCell the target cell to move to
     * 
     * @throws RuntimeException if the move is invalid
     */
    public void handleMove(Cell targetCell) {
        if (!hasSelectedWorker() || isAwaitingBuild()) {
            throw new RuntimeException("Cannot move: No worker selected or awaiting build.");
        }

        GodCard godCard = selectedWorker.getOwner().getGodCard();
        int maxMoves = godCard.allowedMoveCount();

        if (movesMadeThisTurn >= maxMoves) {
            throw new RuntimeException("Cannot move: Maximum moves reached for this turn.");
        }

        // Artemis Rule: Check if trying to move back to the initial spot on the first move
        if (!godCard.allowMoveToInitial() && targetCell == previousMovePosition) {
            throw new RuntimeException("Cannot move back to the initial position.");
        }

        try {
            game.isValidMove(selectedWorker, targetCell);
        } 
        catch (RuntimeException e) {
            throw new RuntimeException("Invalid move: " + e.getMessage());
        }   

        Cell currentCell = selectedWorker.getPosition();
        game.moveWorker(selectedWorker, targetCell.getX(), targetCell.getY());

        // Will only reach here if the move was valid
        movesMadeThisTurn++;
        previousMovePosition = currentCell;

        // Determine if we should transition to build phase
        if (movesMadeThisTurn < maxMoves) {
            // Artemis can move again
            awaitingBuild = false;
        } 
        else {
            // No more moves allowed, transition to build phase
            awaitingBuild = true;
        }
    }

    /**
     * Check player's build action.
     * 
     * @param targetCell the target cell to build on
     * 
     * @throws RuntimeException if the build is invalid
     */
    public void handleBuild(Cell targetCell) {
        if (!hasSelectedWorker() || !isAwaitingBuild()) {
            throw new RuntimeException("Cannot build: No worker selected or not awaiting build.");
        }

        GodCard godCard = selectedWorker.getOwner().getGodCard();
        int maxBuilds = godCard.allowedBuildCount();

        if (buildsMadeThisTurn >= maxBuilds) {
            throw new RuntimeException("Cannot build: Maximum builds reached for this turn.");
        }

        // Demeter Rule: Check if trying to build on the same spot as previous build
        if (buildsMadeThisTurn > 0 && !godCard.allowBuildSamePlace() && targetCell == previousBuildPosition) {
            throw new RuntimeException("Cannot build on the same position as previous build.");
        }

        try {
            game.isValidBuild(selectedWorker, targetCell);
        } 
        catch (RuntimeException e) {
            throw new RuntimeException("Invalid build: " + e.getMessage());
        }

        game.build(selectedWorker, targetCell.getX(), targetCell.getY());

        // Will only reach here if the build was valid
        buildsMadeThisTurn++;
        previousBuildPosition = targetCell;

        // If we've reached max builds, end the turn
        if (buildsMadeThisTurn >= maxBuilds) {
            endTurn();
        }
    }

    /**
     * Ends the current player's turn.
     */
    public void endTurn() {
        selectedWorker = null;
        awaitingBuild = false;
        previousMovePosition = null;
        previousBuildPosition = null;
        movesMadeThisTurn = 0;
        buildsMadeThisTurn = 0;
        game.switchTurn();
    }

    /**
     * Checks if the current player has won the game.
     * 
     * @return true if the player has won, false otherwise
     */
    public boolean checkWinCondition() {
        return hasSelectedWorker() && game.checkWin(selectedWorker);
    }

    /**
     * Get the number of moves made this turn.
     * 
     * @return number of moves made
     */
    public int getMovesMadeThisTurn() {
        return movesMadeThisTurn;
    }

    /**
     * Get the number of builds made this turn.
     * 
     * @return number of builds made
     */
    public int getBuildsMadeThisTurn() {
        return buildsMadeThisTurn;
    }

    /**
     * Set the awaiting build status (used for build logic).
     * 
     * @param awaiting true if awaiting build, false otherwise
     */
    public void setAwaitingBuild(boolean awaiting) {
        this.awaitingBuild = awaiting;
    }
}