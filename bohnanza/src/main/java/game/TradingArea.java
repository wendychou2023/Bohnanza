package game;

import card.Card;
import player.Player;

import java.util.List;

public class TradingArea {
    private List<Card> activePlayerCards;
    private List<Card> inActivePlayerCards;

    private Player activePlayer;
    private Player inactivePlayer;

    public TradingArea() {

    }

    public TradingArea(List<Card> activePlayerCards, List<Card> inActivePlayerCards, Player activePlayer, Player inactivePlayer) {
        this.activePlayerCards = activePlayerCards;
        this.inActivePlayerCards = inActivePlayerCards;
        this.activePlayer = activePlayer;
        this.inactivePlayer = inactivePlayer;
    }

    public List<Card> getActivePlayerCards() {
        return activePlayerCards;
    }

    public void setActivePlayerCards(List<Card> activePlayerCards) {
        this.activePlayerCards = activePlayerCards;
    }

    public List<Card> getInActivePlayerCards() {
        return inActivePlayerCards;
    }

    public void setInActivePlayerCards(List<Card> inActivePlayerCards) {
        this.inActivePlayerCards = inActivePlayerCards;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public Player getInactivePlayer() {
        return inactivePlayer;
    }

    public void setInactivePlayer(Player inactivePlayer) {
        this.inactivePlayer = inactivePlayer;
    }

    /**
     * Trade the cards between the active and inactive player
     * by switching the activePlayerCards and inActivePlayerCards
     */
    public void trade() {
        // switch activePlayerCards and inActivePlayerCards
        List<Card> temp = activePlayerCards;
        activePlayerCards = inActivePlayerCards;
        inActivePlayerCards = temp;
    }
}
