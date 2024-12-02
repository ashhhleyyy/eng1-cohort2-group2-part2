package group1.unisim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Assets {
    private static final List<String> ALL_ASSETS = new ArrayList<>();

    private static String asset(String asset) {
        ALL_ASSETS.add(asset);
        return asset;
    }

    public static final String UI_SKIN = asset("ui/uiskin.json");
    public static final String TOOLBAR = asset("toolbar.png");
    public static final String MAP_TEXTURE = asset("mapTexture.png");
    public static final String SETTINGS_ICON = asset("settingsIcon.png");
    public static final String BUILD_ICON = asset("buildIcon.png");
    public static final String PAUSE = asset("pause.png");
    public static final String PLAY = asset("play.png");

    public static List<String> getAllAssets() {
        return Collections.unmodifiableList(ALL_ASSETS);
    }
}
