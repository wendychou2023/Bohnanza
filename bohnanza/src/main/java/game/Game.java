package game;

import player.Player;

import java.util.LinkedList;
import java.util.List;

public class Game {
    private static Game instance = null;
    private GameController gameController;

    public final int MAX_ROUNDS = 3;

    private final List<Player> players;
    private final Deck deck;

    private int activePlayerID;
    private int currentRound = 0;
    private boolean roundEnded = false; //flag to check if the round has ended

    public int getActivePlayerID() {
        return activePlayerID;
    }

    private Game() {
        this.deck = new Deck();
        this.players = new LinkedList<>();
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Deck getDeck() {
        return deck;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    void setupPlayers() {
        //give each player 5 cards
        int numberOfBeanfields = players.size() == 3 ? 3 : 2;
        for (Player player : players) {

            //draw 5 cards for each player
            player.addToHand(deck.drawN(5));
        //            gameController.userUpdateHandCard(player);

            //set number of beanfields
            player.getBeanField().addBeanfields(numberOfBeanfields);
        }
    }

    private void nextPlayer(){
        activePlayerID = (activePlayerID  + 1)%players.size();
    }

    /**
     * increases round counter by 1
     */
    public void nextRound() {
        if (currentRound >= MAX_ROUNDS) {
            return;
        }

        currentRound += 1;
        roundEnded = true;
    }

    public void startGame() {
        this.activePlayerID = 0;

        //setup players
        String startGameInfo = "Game started. Setup players";
        gameController.updateGameInfo(startGameInfo);
        setupPlayers();

        //game loop
        while (currentRound < MAX_ROUNDS) {
            players.get(activePlayerID).takeTurn();
            // Display current player on gui
            String currentPlayerInfo = "Current player: " + activePlayerID;
            gameController.updatePlayerInfo(currentPlayerInfo);

            // Wait for user action
            waitingForUserAction = true;
            while (waitingForUserAction) {
                try {
                    Thread.sleep(100); // Check every 100 ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            nextPlayer();

            if (roundEnded) {
                System.out.println("Round " + (currentRound - 1) + " ended");
                StringBuilder roundEndInfo = new StringBuilder("Round " + (currentRound - 1) + " ended\n");

                for (Player player:players) {
                    System.out.println(player.getBeanField());
                    roundEndInfo.append("Player ").append(players.indexOf(player)).append(": ")
                            .append(player.getBeanField()).append("\n");
                }
                System.out.println();
                gameController.updateGameInfo(roundEndInfo.toString());

                roundEnded = false;
            }
        }

        //end game (cleanup and calculate winner)
        String endGameInfo = "Game ended. Calculate winner";
        gameController.updateGameInfo(endGameInfo);
        endGame();

    }

    /**
     * end the game and calculate the winner
     * makes all players harvest all their fields
     * prints winner to console
     */
    public void endGame(){
        for (Player player:players) {
            player.getBeanField().harvestAll();
        }

        //check who is the winner(calculate the coins and check if there is any tied winner)
        int winningIdx = 0;
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i).getCoins() > players.get(winningIdx).getCoins()) {
                winningIdx = i;
            }
        }

        System.out.println(
                "The winner is: " + players.get(winningIdx)
                        + " at position " + winningIdx
                        + " with " + players.get(winningIdx).getCoins() + " coins"
        );
    }

    private  boolean waitingForUserAction = true;
    public void userActionCompleted() {
        waitingForUserAction = false;
    }
}
