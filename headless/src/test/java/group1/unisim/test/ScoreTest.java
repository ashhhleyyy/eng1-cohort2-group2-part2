package group1.unisim.test;

import group1.unisim.Score;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreTest {
    @Test
    public void testScore() {
        Score score = new Score("test player", 50);
        assertEquals("test player", score.getName());
        assertEquals(50, score.getScore());
        assertEquals("test player: 50", score.toString());
    }
}
