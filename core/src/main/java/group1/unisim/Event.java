package group1.unisim;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;

public class Event {
    private float duration;
    private String associatedThought;
    private String index;
    private HashMap<String, Event> events;

    public Event(String _associatedThought, int _duration, String _index, HashMap<String, Event> _events){
        duration = _duration;
        associatedThought = _associatedThought;
        index = _index;
        events = _events;
    }

    public void Update(){
        if(duration > 0){
            duration -= Gdx.graphics.getDeltaTime();
            return;
        }
        this.End();
    }

    public void End(){
        events.remove(index);
    }

    public String getAssociatedThought(){
        return associatedThought;
    }

}
