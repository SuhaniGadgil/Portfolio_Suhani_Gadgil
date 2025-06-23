package model;

import java.util.List;

/**
 * Represents the Santorini game.
 */
public class Game {
    private Board board;
    private List<Player> players;
    private GameRuleChecker ruleChecker;
    private int currentPlayerIndex;

    /**
     * Creates a new Game instance.
     * 
     * @param board         the game board
     * @param players       the players in the game
     * @param ruleChecker   validator for moves and builds
     */
    public Game(Board board, List<Player> players, GameRuleChecker ruleChecker) {
        this.board = board;
        this.players = players;
        this.ruleChecker = ruleChecker;
        this.currentPlayerIndex = 0;
    }

    /**
     * Returns the list of players in the game.
     * 
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /** 
     * Returns the current game board.
     * 
     * @return the game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the size of the board.
     * 
     * @return the size of the board
     */
    public int getBoardSize() {
        return board.getSize();
    }

    /**
     * Returns the cell at the specified coordinates.
     * 
     * @param x x-coordinate (row)
     * @param y y-coordinate (column)
     * 
     * @return the cell at the specified coordinates
     */
    public Cell getCell(int x, int y) {
        return board.getCell(x, y);
    }

    /**
     * Returns the current player.
     * 
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Moves a worker to the specified coordinates.
     * 
     * @param w the worker to move
     * @param x the x-coordinate (row) to move to
     * @param y the y-coordinate (column) to move to
     * 
     * @throws RuntimeException if the move is invalid
     */
    public void moveWorker(Worker w, int x, int y) {
        Cell to = getCell(x, y);
        ruleChecker.isValidMove(w, to, w.getOwner());
        w.move(to);
    }
    
    /**
     * Builds a block or dome at the specified coordinates.
     * 
     * @param w the worker building
     * @param x the x-coordinate (row) to build at
     * @param y the y-coordinate (column) to build at
     * 
     * @throws RuntimeException if the build is invalid
     */
    public void build(Worker w, int x, int y) {
        Cell target = getCell(x, y);
        ruleChecker.isValidBuild(w, target, w.getOwner());

        // Decide whether to push Block or Dome
        if (target.getHeight() == target.getHeightLimit() - 1) {
            w.build(target, new Dome());
        } 
        else {
            w.build(target, new Block(target.getHeight() + 1));
        }
    }

    /**
     * Checks if the current player has won the game.
     * 
     * @param w the worker to check
     * 
     * @return true if the player has won, false otherwise
     */
    public boolean checkWin(Worker w) {
        return w.getPosition().getHeight() == w.getPosition().getHeightLimit() - 1;
    }

    /**
     * Switches the turn to the next player.
     */
    public void switchTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Checks if the move for the worker is valid according to game rules.
     * Delegates the check to the internal ruleChecker.
     *
     * @param worker The worker attempting to move
     * @param targetCell The target cell for the move
     * 
     * @throws RuntimeException if the move is invalid
     */
    public void isValidMove(Worker worker, Cell targetCell) {
        this.ruleChecker.isValidMove(worker, targetCell, worker.getOwner());
    }

    /**
     * Checks if the build action for the worker is valid according to game rules.
     * Delegates the check to the internal ruleChecker.
     *
     * @param worker The worker attempting to build
     * @param targetCell The target cell for the build
     * 
     * @throws RuntimeException if the build is invalid
     */
    public void isValidBuild(Worker worker, Cell targetCell) {
        this.ruleChecker.isValidBuild(worker, targetCell, worker.getOwner());
    }

    /**
     * Checks if a player has no valid moves left with any of their workers.
     * 
     * @param player the player to check
     * 
     * @return true if the player has no valid moves, false otherwise
     */
    private boolean checkNoValidMoves(Player player) {
        for (Worker playerWorker : player.getWorkers()) {
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    try {
                        isValidMove(playerWorker, board.getCell(i, j));
                        return false;
                    } catch (Exception e) {
                        // Continue checking other cells
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks if the current game state represents a loss condition.
     * A player loses if both of their workers have no valid moves.
     * 
     * @return the winning player if there is a loss condition, null otherwise
     */
    public Player checkLossCondition() {
        for (Player player : players) {
            if (checkNoValidMoves(player)) {
                // Return the other player as the winner
                return players.get((players.indexOf(player) + 1) % players.size());
            }
        }
        return null;
    }
}
