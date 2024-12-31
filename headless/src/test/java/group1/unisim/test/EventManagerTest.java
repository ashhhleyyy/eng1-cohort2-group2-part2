package group1.unisim.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import group1.unisim.ContentLoader;
import group1.unisim.Paths;
import group1.unisim.SatisfactionBar;
import group1.unisim.Timer;
import group1.unisim.achievement.AchievementsManager;
import group1.unisim.building.Service;
import group1.unisim.events.Event;
import group1.unisim.events.EventManager;
import group1.unisim.thought.Thoughts;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;


public class EventManagerTest extends HeadlessGdxTest {

    @Test
    public void testConstruction(){
        Stage stage = mock(Stage.class);
        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        Timer timer = new Timer();
        SatisfactionBar satisfactionBar = new SatisfactionBar(skin,stage);
        ContentLoader contentLoader = new ContentLoader();
        contentLoader.load();
        AchievementsManager achievementsManager = new AchievementsManager(false);
        EventManager eventManager = new EventManager(timer,satisfactionBar,contentLoader,achievementsManager);
        Map<Service, Integer> services = new HashMap<>();
        for (Service service: Service.values()){
            services.put(service,0);
        }

        eventManager.updateServices(services,0);
        assertEquals(contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS0),satisfactionBar.getThought("construction"));

        eventManager.updateServices(services,1);
        assertEquals(contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS1),satisfactionBar.getThought("construction"));
    }

    @Test
    public void testMultiConstruction() {
        Stage stage = mock(Stage.class);
        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        Timer timer = new Timer();
        SatisfactionBar satisfactionBar = new SatisfactionBar(skin,stage);
        ContentLoader contentLoader = new ContentLoader();
        contentLoader.load();
        AchievementsManager achievementsManager = new AchievementsManager(false);
        EventManager eventManager = new EventManager(timer,satisfactionBar,contentLoader,achievementsManager);
        Map<Service, Integer> services = new HashMap<>();
        for (Service service: Service.values()){
            services.put(service,0);
        }

        eventManager.updateServices(services,2);
        assertEquals(contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS2),satisfactionBar.getThought("construction"));
        eventManager.updateServices(services,3);
        assertEquals(contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS3),satisfactionBar.getThought("construction"));

    }

    @Test
    public void testRandomEvent() {
        Stage stage = mock(Stage.class);
        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        Timer timer = new Timer();
        timer.togglePause();
        SatisfactionBar satisfactionBar = new SatisfactionBar(skin,stage);
        ContentLoader contentLoader = new ContentLoader();
        contentLoader.load();
        AchievementsManager achievementsManager = new AchievementsManager(false);
        EventManager eventManager = new EventManager(timer,satisfactionBar,contentLoader,achievementsManager);

        eventManager.checkEvents();
        assertEquals(0,eventManager.getCurrentEvents().size());

        timer.update(40);
        eventManager.checkEvents();
        assertEquals(0,eventManager.getCurrentEvents().size());

        timer.update(10.1F);
        eventManager.checkEvents();
        assertEquals(1,eventManager.getCurrentEvents().size());

        timer.update(0.1F);
        eventManager.checkEvents();
        assertEquals(1,eventManager.getCurrentEvents().size());

        Event event = eventManager.getCurrentEvents().get(0);
        event.update(event.getDuration());
        assertEquals(0,eventManager.getCurrentEvents().size());
    }

}
