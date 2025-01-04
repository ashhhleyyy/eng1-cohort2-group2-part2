package group1.unisim;

// ADDED/CHANGED: moved timer logic into separate class to improve testability
public class Timer {
    private static final float START_TIME = 300;
    private float timeRemaining;
    private boolean paused = true;

    public Timer() {
        this(START_TIME);
    }

    public Timer(float time) {
        timeRemaining = time;

    }

    public static float getStartTime() {
        return START_TIME;
    }

    public void update(float delta) {
        if (isTimePassing()) {
            timeRemaining -= delta;
        }
    }

    public String toString() {
        return String.format("%d:%02d", (((int) timeRemaining) / 60), (((int) timeRemaining) % 60));
    }

    public boolean isGameEnd() {
        return this.timeRemaining < 0;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isTimePassing() {
        return !(isPaused() || isGameEnd());
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }

    public void togglePause() {
        if (this.isGameEnd()) {
            return;
        }
        paused = !paused;
    }
}
