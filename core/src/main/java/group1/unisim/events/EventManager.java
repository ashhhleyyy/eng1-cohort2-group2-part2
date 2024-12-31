package group1.unisim.events;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import group1.unisim.ContentLoader;
import group1.unisim.Paths;
import group1.unisim.SatisfactionBar;
import group1.unisim.Timer;
import group1.unisim.achievement.AchievementsManager;
import group1.unisim.building.Service;
import group1.unisim.thought.Thoughts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static group1.unisim.building.Service.*;

public class EventManager {
    private final Timer timer;
    private final SatisfactionBar satisfactionBar;
    private final ContentLoader contentLoader;
    private final AchievementsManager achievementsManager;

    private final ArrayList<Event> currentEvents;
    private final HashMap<Service, Integer> buildingRequirements;
    HashMap<Integer, Boolean> eventsRun = new HashMap<>();
    private Label eventDisplayLabel;
    private Image eventBackground;

    public EventManager(Timer timer, SatisfactionBar satisfactionBar, ContentLoader contentLoader, AchievementsManager achievementsManager) {
        this.timer = timer;
        this.satisfactionBar = satisfactionBar;
        this.contentLoader = contentLoader;
        this.achievementsManager = achievementsManager;
        for (int i = 50; i < Timer.getStartTime(); i += 100) {
            eventsRun.put(i, false);
        }
        buildingRequirements = new HashMap<>();
        for (Service service : values()) {
            buildingRequirements.put(service, 1);
        }
        currentEvents = new ArrayList<>();
    }

    public void initUi(Stage stage, Skin skin) {
        Table eventDisplay = new Table(skin);
        eventDisplay.top().right().setPosition(1000, 350);
        Texture eventBackgroundTexture = new Texture(Paths.EVENT_BACKGROUND);
        eventBackground = new Image(eventBackgroundTexture);
        eventBackground.setPosition(800, 250);
        eventDisplayLabel = new Label("New events show up here, when they happen... \nReminder: these effects last for the whole game!", skin);
        eventDisplayLabel.setWrap(true);
        eventDisplay.add(eventDisplayLabel).height(90).width(190).pad(5);

        stage.addActor(eventBackground);
        stage.addActor(eventDisplay);
    }

    public void update(float delta) {
        for (Event event: currentEvents){
            event.update(delta);
        }
        if (!currentEvents.isEmpty()) {
            eventDisplayLabel.setVisible(true);
            eventBackground.setVisible(true);
        } else {
            eventDisplayLabel.setVisible(false);
            eventBackground.setVisible(false);
        }
        checkEvents();
    }

    public void checkEvents() {
        for (int time : eventsRun.keySet()) {
            if (eventsRun.get(time)) {
                continue;
            }
            if (this.timer.getTimeRemaining() < time) {
                runEvent();
                eventsRun.put(time, true);
            }
        }
    }

    private void runEvent() {
        int randNum = (int) (Math.random() * 5);
        //todo move this to a file
        switch (randNum) {
            case 0:
                addEvent(new Event("The university is receiving an unprecedented influx of new students, we may need more accommodation!", 100, Accommodation, 2, this));
                break;
            case 1:
                addEvent(new Event("The university is receiving far less new students than usual, we may need less accommodation!", 100, Accommodation, 0, this));
                break;
            case 2:
                addEvent(new Event("Students are sick of prerecorded mini-lectures and want to go in person, we may need more teaching spaces!", 50, TeachingSpace, 2, this));
                break;
            case 3:
                addEvent(new Event("Lecturers are on strike, we may need less teaching spaces!", 50, TeachingSpace, 0, this));
                break;
            case 4:
                addEvent(new Event("Students are bored, we may need more recreation spaces!", 50, Recreation, 2, this));
                break;
            case 5:
                addEvent(new Event("Fresher's flu is getting around and people are staying in their dorms, we may need less recreation spaces!", 30, Recreation, 0, this));
                break;
        }
    }

    public void addEvent(Event toAdd) {
        currentEvents.removeIf(event -> event.getService() == toAdd.getService());
        buildingRequirements.put(toAdd.getService(), toAdd.getRequirement());
        currentEvents.add(toAdd);
        if (eventDisplayLabel != null) {
            eventDisplayLabel.setText(toAdd.getDescription());
        }
    }

    public void removeEvent(Event toRemove) {
        buildingRequirements.put(toRemove.getService(), 1);
        currentEvents.remove(toRemove);
        if (eventDisplayLabel != null) {
            eventDisplayLabel.setText("");
        }
    }

    public void updateServices(Map<Service, Integer> services, int buildingsUnderConstruction) {
        boolean perfect_buildings = true;
        for (Map.Entry<Service, Integer> target : buildingRequirements.entrySet()) {
            int comp = services.get(target.getKey()).compareTo(target.getValue());
            if (comp != 0) {
                perfect_buildings = false;
            }
            String thought = contentLoader.getServiceThought(target.getKey(), comp);
            if (thought == null) {
                satisfactionBar.removeThought(target.getKey().toString());
                continue;
            }
            satisfactionBar.setThought(target.getKey().toString(), contentLoader.getThought(thought));
        }

        boolean found_zero = false;
        boolean all_1s = true;
        for (Integer value : services.values()) {
            if (value == 0) {
                found_zero = true;
                all_1s = false;
                break;
            }
            if (value != 1) {
                all_1s = false;
            }
        }

        if (perfect_buildings) {
            satisfactionBar.setThought("building_count", contentLoader.getThought(Thoughts.PERFECT_BUILDING_LEVEL));
        } else {
            satisfactionBar.removeThought("building_count");
        }
        if (found_zero) {
            satisfactionBar.setThought("missing_building", contentLoader.getThought(Thoughts.BUILDING_MISSING));
        } else {
            satisfactionBar.removeThought("missing_building");
        }
        if (all_1s) {
            satisfactionBar.setThought("one_of_each", contentLoader.getThought(Thoughts.ONE_OF_EACH_BUILDING));
        } else {
            satisfactionBar.removeThought("one_of_each");
        }

        // Adding construction thought to satisfaction bar:
        if (buildingsUnderConstruction == 1) {
            satisfactionBar.setThought("construction", contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS1));
        } else if (buildingsUnderConstruction == 2) {
            satisfactionBar.setThought("construction", contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS2));
        } else if (buildingsUnderConstruction > 2) {
            satisfactionBar.setThought("construction", contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS3));
        } else {
            satisfactionBar.setThought("construction", contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS0));
        }

        for (var service : values()) {
            achievementsManager.onServiceValueChange(service, services.getOrDefault(service, 0));
        }
        achievementsManager.onSatisfactionChange(satisfactionBar.getScore());
    }

    public ArrayList<Event> getCurrentEvents() {
        return currentEvents;
    }
}
