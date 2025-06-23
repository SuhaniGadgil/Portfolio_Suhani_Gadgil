package model;

import java.util.*;

/**
 * A factory class responsible for creating a Game.
 */
public class GameFactory {

    /**
     * Creates a new Game instance with the specified number of players, board size, and chosen GodCards.
     *
     * @param numPlayers        number of players (e.g., 2 players)
     * @param boardSize         size of the board (e.g., 5 for 5x5)
     * @param godCardChoices    list of GodCards for the game
     * 
     * @return new Game instance
     */
    public Game createGame(int numPlayers, int boardSize, List<GodCard> godCardChoices, int cellHeightLimit) {
        // Create the Board with specified size
        Board board = new Board(boardSize, cellHeightLimit);
        Random random = new Random();

        List<Player> players = new ArrayList<>();
        List<Cell> availableCells = new ArrayList<>();

        // Add available cells into a list
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                availableCells.add(board.getCell(x, y));
            }
        }

        //List<GodCard> assignedGodCards = new ArrayList<>();

        // Assign cards randomly
        Collections.shuffle(godCardChoices);
        //the first god card from the shuffled list for the player
        List<GodCard> assignedGodCards = new ArrayList<>(godCardChoices.subList(0, numPlayers));
//        for (int i = 0; i < numPlayers; i++) {
//            int index = random.nextInt(godCardChoices.size());
//            GodCard godCard = godCardChoices.remove(index); // Remove assigned cards to avoid duplicates
//            assignedGodCards.add(godCard);
//        }

        // Create players and assign god cards randomly
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(i + 1, assignedGodCards.get(i));

            // Create and place 2 workers on random cells
            for (int w = 1; w <= 2; w++) {
                int randomIndex = random.nextInt(availableCells.size());
                Cell randomCell = availableCells.remove(randomIndex); // Remove occupied cells

                Worker worker = new Worker(player, w, randomCell);
                player.addWorker(worker);
            }

            players.add(player);
        }

        // Create GameRuleChecker instance
        GameRuleChecker rules = new GameRuleChecker(board, players);

        // Return the new Game instance
        return new Game(board, players, rules);
    }
}
