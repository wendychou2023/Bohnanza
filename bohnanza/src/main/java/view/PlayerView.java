package view;

import player.Player;
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
        setupPlayerView();
    }

    private void setupPlayerView() {
        handCompartment = gui.addCompartment(new Coordinate(250 * playerId, 800), new Size(250, 300), "Player " + playerId);
        beanFieldCompartment = gui.addCompartment(new Coordinate(250 * playerId, 700), new Size(250, 100), "", "BOHNENFELD_ALLE");
        coinLabel = gui.addLabel(new Coordinate(250 * playerId, 600), "Coins: " + player.getCoins());

//        updateHandView();
        updateBeanFieldView();
    }

    public void updateHandView(CardObject card) {
        int x = 250 * playerId;
        int y = 800;

        gui.addCard(card.getCardType(), new Coordinate(x, y));
        arrangeCardsInCompartment(handCompartment);
    }

    private void arrangeCardsInCompartment(Compartment compartment){
        CardObject[] cardObjects = gui.getCardObjectsInCompartment(compartment);
        compartment.distributeVertical(cardObjects);
    }

    public void updateBeanFieldView() {
        // Logic to display cards in the bean field
    }

    public void updateCoins() {
        coinLabel.updateLabel("Coins: " + player.getCoins());
    }
}

