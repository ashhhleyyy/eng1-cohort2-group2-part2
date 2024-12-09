package group1.unisim.test;

import group1.unisim.ContentLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ContentLoaderTest extends HeadlessGdxTest {
    @Test
    public void testLoad() {
        ContentLoader loader = new ContentLoader();
        loader.load();
        Assertions.assertNotEquals(0, loader.allBuildings().size());
        Assertions.assertNotEquals(0, loader.allThoughts().size());
        loader.dispose();
    }
}
