package group1.unisim;

public class Leaderboard {
    private Score[] scores;

    public Leaderboard() {

    }

    public Leaderboard(Score[] scores) {
        this.scores = scores;
    }

    public String toString() {
        String res = "";
        for (Score score : scores){
            res += score.toString() + "\n";
        }
        return res;
    }
}
