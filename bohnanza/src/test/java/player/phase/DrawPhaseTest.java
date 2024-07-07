package player.phase;

import card.Card;
import game.CardMoveEvent;
import game.Game;
import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Compartment;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import player.Player;
import view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DrawPhaseTest {

    private DrawPhase drawPhase;
    private Game mockGame;
    private Player mockPlayer;
    private GameView mockGameView;

    @BeforeEach
    void setUp() {
        drawPhase = new DrawPhase();
        mockPlayer = mock(Player.class);
        mockGameView = mock(GameView.class);

        // Mocking the singleton instance of Game
        Game.getInstance();
    }


    @Test
    void testEndPhase() {
        // No behavior to test since endPhase is empty
        drawPhase.endPhase(mockPlayer);
        // Just verify that the method can be called without any exceptions
    }

    @Test
    void testIsMoveValid() {
        CardMoveEvent mockCardMoveEvent = mock(CardMoveEvent.class);
        assertFalse(drawPhase.isMoveValid(mockCardMoveEvent));
    }

    @Test
    void testCanEnableNextPhase() {
        assertTrue(drawPhase.canEnableNextPhase());
    }

    @Test
    void testGetNextPhase() {
        assertNull(drawPhase.getNextPhase());
    }

    @Test
    void testToString() {
        assertEquals("DrawPhase", drawPhase.toString());
    }
}
