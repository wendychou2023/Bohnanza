package view;

import card.Card;
import io.bitbucket.plt.sdp.bohnanza.gui.*;
import org.eclipse.swt.internal.C;

import java.util.List;

public class TradingAreaView {
    private final GUI gui;
    private Compartment FirstTradingCompartment;
    private Compartment SecondTradingCompartment;

    public TradingAreaView(GUI gui) {
        this.gui = gui;
        setupTradingView();
    }

    final int X_First = 600;
    final int Y_First = 300;
    final Coordinate FirstTradingPosition = new Coordinate(X_First, Y_First);
    final int X_Second = 850;
    final int Y_Second = 300;
    final Coordinate SecondTradingPosition = new Coordinate(X_Second, Y_Second);
    final Size compartmentSize = new Size(150, 200);
    Button tradeButton;
    Coordinate tradeButtonCoordinate = new Coordinate(760,400);
    Size BUTTON_SIZE = new Size(80, 25);

    private void setupTradingView() {
        FirstTradingCompartment = gui.addCompartment(FirstTradingPosition, compartmentSize, "FirstTrading\nArea");
        SecondTradingCompartment = gui.addCompartment(SecondTradingPosition, compartmentSize, "SecondTrading\nArea");
        tradeButton = gui.addButton("Trade", tradeButtonCoordinate, BUTTON_SIZE, button -> {});
    }

    /**
     * updateFirtTrade removes the first card drawn by the player from the Handcards
     */
    public void updateFirstTrade(Card card)  {

    }

    public void displayTradeCards(List<Card> tradingCards){
        for (int i = 0; i < 2; i++){
            gui.addCard(tradingCards.get(i).getCardType(), new Coordinate(1100 + 100 * i, 300)).flip();
        }
    }
}
