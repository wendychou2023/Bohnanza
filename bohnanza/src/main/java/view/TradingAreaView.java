package view;

import card.Card;
import io.bitbucket.plt.sdp.bohnanza.gui.*;

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
    final Size compartmentSize = new Size(200, 200);


    private void setupTradingView() {
        FirstTradingCompartment = gui.addCompartment(FirstTradingPosition, compartmentSize, "FirstTrading\nArea");
        SecondTradingCompartment = gui.addCompartment(SecondTradingPosition, compartmentSize, "SecondTrading\nArea");
    }

    /**
     * updateFirtTrade removes the first card drawn by the player from the Handcards
     */
    public void updateFirstTrade(Card card)  {

    }

    public void displayFirstTrade(List<Card> drawPile){

    }
}
