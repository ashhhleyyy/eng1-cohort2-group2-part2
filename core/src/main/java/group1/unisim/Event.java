package group1.unisim;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;

public class Event {
    private String associatedThought;
    private float duration;
    private String index;
    private HashMap<String, Event> events;

    public Event(String associatedThought, int duration, String index, HashMap<String, Event> events) {
        this.associatedThought = associatedThought;
        this.duration = duration;
        this.index = index;
        this.events = events;
    }

    public void update() {
        if (duration > 0) {
            duration -= Gdx.graphics.getDeltaTime();
            return;
        }
        this.End();
    }

    public void End() {
        events.remove(index);
    }

    public String getAssociatedThought() {
        return associatedThought;
    }
}
