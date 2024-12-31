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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;


public class EventManagerTest extends HeadlessGdxTest {

    @Test
    public void testUpdate() {
        Stage stage = mock(Stage.class);
        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        Timer timer = new Timer();
        SatisfactionBar satisfactionBar = new SatisfactionBar(skin, stage);
        ContentLoader contentLoader = new ContentLoader();
        contentLoader.load();
        AchievementsManager achievementsManager = new AchievementsManager(false);
        EventManager eventManager = new EventManager(timer, satisfactionBar, contentLoader, achievementsManager);
        eventManager.initUi(stage, skin);
        eventManager.update(10);

    }

    @Test
    public void testConstruction() {
        Stage stage = mock(Stage.class);
        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        Timer timer = new Timer();
        SatisfactionBar satisfactionBar = new SatisfactionBar(skin, stage);
        ContentLoader contentLoader = new ContentLoader();
        contentLoader.load();
        AchievementsManager achievementsManager = new AchievementsManager(false);
        EventManager eventManager = new EventManager(timer, satisfactionBar, contentLoader, achievementsManager);
        Map<Service, Integer> services = new HashMap<>();
        for (Service service : Service.values()) {
            services.put(service, 0);
        }

        eventManager.updateServices(services, 0);
        assertEquals(contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS0), satisfactionBar.getThought("construction"));

        eventManager.updateServices(services, 1);
        assertEquals(contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS1), satisfactionBar.getThought("construction"));
    }

    @Test
    public void testMultiConstruction() {
        Stage stage = mock(Stage.class);
        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        Timer timer = new Timer();
        SatisfactionBar satisfactionBar = new SatisfactionBar(skin, stage);
        ContentLoader contentLoader = new ContentLoader();
        contentLoader.load();
        AchievementsManager achievementsManager = new AchievementsManager(false);
        EventManager eventManager = new EventManager(timer, satisfactionBar, contentLoader, achievementsManager);
        Map<Service, Integer> services = new HashMap<>();
        for (Service service : Service.values()) {
            services.put(service, 0);
        }

        eventManager.updateServices(services, 2);
        assertEquals(contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS2), satisfactionBar.getThought("construction"));
        eventManager.updateServices(services, 3);
        assertEquals(contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS3), satisfactionBar.getThought("construction"));

    }

    @Test
    public void testRandomEvent() {
        Stage stage = mock(Stage.class);
        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        Timer timer = new Timer();
        timer.togglePause();
        SatisfactionBar satisfactionBar = new SatisfactionBar(skin, stage);
        ContentLoader contentLoader = new ContentLoader();
        contentLoader.load();
        AchievementsManager achievementsManager = new AchievementsManager(false);
        EventManager eventManager = new EventManager(timer, satisfactionBar, contentLoader, achievementsManager);
        eventManager.initUi(stage, skin);


        eventManager.update(0);
        assertEquals(0, eventManager.getCurrentEvents().size());

        timer.update(40);
        eventManager.update(40);
        assertEquals(0, eventManager.getCurrentEvents().size());

        timer.update(10.1F);
        eventManager.update(10.1F);
        assertEquals(1, eventManager.getCurrentEvents().size());

        timer.update(0.1F);
        eventManager.update(0.1F);
        assertEquals(1, eventManager.getCurrentEvents().size());

        eventManager.update(eventManager.getCurrentEvents().get(0).getDuration());
        assertEquals(0, eventManager.getCurrentEvents().size());
    }

    @Test
    public void testOneOfEach() {
        Stage stage = mock(Stage.class);
        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        Timer timer = new Timer();
        timer.togglePause();
        SatisfactionBar satisfactionBar = new SatisfactionBar(skin, stage);
        ContentLoader contentLoader = new ContentLoader();
        contentLoader.load();
        AchievementsManager achievementsManager = new AchievementsManager(false);
        EventManager eventManager = new EventManager(timer, satisfactionBar, contentLoader, achievementsManager);

        assertNull(satisfactionBar.getThought("one_of_each"));

        HashMap<Service, Integer> services = new HashMap<>();
        for (Service service : Service.values()) {
            services.put(service, 1);
        }
        eventManager.updateServices(services, 0);
        assertEquals(contentLoader.getThought(Thoughts.ONE_OF_EACH_BUILDING), satisfactionBar.getThought("one_of_each"));

        services.put(Service.Accommodation, 2);
        eventManager.updateServices(services, 0);
        assertNull(satisfactionBar.getThought("one_of_each"));

        services.put(Service.Accommodation, 1);
        eventManager.updateServices(services, 0);
        assertEquals(contentLoader.getThought(Thoughts.ONE_OF_EACH_BUILDING), satisfactionBar.getThought("one_of_each"));

        services.put(Service.Accommodation, 0);
        eventManager.updateServices(services, 0);
        assertNull(satisfactionBar.getThought("one_of_each"));
    }

    @Test
    public void testBuildingMissing() {
        Stage stage = mock(Stage.class);
        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        Timer timer = new Timer();
        timer.togglePause();
        SatisfactionBar satisfactionBar = new SatisfactionBar(skin, stage);
        ContentLoader contentLoader = new ContentLoader();
        contentLoader.load();
        AchievementsManager achievementsManager = new AchievementsManager(false);
        EventManager eventManager = new EventManager(timer, satisfactionBar, contentLoader, achievementsManager);

        assertNull(satisfactionBar.getThought("missing_building"));

        HashMap<Service, Integer> services = new HashMap<>();
        for (Service service : Service.values()) {
            services.put(service, 1);
        }
        eventManager.updateServices(services, 0);
        assertNull(satisfactionBar.getThought("missing_building"));

        services.put(Service.Accommodation, 0);
        eventManager.updateServices(services, 0);
        assertEquals(contentLoader.getThought(Thoughts.BUILDING_MISSING), satisfactionBar.getThought("missing_building"));
    }

    @Test
    public void testPerfectLevel() {
        Stage stage = mock(Stage.class);
        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        Timer timer = new Timer();
        timer.togglePause();
        SatisfactionBar satisfactionBar = new SatisfactionBar(skin, stage);
        ContentLoader contentLoader = new ContentLoader();
        contentLoader.load();
        AchievementsManager achievementsManager = new AchievementsManager(false);
        EventManager eventManager = new EventManager(timer, satisfactionBar, contentLoader, achievementsManager);
        eventManager.initUi(stage, skin);

        assertNull(satisfactionBar.getThought("building_count"));
        HashMap<Service, Integer> services = new HashMap<>();
        for (Service service : Service.values()) {
            services.put(service, 1);
        }
        eventManager.updateServices(services, 0);
        assertEquals(contentLoader.getThought(Thoughts.PERFECT_BUILDING_LEVEL), satisfactionBar.getThought("building_count"));

        timer.update(55);
        eventManager.update(55);


        for (Service service : Service.values()) {
            services.put(service, eventManager.getBuildingRequirements().get(service));
        }
        eventManager.updateServices(services, 0);
        assertEquals(contentLoader.getThought(Thoughts.PERFECT_BUILDING_LEVEL), satisfactionBar.getThought("building_count"));

        for (Service service : Service.values()) {
            services.put(service, 1);
        }
        eventManager.updateServices(services, 0);
        assertNull(satisfactionBar.getThought("missing_building"));
    }

    @Test
    public void testEventDedup() {
        Stage stage = mock(Stage.class);
        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        Timer timer = new Timer();
        timer.togglePause();
        SatisfactionBar satisfactionBar = new SatisfactionBar(skin, stage);
        ContentLoader contentLoader = new ContentLoader();
        contentLoader.load();
        AchievementsManager achievementsManager = new AchievementsManager(false);
        EventManager eventManager = new EventManager(timer, satisfactionBar, contentLoader, achievementsManager);

        assertEquals(0, eventManager.getCurrentEvents().size());
        eventManager.addEvent(new Event("test 1", 100, Service.Accommodation, 2, eventManager));
        assertEquals(1, eventManager.getCurrentEvents().size());
        eventManager.addEvent(new Event("test 2", 50, Service.Recreation, 1, eventManager));
        assertEquals(2, eventManager.getCurrentEvents().size());
        eventManager.addEvent(new Event("test 3", 60, Service.Accommodation, 3, eventManager));
        assertEquals(2, eventManager.getCurrentEvents().size());
    }

}
