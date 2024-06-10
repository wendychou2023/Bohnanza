package player;

import card.Card;
import player.phase.Phase;
import player.phase.PlantingPhase;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Player {
    private Phase currentPhase = null;
    private final List<Card> handCards;
    private final BeanField beanField;
    private final List<Card> tradingArea;
    private int coins = 0;

    public Player() {
        this.handCards = new LinkedList<>();
        this.beanField = new BeanField(this);
        this.tradingArea = new LinkedList<>();
    }

    public List<Card> getHandCards() {
        return handCards;
    }

    public BeanField getBeanField() {return beanField;}

    public List<Card> getTradingArea() {
        return tradingArea;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public void removeCoins(int coins) {
        this.coins -= coins;
    }

    public void addToHand(Card card) {
        handCards.add(card);
    }

    public void addToHand(List<Card> cards) {
        for (Card card: cards) {
            addToHand(card);
        }
    }

    public Card popFromHand() {
        return handCards.remove(0);
    }

    public void takeTurn() {
        // inactive players usually have phase == null
        if (currentPhase == null) {
            currentPhase = new PlantingPhase();
        }

        while (currentPhase != null) {
            currentPhase.doPhase(this);
            currentPhase = currentPhase.getNextPhase();
        }
    }

    public TradeOffer makeOffer(Player activePlayer, Collection<Card> activePlayerCards) {
        return null;
    }
}
