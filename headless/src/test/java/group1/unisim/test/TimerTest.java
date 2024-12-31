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
        assertFalse(timer.isTimePassing());
        timer.update(1.0f);
        assertTrue(timer.isPaused());
        assertFalse(timer.isTimePassing());
        assertEquals(startAmount, timer.getTimeRemaining());

        timer.togglePause();
        assertFalse(timer.isPaused());
        assertTrue(timer.isTimePassing());

        timer.update(1.0f);
        assertEquals(29.0f, timer.getTimeRemaining());

        timer.togglePause();
        assertTrue(timer.isPaused());
        assertFalse(timer.isTimePassing());
        timer.update(1.0f);
        assertEquals(29.0f, timer.getTimeRemaining());
    }

    @Test
    public void testTimerLength() {
        Timer timer = new Timer();
        assertEquals(Timer.getStartTime(),timer.getTimeRemaining());
    }

    @Test
    public void testTimerDisplay() {
        Timer timer = new Timer(300);
        assertEquals("5:00",timer.toString());
    }

    @Test
    public void testEnd(){
        float startAmount = 30.0f;
        Timer timer = new Timer(startAmount);
        assertFalse(timer.isGameEnd());
        timer.togglePause();
        assertFalse(timer.isGameEnd());
        timer.update(29);
        assertFalse(timer.isGameEnd());
        timer.update(2);
        assertTrue(timer.isGameEnd());
        timer.togglePause();
        assertFalse(timer.isPaused());
    }

}
