package game;

import card.BlueBean;
import card.Card;
import card.ChiliBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        Game.resetInstance();
        deck = Game.getInstance().getDeck();
    }

    // drawN: player gets N cards, draw pile has N fewer cards
    @Test
    void drawNPlayerGets2() {
        int n = 2;
        int lenOfDrawPile = deck.getDrawPile().size();
        List<Card> drawnCards =  deck.drawN(n);
        assertEquals(lenOfDrawPile - n, deck.getDrawPile().size());
        assertEquals(n, drawnCards.size());
    }

    @Test
    void drawNPlayerGets3() {
        int n = 3;
        int lenOfDrawPile = deck.getDrawPile().size();
        List<Card> drawnCards =  deck.drawN(n);
        assertEquals(lenOfDrawPile - n, deck.getDrawPile().size());
        assertEquals(n, drawnCards.size());
    }

    // refillDrawPile: check empty discard pile and filled draw pile
    @Test
    void refillDrawPileTest(){
        int n = 6;

        // emptys drawpile
        deck.drawN(deck.getDrawPile().size());
        assertEquals(0, deck.getDrawPile().size());
        assertEquals(0, deck.getDiscardPile().size());

        deck.discardN(new BlueBean(), n);
        deck.refillDrawPile();
        assertEquals(0, deck.getDiscardPile().size());
        assertEquals(n, deck.getDrawPile().size());
    }

    // discardN: check the length of the discard pile added by N
    @Test
    void discardNCardsTest(){
        int lenOfDiscardPile = deck.getDiscardPile().size();
        int n = 2;
        List<Card> cards = new ArrayList<Card>(); //create cards for discarding
        cards.add(new ChiliBean());
        cards.add(new ChiliBean());
        deck.discardN(cards);
        assertEquals(n, deck.getDiscardPile().size() - lenOfDiscardPile);
    }
}