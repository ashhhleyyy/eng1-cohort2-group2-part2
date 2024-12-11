package group1.unisim.achievement;

import group1.unisim.Service;

import java.util.*;
import java.util.stream.Collectors;

public class AchievementsManager {
    private final List<Achievement> incomplete;
    private final List<Achievement> complete;

    public AchievementsManager(List<String> completedIds) {
        this.incomplete = new ArrayList<>();
        this.complete = new ArrayList<>();
        incomplete.addAll(Achievements.ALL_ACHIEVEMENTS.values());
        List<Achievement> completed = completedIds.stream().map(id -> {
            Achievement a = Achievements.ALL_ACHIEVEMENTS.get(id);
            if (a == null) System.err.println("Warning: unknown achievement `" + id + '`');
            return a;
        }).filter(Objects::nonNull).toList();
        this.complete.addAll(completed);
        this.incomplete.removeAll(completed);
    }

    public void onServiceValueChange(Service service, int newValue) {
        for (Achievement achievement : this.incomplete) {
            achievement.onServiceValueChange(service, newValue);
        }
        this.checkCompletion();
    }

    public void onSatisfactionChange(float newSatisfaction) {
        for (Achievement achievement : this.incomplete) {
            achievement.onSatisfactionChange(newSatisfaction);
        }
        this.checkCompletion();
    }

    private void checkCompletion() {
        List<Achievement> completed = new ArrayList<>();
        for (Achievement achievement : this.incomplete) {
            if (achievement.isComplete()) {
                completed.add(achievement);
            }
        }
        this.incomplete.removeAll(completed);
        this.complete.addAll(completed);
    }

    public List<Achievement> getComplete() {
        return complete;
    }
}
