package group1.unisim.test;

import group1.unisim.Event;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    @Test
    public void testEvent() {
        HashMap<String, Event> events = new HashMap<>();
        Event event = new Event("test_thought", 10, "1", events);
        events.put("1", event);
        assertEquals("test_thought", event.getAssociatedThought());
        assertEquals(10.0f, event.getDuration());

        event.update(5.0f);
        assertTrue(events.containsValue(event), "event has not yet ended");
        assertEquals(5.0f, event.getDuration());

        event.update(5.0f);
        assertFalse(events.containsValue(event), "event has ended");
        assertEquals(0.0f, event.getDuration());

        event.update(5.0f);
        assertEquals(0.0f, event.getDuration());
    }
}
