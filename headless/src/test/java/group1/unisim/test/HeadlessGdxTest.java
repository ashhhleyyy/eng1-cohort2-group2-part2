package group1.unisim.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import group1.unisim.headless.HeadlessLauncher;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public class HeadlessGdxTest {
    @BeforeEach
    public void setup() {
        Gdx.gl = Gdx.gl20 = mock(GL20.class);
        HeadlessLauncher.main(new String[0]);
    }
}
