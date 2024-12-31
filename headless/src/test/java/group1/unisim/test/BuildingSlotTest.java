package group1.unisim.test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import group1.unisim.ContentLoader;
import group1.unisim.building.Building;
import group1.unisim.building.BuildingSlot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class BuildingSlotTest extends HeadlessGdxTest {
    @Test
    public void testBuildingSlot() {
        Stage stage = mock(Stage.class);
        new ContentLoader();
        ContentLoader.singleton.load();
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        BuildingSlot slot = new BuildingSlot(new Vector2(0, 0), 100, stage);
        assertEquals(new Vector2(0, 0), slot.getPosition());
        assertEquals(100, slot.getMaxSize());
        ContentLoader.singleton.dispose();
    }

    @Test
    public void testBuildingPlacement() {
        Stage stage = mock(Stage.class);
        new ContentLoader();
        ContentLoader.singleton.load();
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        Building building = ContentLoader.singleton.allBuildings().stream().findFirst().get();
        BuildingSlot slot = new BuildingSlot(new Vector2(0, 0), 100, stage);
        assertNull(slot.getBuilding());
        slot.build(building);
        assertTrue(slot.isConstructing());
        assertEquals(building, slot.getBuilding());
        for (int i = 0; i <= building.getConstructionTime(); i++) {
            slot.update(1);
        }
        assertFalse(slot.isConstructing());
        assertEquals(building, slot.getBuilding());
        slot.setPreview(building);
        assertNull(slot.getPreviewing());
        slot.clearSlot();
        assertNull(slot.getBuilding());
        ContentLoader.singleton.dispose();
    }

    @Test
    public void testBuildingPreview() {
        Stage stage = mock(Stage.class);
        new ContentLoader();
        ContentLoader.singleton.load();
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        Building building = ContentLoader.singleton.allBuildings().stream().findFirst().get();
        BuildingSlot slot = new BuildingSlot(new Vector2(0, 0), 100, stage);
        slot.setPreview(building);
        assertEquals(building, slot.getPreviewing());
        slot.clearPreview();
        assertNull(slot.getPreviewing());
        ContentLoader.singleton.dispose();
    }

    @Test
    public void testBuildingFits() {
        Stage stage = mock(Stage.class);
        new ContentLoader();
        ContentLoader.singleton.load();
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        Building building = ContentLoader.singleton.allBuildings().stream().findFirst().get();
        BuildingSlot slot = new BuildingSlot(new Vector2(0, 0), 1, stage);
        slot.setPreview(building);
        assertNull(slot.getPreviewing());
        assertThrows(IllegalArgumentException.class, ()->slot.build(building));
        ContentLoader.singleton.dispose();
    }

    @Test
    public void testBuildingCollision(){
        Stage stage = mock(Stage.class);
        new ContentLoader();
        ContentLoader.singleton.load();
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        Building building = ContentLoader.singleton.allBuildings().stream().findFirst().get();
        BuildingSlot slot = new BuildingSlot(new Vector2(0, 0), 100, stage);
        slot.build(building);
        assertThrows(IllegalStateException.class,() ->slot.build(building));
        ContentLoader.singleton.dispose();
    }




}
