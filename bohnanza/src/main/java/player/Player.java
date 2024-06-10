package player;

import card.Card;
import game.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Player {
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
        //phase 1: plant beans (1 or 2) from hand

        // default behavior for phase 1
        doPhaseOnePlanting();

        //phase 2: trade with other players
        //manages the whole trading phase
        //including drawing the two trading cards
        //getting offers from other players
        //accepting or declining offers
        startTrading();

        //phase 3: make all players plant traded cards
        Game.getInstance().getPlayers().forEach(Player::doPhaseThreePlanting);

        //phase 4: draw 3 cards from deck
        addToHand(Game.getInstance().getDeck().drawN(3));
    }

    /**
     * Performs the planting phase (phase 1) of a player's turn.
     */
    private void doPhaseOnePlanting() {
        // contains a simple strategy for planting beans in phase 1
        // behavior could be extracted to a strategy pattern
        if (handCards.isEmpty()) {
            return;
        }

        // plant first card from hand
        Card card = popFromHand();
        if (beanField.canPlant(card)) {
            beanField.plant(card);
        } else {
            // harvest first harvestable field if necessary
            // could be optimized
            beanField.harvest(beanField.getHarvestableBeans().get(0));
            beanField.plant(card);
        }

        // plant second card if immediately possible
        if (!handCards.isEmpty()) {
            Card peek = handCards.get(0); // peek at first card in hand without removing it
            if (beanField.canPlant(peek)) {
                popFromHand(); // remove card from hand
                beanField.plant(peek);
            }
        }
    }

    /**
     * Performs the trading phase of a player's turn.
     * The trading phase of a player's turn consists of the following steps:
     * 1. Draw two cards from the deck
     * 2. Get offers from other players and accept or decline them,
     * adding the traded cards to the trading area of the respective players
     * 3. if any of the 2 cards from step 1 were not traded away, add them to the trading area of this player
     */
    private void startTrading() {
        // contains a simple strategy for trading with other players
        // behavior could be extracted to a strategy pattern

        // step 1 - draw two cards from deck
        List<Card> tradingCards = Game.getInstance().getDeck().drawN(2);

        // step 2 - get offeres from other players

        // for now no trading, see comment for some ideas on how to implement trading
        // implies that drawn cards are moved to trading area
        tradingArea.addAll(tradingCards);

        /*
        // get player(s) to trade with
        List<Player> players = Game.getInstance().getPlayers();

        // get a player to trade with that is not the current player
        Player player = players.get(0) != this ? players.get(0) : players.get(1);

        // can get offer on trading cards + some additional hand cards this player is willing to trade
        // example: just trading the two trading cards
        TradeOffer offer = player.makeOffer(this, tradingCards);
        offer.accept();

        */
    }

    /**
     * Plants all cards in the trading area of this player as part of phase 3 of a player's turn.
     * For phase 3 of a player's turn all players plant the cards they traded in phase 2.
     */
    public void doPhaseThreePlanting() {
        for (Card card : tradingArea) {
            if (beanField.canPlant(card)) {
                beanField.plant(card);
            } else {
                // harvest first harvestable field if necessary
                // could be optimized
                beanField.harvest(beanField.getHarvestableBeans().get(0));
                beanField.plant(card);
            }
        }
        tradingArea.clear();

    }

    public TradeOffer makeOffer(Player activePlayer, Collection<Card> activePlayerCards) {
        return null;
    }
}
