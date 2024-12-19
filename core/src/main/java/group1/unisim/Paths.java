package group1.unisim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Paths {
    public static final String LEADERBOARD_JSON = ".unisim-leaderboards.json";
    public static final String ACHIEVEMENTS_JSON = ".unisim-achievements.json";
    private static final List<String> ALL_ASSETS = new ArrayList<>();
    public static final String BUILDINGS_JSON = asset("buildings.json");
    public static final String THOUGHTS_JSON = asset("thoughts.json");
    public static final String UI_SKIN = asset("ui/uiskin.json");
    public static final String TOOLBAR = asset("toolbar.png");
    public static final String MAP_TEXTURE = asset("mapTexture.png");
    public static final String SETTINGS_ICON = asset("settingsIcon.png");
    public static final String BUILD_ICON = asset("buildIcon.png");
    public static final String PAUSE = asset("pause.png");
    public static final String PLAY = asset("play.png");
    public static final String END_SCREEN = asset("endScreen.png");
    public static final String ACCOMMODATION_TEXTURE = asset("accom.png");
    public static final String THOUGHT_BACKGROUND = asset("thoughtBackground.png");
    public static final String EVENT_BACKGROUND = asset("eventBackground.png");
    public static final String BUILD_SELECT_BACKGROUND = asset("buildSelectBackground.png");

    private static String asset(String asset) {
        ALL_ASSETS.add(asset);
        return asset;
    }

    public static List<String> getAllAssets() {
        return Collections.unmodifiableList(ALL_ASSETS);
    }
}
