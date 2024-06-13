package game;

import io.bitbucket.plt.sdp.bohnanza.gui.GUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameTest {
    private GameController gameController;
    private Game game;
    private GameView gameView;
    private GUI gui;
    private String[] args;
    private Scanner scanner;

    @BeforeEach
    void setUp() {
    game = mock(Game.class);
    gameView = mock(GameView.class);
    gui = mock(GUI.class);
    scanner = mock(Scanner.class);
    gameController = new GameController(game, gameView, gui);
    gameController.scanner = scanner;
    }

    @Test
    void testInitialization() {
        assertNotNull(game);
        assertEquals(0, game.getCurrentRound());
        assertEquals(0, game.getActivePlayerID());
        assertEquals(0, game.getPlayers().size());
    }

    @Test
    void testAddPlayer() {
        Player player1 = new Player();
        Player player2 = new Player();

        game.addPlayer(player1);
        game.addPlayer(player2);

        assertEquals(2, game.getPlayers().size());

    }

    @Test
    void testSetupThreePlayers() {
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();


        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);


        game.setupPlayers();

        assertEquals(5, player1.getHandCards().size());
        assertEquals(5, player2.getHandCards().size());
        assertEquals(3, player1.getBeanField().getNumberOfFields());
        assertEquals(3, player2.getBeanField().getNumberOfFields());
    }

    @Test
    void testSetupMoreThanThreePlayers() {
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();

        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);

        game.setupPlayers();

        assertEquals(5, player1.getHandCards().size());
        assertEquals(5, player2.getHandCards().size());
        assertEquals(2, player1.getBeanField().getNumberOfFields());
        assertEquals(2, player2.getBeanField().getNumberOfFields());
    }

    @Test
    void startGameForLessThanThree() {
        Player player1 = new Player();
        Player player2 = new Player();

        game.addPlayer(player1);
        game.addPlayer(player2);

        // Use assertThrows to check for IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            game.startGame();
        });

        // Optionally, assert the exception message or further details if needed
        assertEquals("The game requires at least 3 players to start.", exception.getMessage());

        // Assert that game state remains unchanged
        assertEquals(0, game.getCurrentRound());
        assertEquals(0, game.getActivePlayerID()); // Check if active player is correctly set (should be 0)
        assertEquals(0, player1.getHandCards().size()); // Check if players have no cards in hand after invalid start
        assertEquals(0, player2.getHandCards().size());
    }

    @Test
    void startGameForThree() {
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();

        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);

        assertDoesNotThrow(game::startGame);
    }

    @Test
    void startGameForMoreThanFive() {
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();
        Player player6 = new Player();

        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        game.addPlayer(player5);
        game.addPlayer(player6);


        assertThrows(IllegalAccessError.class, game::startGame);
    }
}
