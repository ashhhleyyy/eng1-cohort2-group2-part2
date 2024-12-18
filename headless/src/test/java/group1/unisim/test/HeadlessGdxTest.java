package group1.unisim.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public class HeadlessGdxTest {
    @BeforeEach
    public void setup() {
        Gdx.gl = Gdx.gl20 = mock(GL20.class);
        // Constructing our actual application (Main), causes all sorts of issues since it expects a non-headless environment
        new HeadlessApplication(new ApplicationAdapter() {
            @Override
            public void create() {
                super.create();
            }
        }, new HeadlessApplicationConfiguration());
    }
}
