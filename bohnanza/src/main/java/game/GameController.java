package game;

import card.Card;
import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import io.bitbucket.plt.sdp.bohnanza.gui.GUI;
import player.Player;
import player.phase.Phase;
import view.GameView;
import view.GlobalInfoView;
import view.DeckView;

import java.util.List;
import java.util.Scanner;

public class GameController {
    //    private static GameController instance = null;
    private Game game;
    private GameView gameView;
    private GUI gui;
    private GlobalInfoView globalInfoView;
    private DeckView deckView;
    private boolean waitingForUserAction = true;

    Scanner scanner;

    public GameController(Game game, GameView gameView, GUI gui) {
        this.game = game;
        this.gameView = gameView;
        this.gui = gui;
        this.scanner = new Scanner(System.in);
        this.globalInfoView = gameView.getGlobalInfoView();
        this.deckView = gameView.getDeckView();

        // Set this controller in the game and gameGUI
        game.setGameController(this);
        gameView.setGameController(this);
        game.getDeck().setDeckView(deckView);
    }

    public void start() {
        new Thread(gameView).start();

        // Read number of players
        System.out.println("Enter number of players (3 to 5 players are allowed):");
        int numOfPlayers = scanner.nextInt();
        System.out.println("You entered: " + numOfPlayers);

        for (int i = 0; i < numOfPlayers; i++) {
            game.addPlayer(new Player());
        }

        gui.start();

    }

    public void startGame() {
        waitForUserAction();
        globalInfoView.updateGameInfo("Game started. Current round: " + game.getCurrentRound());
        gameLoop();
    }

    Player activePlayer;
    private void gameLoop() {
        while (game.isGameNotOver()) {
            //gameView.enableActivePlayerButton();

            activePlayer = game.getActivePlayer();
            globalInfoView.updatePlayerInfo("Active player: " + game.getActivePlayerID());
            //waitForUserAction();

            activePlayer.startTurn();
            updateGUIForPhase(activePlayer);

            processPhase(activePlayer);

            game.nextPlayer();

            if (game.isRoundEnded()) {
                endRound();
            }
        }

        endGame();
    }

    public boolean actionIsAllowed(Coordinate from, Coordinate to, CardObject card){
        //check if the action is allowed in the current phase (call isMoveValid)
        // see how to get current phase
        Phase currentPhase = activePlayer.getCurrentPhase();
        currentPhase.isMoveValid(from, to, card);

        return false;
    }

    private void processPhase(Player activePlayer) {
        updateGUIForPhase(activePlayer);
        while (activePlayer.endPhaseAndStartNext()) {
            gameView.enableNextPhaseButton();
            waitForUserAction();
            updateGUIForPhase(activePlayer);
        }
    }

    private void updateGUIForPhase(Player activePlayer) {
        globalInfoView.updatePlayerInfo("Active player: " + game.getActivePlayerID() + " ("+ activePlayer.getCurrentPhase() + ")");
    }

    private void waitForUserAction() {
        waitingForUserAction = true;
        while (waitingForUserAction) {
            try {
                Thread.sleep(100); // Check every 100 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void endRound() {
        StringBuilder roundEndInfo = new StringBuilder("Round " + (game.getCurrentRound() - 1) + " ended\n");
        for (Player player : game.getPlayers()) {
            roundEndInfo.append("Player ").append(game.getPlayers().indexOf(player)).append(": ")
                    .append(player.getBeanField()).append("\n");
        }
        globalInfoView.updateGameInfo(roundEndInfo.toString());
        game.resetRoundEnded();
    }

    private void endGame() {
        game.endGame();
        globalInfoView.updateGameInfo("Game ended. Calculate winner.");
    }

    public void userActionCompleted() {
        waitingForUserAction = false;
    }

    public void userClickStart() {
        new Thread(this::startGame).start();
        gameView.updateInitialView(game.getDeck().getDrawPile(), game.getPlayers(), game.getActivePlayerID());
        game.setupPlayers();
    }


}
