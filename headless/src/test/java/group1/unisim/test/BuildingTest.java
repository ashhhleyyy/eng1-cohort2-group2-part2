package group1.unisim.test;

import group1.unisim.building.Building;
import group1.unisim.building.Service;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuildingTest {
    @Test
    public void testBuilding() {
        Building building = new Building("test building", new Service[]{Service.Accommodation}, 20, 1, "textures/test.png");

        assertEquals("test building", building.getName());
        assertArrayEquals(building.getServicesProvided(), new Service[]{Service.Accommodation});
        assertEquals(building.getConstructionTime(), 20);
        assertEquals(building.getSize(), 1);
        assertEquals(building.getTexture(), "textures/test.png");
    }
}
