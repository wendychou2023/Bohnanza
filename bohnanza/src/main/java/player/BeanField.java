package player;

import card.Card;
import game.Game;

import java.util.*;
import java.util.stream.Collectors;

public class BeanField {
    private final Player player;
    private final List<PlantingSpot> plantingSpots = new ArrayList<>();

    protected BeanField(Player player) {
        this.player = player;
    }

    /**
     * returns if there is any field that can the card can be planted in
     */
    public boolean canPlant(Card card) {
        return plantingSpots.stream().anyMatch(spot -> spot.canPlant(card));
    }

    /**
     * returns if a card can be planted in field at index plantingSpotIdx
     */
    public boolean canPlant(int plantingSpotIdx, Card card) {
        return plantingSpots.get(plantingSpotIdx).canPlant(card);
    }

    /**
     * plant a bean in a field
     * @param card to plant
     * @param plantingSpotIdx index of the field to plant the card
     * @throws RuntimeException if there is no field to plant the card
     * see canPlant()
     */
    public void plant(int plantingSpotIdx, Card card) {
        PlantingSpot spot = plantingSpots.get(plantingSpotIdx);
        if (!spot.canPlant(card)) {
            throw new RuntimeException("can not plant card in field");
        }
        spot.plant(card);
    }

    public List<PlantingSpot> getPlantingSpots(){
        return plantingSpots;
    }

    public int getNumberOfFields(){return plantingSpots.size();}

    public void addBeanfields(int numOfFields){
        for (int i = 0; i < numOfFields; i++) {
            plantingSpots.add(new PlantingSpot());
        }
    }

    /**
     * harvest a field and add coins to player
     * @param plantingSpotIdx index of the field to harvest
     */
    public void harvest(int plantingSpotIdx) {
        if (canHarvest(plantingSpotIdx)) {
            throw new RuntimeException("can not harvest bean  of type: ");
        }

        uncheckedHarvest(plantingSpotIdx);
    }

    /**
     * harvest a field and add coins to player without checking if it can be harvested
     * @param plantingSpotIdx index of the field to harvest
     */
    private void uncheckedHarvest(int plantingSpotIdx) {
        PlantingSpot spot = plantingSpots.get(plantingSpotIdx);
        int cardCount = spot.getNumberOfBeans();
        Card card = spot.getPlantedCard();
        int coins = card.getHarvestRevenue(cardCount);

        // discard n cards (n = cardCount - coins as beancoins stay with player and are not discarded)
        Game.getInstance().getDeck().discardN(card, cardCount - coins);
        player.addCoins(coins);

        spot.clear();
    }

    /**
     * check if a field can be harvested
     */
    public boolean canHarvest(int plantingSpotIdx) {
        int n = plantingSpots.get(plantingSpotIdx).getNumberOfBeans();

        // Bean Protection Act
        return ! (n == 1 && plantingSpots.stream().anyMatch(spot -> spot.getNumberOfBeans() > 1));
    }

    public int getNumberOfBeansInField(int plantingSpotIdx) {
        return plantingSpots.get(plantingSpotIdx).getNumberOfBeans();
    }

    /**
     * get all planted beans
     * empty fields are represented as card == null
     */
    public List<Card> getPlantedBeans() {
        return plantingSpots.stream()
                .map(PlantingSpot::getPlantedCard)
                .collect(Collectors.toList());
    }

    /**
     * harvest all fields and add coins to player
     * used at the end of the game
     */
    public void harvestAll() {
        for (int i = 0; i < getNumberOfFields(); i++) {
            uncheckedHarvest(i);
        }
    }

    public void buyExtraBeanField() {
        if (player.getCoins() < 3) {
            throw new NotEnoughCoinsException();
        }

        // throws excepion if player already has 3 fields
        addBeanfields(1);

        player.removeCoins(3);
    }

    @Override
    public String toString() {
        return "BeanField{" +
                "beans=" + plantingSpots +
                '}';
    }
}
