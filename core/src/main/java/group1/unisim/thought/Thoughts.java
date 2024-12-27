package group1.unisim.thought;

import java.util.ArrayList;
import java.util.List;

public class Thoughts {
    private static final List<String> ALL_THOUGHTS = new ArrayList<>();


    public static final String PERFECT_BUILDING_LEVEL = thought("perfectBuildingLevel");
    public static final String ONE_OF_EACH_BUILDING = thought("oneOfEachBuilding");
    public static final String BUILDING_MISSING = thought("buildingMissing");
    public static final String ACTIVE_CONSTRUCTIONS1 = thought("activeConstructions1");
    public static final String ACTIVE_CONSTRUCTIONS2 = thought("activeConstructions2");
    public static final String ACTIVE_CONSTRUCTIONS3 = thought("activeConstructions3");
    public static final String ACTIVE_CONSTRUCTIONS0 = thought("activeConstructions0");

    private Thoughts() {
    }

    public static List<String> getAllThoughts() {
        return ALL_THOUGHTS;
    }

    private static String thought(String thought) {
        ALL_THOUGHTS.add(thought);
        return thought;
    }
}
