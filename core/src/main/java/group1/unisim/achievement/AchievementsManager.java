package group1.unisim.achievement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import group1.unisim.Paths;
import group1.unisim.Building.Service;

import java.util.*;

public class AchievementsManager {
    private final List<Achievement> incomplete;
    private final List<Achievement> complete;

    public AchievementsManager() {
        this(true);
    }

    public AchievementsManager(boolean loadFromJson) {
        List<String> completedIds = Collections.emptyList();
        if (loadFromJson) completedIds = this.loadAchievements();
        this.incomplete = new ArrayList<>();
        this.complete = new ArrayList<>();
        incomplete.addAll(Achievements.ALL_ACHIEVEMENTS.values());
        List<Achievement> completed = completedIds.stream().map(id -> {
            Achievement a = Achievements.ALL_ACHIEVEMENTS.get(id);
            if (a == null) System.err.println("Warning: unknown achievement `" + id + '`');
            return a;
        }).filter(Objects::nonNull).toList();
        // mark achievements as completed
        completed.forEach(Achievement::complete);
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

    private List<String> loadAchievements() {
        Json json = new Json();
        try {
            //noinspection unchecked
            return (List<String>) json.fromJson(List.class, Gdx.files.external(Paths.ACHIEVEMENTS_JSON));
        } catch (SerializationException e) {
            return Collections.emptyList();
        }
    }

    public void saveAchievements() {
        this.checkCompletion();
        List<String> completedAchievements = this.complete.stream().map(Achievement::getId).toList();
        Json json = new Json();
        json.toJson(completedAchievements, Gdx.files.external(Paths.ACHIEVEMENTS_JSON));
    }

    public String formatCompleted() {
        StringBuilder builder = new StringBuilder();
        // Ensure the ordering is deterministic
        this.complete.sort(Comparator.comparing(Achievement::getId));
        for (var achievement : this.complete) {
            builder.append(achievement.toString()).append("\n\n");
        }
        return builder.toString();
    }
}
