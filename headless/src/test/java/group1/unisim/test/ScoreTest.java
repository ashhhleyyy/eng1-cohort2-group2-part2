package group1.unisim.test;

import group1.unisim.Leaderboard.Score;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ScoreTest {
    @Test
    public void testScore() {
        Score score = new Score("test player", 50);
        assertEquals("test player", score.getName());
        assertEquals(50, score.getScore());
        assertEquals("test player: 50", score.toString());
        Score score2 = new Score();
        assertNull(score2.getName());
        assertEquals(0, score2.getScore());
    }
}
