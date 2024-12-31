package group1.unisim.test;

import group1.unisim.building.Service;
import group1.unisim.thought.Thought;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ThoughtTest {
    @Test
    public void testBasicThought() {
        Thought thought = new Thought("test thought", "test description", 5);
        assertEquals("test thought", thought.getTitle());
        assertEquals("test description", thought.getDescription());
        assertEquals(5, thought.getModification());
        assertEquals("test thought: test description",thought.toString());
        assertEquals(0,thought.getDiff());
        assertNull(thought.getService());
    }

    @Test
    public void testServiceThought() {
        Thought thought = new Thought("test thought", "test description", 5, Service.Recreation,5);
        assertEquals("test thought", thought.getTitle());
        assertEquals("test description", thought.getDescription());
        assertEquals(5, thought.getModification());
        assertEquals(5, thought.getDiff());
        assertEquals(Service.Recreation,thought.getService());
    }
}
