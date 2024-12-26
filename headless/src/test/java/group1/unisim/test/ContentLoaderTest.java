package group1.unisim.test;

import com.badlogic.gdx.Gdx;
import group1.unisim.Building.Building;
import group1.unisim.ContentLoader;
import group1.unisim.Thought.Thoughts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContentLoaderTest extends HeadlessGdxTest {
    @Test
    public void testLoad() {
        ContentLoader loader = new ContentLoader();
        loader.load();
        assertNotEquals(0, loader.allBuildings().size());
        assertNotEquals(0, loader.allThoughts().size());

        for (Building building : loader.allBuildings()) {
            Assertions.assertTrue(Gdx.files.internal(building.getTexture()).exists());
        }

        assertNotNull(loader.getThought("perfectBuildingLevel"));
        assertNotNull(loader.getThought("oneOfEachBuilding"));
        assertNotNull(loader.getThought("buildingMissing"));
        assertNotNull(loader.getThought("activeConstructions1"));
        assertNotNull(loader.getThought("activeConstructions2"));
        assertNotNull(loader.getThought("activeConstructions3"));
        assertNotNull(loader.getThought("activeConstructions0"));

        assertNotNull(loader.getLeaderboard());

        loader.saveLeaderboard(loader.getLeaderboard());

        for (String thought : Thoughts.getAllThoughts()) {
            Assertions.assertNotNull(loader.getThought(thought));
        }

        assertNotNull(loader.getLeaderboard());

        loader.saveLeaderboard(loader.getLeaderboard());

        loader.dispose();
    }
}
