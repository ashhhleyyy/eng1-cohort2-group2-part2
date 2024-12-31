package group1.unisim.test;

import group1.unisim.leaderboard.Leaderboard;
import group1.unisim.leaderboard.Score;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LeaderboardTest {
    @Test
    public void testLeaderboardOrdering() {
        Score score1 = new Score("1", 10);
        Score score2 = new Score("2", 20);
        // ensure the leaderboard sorts correctly even when scores are added in a different order
        Leaderboard leaderboard1 = new Leaderboard();
        leaderboard1.addScore(score1);
        leaderboard1.addScore(score2);
        assertEquals("2: 20\n1: 10\n", leaderboard1.toString());

        Leaderboard leaderboard2 = new Leaderboard();
        leaderboard2.addScore(score2);
        leaderboard2.addScore(score1);
        assertEquals("2: 20\n1: 10\n", leaderboard2.toString());
    }

    @Test
    public void testLeaderboardLimit() {
        List<Score> scores = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            scores.add(new Score(String.format("player %d", i), i * 10));
        }
        Leaderboard leaderboard = new Leaderboard(scores);
        leaderboard.addScore(new Score("player 11", 100));
        assertEquals(10, leaderboard.getScores().size());
        assertEquals(20,leaderboard.getScores().getLast().getScore());
    }
}
