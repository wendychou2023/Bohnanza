package game;

import card.Card;
import player.Player;

import java.util.Collection;
import java.util.List;

public class TradingArea {
    private List<Card> activePlayerCards;
    private List<Card> inActivePlayerCards;

    private Player activePlayer;
    private Player inactivePlayer;

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
