package player;

import card.BlueBean;
import card.Card;
import card.ChiliBean;
import card.GardenBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BeanFieldTest {
    private BeanField beanField;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
        beanField = player.getBeanField();
    }

    @Test
    // Attempting to harvest unplanted bean
    void testHarvestUnplantedBean() {
        assertThrows(RuntimeException.class, () -> beanField.harvest(new ChiliBean()));
    }

    @Test
    // Planting a bean with enough bean fields available
    void testPlantWithEnoughBeanFields() {
        assertDoesNotThrow(() -> {
            beanField.plant(new ChiliBean());
        });
    }

    @Test
    // Plant multiple beans of the same type and harvest them
    void testMultipleHarvestBeans() throws NotEnoughBeanFieldException {
        int n = 6;
        for (int i = 0; i < n; i++) {
            beanField.plant(new ChiliBean());
        }
        beanField.harvest(new ChiliBean());

        Card card = new ChiliBean();
        assertEquals(card.getHarvestRevenue(n), player.getCoins());
    }

    @Test
    // Attempting to plant a bean without enough bean fields
    void testPlantWithoutEnoughBeanFields() {
        beanField.plant(new BlueBean());
        beanField.plant(new GardenBean());
        assertThrows(NotEnoughBeanFieldException.class, () -> beanField.plant(new ChiliBean()));
    }

    @Test
    void buyExtraBeanFieldWithoutCoins() {
        Player player = new Player(); // player has no coins when initialized

        assertThrows(NotEnoughCoinsException.class, player.getBeanField()::buyExtraBeanField);
        assert player.getBeanField().getNumberOfFields() == 2;
    }

    @Test
    void buyExtraBeanField() {
        Player player = new Player(); // player has no coins when initialized
        player.addCoins(3);

        assertDoesNotThrow(player.getBeanField()::buyExtraBeanField);
        assert player.getBeanField().getNumberOfFields() == 3;
        assert player.getCoins() == 0;
    }
}

