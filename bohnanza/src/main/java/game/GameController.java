package game;

import card.Card;
import io.bitbucket.plt.sdp.bohnanza.gui.GUI;
import player.Player;

import java.util.List;
import java.util.Scanner;

public class GameController {
    //    private static GameController instance = null;
    private Game game;
    private GameView gameView;
    private GUI gui;
    private boolean waitingForUserAction = true;

    Scanner scanner;

    public GameController(Game game, GameView gameView, GUI gui) {
        this.game = game;
        this.gameView = gameView;
        this.gui = gui;
        this.scanner = new Scanner(System.in);

        // Set this controller in the game and gameGUI
        game.setGameController(this);
        gameView.setGameController(this);
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
        gameView.updateGameInfo("Game started. Current round: " + game.getCurrentRound());
        gameLoop();
    }

    private void gameLoop() {
        while (game.isGameNotOver()) {
            Player activePlayer = game.getPlayers().get(game.getActivePlayerID());
            gameView.updatePlayerInfo("Active player: " + game.getActivePlayerID());
            waitForUserAction();

            activePlayer.takeTurn();
            updateGUIForPhase(activePlayer);

            processPhase(activePlayer);

            game.nextPlayer();

            if (game.isRoundEnded()) {
                endRound();
            }
        }

        endGame();
    }

    private void processPhase(Player activePlayer) {
        while (activePlayer.executeNextPhase()) {
            waitForUserAction();
            updateGUIForPhase(activePlayer);
        }
    }

    private void updateGUIForPhase(Player activePlayer) {
        gameView.updatePlayerInfo("Active player: " + game.getActivePlayerID() + " ("+ activePlayer.getCurrentPhase() + ")");
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
        gameView.updateGameInfo(roundEndInfo.toString());
        game.resetRoundEnded();
    }

    private void endGame() {
        game.endGame();
        gameView.updateGameInfo("Game ended. Calculate winner.");
    }

    public void userActionCompleted() {
        waitingForUserAction = false;
    }

    public void userClickStart() {
        new Thread(this::startGame).start();
        game.setupPlayers();
        gameView.updateInitialView(game.getDeck().getDrawPile(), game.getPlayers(), game.getActivePlayerID());
    }

    private int selectedPlantOption;
    public void selectedPlantOption(int option){
        selectedPlantOption = option;
    }

    public Integer requestUserPlantAction(String message, Player player) {
        gameView.requestUserPlantAction(message, player, game.getActivePlayerID());
        waitForUserAction();
        return selectedPlantOption;
    }

    public void requestUserHarvestAction(String message, List<Card> harvestableBean, Player player) {
        gameView.requestUserHarvestAction(message, harvestableBean, player);
    }

//    public void harvestField(int fieldId, Player player){
//        player.getBeanField().harvest(player.getBeanField().getHarvestableBeans().get(0));
//    }
}
