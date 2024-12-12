package group1.unisim.test;

import com.badlogic.gdx.Gdx;
import group1.unisim.Paths;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssetsTest extends HeadlessGdxTest {
    @Test
    public void ensureAssets() {
        for (String asset : Paths.getAllAssets()) {
            assertTrue(Gdx.files.internal(asset).exists(), "the asset `" + asset + "` must exist!");
        }
    }
}
