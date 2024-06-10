package player;

import card.Card;
import game.Game;

import java.util.*;
import java.util.stream.Collectors;

public class BeanField {
    private final Player player;
    private final Map<Card, Integer> beans = new HashMap<>();
    private int numberOfFields = 2; //a default value

    protected BeanField(Player player) {
        this.player = player;
    }

    public boolean canPlant(Card card) {
        if (beans.containsKey(card)) {
            return true;
        }

        return beans.size() < numberOfFields;
    }

    /**
     * plant a bean in a field
     * @param card to plant
     * @throws NotEnoughBeanFieldException if there is no field to plant the card
     * see canPlant()
     */
    public void plant(Card card) {
        if (!canPlant(card)) {
            throw new NotEnoughBeanFieldException();
        }

        if (beans.containsKey(card)) {
            beans.put(card, beans.get(card) + 1);
        } else {
            beans.put(card, 1);
        }
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
