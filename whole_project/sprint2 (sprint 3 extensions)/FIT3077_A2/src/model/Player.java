package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 * Each player has a number, exactly two workers, and a GodCard.
 */
public class Player {
    private final int playerNo;
    private final List<Worker> workers;
    private final GodCard godCard;

    //tracks second chances
    // 2 chances max per player
    private int secondChanceLeft = 2;
    //tracks if the last forgivemess request was rejected or not
    private boolean lastRequestRejected = false;


    /**
     * Creates a Player instance with a player number and a GodCard.
     * 
     * @param playerNo player's number (1 or 2)
     * @param godCard player's assigned GodCard
     */
    public Player(int playerNo, GodCard godCard) {
        this.playerNo = playerNo;
        this.godCard = godCard;
        this.workers = new ArrayList<>();
    }

    /**
     * Adds a worker to the player's list of workers.
     * 
     * @param worker the worker to add
     */
    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    /**
     * Returns the list of the player's workers.
     * 
     * @return a player's workers list
     */
    public List<Worker> getWorkers() {
        return workers;
    }

    /**
     * Returns the player's number.
     * 
     * @return player number
     */
    public int getPlayerNo() {
        return playerNo;
    }

    /**
     * Returns the player's GodCard.
     * 
     * @return player's assigned GodCard.
     */
    public GodCard getGodCard() {
        return godCard;
    }

    // this stores the player's remaining time in seconds 10min = 600sec
    private int timeLeftInSeconds = 600;

    /**
     * this returns the amount of time left for the player in seconds
     * it will update the timer display or check for timeouts
     */
    public int getTimeLeftInSeconds() {
        return timeLeftInSeconds;
    }

    /**
     * Decrements the player's time by 1 sec
     * called per seconds by the game timer
     */
    public void decrementTime() {
        timeLeftInSeconds--;
    }

    /**
     * allows setting the player's time left (e.g -> for resetting or testing)
     *
     * @param seconds the number of seconds to set
     */
    public void setTimeLeftInSeconds(int seconds) {
        timeLeftInSeconds = seconds;
    }

    /**
     * Returns the number of second chances left for a player
     * @return remaining second chances
     */
    public int getSecondChanceLeft() {
        return secondChanceLeft;
    }

    /**
     * Decrements the number of second chances left after one is used
     */
    public void useSecondChance(){
        secondChanceLeft--;
    }

    /**
     * checks if the player's last forgiveness request was rejected
     * @return true if last request was rejected, false otherwise
     */
    public boolean hasLastRequestBeenRejected(){
        return lastRequestRejected;
    }

    /**
     * sets the rejected status of the player's last forgiveness request
     * @param rejected true if last request was rejected, false if accepted
     */
    public void setLastRequestRejected(boolean rejected) {
        this.lastRequestRejected = rejected;
    }
}
