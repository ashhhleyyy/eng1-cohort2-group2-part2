package group1.unisim.test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import group1.unisim.Building.Building;
import group1.unisim.Building.BuildingSlot;
import group1.unisim.ContentLoader;
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
        Building building = ContentLoader.singleton.allBuildings().stream().findFirst().get();
        BuildingSlot slot = new BuildingSlot(new Vector2(0, 0), 100, stage);
        assertEquals(new Vector2(0, 0), slot.getPosition());
        assertNull(slot.getBuilding());
        assertEquals(100, slot.getMaxSize());
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
        slot.setPreview(building);
        assertEquals(building, slot.getPreviewing());
        slot.clearPreview();
        assertNull(slot.getPreviewing());

        BuildingSlot slot2 = new BuildingSlot(new Vector2(0, 0), 1, stage);
        slot2.setPreview(building);
        assertNull(slot2.getPreviewing());
        ContentLoader.singleton.dispose();
    }
}
