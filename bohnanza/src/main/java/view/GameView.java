package view;

import card.Card;
import game.GameController;
import io.bitbucket.plt.sdp.bohnanza.gui.*;
import player.PlantingSpot;
import player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class GameView implements Runnable {
    private GameController gameController;
    private final GUI gui;
    private final GlobalInfoView globalInfoView;
    private final DeckView deckView;
    private PlayerView[] playerViews;


    @SuppressWarnings("unused")
    private final String[] args;
//    private Label gameInfoLabel; // Label to display game information
//    private Label playerInfoLabel; // Label to display player information
//    private Label label;
    private Button startButton;
    private Button activePlayerButton;
    private Button nextPhaseButton;

    public GameView(GUI gui, String[] args) {
        super();
        this.gui = gui;
        this.args = args;
        this.globalInfoView = new GlobalInfoView(gui);
        this.deckView = new DeckView(gui);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GlobalInfoView getGlobalInfoView() {
        return globalInfoView;
    }

    @Override
    public void run() {
        setupGUI();
    }

    private void setupGUI(){
        globalInfoView.updateGameInfo("Click start to start the game.");
        startButton = gui.addButton("start", new Coordinate(10, 100), BUTTON_SIZE, button -> {
            gameController.userClickStart();
            gameController.userActionCompleted();
        });

//        label = gui.addLabel(new Coordinate(10, 50), "<none>");
//        label.updateLabel("Click start to start the game.");

        // a button to explicitly terminate the game. This closes the window.
        gui.addButton("exit", new Coordinate(200, 100), BUTTON_SIZE, button -> {
            gui.stop();
        });

        // set the handler for drag'n'drop events. With this handler:
        // - whenever a d'n'd action finishes, the dropped card is flipped (toggle whether the front or back is shown)
        // - information on the dropped card is shown in the dedicated label
        // - the card is moved to the front, i.e., displayed top-most
        gui.setCardDnDHandler((CardObject card, Coordinate mouseCoordinate, Coordinate newCoordinate) -> {
            card.flip();
//            label.updateLabel(card.toString());
            gui.moveToTop(card);
            return newCoordinate;
        });

        // Initialize labels for displaying information
//        gameInfoLabel = gui.addLabel(new Coordinate(10, 150), "Game Information");
//        playerInfoLabel = gui.addLabel(new Coordinate(10, 250), "Player Information");

        activePlayerButton = gui.addButton("Active player takes turn", new Coordinate(10, 300), new Size(200, 25), button -> {
            gameController.userActionCompleted();
            gui.setButtonEnabled(activePlayerButton, false);
        });
        gui.setButtonEnabled(activePlayerButton, false);

        nextPhaseButton = gui.addButton("Next phase", new Coordinate(10, 350), new Size(200, 25), button -> {
            gameController.userActionCompleted();
            gui.setButtonEnabled(nextPhaseButton, false);
        });
        gui.setButtonEnabled(nextPhaseButton, false);

    }

    final int Y = 800;
    final int WIDTH = 250;
    final int HEIGHT = 300;
    final int V_SPACING = 5;
    final int H_SPACING = 5;
    final Size BUTTON_SIZE = new Size(100, 25);

    public void updateInitialView(List<Card> drawPile, List<Player> players, int activePlayerId){
        // Disable the start button after start
        gui.setButtonEnabled(startButton, false);
        gui.setButtonEnabled(activePlayerButton, true);
        globalInfoView.updateGameInfo("");

        deckView.displayDrawPile(drawPile);

        playerViews = new PlayerView[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playerViews[i] = new PlayerView(gui, players.get(i), i);
        }
    }
    public void updatePlayerHandCard(Player player, int playerId){
        final int X_DIFF = 20;
        final int Y_DIFF = 20;
        final int X_ORIGIN = WIDTH * playerId;
        final int Y_ORIGIN = Y;
        final int COLS = 10;
        final int ROWS = 5;

        int x = WIDTH * playerId;
        int y = Y;
        for (Card card : player.getHandCards()) {
            // Bean side facing up
            gui.addCard(card.getCardType(), new Coordinate(x + H_SPACING + BUTTON_SIZE.height, y-V_SPACING + BUTTON_SIZE.width));

            x += X_DIFF;
            if (x > X_ORIGIN + X_DIFF * ROWS) {
                x = X_ORIGIN;
                y += Y_DIFF;
            }
            if (y > Y_ORIGIN + Y_DIFF * COLS) {
                y = Y_ORIGIN;
                x = X_ORIGIN;
            }
        }
    }

    public void updatePlayerBeanfield(Player player, int playerId){
        List<PlantingSpot> plantingSpots = player.getBeanField().getPlantingSpots();
        int fieldId = -1;
        for (PlantingSpot spot : plantingSpots) {
            fieldId++;
            if (spot.getPlantedCard() == null){
                continue;
            }
            Card card = spot.getPlantedCard();
            Integer numOfCards = spot.getNumberOfBeans();
            int finalFieldId = fieldId;
            IntStream.range(0, numOfCards).forEach(i -> {
                gui.addCard(card.getCardType(), new Coordinate(WIDTH * playerId + 10 + 80 * finalFieldId, Y-HEIGHT/2 + 50)).flip();
            });
        }
    }

    public void enableActivePlayerButton(){
        gui.setButtonEnabled(activePlayerButton, true);
    }

    public void enableNextPhaseButton(){
        gui.setButtonEnabled(nextPhaseButton, true);
    }
}
