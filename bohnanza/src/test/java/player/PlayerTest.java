package player;

import card.Card;
import card.ChiliBean;
import game.TradingArea;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

//    @Test
//    void makeOfferSingleCard() {
//        Player inactivePlayer = new Player();
//        Player activePlayer = new Player();
//        HashSet<Card> activePlayerCards = new HashSet<Card>();
//        activePlayerCards.add(new ChiliBean());
//
//        TradingArea offer = inactivePlayer.makeOffer(activePlayer, activePlayerCards);
//
//        if (offer == null) {
//            assert true;
//        } else if (offer.getCardsReceivedFromActive().isEmpty()) {
//            assert true;
//        } else {
//            Collection<Card> setOfCards = offer.getCardsReceivedFromActive();
//            assert setOfCards.size() == 1;
//            assertEquals(new ChiliBean(), setOfCards.iterator().next());
//        }
//    }
//
//    @Test
//    void makeOfferEmptySet() {
//        Player inactivePlayer = new Player();
//        Player activePlayer = new Player();
//        HashSet<Card> activePlayerCards = new HashSet<Card>();
//        activePlayerCards.add(new ChiliBean());
//
//        TradingArea offer = inactivePlayer.makeOffer(activePlayer, activePlayerCards);
//
//        assert (offer == null || offer.getCardsReceivedFromActive().isEmpty());
//    }
}