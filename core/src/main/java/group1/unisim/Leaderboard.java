package group1.unisim;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Leaderboard {
    private List<Score> scores = new ArrayList<>();

    public Leaderboard() {

    }

    public Leaderboard(List<Score> scores) {
        this.scores = scores;
    }

    public String toString() {
        String res = "";
        for (Score score : scores.reversed()) {
            res += score.toString() + "\n";
        }
        return res;
    }

    public void addScore(Score score) {
        this.scores.add(score);
        this.scores.sort(Comparator.comparing(Score::getScore));
        if (this.scores.size() > 10) {
            this.scores.remove(0);
        }
    }
}
