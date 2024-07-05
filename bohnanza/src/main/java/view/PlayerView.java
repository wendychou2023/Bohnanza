package view;

import player.Player;
import card.Card;
import io.bitbucket.plt.sdp.bohnanza.gui.*;

public class PlayerView {
    private final GUI gui;
    private final Player player;
    private final int playerId;
    private Compartment handCompartment;
    private Compartment beanFieldCompartment;
    private Label coinLabel;

    public PlayerView(GUI gui, Player player, int playerId) {
        this.gui = gui;
        this.player = player;
        this.playerId = playerId;
        player.setPlayerView(this);
        setupPlayerView();
    }

    private void setupPlayerView() {
        handCompartment = gui.addCompartment(new Coordinate(500 * playerId, 800), new Size(500, 400), "Player " + playerId);
        beanFieldCompartment = gui.addCompartment(new Coordinate(500 * playerId, 700), new Size(500, 150), "", "BOHNENFELD_ALLE");
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
}

