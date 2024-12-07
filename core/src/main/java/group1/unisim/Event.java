package group1.unisim;

import com.badlogic.gdx.Gdx;

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

    public void update() {
        if (duration <= 0) {
            this.end();
            return;
        }
        duration -= Gdx.graphics.getDeltaTime();
    }

    public void end() {
        events.remove(index);
    }

    public String getAssociatedThought() {
        return associatedThought;
    }
}
