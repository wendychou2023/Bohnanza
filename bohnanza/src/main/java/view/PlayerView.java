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
    private void setupPlayerView() {
        handUpperLeftCoordinate = new Coordinate(500 * playerId, 800);
        handCompartmentSize = new Size(500, 400);
        bfImageUpperLeftCoordinate = new Coordinate(500 * playerId, 700);
        bfImageComparmentSize = new Size(500, 150);

        handCompartment = gui.addCompartment(handUpperLeftCoordinate, handCompartmentSize, "Player " + playerId);
        beanFieldImage = gui.addCompartment(bfImageUpperLeftCoordinate, bfImageComparmentSize, "", "BOHNENFELD_ALLE");
        beanFieldCompartment = new Compartment[3];
        for (int i = 0; i < beanFieldCompartment.length; i++) {
            beanFieldCompartment[i] = gui.addCompartment(
                    new Coordinate(500 * playerId + 170 * i, 700),
                    new Size(170, 150),
                    "_" + (i + 1)
            );
        }
        coinLabel = gui.addLabel(new Coordinate(500 * playerId, 650), "Coins: " + player.getCoins());

//        updateHandView();
        updateBeanFieldView();
    }

    public void updateHandView(Card card) {
        int x = 500 * playerId;
        int y = 800;

        gui.addCard(card.getCardType(), new Coordinate(x, y)).flip();
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

    public boolean toInBeanField(Coordinate coordinate) {
        // Define the bounds of the beanFieldCompartment
        int xMin = bfImageUpperLeftCoordinate.x;
        int yMin = bfImageUpperLeftCoordinate.y;
        int xMax = xMin + bfImageComparmentSize.width;
        int yMax = yMin + bfImageComparmentSize.height;

        // Check if the coordinate is within these bounds
        return coordinate.x >= xMin && coordinate.x <= xMax && coordinate.y >= yMin && coordinate.y <= yMax;
    }

    public boolean fromInHand(Coordinate coordinate) {
        // Define the bounds of the beanFieldCompartment
        int xMin = handUpperLeftCoordinate.x;
        int yMin = handUpperLeftCoordinate.y;
        int xMax = xMin + handCompartmentSize.width;
        int yMax = yMin + handCompartmentSize.height;

        // Check if the coordinate is within these bounds
        return coordinate.x >= xMin && coordinate.x <= xMax && coordinate.y >= yMin && coordinate.y <= yMax;
    }

}

