package controller;

import model.Cell;
import model.Worker;

/**
 * Interface defining how UI events from the game board should be handled.
 */
public interface GameBoardEventHandler {
    void onCellSelected(Cell cell);
    void onWorkerSelected(Worker worker);
    void displayError(String message);
    void updateDisplay();
}