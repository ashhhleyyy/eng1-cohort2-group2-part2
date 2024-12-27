package group1.unisim.test;

import group1.unisim.ContentLoader;
import group1.unisim.SatisfactionBar;
import group1.unisim.Timer;
import group1.unisim.achievement.AchievementsManager;
import group1.unisim.building.Service;
import group1.unisim.events.Event;
import group1.unisim.events.EventManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EventTest extends HeadlessGdxTest {
    @Test
    public void testEvent() {
        EventManager eventManager = new EventManager(new Timer(), mock(SatisfactionBar.class), new ContentLoader(), new AchievementsManager(false));
        Event event = new Event("test_thought", 10, Service.Recreation, 10, eventManager);
        //TODO make this work
        eventManager.addEvent(event);
        System.out.println(eventManager.getCurrentEvents());

        assertEquals("test_thought", event.getDescription());
        assertEquals(10.0f, event.getDuration());
        assertEquals(Service.Recreation, event.getService());
        assertEquals(10, event.getRequirement());

        event.update(5.0f);
        assertTrue(eventManager.getCurrentEvents().contains(event), "event has not yet ended");
        assertEquals(5.0f, event.getDuration());

        event.update(5.0f);
        assertFalse(eventManager.getCurrentEvents().contains(event), "event has ended");
        assertEquals(0.0f, event.getDuration());

        event.update(5.0f);
        assertEquals(0.0f, event.getDuration());
    }
}
