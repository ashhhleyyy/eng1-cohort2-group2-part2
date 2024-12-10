package group1.unisim;

import java.util.HashMap;

public class Event {
    private final String associatedThought;
    private final String index;
    private final HashMap<String, Event> events;
    private float duration;

    public Event(String associatedThought, int duration, String index, HashMap<String, Event> events) {
        this.associatedThought = associatedThought;
        this.duration = duration;
        this.index = index;
        this.events = events;
    }

    public void update(float deltaTime) {
        if (duration <= 0) return;
        duration -= deltaTime;
        if (duration <= 0) this.end();
    }

    public void end() {
        events.remove(index);
    }

    public String getAssociatedThought() {
        return associatedThought;
    }
}
