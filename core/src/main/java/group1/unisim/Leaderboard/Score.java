package group1.unisim.Leaderboard;

public class Score {
    private String name;
    private int score;

    public Score() {

    }

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String toString() {
        return String.format("%s: %s", name, score);
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
