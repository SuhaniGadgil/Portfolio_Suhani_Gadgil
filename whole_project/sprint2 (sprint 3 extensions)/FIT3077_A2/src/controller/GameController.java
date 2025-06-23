package controller;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

import model.*;
import view.GameBoardPanel;
import view.PlayerTurnPanel;
import java.util.Stack;

/**
 * Handles user interaction and links model with view.
 */
public class GameController implements GameBoardEventHandler {
    private Game game;
    private GameBoardPanel boardPanel;
    private PlayerTurnPanel turnPanel;
    private TurnManager turnManager;
    private GameTimer gameTimer;
    private Stack<MoveRecord> moveHistory = new Stack<>();

    /**
     * Constructs a GameController with the game model and board view.
     */
    public GameController(Game game, GameBoardPanel boardPanel, PlayerTurnPanel turnPanel) {
        this.game = game;
        this.boardPanel = boardPanel;
        this.turnPanel = turnPanel;
        this.turnManager = new TurnManager(game);

        setupListeners();
        //undo request button listener
        turnPanel.getRequestUndoButton().addActionListener(e -> handleUndoRequest());
        updateDisplay();

        //creates and starts the timer
        gameTimer = new GameTimer( game,
                //says what to do when the timer reaches 0
                () -> displayLossMessage(game.getCurrentPlayer()),
                //updates the UI
                () -> turnPanel.updateTimers(game.getPlayers().get(0), game.getPlayers().get(1))
        );

        gameTimer.start();
    }

    /**
     * Sets up listeners for each cell on the board.
     */
    private void setupListeners() {
        Board board = game.getBoard();
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Cell cell = board.getCell(row, col);
                boardPanel.getButtonAt(cell).addActionListener(event -> onCellSelected(cell));
            }
        }
    }

    /**
     * Handles cell selection.
     * If a worker is selected, it checks if the cell has a worker.
     * If not, it checks if the cell is valid for moving or building.
     *
     * @param cell The selected cell
     */
    @Override
    public void onCellSelected(Cell cell) {
        // Check if cell has a worker by looking through all players' workers
        Worker cellWorker = null;
        for (Player player : game.getPlayers()) {
            for (Worker worker : player.getWorkers()) {
                if (worker.getPosition() == cell) {
                    cellWorker = worker;
                    break;
                }
            }
            if (cellWorker != null) break;
        }

        // If onCellSelected selects a worker, let onWorkerSelected handle it
        if (cellWorker != null && !turnManager.isAwaitingBuild()) {
            onWorkerSelected(cellWorker); 
            return;
        }

        // If no worker is selected, we can't do anything with an empty cell
        if (!turnManager.hasSelectedWorker()) {
            return;
        }

        // Handle move or build based on current game state
        if (!turnManager.isAwaitingBuild()) {
            handleMoveAction(cell);
        } 
        else {
            handleBuildAction(cell);
        }
    }

    /**
     * Handles the move action for the selected worker.
     * If the move is valid, it updates the display and checks for win condition.
     *
     * @param targetCell The target cell for the move
     */
    private void handleMoveAction(Cell targetCell) {
        Worker currentWorker = turnManager.getSelectedWorker();
        if (currentWorker == null) return;

        // Check for win by opponent having no valid moves
        Player winner = game.checkLossCondition();
        if (winner != null) {
            displayWinMessage(winner, "The opponent has no valid moves! ");
            return;
        }

        //save the worker's current position before moving
        Cell fromCell = currentWorker.getPosition();

        // Try to move the worker
        try {
            turnManager.handleMove(targetCell);
            //after a successful move, push to move history
            moveHistory.push(new MoveRecord(currentWorker, fromCell, targetCell));
        } catch (Exception e) {
            displayError(e.getMessage());
            return;
        }

        boardPanel.clearAdjacentHighlights();
        updateDisplay();

        // Check for win by reaching level 3
        if (turnManager.checkWinCondition()) {
            displayWinMessage(currentWorker.getOwner(), "Your worker has reached the top level! ");
            return;
        }

        if (turnManager.isAwaitingBuild()) {
            // Standard case or Artemis finished moving: build next
            displayError("Select a space to build");
            highlightValidBuildCells();
        } 
        else {
            // Artemis has moved once and can move again
            displayError("Artemis: Move again or click your worker to skip second move");
            highlightValidMoveCells(currentWorker);
        }
    }

    /**
     * Handles the build action for the selected worker.
     * If the build is valid, it updates the display and ends the turn.
     *
     * @param targetCell The target cell for the build
     */
    private void handleBuildAction(Cell targetCell) {
        Player playerBeforeBuild = game.getCurrentPlayer();
        Worker currentWorker = turnManager.getSelectedWorker();
        if (currentWorker == null) return;

        try {
            // Try to build on the target cell
            turnManager.handleBuild(targetCell);
            //after a successful build, save what was built
            BuildingComponent lastBuilt = targetCell.getTopBlock();
            //records the build
            moveHistory.push(new MoveRecord(currentWorker, targetCell, lastBuilt));
        } catch (Exception e) {
            displayError(e.getMessage());
            if (turnManager.isAwaitingBuild()) {
                highlightValidBuildCells();
            }
            return;
        }


        boardPanel.clearAdjacentHighlights();
        updateDisplay();

        boolean turnEnded = (game.getCurrentPlayer() != playerBeforeBuild || !turnManager.hasSelectedWorker());

        if (!turnEnded) {
            // Demeter built once and can build again
            displayError("Demeter: Build again or click your worker to end turn");
            highlightValidBuildCells();
        }
    }

    /**
     * Handles worker selection and allows switching between workers of the same player before making a move.
     * If the selected worker belongs to the current player, it highlights the cell.
     * It also highlights adjacent cells where the worker can move.
     *
     * @param worker The selected worker
     */
    @Override
    public void onWorkerSelected(Worker worker) {
        if (worker.getOwner() == game.getCurrentPlayer()) {
            Worker previousWorker = turnManager.getSelectedWorker();

            // If this is the same worker already selected
            if (worker == previousWorker) {
                // Skip Artemis's second move
                if (!turnManager.isAwaitingBuild() && turnManager.getMovesMadeThisTurn() == 1) {
                    turnManager.setAwaitingBuild(true);
                    displayError("Select a space to build");
                    highlightValidBuildCells();
                    return;
                }
                // End Demeter's turn after first build
                else if (turnManager.isAwaitingBuild() && turnManager.getBuildsMadeThisTurn() == 1) {
                    //turnManager.endTurn();
                    //stops the current player's timer
                    gameTimer.stop();
                    //end the turn logic
                    turnManager.endTurn();
                    //star the next player's timer
                    gameTimer.start();

                    boardPanel.highlightCell(new Cell(-1, -1, 0));
                    boardPanel.clearAdjacentHighlights();
                    updateDisplay();
                    return;
                }
                // Standard deselection case
                else if (!turnManager.isAwaitingBuild()) {
                    turnManager.deselectWorker();
                    boardPanel.highlightCell(new Cell(-1, -1, 0));
                    boardPanel.clearAdjacentHighlights();
                    return;
                }
            }

            // If already in building phase, don't allow switching workers
            if (turnManager.isAwaitingBuild()) {
                displayError("You must build.");
                return;
            }

            // Otherwise select the new worker
            boolean selected = turnManager.selectWorker(worker);
            if (selected) {
                boardPanel.highlightCell(worker.getPosition());
                highlightValidMoveCells(worker);
            }
        } 
        else {
            displayError("It's Player " + game.getCurrentPlayer().getPlayerNo() + "'s turn!");
        }
    }

    /**
     * Highlights cells where the selected worker can move.
     *
     * @param worker    The worker to check valid moves for.
     */
    private void highlightValidMoveCells(Worker worker) {
        boardPanel.clearAdjacentHighlights();

        List<Cell> validMoveCells = new ArrayList<>();
        List<Cell> adjacentCells = game.getBoard().getAdjacentCells(worker.getPosition());

        // Filter out invalid moves
        for (Cell cell : adjacentCells) {
            try {
                game.isValidMove(worker, cell);

                // If the move is valid, add it to the list
                validMoveCells.add(cell);
            } catch (Exception e) {
                // Invalid move, do nothing
            }             
        }

        // Highlight valid move positions
        boardPanel.highlightAdjacentCells(validMoveCells, true);
    }

    /**
     * Highlights cells where the selected worker can build after moving.
     */
    private void highlightValidBuildCells() {
        Worker worker = turnManager.getSelectedWorker();
        if (worker == null) return;

        List<Cell> validbuildCells = new ArrayList<>();

        //adjacent cells where worker can build
        //List<Cell> cellsToCheck = game.getBoard().getAdjacentCells(worker.getPosition());

        List<Cell> adjacentCells = game.getBoard().getAdjacentCells(worker.getPosition());

        //for Zeus god card -> if allowed can add self-cell for a buildable option
        if (worker.getOwner().getGodCard().allowBuildUnderSelf()){
            adjacentCells.add(worker.getPosition());
        }
        // Filter out invalid build locations
        for (Cell cell : adjacentCells) {
            try {
                game.isValidBuild(worker, cell);

                // If the build is valid, add it to the list
                validbuildCells.add(cell);
            } catch (Exception e) {
                // Invalid build location, do nothing
                continue;
            }
        }

        // Highlight valid build positions
        boardPanel.highlightAdjacentCells(validbuildCells, false);
    }

    /**
     * Displays an error message in a dialog box.
     *
     * @param message The error message to display
     */
    @Override
    public void displayError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Displays a win message and exits the game.
     * 
     * @param winner The player who won the game
     * @param message The message to display
     */
    private void displayWinMessage(Player winner, String message) {
        if (winner != null) {
            JOptionPane.showMessageDialog(null, 
                message + "Player " + winner.getPlayerNo() + " wins!");
            System.exit(0);
        }
    }

    private void displayLossMessage(Player loser) {
        JOptionPane.showMessageDialog(null, "Player " + loser.getPlayerNo() + " ran out of time. You Have Lost.");
        System.exit(0);
    }

    /**
     * Updates the display of the game board and the turn panel to show current player.
     * It iterates through each cell and updates its visual representation.
     */
    @Override
    public void updateDisplay() {
        turnPanel.updateTurn(game.getCurrentPlayer());
        Board board = game.getBoard();
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Cell cell = board.getCell(row, col);
                // Find worker at this cell position
                Worker cellWorker = null;
                for (Player player : game.getPlayers()) {
                    for (Worker worker : player.getWorkers()) {
                        if (worker.getPosition() == cell) {
                            cellWorker = worker;
                            break;
                        }
                    }
                    if (cellWorker != null) break;
                }
                boardPanel.updateCellDisplay(cell, cellWorker);
            }
        }

        // Update turn panel
        turnPanel.updateTurn(game.getCurrentPlayer());
    }

    /**
     * handles a player's request to undo their last move (forgiveness mechanic)
     * the opponent can accept or reject the request, but after one rejection, the next must be accepted
     */
    private void handleUndoRequest() {
        //gets the player who is requesting the undo
        Player requestingPlayer = game.getCurrentPlayer();
        //gets the opposing player
        Player opponentPlayer = getOpponentPlayer();

        //checks if the requesting player has any second chances left
        if (requestingPlayer.getSecondChanceLeft() <= 0) {
            displayError("You have no second chances left!");
            return;
        }

        int response;
        //if the opponent rejects the last forgiveness request, they myst accept this one automatically
        if (opponentPlayer.hasLastRequestBeenRejected()) {
            JOptionPane.showMessageDialog(null, "Forgiveness must be granted so the undo request has been accepted automatically");
            response = JOptionPane.YES_OPTION;
        } else {
            //else gives the opponent a choice to accept or reject the request
            response = JOptionPane.showConfirmDialog(null, "Player " + requestingPlayer.getPlayerNo() + " is requesting a second chance.\nDo you accept?", "Forgiveness Request", JOptionPane.YES_NO_OPTION);
        }

        if (response == JOptionPane.YES_OPTION) {
            //if accepted, the last move is undo and decreases the requesting player's second chances
            undoLastMove();
            requestingPlayer.setLastRequestRejected(false);
            updateDisplay();
        } else {
            opponentPlayer.setLastRequestRejected(true);
            displayError("Second chance request denied. Reminder it must be accepted next time!");
        }

    }

    /**
     * return the opponent player --> the player who is not the current player
     * @return the opponent player
     */
    private Player getOpponentPlayer() {
        //Get the list of all players
        List<Player> players = game.getPlayers();

        //loops the players and finds the one who is not the current player
        for (Player p: players) {
            if (p != game.getCurrentPlayer()) {
                return p;
            }
        }

        return null;
    }

    /**
     * undoes the last move and builds action performed by the current player.
     */

    private void undoLastMove() {
        if (moveHistory.isEmpty()) {
            displayError("No moves to undo!");
            return;
        }

        //undoes the last 2 actions (build then moves back to the previous position)
        for (int i = 0; i < 2 && !moveHistory.isEmpty(); i++) {
            MoveRecord record = moveHistory.pop();
            if (record.isBuildAction()) {
                //undo the build -> removes last block built
                Cell buildCell = record.getBuildCell();
                buildCell.removeTopBlock();
            } else {
                //undoes the move action -> moves worker to orignal cell
                Worker worker = record.getWorker();
                worker.move(record.getFromCell());
            }
        }

        boardPanel.clearAdjacentHighlights();
        updateDisplay();
    }

}