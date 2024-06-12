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

    private Scanner scanner;

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

        new Thread(game::startGame).start();
        gui.start();

    }

    public void updatePlayerInfo(String playerInfo){
        gameView.updatePlayerInfo(playerInfo); //Show current player on gui
    }

    public void updateGameInfo(String gameInfo){
        gameView.updateGameInfo(gameInfo); //Show current game info on gui
    }

    public void userActionCompleted() {
        // Notify the game that the user action is completed
        game.userActionCompleted();
    }

    public void userClickStart() {
        gameView.updateInitialView(game.getDeck().getDrawPile(), game.getPlayers());
    }
    public void userUpdateHandCard(Player player, int playerId) {
        gameView.updatePlayerHandCard(player, playerId);
    }

    public void updateGUI(){
        //after changes in model, invoke gui update
    }
}
