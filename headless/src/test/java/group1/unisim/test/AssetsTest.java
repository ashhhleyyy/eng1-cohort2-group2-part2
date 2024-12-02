package group1.unisim.test;

import com.badlogic.gdx.Gdx;
import group1.unisim.Assets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssetsTest extends HeadlessGdxTest {
    @Test
    public void ensureAssets() {
        for (String asset : Assets.getAllAssets()) {
            assertTrue(Gdx.files.internal(asset).exists(), "the asset `" + asset + "` must exist!");
        }
    }
}
