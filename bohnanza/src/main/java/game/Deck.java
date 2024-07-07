package game;

import card.Card;
import card.BlackEyedBean;
import card.BlueBean;
import card.ChiliBean;
import card.GardenBean;
import card.GreenBean;
import card.RedBean;
import card.SoyBean;
import card.StinkBean;
import player.NotEnaughCardsException;
import view.DeckView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Deck {
    private final List<Card> drawPile = new LinkedList<>();
    private final List<Card> discardPile = new LinkedList<>();
    private DeckView deckView;

    protected Deck() {
        //init drawpile with appropriate amouts of each card
        //and then shuffle it

        //add 2ßx BlueBean
        for (int i = 0; i < 20; i++) {
            drawPile.add(new BlueBean());
        }
        //add 18x ChiliBean
        for (int i = 0; i < 18; i++) {
            drawPile.add(new ChiliBean());
        }
        //add 16x StinkBean
        for (int i = 0; i < 16; i++) {
            drawPile.add(new StinkBean());
        }
        //add 14x GreenBean
        for (int i = 0; i < 14; i++) {
            drawPile.add(new GreenBean());
        }
        //add 12x SoyBean
        for (int i = 0; i < 12; i++) {
            drawPile.add(new SoyBean());
        }
        //add 10x BlackEyedBean
        for (int i = 0; i < 10; i++) {
            drawPile.add(new BlackEyedBean());
        }
        //add 8x RedBean
        for (int i = 0; i < 8; i++) {
            drawPile.add(new RedBean());
        }
        //add 6x GardenBean
        for (int i = 0; i < 6; i++) {
            drawPile.add(new GardenBean());
        }

        shuffleDrawPile();
    }

    public void setDeckView(DeckView deckView) {
        this.deckView = deckView;
    }

    public List<Card> getDrawPile() {
        return drawPile;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public void shuffleDrawPile() {
        Collections.shuffle(drawPile);
    }

    public void shuffleDiscardPile() {
        Collections.shuffle(discardPile);
    }

    /**
     * if trying to draw but draw pile has too few cards
     * start next turn
     * @return card drawn from drawPile
     */
    public Card draw() throws Exception {
        if (drawPile.isEmpty()) {
            refillDrawPile();
        }

        // Still no cards in draw pile
        if (drawPile.isEmpty()) {
            throw new NotEnaughCardsException();
        }

        Card card = drawPile.remove(0);
        return card;
    }

    /**
     * Try to draw n cards from drawPile
     * If draw pile has too few cards and this is not the last round,
     * triggers new round, refills draw pile and continues drawing.
     * If this is the last round, the returned list may be smaller than n
     * @param n number of cards to draw
     * @return list of cards drawn, may be smaller than n
     */
    public List<Card> drawN(int n) {
        List<Card> output = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            try {
                Card card = draw();
                output.add(card);
            } catch (NotEnaughCardsException e) {
                break;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return output;
    }

    public void discard(Card card) {
        discardPile.add(card);
    }

    public void discardN(List<Card> cards) {
        discardPile.addAll(cards);
        deckView.updateDiscardPile(cards);
    }

    public void discardN(Card card, int n) {
        for (int i = 0; i < n; i++) {
            discard(card);
        }
    }

    /**
     * Refill draw pile with discard pile
     * shuffle draw pile
     * Notify game.Game that next round started
     */
    public void refillDrawPile() {
        //notify game that next round started
        Game.getInstance().nextRound();

        if (Game.getInstance().getCurrentRound() >= Game.getInstance().MAX_ROUNDS) {
            // no new cards, game has ended
            return;
        }

        drawPile.addAll(discardPile);
        discardPile.clear();
        shuffleDrawPile();
    }
}