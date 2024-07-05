package view;

import card.Card;
import game.CardMoveEvent;
import game.GameController;
import io.bitbucket.plt.sdp.bohnanza.gui.*;
import player.Player;

import java.util.List;

public class GameView implements Runnable {
    private GameController gameController;
    private final GUI gui;
    private final GlobalInfoView globalInfoView;
    private final DeckView deckView;
    private PlayerView[] playerViews;
    private TradingAreaView tradingAreaView;


    @SuppressWarnings("unused")
    private final String[] args;
    private Button startButton;
    private Button activePlayerButton;
    private Button nextPhaseButton;

    public GameView(GUI gui, String[] args) {
        super();
        this.gui = gui;
        this.args = args;
        this.globalInfoView = new GlobalInfoView(gui);
        this.deckView = new DeckView(gui);
        this.tradingAreaView = new TradingAreaView(gui);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GlobalInfoView getGlobalInfoView() {
        return globalInfoView;
    }

    public DeckView getDeckView(){
        return deckView;
    }
    public TradingAreaView getTradingAreaView(){
        return tradingAreaView;
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

        // a button to explicitly terminate the game and closes the window.
        gui.addButton("exit", new Coordinate(200, 100), BUTTON_SIZE, button -> {
            gui.stop();
        });

        // set the handler for drag'n'drop events. With this handler:
        // - whenever a d'n'd action finishes, the dropped card is flipped (toggle whether the front or back is shown)
        // - the card is moved to the front, i.e., displayed top-most
        gui.setCardDnDHandler((CardObject card, Coordinate originalCoordinate, Coordinate newCoordinate) -> {
            CardMoveEvent cardMoveEvent = new CardMoveEvent(originalCoordinate, newCoordinate, card);
            if(!gameController.actionIsAllowed(cardMoveEvent)){
                return originalCoordinate;
            }
            card.flip();
            gui.moveToTop(card);
            gameController.userActionCompleted();
            return newCoordinate;
        });

        nextPhaseButton = gui.addButton("Next phase", new Coordinate(10, 350), new Size(200, 25), button -> {
            gameController.nextPhaseButtonClicked();
            gui.setButtonEnabled(nextPhaseButton, false);
        });
        gui.setButtonEnabled(nextPhaseButton, false);

    }

    final Size BUTTON_SIZE = new Size(100, 25);

    public void updateInitialView(List<Card> drawPile, List<Player> players, int activePlayerId){
        // Disable the start button after start
        gui.setButtonEnabled(startButton, false);
        //gui.setButtonEnabled(activePlayerButton, true);
        globalInfoView.updateGameInfo("");

        deckView.displayDrawPile(drawPile);

        playerViews = new PlayerView[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playerViews[i] = new PlayerView(gui, players.get(i), i);
        }
    }

//    public void enableActivePlayerButton(){
//        gui.setButtonEnabled(activePlayerButton, true);
//    }

    public void enableNextPhaseButton(){
        gui.setButtonEnabled(nextPhaseButton, true);
    }
}
