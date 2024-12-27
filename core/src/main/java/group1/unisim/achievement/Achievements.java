package group1.unisim.achievement;

import group1.unisim.building.Service;

import java.util.HashMap;

public class Achievements {
    public static final HashMap<String, Achievement> ALL_ACHIEVEMENTS = new HashMap<>();
    public static final Achievement ZERO_SATISFACTION = register(new SatisfactionAchievement("zero_satisfaction", "Oops", "Wow you're faster than Russel Group unis at losing money", 5f, false));
    public static final Achievement FULL_SATISFACTION = register(new SatisfactionAchievement("full_satisfaction", "Money Money Money", "With all that satisfaction you could build a student centre and actually succeed", 95f, true));

    public static final Achievement ALL_THE_ACCOMODATION = register(new ServiceAchievement("all_the_accomodation", "That's a whole lotta asbestos", "Fill all the slots with accomodation", Service.Accommodation, 7));

    private Achievements() {
    }

    public static Achievement register(Achievement achievement) {
        ALL_ACHIEVEMENTS.put(achievement.getId(), achievement);
        return achievement;
    }
}
