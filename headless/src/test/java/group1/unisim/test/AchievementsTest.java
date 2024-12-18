package group1.unisim.test;

import group1.unisim.Service;
import group1.unisim.achievement.Achievement;
import group1.unisim.achievement.AchievementsManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AchievementsTest extends HeadlessGdxTest {
    @Test
    public void testAchievementManager() {
        AchievementsManager achievementsManager = new AchievementsManager(false);
        achievementsManager.onServiceValueChange(Service.Accommodation, 7);
        assertEquals(1, achievementsManager.getComplete().size());
        achievementsManager.onSatisfactionChange(0.0f);
        assertEquals(2, achievementsManager.getComplete().size());
        achievementsManager.onSatisfactionChange(100.0f);
        assertEquals(3, achievementsManager.getComplete().size());

        String formatted = achievementsManager.formatCompleted();
        assertFalse(formatted.isEmpty(), "formatted achievements is not empty");
    }
}
