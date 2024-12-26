package group1.unisim.test;

import group1.unisim.Building.Service;
import group1.unisim.Event;
import group1.unisim.Main;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EventTest extends HeadlessGdxTest{
    @Test
    public void testEvent() {
        Main main = mock(Main.class);
        Event event = new Event("test_thought", 10, Service.Recreation, 10,main );
        main.addEvent(event);
        System.out.println(main.getCurrentEvents());

        assertEquals("test_thought", event.getDescription());
        assertEquals(10.0f, event.getDuration());
        assertEquals(Service.Recreation,event.getService());
        assertEquals(10,event.getRequirement());

        event.update(5.0f);
        //TODO make this work
        //assertTrue(main.getCurrentEvents().contains(event), "event has not yet ended");
        assertEquals(5.0f, event.getDuration());

        event.update(5.0f);
        //assertFalse(main.getCurrentEvents().contains(event), "event has ended");
        assertEquals(0.0f, event.getDuration());

        event.update(5.0f);
        assertEquals(0.0f, event.getDuration());
    }
}
