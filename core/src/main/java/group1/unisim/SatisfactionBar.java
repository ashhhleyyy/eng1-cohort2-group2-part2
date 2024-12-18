package group1.unisim;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

public class SatisfactionBar extends ProgressBar {
    private static final float SPEED = 0.15f;

    private final float baseValue = 50;
    private final Map<String, Thought> currentThoughts;
    private final Image targetMarker;
    private float satisfactionScore;
    private float target;

    public SatisfactionBar(Skin skin, Stage stage) {
        super(0.0f, 100.0f, 0.1f, false, skin);
        setSize(200, 50);
        resetScore();
        currentThoughts = new HashMap<>();
        targetMarker = new Image(new Texture("triangle.png"));
        setPosition(775, 735);
        targetMarker.setPosition(760, 730);
        calculateTarget();
        if (stage != null) {
            stage.addActor(this);
            stage.addActor(targetMarker);
        }
    }

    private void resetScore() {
        satisfactionScore = baseValue;
    }

    public float getScore() {
        return satisfactionScore;
    }

    public void updateScore() {
        float difference = target - satisfactionScore;
        if (abs(difference) < SatisfactionBar.SPEED) satisfactionScore = target;
        else {
            float direction = (difference < 0) ? -1 : 1;
            satisfactionScore += SatisfactionBar.SPEED * direction;
        }
        setValue(satisfactionScore);
        if (satisfactionScore < 31) {
            setColor(Color.RED);
        } else if (satisfactionScore < 61) {
            setColor(Color.YELLOW);
        } else {
            setColor(Color.GREEN);
        }
    }

    public void setThought(String key, Thought thought) {
        currentThoughts.put(key, thought);
        calculateTarget();
    }

    public Thought getThought(String key) {
        return currentThoughts.get(key);
    }

    public void removeThought(String key) {
        currentThoughts.remove(key);
        calculateTarget();
    }

    private void calculateTarget() {
        target = baseValue;

        for (String key : currentThoughts.keySet()) {
            target += currentThoughts.get(key).getModification();
        }

        target = min(max(target, 0), 100);

        targetMarker.setPosition(760 + target * 2, 730);
    }

    public ArrayList<Thought> getAllThoughts() {
        return new ArrayList<>(currentThoughts.values());
    }
}
