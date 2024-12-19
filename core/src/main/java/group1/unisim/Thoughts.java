package group1.unisim;

import java.util.ArrayList;
import java.util.List;

public class Thoughts {
    private static final List<String> ALL_THOUGHTS = new ArrayList<>();

    public static final String UNDER_CROWDING = thought("underCrowding");
    public static final String OVERCROWDING = thought("overCrowding");
    public static final String NEUTRAL_CROWDING = thought("neutralCrowding");
    public static final String UNDER_TEACHING = thought("underTeaching");
    public static final String OVER_TEACHING = thought("overTeaching");
    public static final String UNDER_RECREATION = thought("underRecreation");
    public static final String OVER_RECREATION = thought("overRecreation");
    public static final String PERFECT_BUILDING_LEVEL = thought("perfectBuildingLevel");
    public static final String ONE_OF_EACH_BUILDING = thought("oneOfEachBuilding");
    public static final String BUILDING_MISSING = thought("buildingMissing");
    public static final String ACTIVE_CONSTRUCTIONS1 = thought("activeConstructions1");
    public static final String ACTIVE_CONSTRUCTIONS2 = thought("activeConstructions2");
    public static final String ACTIVE_CONSTRUCTIONS3 = thought("activeConstructions3");
    public static final String ACTIVE_CONSTRUCTIONS0 = thought("activeConstructions0");

    private static String thought(String thought) {
        ALL_THOUGHTS.add(thought);
        return thought;
    }

    public static List<String> getAllThoughts() {
        return ALL_THOUGHTS;
    }

    private Thoughts() {
    }
}
