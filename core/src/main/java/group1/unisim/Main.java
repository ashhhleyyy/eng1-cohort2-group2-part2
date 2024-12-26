package group1.unisim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import group1.unisim.Building.Building;
import group1.unisim.Building.BuildingSlot;
import group1.unisim.Building.Service;
import group1.unisim.Leaderboard.Leaderboard;
import group1.unisim.Leaderboard.Score;
import group1.unisim.Thought.Thoughts;
import group1.unisim.achievement.AchievementsManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private ContentLoader contentLoader;
    private SpriteBatch batch;
    private Texture toolbar;
    private Texture mapTexture;
    private Texture settingsTexture;
    private Texture buildIconTexture;
    private Texture pauseTexture;
    private Texture playTexture;

    private SatisfactionBar satisfactionBar;
    private boolean endScreenGenerated = false;
    private ArrayList<Event> currentEvents;
    private HashMap<Service, Integer> buildingRequirements;

    private Stage stage;
    private Stage endScreen;


    HashMap<Integer,Boolean> eventsRun = new HashMap<>();

    private float lastThoughtUpdate;

    private Timer timer;

    private Label scoreNumberLabel;
    private Label scoreCommentLabel;

    //UI
    private Stage ui;
    private Label gameTimeText;

    private Image buildSelectBackground;
    private ScrollPane buildSelect;
    private Table buildSelectText;
    private ArrayList<BuildingSlot> buildingSlots;
    private Building buildingPreview = null;

    private HashMap<Service, Label> servicesText;

    private Image pauseImage;

    private Label thoughtDisplayLabel;
    private Label eventDisplayLabel;
    private Image eventBackground;

    private TextArea achievementsEmbed;
    private TextArea leaderboardEmbed;

    private AchievementsManager achievementsManager;

    @Override
    public void create() {
        this.timer = new Timer();
        this.contentLoader = new ContentLoader();
        this.contentLoader.load();
        this.achievementsManager = new AchievementsManager();


        for (int i = 50; i < timer.getStartTime(); i+=100) {
            eventsRun.put(i,false);
        }
        currentEvents = new ArrayList<>();
        lastThoughtUpdate = timer.getTimeRemaining();


        buildingRequirements = new HashMap<>();
        for (Service service: Service.values()) {
            buildingRequirements.put(service,1);
        }


        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        batch = new SpriteBatch();

        toolbar = new Texture(Paths.TOOLBAR);
        mapTexture = new Texture(Paths.MAP_TEXTURE);
        settingsTexture = new Texture(Paths.SETTINGS_ICON);
        buildIconTexture = new Texture(Paths.BUILD_ICON);
        pauseTexture = new Texture(Paths.PAUSE);
        playTexture = new Texture(Paths.PLAY);
        this.ui = new Stage();

        Texture buildSelectBackgroundTexture = new Texture(Paths.BUILD_SELECT_BACKGROUND);

        Texture thoughtBackgroundTexture = new Texture(Paths.THOUGHT_BACKGROUND);
        Texture eventBackgroundTexture = new Texture(Paths.EVENT_BACKGROUND);

        Texture endScreenTexture = new Texture(Paths.END_SCREEN);

        satisfactionBar = new SatisfactionBar(skin, ui);

        gameTimeText = new Label(timer.toString(), skin);
        gameTimeText.setPosition(400, 735);
        gameTimeText.setSize(200, 50);
        gameTimeText.setFontScale(4);
        gameTimeText.setAlignment(1);
        ui.addActor(gameTimeText);

        ImageButton buildButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buildIconTexture)));
        buildButton.setPosition(70, 728);
        buildButton.setTransform(true);
        buildButton.setScale(2.5f);

        buildButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buildSelect.setVisible(true);
                buildSelectText.setVisible(true);
                buildSelectBackground.setVisible(true);
            }
        });

        int[][] slots = {
            {100, 200, 2}, {150, 550, 2}, {550, 200, 3}, {600, 400, 2}, {200, 200, 1}, {200, 350, 2}, {550, 300, 3}, {600, 500, 2}
        };

        stage = new Stage();

        buildingSlots = new ArrayList<>();

        for (int[] slot : slots) {
            buildingSlots.add(new BuildingSlot(new Vector2(slot[0], slot[1]), slot[2], stage));
        }

        VerticalGroup buttons = new VerticalGroup();

        for (Building building : contentLoader.allBuildings()) {
            ImageButton button = new ImageButton(new TextureRegionDrawable(new TextureRegion(contentLoader.getTexture(building.getTexture()))));

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    preview(building);
                    buildSelect.setVisible(false);
                    buildSelectText.setVisible(false);
                    buildSelectBackground.setVisible(false);
                }
            });

            buttons.addActor(button);
        }

        buttons.setWidth(10);

        // background image for build select tool
        buildSelectBackground = new Image(buildSelectBackgroundTexture);
        buildSelectBackground.setPosition(79, 523);
        buildSelectBackground.setVisible(false);
        ui.addActor(buildSelectBackground);

        buildSelect = new ScrollPane(buttons);
        buildSelect.setPosition(30, 480);
        buildSelect.setHeight(240);
        buildSelect.setVisible(false);

        // text next to building options when opening build select tool
        buildSelectText = new Table(skin);
        buildSelectText.setPosition(180, 620);
        buildSelectText.setVisible(false);
        ui.addActor(buildSelectText);
        Label buildingTextLectureHalls = new Label(" Lecture Halls", skin);
        buildSelectText.add(buildingTextLectureHalls).left().height(45);
        buildSelectText.row();
        Label buildingTextRecreation = new Label("Recreation", skin);
        buildSelectText.add(buildingTextRecreation).left().height(38);
        buildSelectText.row();
        Label buildingTextCafe = new Label("Cafe", skin);
        buildSelectText.add(buildingTextCafe).left().height(28);
        buildSelectText.row();
        Label buildingTextLibrary = new Label("Library", skin);
        buildSelectText.add(buildingTextLibrary).left().height(44);
        buildSelectText.row();
        Label buildingTextAccommodation = new Label("Accommodation", skin);
        buildSelectText.add(buildingTextAccommodation).left().height(41);

        servicesText = new HashMap<>();

        for (Service service : Service.values()) {
            servicesText.put(service, new Label("0 (0)", skin));
        }

        Table servicesDisplay = new Table(skin);
        servicesDisplay.add(new Label("Accommodation", skin));
        servicesDisplay.add(new Label("Teaching Space", skin));
        servicesDisplay.add(new Label("Self Study", skin));
        servicesDisplay.row();
        servicesDisplay.add(servicesText.get(Service.Accommodation));
        servicesDisplay.add(servicesText.get(Service.TeachingSpace));
        servicesDisplay.add(servicesText.get(Service.SelfStudy));
        servicesDisplay.row();
        servicesDisplay.add(new Label("Food/Drink", skin));
        servicesDisplay.add(new Label("Recreation", skin));
        servicesDisplay.row();
        servicesDisplay.add(servicesText.get(Service.FoodDrink));
        servicesDisplay.add(servicesText.get(Service.Recreation));

        for (Cell<?> cell : servicesDisplay.getCells()) {
            cell.padLeft(10);
            cell.padTop(5);
        }

        servicesDisplay.setPosition(300, 760);

        pauseImage = new Image(new TextureRegionDrawable(new TextureRegion(pauseTexture)));
        pauseImage.setPosition(570, 730);
        pauseImage.setScale(2f);
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseTexture)));
        pauseButton.setPosition(570, 730);
        pauseButton.setScale(2f);
        pauseButton.setColor(1, 1, 1, 0);

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                timer.togglePause();
                if (timer.isPaused()) pauseImage.setDrawable(new TextureRegionDrawable(new TextureRegion(pauseTexture)));
                else pauseImage.setDrawable(new TextureRegionDrawable(new TextureRegion(playTexture)));
            }
        });

        ui.addActor(servicesDisplay);
        ui.addActor(buildSelect);
        ui.addActor(buildButton);
        ui.addActor(pauseImage);
        ui.addActor(pauseButton);
        Gdx.input.setInputProcessor(new InputMultiplexer(ui, stage));

        // creates and sets up thoughts display, which is drawn immediately
        Image thoughtBackground = new Image(thoughtBackgroundTexture);
        thoughtBackground.setPosition(700, 350);
        ui.addActor(thoughtBackground);

        Table thoughtDisplay = new Table(skin);
        thoughtDisplay.top().right().setPosition(1000, 717);
        thoughtDisplayLabel = new Label("Unpause time to get feedback!", skin);
        thoughtDisplayLabel.setWrap(true);
        thoughtDisplay.add(thoughtDisplayLabel).width(290).pad(5);
        ui.addActor(thoughtDisplay);

        // creates and sets up events display, which is drawn immediately and will be hidden when unpaused for the first time
        eventBackground = new Image(eventBackgroundTexture);
        eventBackground.setPosition(800, 250);
        ui.addActor(eventBackground);

        Table eventDisplay = new Table(skin);
        eventDisplay.top().right().setPosition(1000, 350);
        eventDisplayLabel = new Label("New events show up here, when they happen... \nReminder: these effects last for the whole game!", skin);
        eventDisplayLabel.setWrap(true);
        eventDisplay.add(eventDisplayLabel).height(90).width(190).pad(5);
        ui.addActor(eventDisplay);

        // creates and sets up background of end screen, will not be drawn until game over
        endScreen = new Stage();
        Image endScreenBackground = new Image(endScreenTexture);
        endScreen.addActor(endScreenBackground);
        endScreenBackground.setPosition(115, 125);

        // sets up the table to store contents of end screen
        Table table = new Table();
        table.defaults().center();
        endScreen.addActor(table);
        table.setPosition(500, 360);

        Label titleLabel = new Label("TIME'S UP!!", skin);
        titleLabel.setFontScale(4);
        table.add(titleLabel).colspan(2).height(50);

        table.row().height(10);
        Label emptyLabel = new Label("", skin);
        table.add(emptyLabel).width(370);
        table.add(emptyLabel).width(370);

        table.row().height(30);
        Label scoreTextLabel = new Label("You finished with a student satisfaction of: ", skin);
        table.add(scoreTextLabel).right();
        scoreNumberLabel = new Label("XX%", skin);
        scoreNumberLabel.setFontScale(3);
        table.add(scoreNumberLabel);

        table.row().height(30);
        scoreCommentLabel = new Label("You did something something-", skin);
        table.add(scoreCommentLabel).colspan(2);

        table.row().height(20);
        table.add(emptyLabel);

        table.row().height(50);
        Label leaderboardTitleLabel = new Label("Leaderboard", skin);
        leaderboardTitleLabel.setFontScale(3);
        table.add(leaderboardTitleLabel);
        Label achievementsTitleLabel = new Label("Achievements", skin);
        achievementsTitleLabel.setFontScale(3);
        table.add(achievementsTitleLabel);

        table.row().height(220);
        leaderboardEmbed = new TextArea("", skin);
        table.add(leaderboardEmbed).fill().space(10);
        this.achievementsEmbed = new TextArea("placeholder", skin);
        table.add(achievementsEmbed).fill().space(10);


        ui.addActor(servicesDisplay);
        ui.addActor(buildSelect);
        ui.addActor(buildButton);
        ui.addActor(pauseImage);
        ui.addActor(pauseButton);
        for (BuildingSlot slot : buildingSlots) {
            ui.addActor(slot.constructionCountdownText);
        }

        Gdx.input.setInputProcessor(new InputMultiplexer(ui, stage));

        satisfactionBar.updateScore();
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (timer.isTimePassing()) {
            timer.update(deltaTime);
            gameTimeText.setText(timer.toString());

            update(deltaTime);
        }

        if (timer.getTimeRemaining() +2 < lastThoughtUpdate) {
            lastThoughtUpdate = timer.getTimeRemaining();
            thoughtDisplayLabel.setText(satisfactionBar.getThoughtsString());
        }

        if (!currentEvents.isEmpty()) {
            eventDisplayLabel.setVisible(true);
            eventBackground.setVisible(true);
        } else {
            eventDisplayLabel.setVisible(false);
            eventBackground.setVisible(false);
        }


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(mapTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(toolbar, 0, 720, Gdx.graphics.getWidth(), 80);
        batch.draw(settingsTexture, 0, 720, 70, 70);

        batch.end();

        stage.act(deltaTime);
        stage.draw();

        ui.act(deltaTime);
        ui.draw();

        // when time is up, update contents of end screen and then draw
        if (timer.isGameEnd()) {
            if (!endScreenGenerated) {
                generate_end_screen();
            }
            endScreen.draw();
        }
    }

    private void generate_end_screen() {
        endScreenGenerated = true;

        scoreNumberLabel.setText(String.format("%d", Math.round(satisfactionBar.getScore())) + "%");
        if (Math.round(satisfactionBar.getScore()) == 100) {
            scoreCommentLabel.setText("THE BEST TO EVER DO IT!!!");
        } else if (satisfactionBar.getScore() > 60) {
            scoreCommentLabel.setText("The university runs excellently!!");
        } else if (satisfactionBar.getScore() > 30) {
            scoreCommentLabel.setText("The university runs just fine!");
        } else {
            scoreCommentLabel.setText("Everyone's quite upset...");
        }
            Leaderboard leaderboard = contentLoader.getLeaderboard();
            String name = JOptionPane.showInputDialog("whats your username");
            leaderboard.addScore(new Score(name, Math.round(satisfactionBar.getScore())));
            leaderboardEmbed.setText(leaderboard.toString());
            contentLoader.saveLeaderboard(leaderboard);

            this.achievementsEmbed.setText(achievementsManager.formatCompleted());
            this.achievementsManager.saveAchievements();
    }


    private void checkEvents() {
        for (int time: eventsRun.keySet()) {
            if (eventsRun.get(time)) {
                continue;
            }
            if (timer.getTimeRemaining()< time){
                runEvent();
                eventsRun.put(time,true);
            }
        }
    }

    private void update(float delta) {
        for (BuildingSlot slot : buildingSlots) {
            slot.update(delta);
        }
        checkEvents();

        if (buildingPreview != null) {
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                stopPreview();
            }
        }

        updateServices();

        satisfactionBar.updateScore();
    }

    private void updateServices() {
        HashMap<Service, Integer> services = new HashMap<>();
        HashMap<Service, Integer> servicesUnderConstruction = new HashMap<>();
        int constructionTotal = 0;
        for (BuildingSlot slot : buildingSlots) {
            if (slot.getBuilding() == null) continue;
            for (Service service : slot.getBuilding().getServicesProvided()) {
                if (slot.isConstructing()) servicesUnderConstruction.merge(service, 1, Integer::sum);
                else services.merge(service, 1, Integer::sum);
            }
        }


        for (Service service : Service.values()) {
            services.putIfAbsent(service, 0);
            servicesUnderConstruction.putIfAbsent(service, 0);
            servicesText.get(service).setText(String.format("%d (%d)", services.get(service), servicesUnderConstruction.get(service)));
            constructionTotal += servicesUnderConstruction.get(service);
        }

        boolean perfect_buildings = true;
        for (Map.Entry<Service, Integer> target: buildingRequirements.entrySet()) {
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
        for (Integer value: services.values()) {
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
            satisfactionBar.setThought("building_count",contentLoader.getThought(Thoughts.PERFECT_BUILDING_LEVEL));
        } else {
            satisfactionBar.removeThought("building_count");
        }
        if (found_zero) {
            satisfactionBar.setThought("missing_building",contentLoader.getThought(Thoughts.BUILDING_MISSING));
        } else {
            satisfactionBar.removeThought("missing_building");
        }
        if (all_1s) {
            satisfactionBar.setThought("one_of_each",contentLoader.getThought(Thoughts.ONE_OF_EACH_BUILDING));
        } else {
            satisfactionBar.removeThought("one_of_each");
        }



        // Adding construction thought to satisfaction bar:
        if (constructionTotal == 1) {
            satisfactionBar.setThought("construction", contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS1));
        } else if (constructionTotal == 2) {
            satisfactionBar.setThought("construction", contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS2));
        } else if (constructionTotal > 2) {
            satisfactionBar.setThought("construction", contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS3));
        } else {
            satisfactionBar.setThought("construction", contentLoader.getThought(Thoughts.ACTIVE_CONSTRUCTIONS0));
        }

        for (var service : Service.values()) {
            this.achievementsManager.onServiceValueChange(service, services.getOrDefault(service, 0));
        }
        this.achievementsManager.onSatisfactionChange(this.satisfactionBar.getScore());
    }

    private void preview(Building building) {
        buildingPreview = building;
        for (BuildingSlot slot : buildingSlots) {
            slot.setPreview(building);
        }
    }

    private void stopPreview() {
        buildingPreview = null;
        for (BuildingSlot slot : buildingSlots) {
            slot.clearPreview();
        }
    }

    private void runEvent() {
        int randNum = (int) (Math.random() * 5);
        //todo move this to a file
        switch (randNum) {
            case 0:
                addEvent(new Event("The university is receiving an unprecedented influx of new students, we may need more accommodation!", 100,  Service.Accommodation,2, this));
                break;
            case 1:
                addEvent(new Event("The university is receiving far less new students than usual, we may need less accommodation!", 100,  Service.Accommodation,0, this));
                break;
            case 2:
                addEvent(new Event("Students are sick of prerecorded mini-lectures and want to go in person, we may need more teaching spaces!", 50,   Service.TeachingSpace,2, this));
                break;
            case 3:
                addEvent(new Event("Lecturers are on strike, we may need less teaching spaces!", 50,  Service.TeachingSpace, 0, this));
                break;
            case 4:
                addEvent( new Event("Students are bored, we may need more recreation spaces!", 50,  Service.Recreation, 2, this));
                break;
            case 5:
                addEvent( new Event("Fresher's flu is getting around and people are staying in their dorms, we may need less recreation spaces!", 30, Service.Recreation, 0, this));
                break;
        }
    }

    public void addEvent(Event toAdd) {
        currentEvents.removeIf(event -> event.getService() == toAdd.getService());
        buildingRequirements.put(toAdd.getService(),toAdd.getRequirement());
        currentEvents.add(toAdd);
        eventDisplayLabel.setText(toAdd.getDescription());
    }
    public void removeEvent(Event toRemove) {
        buildingRequirements.put(toRemove.getService(),1);
        currentEvents.remove(toRemove);
        eventDisplayLabel.setText("");

    }

    public ArrayList<Event> getCurrentEvents() {
        return currentEvents;
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        ui.dispose();
        mapTexture.dispose();
        toolbar.dispose();
        settingsTexture.dispose();
        buildIconTexture.dispose();
        contentLoader.dispose();
    }
}
