package group1.unisim.test;

import group1.unisim.Timer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TimerTest {
    // FR_TIMER_PAUSE
    @Test
    public void testTimerPause() {
        float startAmount = 30.0f;
        Timer timer = new Timer(startAmount);
        assertTrue(timer.isPaused());
        timer.update(1.0f);
        assertTrue(timer.isPaused());
        assertEquals(startAmount, timer.getTimeRemaining());

        timer.togglePause();
        assertFalse(timer.isPaused());

        timer.update(1.0f);
        assertEquals(29.0f, timer.getTimeRemaining());
    }
}
