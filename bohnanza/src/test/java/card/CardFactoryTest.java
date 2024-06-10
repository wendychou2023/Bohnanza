package card;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardFactoryTest {

    @Test
    void createCard() {
        Card card = CardFactory.createCard(BlueBean.class);
        assertTrue(card instanceof BlueBean);
    }
}