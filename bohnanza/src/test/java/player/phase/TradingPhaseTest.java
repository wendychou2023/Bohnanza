package player.phase;

import card.Card;
import game.CardMoveEvent;
import game.Deck;
import game.Game;
import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Compartment;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import io.bitbucket.plt.sdp.bohnanza.gui.GUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import player.Player;
import player.phase.TradingPhase;
import view.GameView;
import view.PlayerView;
import view.TradingAreaView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TradingPhaseTest {

    @Mock private GameView mockGameView;
    @Mock private Player mockPlayer;
    @Mock private Deck mockDeck;
    @Mock private TradingAreaView mockTradingAreaView;
    @Mock private PlayerView mockPlayerView;
    @Mock private Game mockGame;
    @Mock private GUI gui;
    @Mock private String[] args;
    @Mock private Map<CardObject, Card> mockCardObjectToCardMap;

    private TradingPhase tradingPhase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tradingPhase = new TradingPhase();
    }

    @Test
    void startPhase_shouldInitializeCorrectly() {
        // Mock behavior for GameView and Game dependencies
        when(mockGameView.getTradingAreaView()).thenReturn(mockTradingAreaView);
        when(mockGameView.getPlayerViews()).thenReturn(new PlayerView[] { mockPlayerView });
        when(mockGame.getDeck()).thenReturn(mockDeck);
        when(Game.getInstance()).thenReturn(mockGame);

        // Stubbing for methods that would be called in startPhase
        when(mockTradingAreaView.getNumOfTradedCard()).thenReturn(2);
        when(mockDeck.drawN(2)).thenReturn(Collections.emptyList()); // Adjust based on your drawN() behavior

        // Call the method under test
        tradingPhase.startPhase(mockPlayer, mockGameView);

        // Verify interactions and assertions
        verify(mockTradingAreaView).setTradedCard();
        verify(mockTradingAreaView).displayTradeCards(anyList()); // Verify that displayTradeCards is called with correct arguments
    }

    @Test
    void isMoveValid_shouldReturnTrueForValidMoves() {
        // Prepare test data
        CardMoveEvent mockMoveEvent = mock(CardMoveEvent.class);
        when(mockGameView.getCardObjectToCardMap()).thenReturn(mockCardObjectToCardMap);
        when(mockTradingAreaView.tradingCardMoved(any(Coordinate.class))).thenReturn(true);
        when(mockTradingAreaView.withinFirstTradingCompartment(any(Coordinate.class))).thenReturn(true);

        // Stubbing for other methods as needed
        when(mockPlayerView.fromInHand(any(Coordinate.class))).thenReturn(true);
        when(mockTradingAreaView.withinSecondTradingCompartment(any(Coordinate.class), eq(true))).thenReturn(true);

        // Call the method under test
        boolean result = tradingPhase.isMoveValid(mockMoveEvent);

        // Assert the result
        assertTrue(result);
    }


}
