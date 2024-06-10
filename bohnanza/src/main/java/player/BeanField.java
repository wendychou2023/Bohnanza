package player;

import card.Card;
import game.Game;

import java.util.*;
import java.util.stream.Collectors;

public class BeanField {
    private final Player player;
    private final List<PlantingSpot> plantingSpots = new LinkedList<>();

    protected BeanField(Player player) {
        this.player = player;
    }

    protected List<PlantingSpot> getPlantingSpots() {
        return plantingSpots;
    }

    public boolean canPlant(Card card) {
        return plantingSpots.stream().anyMatch(spot -> spot.canPlant(card));
    }

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

    public int getNumberOfFields(){return numberOfFields;}

    public void setNumberOfFields(int numOfFields){
        if (numOfFields > 3) {
            throw new RuntimeException("can not buy more than 3 fields");
        }
        this.numberOfFields = numOfFields;
    }

    /**
     * harvest a field and add coins to player
     * @param card beantype to harvest
     */
    public void harvest(Card card) {
        if (!canHarvest(card)) {
            System.out.println("current beans: " + beans);
            throw new RuntimeException("can not harvest bean  of type: " + card);
        }

        uncheckedHarvest(card);
    }

    /**
     * harvest a field and add coins to player without checking if it can be harvested
     * @param card to harvest
     */
    private void uncheckedHarvest(Card card) {
        int cardCount = beans.get(card);
        int coins = card.getHarvestRevenue(cardCount);

        beans.remove(card); // remove from field
        // discard n cards (n = cardCount - coins as beancoins stay with player and are not discarded)
        Game.getInstance().getDeck().discardN(card, cardCount - coins);
        player.addCoins(coins);
    }

    /**
     * check if a bean can be harvested
     * bean can not be harvested if
     * 1. the field contains only one bean and another field contains more than one bean
     * 2. there is no field with that bean
     * @param card beantype
     */
    public boolean canHarvest(Card card) {
        if (!beans.containsKey(card)) {
            return false;
        }

        if (beans.get(card) == 1) {
            for (int val : beans.values()) {
                if (val > 1) {
                    return false;
                }
            }
        }

        return true;
    }

    public List<Card> getHarvestableBeans() {
        List<Card> output = new LinkedList<>();
        for (Card card : beans.keySet()) {
            if (canHarvest(card)) {
                output.add(card);
            }
        }
        return output;
    }

    public int getNumberOfBeansInField(Card card) {
        return beans.get(card);
    }

    /**
     * harvest all fields and add coins to player
     * used at the end of the game
     */
    public void harvestAll() {
        // collects to list to avoid concurrent modification exception
        new LinkedList<>(beans.keySet()).forEach(this::uncheckedHarvest);
    }

    public void buyExtraBeanField() {
        if (player.getCoins() < 3) {
            throw new NotEnoughCoinsException();
        }

        // throws excepion if player already has 3 fields
        setNumberOfFields(this.numberOfFields + 1);

        player.removeCoins(3);
    }

    @Override
    public String toString() {
        return "BeanField{" +
                "beans=" + beans +
                '}';
    }
}
