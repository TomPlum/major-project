package controller;

import robocode.control.events.*;

public class BattleObserver extends BattleAdaptor {
    private BattleResultLogger resultLogger = new BattleResultLogger();

    // Called when the battle is completed successfully with battle results
    public void onBattleCompleted(BattleCompletedEvent e) {
        System.out.println("-- Battle has completed --");
        resultLogger.saveResultsToDatabase(e.getSortedResults());
    }

    // Called when the game sends out an information message during the battle
    public void onBattleMessage(BattleMessageEvent e) {
        System.out.println("Msg> " + e.getMessage());
    }

    // Called when the game sends out an error message during the battle
    public void onBattleError(BattleErrorEvent e) {
        System.out.println("Err> " + e.getError());
    }
}
