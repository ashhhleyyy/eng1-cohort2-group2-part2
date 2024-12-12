package group1.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class BuildingSlot {
    private final Vector2 position;
    private final int maxSize;
    private final Image sprite;
    private int timeConstructing;
    private boolean isConstructingActive;
    private Building building;
    private Building previewing;
    public Label constructionCountdownText;
    Skin skin = new Skin(Gdx.files.internal(Assets.UI_SKIN));

    public BuildingSlot(Vector2 position, int maxSize, Stage stage) {
        this.position = position;
        this.maxSize = maxSize;
        this.sprite = new Image(new TextureRegionDrawable(new TextureRegion(ContentLoader.singleton.getTexture(Assets.ACCOMMODATION_TEXTURE))));
        this.sprite.setDrawable(null); // Image needs to be instantiated with a texture or setting it later won't work
        this.sprite.setScale(2f);
        this.sprite.setPosition(this.position.x, this.position.y);
        ImageButton button = new ImageButton(new TextureRegionDrawable(new TextureRegion(ContentLoader.singleton.getTexture(Assets.ACCOMMODATION_TEXTURE))));
        button.setColor(1, 1, 1, 0);
        button.setScale(2f);
        button.setPosition(this.position.x, this.position.y);
        // left click on the preview to turn it into construction
        button.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (previewing != null) {
                    build(previewing);
                }
            }
        });
        // right click on a building to turn it into rubble
        button.addListener(new ClickListener(Input.Buttons.RIGHT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (building != null) {
                    clearSlot(BuildingSlot.this);
                }
            }
        });
        stage.addActor(sprite);
        stage.addActor(button);

        constructionCountdownText = new Label(null, skin);
        constructionCountdownText.setPosition(position.x, position.y);
        constructionCountdownText.setSize(50, 50);
        constructionCountdownText.setFontScale(2);
        constructionCountdownText.setAlignment(1);
    }

    public boolean isConstructing() {
        return isConstructingActive;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public Building getBuilding() {
        return building;
    }

    public void update() {
        if (timeConstructing > 0) {
            timeConstructing--;
            constructionCountdownText.setVisible(true); // I KNOW THIS LINE OF CODE LOOKS DUMB BUT TRUST ME IT IS NEEDED
            constructionCountdownText.setText(Integer.toString(timeConstructing));
        } else if (timeConstructing == 0) {
            isConstructingActive = false;
            constructionCountdownText.setVisible(false);
        }
    }

    public void build(Building building) {
        clearPreview();
        this.timeConstructing = building.getConstructionTime();
        this.building = building;
        this.sprite.setDrawable(new TextureRegionDrawable(new TextureRegion(ContentLoader.singleton.getTexture(building.getTexture()))));
        this.isConstructingActive = true;
    }

    public void setPreview(Building preview) {
        clearPreview();
        if (building != null) return;
        if (preview.getSize() > maxSize) return;

        sprite.setColor(1, 1, 1, 0.6f);
        sprite.setDrawable(new TextureRegionDrawable(new TextureRegion(ContentLoader.singleton.getTexture(preview.getTexture()))));
        previewing = preview;
    }

    public void clearPreview() {
        if (previewing == null) return;

        sprite.setColor(1, 1, 1, 1);
        sprite.setDrawable(null);
        previewing = null;
    }

    public void clearSlot(BuildingSlot slot){
        this.sprite.setDrawable(null);
        this.previewing = null;
        this.building = null;
    }
}
