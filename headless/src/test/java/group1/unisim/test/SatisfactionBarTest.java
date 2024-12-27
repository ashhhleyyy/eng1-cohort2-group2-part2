package group1.unisim.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import group1.unisim.Paths;
import group1.unisim.SatisfactionBar;
import group1.unisim.thought.Thought;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class SatisfactionBarTest extends HeadlessGdxTest {
    @Test
    public void testSatisfactionBar() {
        Stage stage = mock(Stage.class);
        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        SatisfactionBar satisfactionBar = new SatisfactionBar(skin, stage);
        assertEquals(50, satisfactionBar.getScore());

        satisfactionBar.updateScore();
        assertEquals(50, satisfactionBar.getScore());

        Thought positiveThought = new Thought("positive thought", "description", 10);
        Thought negativeThought = new Thought("positive thought", "description", -10);

        satisfactionBar.setThought("test thought", positiveThought);
        satisfactionBar.updateScore();
        assertEquals(50.15, satisfactionBar.getScore(), 0.0001);
        satisfactionBar.updateScore();
        assertEquals(50.30, satisfactionBar.getScore(), 0.0001);
        for (int i = 0; i < 1000; i++) {
            satisfactionBar.updateScore();
        }
        assertEquals(60, satisfactionBar.getScore(), 0.0001);


        satisfactionBar.setThought("negative thought", negativeThought);
        satisfactionBar.updateScore();
        assertEquals(59.85, satisfactionBar.getScore(), 0.0001);
        for (int i = 0; i < 1000; i++) {
            satisfactionBar.updateScore();
        }
        assertEquals(50, satisfactionBar.getScore(), 0.0001);

        assertNotNull(satisfactionBar.getThought("negative thought"));
        assertEquals(2, satisfactionBar.getAllThoughts().size());

        satisfactionBar.removeThought("test thought");
        for (int i = 0; i < 1000; i++) {
            satisfactionBar.updateScore();
        }
        assertEquals(40, satisfactionBar.getScore(), 0.0001);

        satisfactionBar.setThought("negative thought2", negativeThought);
        for (int i = 0; i < 1000; i++) {
            satisfactionBar.updateScore();
        }
        assertEquals(30, satisfactionBar.getScore(), 0.0001);


        Thought amazingThought = new Thought("amazing thought", "description", 60);
        satisfactionBar.setThought("amazing thought", amazingThought);
        for (int i = 0; i < 1000; i++) {
            satisfactionBar.updateScore();
        }
        assertEquals(90, satisfactionBar.getScore(), 0.0001);

        SatisfactionBar satisfactionBar2 = new SatisfactionBar(skin, null);
        assertEquals(50, satisfactionBar2.getScore(), 0.0001);
    }
}
