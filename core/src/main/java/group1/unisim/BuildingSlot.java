package group1.unisim;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class BuildingSlot {
    private final Vector2 position;
    private final int maxSize;
    private final Image sprite;
    private final ImageButton button;
    private int timeConstructing;
    private boolean isConstructingActive;
    private Building building;
    private Building previewing;

    public BuildingSlot(Vector2 position, int maxSize, Stage stage) {
        this.position = position;
        this.maxSize = maxSize;
        sprite = new Image(new TextureRegionDrawable(new TextureRegion(ContentLoader.singleton.getTexture(Assets.ACCOMMODATION_TEXTURE))));
        sprite.setDrawable(null); // Image needs to be instantiated with a texture or setting it later won't work
        sprite.setScale(2f);
        sprite.setPosition(this.position.x, this.position.y);
        button = new ImageButton(new TextureRegionDrawable(new TextureRegion(ContentLoader.singleton.getTexture(Assets.ACCOMMODATION_TEXTURE))));
        button.setColor(1, 1, 1, 0);
        button.setScale(2f);
        button.setPosition(this.position.x, this.position.y);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (previewing != null) {
                    build(previewing);
                }
            }
        });
        stage.addActor(sprite);
        stage.addActor(button);
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
        } else if (timeConstructing == 0) {
            isConstructingActive = false;
        }
    }

    public void build(Building building) {
        clearPreview();
        timeConstructing = building.getConstructionTime();
        this.building = building;
        sprite.setDrawable(new TextureRegionDrawable(new TextureRegion(ContentLoader.singleton.getTexture(building.getTexture()))));
        isConstructingActive = true;
    }

    public void upgrade() {

    }

    public void Demolish() {

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
}

