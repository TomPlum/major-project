package controller;

public class StartRobocode {
    private static void launchGame(int times) {
        for (int i = 0; i < times; i++) {
            GameConfigurer.startBattle();
            GameConfigurer.closeJVM();
        }
    }

    public static void main(String[] args) {
        StartRobocode.launchGame(1);
    }
}
