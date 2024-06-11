package player;

import card.Card;

public class PlantingSpot {
    private int numberOfBeans;
    private Card plantedCard = null;

    public Card getPlantedCard() {
        return plantedCard;
    }

    public int getNumberOfBeans() {
        return numberOfBeans;
    }

    public void plant(Card card) {
        if (isEmpty()) {
            plantedCard = card;
        }
        numberOfBeans++;
    }


    public void clear() {
        plantedCard = null;
        numberOfBeans = 0;
    }

    public boolean canPlant(Card card) {
        return isEmpty() || plantedCard.equals(card);
    }

    public boolean isEmpty() {
        return numberOfBeans == 0;
    }

    @Override
    public String toString() {
        return "PlantingSpot{" +
                "plantedCard=" + plantedCard + ", " +
                "numberOfBeans=" + numberOfBeans +
                '}';
    }
}
