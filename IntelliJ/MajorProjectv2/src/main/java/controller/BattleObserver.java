package controller;

import robocode.BattleRules;
import robocode.control.events.*;

/**
 * ---------------------------------------------------------------------------------------------------------
 * This class extends the functionality of the BattleAdaptor is Robocode. It monitors for events that happen
 * during and after the battle. I.e. onBattleCompleted, onBattleMessage, onBattleError.
 * ---------------------------------------------------------------------------------------------------------
 * @author Thomas Plumpton
 * @version 1.1.0
 */
public class BattleObserver extends BattleAdaptor {
    private BattleResultLogger resultLogger = new BattleResultLogger();
    private static long startTime;
    static int numberOfTurns = 0;

    /**
     * This method is called when a new battle has started.
     * @param e Robocode BattleStartedEvent
     */
    public void onBattleStarted(BattleStartedEvent e) {
        startTime = System.currentTimeMillis();

        //Print Rules
        BattleRules rules = e.getBattleRules();
        System.out.println("Battle Rules:");
        System.out.println("Inactivity Time: " + rules.getInactivityTime());
        System.out.println("Gun Cooling Rate: " + rules.getGunCoolingRate());
        System.out.println("No. of Rounds: " + rules.getNumRounds());
        System.out.println("Sentry Border Size: " + rules.getSentryBorderSize());
        System.out.println("Battlefield Dimensions: (" + rules.getBattlefieldWidth() + " x " + rules.getBattlefieldHeight() + ")");
    }

    /**
     * Called when a Robocode Battle ends. Contains the results.
     * @param e Robocode BattleCompletedEvent
     */
    public void onBattleCompleted(BattleCompletedEvent e) {
        long stopTime = System.currentTimeMillis();
        System.out.println("Battle Lasted: " + getTimeDiff(startTime, stopTime) + " ms.");
        System.out.println("Number of Turns: " + numberOfTurns);
        resultLogger.saveResultsToDatabase(e.getSortedResults(), e.getBattleRules(), getTimeDiff(startTime, stopTime), numberOfTurns);
    }

    /**
     * Called when the game sends out an information message during the battle
     * @param e Robocode BattleMessageEvent
     */
    public void onBattleMessage(BattleMessageEvent e) {
        System.out.println("Msg> " + e.getMessage());
    }

    /**
     * Called when the game sends out an error message during the battle
     * @param e Robocode BattleErrorEvent
     */
    public void onBattleError(BattleErrorEvent e) {
        System.out.println("Err> " + e.getError());
    }

    /**
     * Calculates the time difference between the start and end times.
     * @param start Current System Time At Start
     * @param end Current System Time At End
     * @return Relative Time Different in Milliseconds
     */
    private int getTimeDiff(long start, long end) {
        long elapsed = end - start;
        return (int) elapsed;
    }
}
