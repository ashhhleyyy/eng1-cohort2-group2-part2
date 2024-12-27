package group1.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import group1.unisim.building.Building;
import group1.unisim.building.Service;
import group1.unisim.leaderboard.Leaderboard;
import group1.unisim.thought.Thought;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ContentLoader {
    public static ContentLoader singleton;
    public final AssetManager assetManager;
    private final Map<Service, Map<Integer, String>> serviceThoughts = new HashMap<>();
    private HashMap<String, Building> buildings;
    private HashMap<String, Thought> thoughts;
    private Leaderboard leaderboard;


    public ContentLoader() {
        singleton = this;
        assetManager = new AssetManager();
    }

    @SuppressWarnings("unchecked")
    public void load() {
        Json json = new Json();

        try {
            this.thoughts = (HashMap<String, Thought>) json.fromJson(HashMap.class, Thought.class, Gdx.files.internal(Paths.THOUGHTS_JSON));
        } catch (Exception e) {
            Gdx.app.error("LoadThoughts", e.getMessage());
            throw e;
        }
        for (Service service : Service.values()) {
            serviceThoughts.put(service, new HashMap<>());
        }
        for (Map.Entry<String, Thought> entry : this.thoughts.entrySet()) {
            Thought thought = entry.getValue();
            if (thought.getService() == null) {
                continue;
            }
            serviceThoughts.get(thought.getService()).put(thought.getDiff(), entry.getKey());
        }

        try {
            this.buildings = (HashMap<String, Building>) json.fromJson(HashMap.class, Building.class, Gdx.files.internal(Paths.BUILDINGS_JSON));
        } catch (Exception e) {
            Gdx.app.error("LoadBuildings", e.getMessage());
            throw e;
        }

        for (Building building : allBuildings()) {
            assetManager.load(building.getTexture(), Texture.class);
        }
        try {
            leaderboard = json.fromJson(Leaderboard.class, Gdx.files.external(Paths.LEADERBOARD_JSON));
        } catch (SerializationException e) {
            leaderboard = new Leaderboard();
        }

        assetManager.finishLoading();
    }

    public Thought getThought(String key) {
        return thoughts.get(key);
    }

    public String getServiceThought(Service service, int diff) {
        Map<Integer, String> serviceThought = serviceThoughts.get(service);
        if (serviceThought == null) {
            return null;
        }
        return serviceThought.get(diff);
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
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

    public void saveLeaderboard(Leaderboard leaderboard) {
        Json json = new Json();
        json.toJson(leaderboard, Gdx.files.external(Paths.LEADERBOARD_JSON));
    }

    public void dispose() {
        singleton = null;
        assetManager.dispose();
    }
}
