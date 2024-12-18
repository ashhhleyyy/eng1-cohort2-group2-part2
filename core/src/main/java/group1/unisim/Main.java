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
import group1.unisim.achievement.AchievementsManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private static final float UPDATE_TIME = 1 / 30f; // 30 updates/second
    private ContentLoader contentLoader;
    private SpriteBatch batch;
    private Texture toolbar;
    private Texture mapTexture;
    private Texture settingsTexture;
    private Texture buildIconTexture;
    private Texture pauseTexture;
    private Texture playTexture;

    private SatisfactionBar satisfactionBar;
    private float updateTimer;
    private boolean isPaused = true;
    private boolean statsUpdated = false;
    private float gameTimer = 300;
    private HashMap<String, Event> currentEvents;

    private Stage stage;
    private Stage endScreen;

    private boolean event1;
    private boolean event2;
    private boolean event3;
    private int reqAcc;
    private int reqTea;
    private int reqSel;
    private int reqFoo;
    private int reqRec;
    private int previousSecond;

    private Label scoreNumberLabel;
    private Label scoreCommentLabel;

    //UI
    private Stage ui;
    private Label gameTimeText;

    private ScrollPane buildSelect;
    private ArrayList<BuildingSlot> buildingSlots;
    private Building buildingPreview = null;

    private HashMap<Service, Label> servicesText;

    private Image pauseImage;

    private Table thoughtDisplay;
    private Table eventDisplay;
    private Label thoughtDisplayLabel;
    private Label eventDisplayLabel;
    private Image thoughtBackground;
    private Image eventBackground;

    private TextArea achievementsEmbed;
    private TextArea leaderboardEmbed;

    private AchievementsManager achievementsManager;

    @Override
    public void create() {
        this.contentLoader = new ContentLoader();
        this.contentLoader.load();
        this.achievementsManager = new AchievementsManager();

        // events start at false and after being triggered are set to true.
        currentEvents = new HashMap<>();
        event1 = false;
        event2 = false;
        event3 = false;
        reqAcc = 1;
        reqFoo = 1;
        reqRec = 1;
        reqSel = 1;
        reqTea = 1;
        previousSecond = 300;

        Skin skin = new Skin(Gdx.files.internal(Paths.UI_SKIN));
        batch = new SpriteBatch();

        toolbar = new Texture(Paths.TOOLBAR);
        mapTexture = new Texture(Paths.MAP_TEXTURE);
        settingsTexture = new Texture(Paths.SETTINGS_ICON);
        buildIconTexture = new Texture(Paths.BUILD_ICON);
        pauseTexture = new Texture(Paths.PAUSE);
        playTexture = new Texture(Paths.PLAY);
        this.ui = new Stage();

        Texture thoughtBackgroundTexture = new Texture(Paths.THOUGHT_BACKGROUND);
        Texture eventBackgroundTexture = new Texture(Paths.EVENT_BACKGROUND);

        Texture endScreenTexture = new Texture(Paths.END_SCREEN);

        satisfactionBar = new SatisfactionBar(skin, ui);

        gameTimeText = new Label("5:00", skin);
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
                }
            });

            buttons.addActor(button);
        }

        buttons.setWidth(10);

        buildSelect = new ScrollPane(buttons);
        buildSelect.setPosition(30, 480);
        buildSelect.setHeight(240);
        buildSelect.setVisible(false);

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
                if (gameTimer < 0) return;
                isPaused = !isPaused;
                if (isPaused) pauseImage.setDrawable(new TextureRegionDrawable(new TextureRegion(pauseTexture)));
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
        thoughtBackground = new Image(thoughtBackgroundTexture);
        thoughtBackground.setPosition(700,350);
        ui.addActor(thoughtBackground);

        thoughtDisplay = new Table(skin);
        thoughtDisplay.top().right().setPosition(1000, 720);
        thoughtDisplayLabel = new Label("Unpause time to get feedback!", skin);
        thoughtDisplayLabel.setWrap(true);
        thoughtDisplay.add(thoughtDisplayLabel).width(290).pad(5);
        ui.addActor(thoughtDisplay);

        // creates and sets up events display, which is drawn immediately and will be hidden when unpaused for the first time
        eventBackground = new Image(eventBackgroundTexture);
        eventBackground.setPosition(800, 250);
        ui.addActor(eventBackground);

        eventDisplay = new Table(skin);
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
        for (BuildingSlot slot : buildingSlots){
            ui.addActor(slot.constructionCountdownText);
        }

        Gdx.input.setInputProcessor(new InputMultiplexer(ui, stage));

        satisfactionBar.updateScore();
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (!isPaused && gameTimer > 0) {
            gameTimer -= deltaTime;
            updateGameTimeText((int) gameTimer);
            if (gameTimer < 0) {
                isPaused = true;
                pauseImage.setDrawable(new TextureRegionDrawable(new TextureRegion(pauseTexture)));
            }

            updateTimer += deltaTime;
            while (updateTimer > Main.UPDATE_TIME) { // in case of a long freeze, able to do multiple updates
                update();
                updateTimer -= Main.UPDATE_TIME;
            }
        }

        if ((Math.round(gameTimer) % 2 == 0) && (previousSecond != Math.round(gameTimer))) {
            // Thought bubble
            StringBuilder thoughtBubble = new StringBuilder("Current Student Thoughts:\n");
            for (Thought thought : satisfactionBar.getAllThoughts()) {
                thoughtBubble.append(thought.getTitle()).append(": ").append(thought.getDescription()).append("\n\n");
            }
            thoughtDisplayLabel.setText(thoughtBubble);
            previousSecond = Math.round(gameTimer);
        }

        // Event runner:
        if (!isPaused) {
            if (!event1 && eventDisplayLabel.isVisible()) {
                eventDisplayLabel.setVisible(false);
                eventBackground.setVisible(false);
            }
            if (!event1 && gameTimer < 250) {
                runEvent();
                event1 = true;
                eventDisplayLabel.setVisible(true);
                eventBackground.setVisible(true);
            }
            if (!event2 && eventDisplayLabel.isVisible() && gameTimer < 220) {
                eventDisplayLabel.setVisible(false);
                eventBackground.setVisible(false);
            }
            if (!event2 && gameTimer < 150) {
                runEvent();
                event2 = true;
                eventDisplayLabel.setVisible(true);
                eventBackground.setVisible(true);
            }
            if (!event3 && eventDisplayLabel.isVisible() && gameTimer < 120) {
                eventDisplayLabel.setVisible(false);
                eventBackground.setVisible(false);
            }
            if (!event3 && gameTimer < 50) {
                runEvent();
                event3 = true;
                eventDisplayLabel.setVisible(true);
                eventBackground.setVisible(true);
            }
            if (event3 && eventDisplayLabel.isVisible() && gameTimer < 20) {
                eventDisplayLabel.setVisible(false);
                eventBackground.setVisible(false);
            }
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
        if (gameTimer < 0) {
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
            if (!statsUpdated) {
                statsUpdated = true;
                Leaderboard leaderboard = contentLoader.getLeaderboard();
                String name = JOptionPane.showInputDialog("whats your username");
                leaderboard.addScore(new Score(name, Math.round(satisfactionBar.getScore())));
                leaderboardEmbed.setText(leaderboard.toString());
                contentLoader.saveLeaderboard(leaderboard);

                this.achievementsEmbed.setText(achievementsManager.formatCompleted());
                this.achievementsManager.saveAchievements();
            }

            endScreen.draw();
        }
    }

    private void update() {
        if (isPaused) return;

        for (BuildingSlot slot : buildingSlots) {
            slot.update();
        }

        if (buildingPreview != null) {
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
                stopPreview();
            }
        }

        updateServiceCounts();

        satisfactionBar.updateScore();
    }

    private void updateGameTimeText(int seconds) {
        gameTimeText.setText(String.format("%d:%02d", (seconds / 60), (seconds % 60)));
    }

    private void updateServiceCounts() {
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

        // Calculate number of buildings needed + assign correct thoughts:
        if (currentEvents.get("1") != null) {
            if (Objects.equals(currentEvents.get("1").getAssociatedThought(), "underCrowding")) {
                reqAcc = 0;
            } else {
                reqAcc = 2;
            }
        }
        if (currentEvents.get("2") != null) {
            if (Objects.equals(currentEvents.get("2").getAssociatedThought(), "underTeaching")) {
                reqTea = 2;
            } else {
                reqTea = 0;
            }
        }
        if (currentEvents.get("3") != null) {
            if (Objects.equals(currentEvents.get("3").getAssociatedThought(), "underRecreation")) {
                reqRec = 2;
            } else {
                reqRec = 0;
            }
        }

        if (reqAcc < services.get(Service.Accommodation)) {
            satisfactionBar.setThought("1", contentLoader.getThought("underCrowding"));
        } else if (reqAcc > services.get(Service.Accommodation)) {
            satisfactionBar.setThought("1", contentLoader.getThought("overCrowding"));
        } else {
            satisfactionBar.setThought("1", contentLoader.getThought("neutralCrowding"));
        }

        if (reqTea > services.get(Service.TeachingSpace)) {
            satisfactionBar.setThought("2", contentLoader.getThought("underTeaching"));
        } else if (reqTea < services.get(Service.TeachingSpace)) {
            satisfactionBar.setThought("2", contentLoader.getThought("overTeaching"));
        } else {
            satisfactionBar.removeThought("2");
        }

        if (reqRec > services.get(Service.Recreation)) {
            satisfactionBar.setThought("3", contentLoader.getThought("underRecreation"));
        } else if (reqRec < services.get(Service.Recreation)) {
            satisfactionBar.setThought("3", contentLoader.getThought("overRecreation"));
        } else {
            satisfactionBar.removeThought("3");
        }

        if (reqAcc == services.get(Service.Accommodation) && reqTea == services.get(Service.TeachingSpace) &&
            reqSel == services.get(Service.SelfStudy) && reqFoo == services.get(Service.FoodDrink) && reqRec == services.get(Service.Recreation)) {
            satisfactionBar.setThought("4", contentLoader.getThought("perfectBuildingLevel"));
        } else {
            satisfactionBar.removeThought("4");
        }

        if (1 <= services.get(Service.Accommodation) && 1 <= services.get(Service.TeachingSpace) &&
            1 <= services.get(Service.SelfStudy) && 1 <= services.get(Service.FoodDrink) && 1 <= services.get(Service.Recreation)) {
            satisfactionBar.setThought("5", contentLoader.getThought("oneOfEachBuilding"));
        } else {
            satisfactionBar.removeThought("5");
        }

        if (0 == services.get(Service.Accommodation) || 0 == services.get(Service.TeachingSpace) ||
            0 == services.get(Service.SelfStudy) || 0 == services.get(Service.FoodDrink) || 0 == services.get(Service.Recreation)) {
            satisfactionBar.setThought("6", contentLoader.getThought("buildingMissing"));
        } else {
            satisfactionBar.removeThought("6");
        }

        // Adding construction thought to satisfaction bar:
        if (constructionTotal == 1) {
            satisfactionBar.setThought("0", contentLoader.getThought("activeConstructions1"));
        } else if (constructionTotal == 2) {
            satisfactionBar.setThought("0", contentLoader.getThought("activeConstructions2"));
        } else if (constructionTotal > 2) {
            satisfactionBar.setThought("0", contentLoader.getThought("activeConstructions3"));
        } else {
            satisfactionBar.setThought("0", contentLoader.getThought("activeConstructions0"));
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
        switch (randNum) {
            case 0:
                eventDisplayLabel.setText("The university is receiving an unprecedented influx of new students, we may need more accomodation!");
                currentEvents.put("1", new Event("overCrowding", 100, "1", currentEvents));
                break;
            case 1:
                eventDisplayLabel.setText("The university is receiving far less new students than usual, we may need less accomodation!");
                currentEvents.put("1", new Event("underCrowding", 100, "1", currentEvents));
                break;
            case 2:
                eventDisplayLabel.setText("Students are sick of prerecorded mini-lectures and want to go in person, we may need more teaching spaces!");
                currentEvents.put("2", new Event("underTeaching", 50, "2", currentEvents));
                break;
            case 3:
                eventDisplayLabel.setText("Lecturers are on strike, we may need less teaching spaces!");
                currentEvents.put("2", new Event("overTeaching", 50, "2", currentEvents));
                break;
            case 4:
                eventDisplayLabel.setText("Students are bored, we may need more recreation spaces!");
                currentEvents.put("3", new Event("underRecreation", 50, "3", currentEvents));
                break;
            case 5:
                eventDisplayLabel.setText("Fresher's flu is getting around and people are staying in their dorms, we may need less recreation spaces!");
                currentEvents.put("3", new Event("overRecreaction", 30, "3", currentEvents));
                break;
        }
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
