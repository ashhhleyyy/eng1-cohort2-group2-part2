package group1.unisim.test;

import com.badlogic.gdx.Gdx;
import group1.unisim.ContentLoader;
import group1.unisim.building.Building;
import group1.unisim.building.Service;
import group1.unisim.thought.Thoughts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

        assertNotNull(loader.getLeaderboard());

        for (String thought : Thoughts.getAllThoughts()) {
            Assertions.assertNotNull(loader.getThought(thought));
        }

        assertNotNull(loader.getServiceThought(Service.Accommodation, 0));
        assertNull(loader.getServiceThought(Service.Recreation, 0));


        loader.dispose();
    }

    @Test
    public void testSave() {
        ContentLoader loader = new ContentLoader();
        loader.load();
        loader.saveLeaderboard(loader.getLeaderboard());
        loader.dispose();
    }

}
