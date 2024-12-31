package group1.unisim.test;

import group1.unisim.achievement.AchievementsManager;
import group1.unisim.achievement.SatisfactionAchievement;
import group1.unisim.building.Service;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class AchievementsTest extends HeadlessGdxTest {

    @Test
    public void testAchievementManager() {
        AchievementsManager achievementsManager = new AchievementsManager(false);
        achievementsManager.onServiceValueChange(Service.Recreation, 7);
        assertEquals(0, achievementsManager.getComplete().size());
        achievementsManager.onServiceValueChange(Service.Accommodation, 7);
        assertEquals(1, achievementsManager.getComplete().size());

        achievementsManager.onSatisfactionChange(50.0f);
        assertEquals(1, achievementsManager.getComplete().size());
        achievementsManager.onSatisfactionChange(0.0f);
        assertEquals(2, achievementsManager.getComplete().size());

        achievementsManager.onSatisfactionChange(100.0f);
        assertEquals(3, achievementsManager.getComplete().size());

        String formatted = achievementsManager.formatCompleted();
        assertFalse(formatted.isEmpty(), "formatted achievements is not empty");
    }

    @Test
    public void testDoubleAchievement() {
        ByteArrayOutputStream errOutput = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOutput));
        SatisfactionAchievement satisfactionAchievement = new SatisfactionAchievement("test", "test satisfaction achievemnt", "description", 10, true);
        satisfactionAchievement.onSatisfactionChange(11);
        satisfactionAchievement.onSatisfactionChange(9);
        assertFalse(errOutput.size() > 0);
        satisfactionAchievement.onSatisfactionChange(11);
        assertTrue(errOutput.size() > 0);
    }

    @Test
    public void testLoadSave() {
        AchievementsManager achievementsManager = new AchievementsManager();
        achievementsManager.saveAchievements();
    }

}
