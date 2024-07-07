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
        player = new Player(1);
        beanField = player.getBeanField();
        beanField.addBeanfields(2);
    }

    @Test
    // Attempting to harvest unplanted bean
    void testHarvestUnplantedBean() {
        assertThrows(RuntimeException.class, () -> beanField.harvest(0));
    }

    @Test
    // Planting a bean with enough bean fields available
    void testPlantWithEnoughBeanFields() {
        beanField.plant(0, new BlueBean());
        assertEquals(1, beanField.getNumberOfBeansInField(0));
        assertEquals(new BlueBean(), beanField.getPlantedBeans().get(0));
    }

    @Test
    // Plant multiple beans of the same type and harvest them
    void testMultipleHarvestBeans() {
        int n = 6;
        for (int i = 0; i < n; i++) {
            beanField.plant(0, new ChiliBean());
        }
        beanField.harvest(0);

        Card card = new ChiliBean();
        assertEquals(card.getHarvestRevenue(n), player.getCoins());
        assert beanField.isEmpty(0);
    }

    @Test
    void testPlantTwoCardTypesInOneSpot() {
        beanField.plant(0, new BlueBean());
        assertThrows(RuntimeException.class,
            () -> beanField.plant(0, new GardenBean())
        );
    }

    @Test
    void buyExtraBeanFieldWithoutCoins() {
        assertThrows(NotEnoughCoinsException.class, beanField::buyExtraBeanField);
        assertEquals(2, beanField.getNumberOfFields());
    }

    @Test
    void buyExtraBeanField() {
        player.addCoins(3);

        assertDoesNotThrow(beanField::buyExtraBeanField);
        assert beanField.getNumberOfFields() == 3;
        assert player.getCoins() == 0;
    }
}

