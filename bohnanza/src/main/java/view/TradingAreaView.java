package view;

import card.Card;
import io.bitbucket.plt.sdp.bohnanza.gui.*;
import org.eclipse.swt.internal.C;

import java.util.List;

public class TradingAreaView {
    private final GUI gui;
    private Compartment firstTradingCompartment;
    private Compartment secondTradingCompartment;

    public TradingAreaView(GUI gui) {
        this.gui = gui;
        setupTradingView();
    }

    final int X_First = 600;
    final int Y_First = 300;
    final Coordinate firstTradingPosition = new Coordinate(X_First, Y_First);
    final int X_Second = 850;
    final int Y_Second = 300;
    final Coordinate secondTradingPosition = new Coordinate(X_Second, Y_Second);
    final Size tradeCompartmentSize = new Size(150, 200);
    Button tradeButton;
    Coordinate tradeButtonCoordinate = new Coordinate(760,400);
    Size BUTTON_SIZE = new Size(80, 25);

    private void setupTradingView() {
        firstTradingCompartment = gui.addCompartment(firstTradingPosition, tradeCompartmentSize, "FirstTrading\nArea");
        secondTradingCompartment = gui.addCompartment(secondTradingPosition, tradeCompartmentSize, "SecondTrading\nArea");
        tradeButton = gui.addButton("Trade", tradeButtonCoordinate, BUTTON_SIZE, button -> {
            tradeCard();
        });
    }

    /**
     * Exchange cards in the two trade compartments
     */
    private void tradeCard(){
        CardObject[] activePlayerCard = gui.getCardObjectsInCompartment(firstTradingCompartment);
        CardObject[] nonActivePlayerCard = gui.getCardObjectsInCompartment(secondTradingCompartment);

        if (activePlayerCard != null && nonActivePlayerCard !=null){
            gui.moveCard(activePlayerCard[0], secondTradingPosition);
            gui.moveCard(nonActivePlayerCard[0], firstTradingPosition);
        }
    }

    /**
     * updateFirtTrade removes the first card drawn by the player from the Handcards
     */
    public void updateFirstTrade(Card card)  {

    }

    Coordinate[] tradingCardCoordinates = new Coordinate[2];
    public void displayTradeCards(List<Card> tradingCards){
        for (int i = 0; i < 2; i++){
            tradingCardCoordinates[i] = new Coordinate(1100 + 150 * i, 300);
            gui.addCard(tradingCards.get(i).getCardType(), tradingCardCoordinates[i]).flip();
        }
    }

    public boolean tradingCardMoved(Coordinate coordinate){
        return (coordinate.x == tradingCardCoordinates[0].x && coordinate.y == tradingCardCoordinates[0].y) ||
                (coordinate.x == tradingCardCoordinates[1].x && coordinate.y == tradingCardCoordinates[1].y);
    }

    private boolean withinBound(Coordinate coordinate, Coordinate compartmentCoordinate, Size compartmentSize){
        int xMin = compartmentCoordinate.x;
        int yMin = compartmentCoordinate.y;
        int xMax = xMin + compartmentSize.width;
        int yMax = yMin + compartmentSize.height;

        return coordinate.x >= xMin && coordinate.x <= xMax && coordinate.y >= yMin && coordinate.y <= yMax;
    }

    public boolean placeAtFirstTradingCompartment(Coordinate coordinate){
        return withinBound(coordinate, firstTradingPosition, tradeCompartmentSize);
    }

    public boolean placeAtSecondTradingCompartment(Coordinate coordinate){
        return withinBound(coordinate, secondTradingPosition, tradeCompartmentSize);
    }

}
