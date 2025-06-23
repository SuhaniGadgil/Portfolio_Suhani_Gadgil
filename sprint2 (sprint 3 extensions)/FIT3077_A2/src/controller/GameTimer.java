package controller;

import javax.swing.*;
import model.Game;
import model.Player;

public class GameTimer {
    private Timer swingTimer;
    private final Game game;
    private final Runnable onLossCallBack;
    private final Runnable onTimerUpdate;

    public GameTimer(Game game, Runnable onLossCallBack, Runnable onTimerUpdate) {
        this.game = game;
        this.onLossCallBack = onLossCallBack;
        this.onTimerUpdate = onTimerUpdate;

        //this will make the time tick every second
        swingTimer = new Timer(1000, e -> {
            Player current = game.getCurrentPlayer();
            current.decrementTime();

            //updates the UI
            onTimerUpdate.run();

            if (current.getTimeLeftInSeconds() <= 0) {
                swingTimer.stop();
                //the player loses
                onLossCallBack.run();
            }
        });
    }

    public void start(){
        swingTimer.start();
    }

    public void stop () {
        swingTimer.stop();
    }

    public void reset() {
        swingTimer.restart();
    }

}
