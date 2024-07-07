package view;

import io.bitbucket.plt.sdp.bohnanza.gui.*;
import card.Card;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeckView {
    private final GUI gui;
    private Compartment drawPileCompartment;
    private Compartment discardPileCompartment;

    public DeckView(GUI gui) {
        this.gui = gui;
        setupDeckView();
    }

    final int X_DRAW = 600;
    final int Y_DRAW = 50;
    final Coordinate drawPilePosition = new Coordinate(X_DRAW, Y_DRAW);
    final int X_DISCARD = 850;
    final int Y_DISCARD = 50;
    final Coordinate discardPilePosition = new Coordinate(X_DISCARD, Y_DISCARD);
    final Size compartmentSize = new Size(150, 200);
    private void setupDeckView() {
        drawPileCompartment = gui.addCompartment(drawPilePosition, compartmentSize, "Draw\nPile");
        discardPileCompartment = gui.addCompartment(discardPilePosition, compartmentSize, "Discard\nPile");
    }

    /**
     * updateDrawPile removes the first card drawn by the player from the draw pile
     */
    public void updateDrawPile(Card card) throws Exception {
        CardObject[] drawPile = gui.getCardObjectsInCompartment(drawPileCompartment);
        CardObject drawnCard = drawPile[0];

        if (drawnCard.getCardType() == card.getCardType()){
            gui.removeCard(drawnCard);
        }else{
            throw new Exception();
        }
    }


    private static final Map<CardObject, Card> cardObjectToCardMap = new HashMap<>();
    public Map<CardObject, Card> displayDrawPile(List<Card> drawPile){
        for (Card card : drawPile) {
            CardObject cardObject = gui.addCard(card.getCardType(), drawPilePosition);
            cardObjectToCardMap.put(cardObject, card);
        }
        return cardObjectToCardMap;
    }

    public void updateDiscardPile(List<Card> discardPile) {

    }

    public Compartment getDrawPileCompartment(){
        return drawPileCompartment;
    }

    public Coordinate getDiscardPilePosition(){
        return discardPilePosition;
    }
}
