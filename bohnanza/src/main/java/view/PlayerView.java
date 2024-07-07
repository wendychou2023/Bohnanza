package view;

import org.eclipse.swt.graphics.Rectangle;
import player.Player;
import card.Card;
import io.bitbucket.plt.sdp.bohnanza.gui.*;

public class PlayerView {
    private final GUI gui;
    private final Player player;
    private final int playerId;
    private DeckView deckView;
    private Compartment handCompartment;
    private Compartment[] beanFieldCompartment;
    private Compartment beanFieldImage;
    private Label coinLabel;

    public PlayerView(GUI gui, Player player, int playerId, DeckView deckView) {
        this.gui = gui;
        this.player = player;
        this.playerId = playerId;
        this.deckView = deckView;

        player.setPlayerView(this);
        setupPlayerView();
    }

    Coordinate handUpperLeftCoordinate;
    Size handCompartmentSize;
    Coordinate bfImageUpperLeftCoordinate;
    Size bfImageComparmentSize;
    Coordinate[] bfCompartmentUpperLeftCoordinate;
    Button[] harvestButtons;
    Size bfCompartmentSize;
    Size BUTTON_SIZE = new Size(70, 25);

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

        harvestButtons = new Button[3];
        for(int i = 0; i < beanFieldCompartment.length; i++){
            int finalI = i;
            harvestButtons[i] = gui.addButton("harvest", new Coordinate(500 * playerId + 170 * i, 525), BUTTON_SIZE, button -> {
                int plantingSpotIdx = finalI;
                if(player.getBeanField().canHarvest(plantingSpotIdx)){
                    player.getBeanField().harvest(plantingSpotIdx);
                    // discard card
                    CardObject[] cardsInPlantingSpot = gui.getCardObjectsInCompartment(beanFieldCompartment[plantingSpotIdx]);
                    for (CardObject discardedCard: cardsInPlantingSpot){
                        gui.moveCard(discardedCard, deckView.getDiscardPilePosition());
                    }
                    updateCoinLabel();
                }
            });
        }

        coinLabel = gui.addLabel(new Coordinate(500 * playerId, 500), "Coins: " + player.getCoins());
    }

    private void updateCoinLabel(){
        coinLabel.updateLabel("Coins: " + player.getCoins());
    }

    public void updateHandView(Card card) {
        CardObject[] drawPile = gui.getCardObjectsInCompartment(deckView.getDrawPileCompartment());
        gui.moveCard(drawPile[0], handUpperLeftCoordinate);
        drawPile[0].flip();
        arrangeCardsInCompartment(handCompartment);
    }

    private void arrangeCardsInCompartment(Compartment compartment){
        CardObject[] cardObjects = gui.getCardObjectsInCompartment(compartment);
        gui.setCardFlipFlags(cardObjects);
        compartment.distributeHorizontal(cardObjects);
    }

    public void updateBeanFieldView(int plantingSpotId) {
        Compartment plantedCompartment = beanFieldCompartment[plantingSpotId];
        CardObject[] cardObjects = gui.getCardObjectsInCompartment(plantedCompartment);

        plantedCompartment.distributeVertical(cardObjects);
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

    public Compartment getBeanFieldCompartment(int idx){
        return beanFieldCompartment[idx];
    }
}

