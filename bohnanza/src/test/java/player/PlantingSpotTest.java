package player;

import card.BlueBean;
import card.Card;
import card.ChiliBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlantingSpotTest {

    private PlantingSpot plantingSpot;
    private Card card1;
    private Card card2;

    @BeforeEach
    void setUp() {
        plantingSpot = new PlantingSpot();
        card1 = new ChiliBean(); // Assuming Card has a constructor with a name parameter
        card2 = new BlueBean(); // Assuming Card has a constructor with a name parameter
    }

    @Test
    void testInitialValues() {
        assertTrue(plantingSpot.isEmpty());
        assertEquals(0, plantingSpot.getNumberOfBeans());
        assertNull(plantingSpot.getPlantedCard());
    }

    @Test
    void testPlantCardInEmptySpot() {
        plantingSpot.plant(card1);
        assertFalse(plantingSpot.isEmpty());
        assertEquals(1, plantingSpot.getNumberOfBeans());
        assertEquals(card1, plantingSpot.getPlantedCard());
    }

    @Test
    void testPlantSameCard() {
        plantingSpot.plant(card1);
        plantingSpot.plant(card1);
        assertEquals(2, plantingSpot.getNumberOfBeans());
        assertEquals(card1, plantingSpot.getPlantedCard());
    }

    @Test
    void testPlantDifferentCard() {
        plantingSpot.plant(card1);
        plantingSpot.plant(card2);
        assertEquals(1, plantingSpot.getNumberOfBeans(), "Number of beans should not increase when planting a different card");
        assertEquals(card1, plantingSpot.getPlantedCard(), "Planted card should remain the same when trying to plant a different card");
    }

    @Test
    void testClearSpot() {
        plantingSpot.plant(card1);
        plantingSpot.clear();
        assertTrue(plantingSpot.isEmpty());
        assertEquals(0, plantingSpot.getNumberOfBeans());
        assertNull(plantingSpot.getPlantedCard());
    }

    @Test
    void testCanPlant() {
        assertTrue(plantingSpot.canPlant(card1));
        plantingSpot.plant(card1);
        assertTrue(plantingSpot.canPlant(card1));
        assertFalse(plantingSpot.canPlant(card2));
    }

    @Test
    void testToString() {
        assertEquals("PlantingSpot{plantedCard=null, numberOfBeans=0}", plantingSpot.toString());
        plantingSpot.plant(card1);
        assertEquals("PlantingSpot{plantedCard=" + card1 + ", numberOfBeans=1}", plantingSpot.toString());
    }

    @Test
    void testSetAndGetCard() {
        plantingSpot.setCard(card1);
        assertEquals(card1, plantingSpot.getCard());
        plantingSpot.setCard(card2);
        assertEquals(card2, plantingSpot.getCard());
    }
}
