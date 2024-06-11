package player;

import card.Card;

public class PlantingSpot {
    private final BeanField beanField;
    private int numberOfBeans;
    private Card plantedCard = null;

    public PlantingSpot(BeanField beanField) {
        this.beanField = beanField;
    }

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

    public boolean canHarvest() {
        // Bean Protection rule
        return ! (numberOfBeans == 1 && beanField.getPlantingSpots().stream().anyMatch(spot -> spot.getNumberOfBeans() > 1));
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
