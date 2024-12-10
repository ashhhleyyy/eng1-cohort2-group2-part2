package group1.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;

import java.util.Collection;
import java.util.HashMap;

public class ContentLoader {
    public static ContentLoader singleton;
    public final AssetManager assetManager;
    private HashMap<String, Building> buildings;
    private HashMap<String, Thought> thoughts;

    public ContentLoader() {
        singleton = this;
        assetManager = new AssetManager();
    }

    @SuppressWarnings("unchecked")
    public void load() {
        Json json = new Json();

        try {
            this.thoughts = (HashMap<String, Thought>) json.fromJson(HashMap.class, Thought.class, Gdx.files.internal(Assets.THOUGHTS_JSON));
        } catch (Exception e) {
            Gdx.app.error("LoadThoughts", e.getMessage());
            throw e;
        }

        try {
            this.buildings = (HashMap<String, Building>) json.fromJson(HashMap.class, Building.class, Gdx.files.internal(Assets.BUILDINGS_JSON));
        } catch (Exception e) {
            Gdx.app.error("LoadBuildings", e.getMessage());
            throw e;
        }

        for (Building building : allBuildings()) {
            assetManager.load(building.getTexture(), Texture.class);
        }

        assetManager.finishLoading();
    }

    public Thought getThought(String key) {
        return thoughts.get(key);
    }

    public Building getBuilding(String key) {
        return buildings.get(key);
    }

    public Collection<Building> allBuildings() {
        return buildings.values();
    }

    public Collection<Thought> allThoughts() {
        return thoughts.values();
    }

    public Texture getTexture(String path) {
        return assetManager.get(path, Texture.class);
    }

    public void dispose() {
        singleton = null;
        assetManager.dispose();
    }
}
