package view;

import org.eclipse.swt.graphics.Rectangle;
import player.Player;
import card.Card;
import io.bitbucket.plt.sdp.bohnanza.gui.*;

public class PlayerView {
    private final GUI gui;
    private final Player player;
    private final int playerId;
    private Compartment handCompartment;
    private Compartment[] beanFieldCompartment;
    private Compartment beanFieldImage;
    private Label coinLabel;

    public PlayerView(GUI gui, Player player, int playerId) {
        this.gui = gui;
        this.player = player;
        this.playerId = playerId;
        player.setPlayerView(this);
        setupPlayerView();
    }

    Coordinate handUpperLeftCoordinate;
    Size handCompartmentSize;
    Coordinate bfImageUpperLeftCoordinate;
    Size bfImageComparmentSize;
    Coordinate[] bfCompartmentUpperLeftCoordinate;
    Size bfCompartmentSize;

    private void setupPlayerView() {
        handUpperLeftCoordinate = new Coordinate(500 * playerId, 800);
        handCompartmentSize = new Size(500, 400);
        bfImageUpperLeftCoordinate = new Coordinate(500 * playerId, 550);
        bfImageComparmentSize = new Size(500, 250);
        bfCompartmentSize = new Size(170, 250);

        handCompartment = gui.addCompartment(handUpperLeftCoordinate, handCompartmentSize, "Player " + playerId);
        beanFieldImage = gui.addCompartment(bfImageUpperLeftCoordinate, bfImageComparmentSize, "", "BOHNENFELD_ALLE");
        beanFieldCompartment = new Compartment[3];
        bfCompartmentUpperLeftCoordinate = new Coordinate[3];
        for (int i = 0; i < beanFieldCompartment.length; i++) {
            bfCompartmentUpperLeftCoordinate[i] = new Coordinate(500 * playerId + 170 * i, 550);
            beanFieldCompartment[i] = gui.addCompartment(
                    bfCompartmentUpperLeftCoordinate[i],
                    bfCompartmentSize,
                    "_" + (i + 1)
            );
        }
        coinLabel = gui.addLabel(new Coordinate(500 * playerId, 500), "Coins: " + player.getCoins());

//        updateHandView();
        updateBeanFieldView();
    }

    public void updateHandView(Card card) {
        gui.addCard(card.getCardType(), handUpperLeftCoordinate).flip();
        arrangeCardsInCompartment(handCompartment);
    }

    private void arrangeCardsInCompartment(Compartment compartment){
        CardObject[] cardObjects = gui.getCardObjectsInCompartment(compartment);
        gui.setCardFlipFlags(cardObjects);
        compartment.distributeHorizontal(cardObjects);
    }

    public void updateBeanFieldView() {
        // Logic to display cards in the bean field
    }

    public void updateCoins() {
        coinLabel.updateLabel("Coins: " + player.getCoins());
    }

    private boolean withinBound(Coordinate coordinate, Coordinate compartmentCoordinate, Size compartmentSize){
        int xMin = compartmentCoordinate.x;
        int yMin = compartmentCoordinate.y;
        int xMax = xMin + compartmentSize.width;
        int yMax = yMin + compartmentSize.height;

        return coordinate.x >= xMin && coordinate.x <= xMax && coordinate.y >= yMin && coordinate.y <= yMax;
    }

    public boolean toInBeanField(Coordinate coordinate) {
        return withinBound(coordinate, bfImageUpperLeftCoordinate,bfImageComparmentSize);
    }

    public boolean fromInHand(Coordinate coordinate) {
        return withinBound(coordinate, handUpperLeftCoordinate, handCompartmentSize);
    }

    public int getPlantingSpotIdx(Coordinate coordinate){
        int idx;
        for(idx = 0; idx < 3; idx++){
            if(withinBound(coordinate, bfCompartmentUpperLeftCoordinate[idx], bfCompartmentSize)){
                break;
            }
        }

        return idx;
    }
}

