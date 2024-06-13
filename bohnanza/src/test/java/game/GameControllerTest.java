package game;

import card.Card;
import io.bitbucket.plt.sdp.bohnanza.gui.GUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import player.Player;
import java.util.List;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GameControllerTest {
    private Game game;
    private GameView gameView;
    private GUI gui;
    private GameController gameController;
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
    void testStart() {
        when(scanner.nextInt()).thenReturn(3);

        gameController.start();

        verify(gameView, times(1)).setGameController(gameController);
        verify(game, times(1)).setGameController(gameController);
        verify(game, times(3)).addPlayer(any(Player.class));
        verify(gui, times(1)).start();
    }

    @Test
    void testStartLessThanThree() {
        when(scanner.nextInt()).thenReturn(2);

        gameController.start();

        assertThrows(IllegalAccessError.class, gameController::start);
        verify(gameView, times(1)).setGameController(gameController);
        verify(game, times(1)).setGameController(gameController);
        verify(game, times(2)).addPlayer(any(Player.class));
        verify(gui, times(1)).start();
    }

    @Test
    void testUpdatePlayerInfo() {
        String playerInfo = "Player info";
        gameController.updatePlayerInfo(playerInfo);

        verify(gameView, times(1)).updatePlayerInfo(playerInfo);
    }

    @Test
    void testUpdateGameInfo() {
        String gameInfo = "Game info";
        gameController.updateGameInfo(gameInfo);

        verify(gameView, times(1)).updateGameInfo(gameInfo);
    }

    @Test
    void testUserActionCompleted() {
        gameController.userActionCompleted();

        verify(game, times(1)).userActionCompleted();
    }

    @Test
    void testUserClickStart() {
        List<Card> drawPile = mock(List.class);
        List<Player> players = mock(List.class);

        when(game.getDeck()).thenReturn(mock(Deck.class));
        when(game.getDeck().getDrawPile()).thenReturn(drawPile);
        when(game.getPlayers()).thenReturn(players);

        gameController.userClickStart();

        verify(gameView, times(1)).updateInitialView(drawPile, players);
    }

    @Test
    void testUserUpdateHandCard() {
        Player player = mock(Player.class);
        int playerId = 1;

        gameController.userUpdateHandCard(player, playerId);

        verify(gameView, times(1)).updatePlayerHandCard(player, playerId);
    }
}