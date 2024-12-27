package group1.unisim.test;

import group1.unisim.thought.Thought;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThoughtTest {
    @Test
    public void testThought() {
        Thought thought = new Thought("test thought", "test description", 5);
        assertEquals("test thought", thought.getTitle());
        assertEquals("test description", thought.getDescription());
        assertEquals(5, thought.getModification());
    }
}
