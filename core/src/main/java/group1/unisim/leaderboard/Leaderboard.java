package group1.unisim.leaderboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// added leaderboard class
public class Leaderboard {
    private List<Score> scores = new ArrayList<>();

    public Leaderboard() {
    }

    public Leaderboard(List<Score> scores) {
        this.scores = scores;
    }

    public List<Score> getScores() {
        return scores;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Score score : scores) {
            res.append(score.toString()).append("\n");
        }
        return res.toString();
    }

    public void addScore(Score score) {
        this.scores.add(score);
        this.scores.sort(Comparator.comparing(Score::getScore).reversed());
        if (this.scores.size() > 10) {
            this.scores.remove(10);
        }
    }
}
