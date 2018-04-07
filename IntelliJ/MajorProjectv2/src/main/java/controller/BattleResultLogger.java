package controller;

import org.bson.Document;
import robocode.BattleResults;
import robocode.BattleRules;
import twitter.MongoConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * ---------------------------------------------------------------------------------------------------
 * This class is responsible for building a MongoDB Document that is saved to a MongoDB Atlas Cluster.
 * ---------------------------------------------------------------------------------------------------
 * @author Thomas Plumpton
 * @version 1.1.0
 */
public class BattleResultLogger {
    private MongoConnection conn = new MongoConnection("twitter", "results");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private LocalDateTime now = LocalDateTime.now();

    /**
     * Builds a MongoDB Document containing Robocode battle results from the BattleObserver.
     * The results are then saved to the specified MongoDB Atlas Database Collection.
     * @param results Array of Robocode Battle Results
     */
    public void saveResultsToDatabase(BattleResults[] results, BattleRules rules, int realTime, int turns) {
        Document finalResults = new Document();
        Document finalRules = new Document();
        ArrayList<Document> formattedResults = new ArrayList<>();
        System.out.println("Battle results:");
        for (robocode.BattleResults result : results) {
            Document doc = new Document();
            doc.put("name", result.getTeamLeaderName());
            doc.put("score", result.getScore());
            doc.put("bullet_damage", result.getBulletDamage());
            doc.put("bullet_damage_bonus", result.getBulletDamageBonus());
            doc.put("firsts", result.getFirsts());
            doc.put("seconds", result.getSeconds());
            doc.put("thirds", result.getThirds());
            doc.put("last_survivor_bonus", result.getLastSurvivorBonus());
            doc.put("ram_damage", result.getRamDamage());
            doc.put("ram_damage_bonus", result.getRamDamageBonus());
            doc.put("rank", result.getRank());
            doc.put("survival", result.getSurvival());
            formattedResults.add(doc);
        }

        //Battle Rules
        finalRules.put("no_of_rounds", rules.getNumRounds());
        finalRules.put("battlefield_h", rules.getBattlefieldHeight());
        finalRules.put("battlefield_w", rules.getBattlefieldWidth());
        finalRules.put("sentry_border_size", rules.getSentryBorderSize());
        finalRules.put("gun_cooling_rate", rules.getGunCoolingRate());
        finalRules.put("inactivity_time", rules.getInactivityTime());
        finalRules.put("hide_enemy_names", rules.getHideEnemyNames());

        //Add Everything to Results Document
        finalResults.put("date", dtf.format(now));
        finalResults.put("real_time", realTime);
        finalResults.put("no_of_turns", turns);
        finalResults.put("results", formattedResults);
        finalResults.put("rules", finalRules);
        conn.insertDocument(finalResults);
    }
}
