package player;

import card.Card;

public class PlantingSpot {
    private int numberOfBeans;
    private Class<? extends Card> beanType;

    public int getNumberOfBeans() {
        return numberOfBeans;
    }

    public void plant(Card card) {
        if (isEmpty()) {
            beanType = card.getClass();
        }
        numberOfBeans++;
    }

    public void clear() {
        beanType = null;
        numberOfBeans = 0;
    }

    public boolean canPlant(Card card) {
        return isEmpty() || beanType.equals(card.getClass());
    }

    public boolean isEmpty() {
        return numberOfBeans == 0;
    }
}
