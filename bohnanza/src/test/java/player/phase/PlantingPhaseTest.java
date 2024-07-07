package player.phase;

import card.Card;
import game.CardMoveEvent;
import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.CardType;
import io.bitbucket.plt.sdp.bohnanza.gui.Compartment;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import player.BeanField;
import player.Player;
import view.GameView;
import view.PlayerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlantingPhaseTest {

    private PlantingPhase plantingPhase;
    private PlayerView mockPlayerView;
    private GameView mockGameView;
    private Player mockPlayer;
    private BeanField mockBeanField;
    private CardMoveEvent mockCardMoveEvent;
    private CardObject mockCardObject;
    private Card mockCard;
    private Map<CardObject, Card> cardObjectToCardMap;

    @BeforeEach
    void setUp() {
        mockPlayerView = mock(PlayerView.class);
        mockGameView = mock(GameView.class);
        mockPlayer = mock(Player.class);
        mockBeanField = mock(BeanField.class);
        mockCardMoveEvent = mock(CardMoveEvent.class);
        mockCardObject = mock(CardObject.class);
        mockCard = mock(Card.class);
        cardObjectToCardMap = new HashMap<>();

        when(mockGameView.getCardObjectToCardMap()).thenReturn(cardObjectToCardMap);
        when(mockPlayer.getBeanField()).thenReturn(mockBeanField);


        plantingPhase = new PlantingPhase(mockPlayerView);
    }

    @Test
    void testStartPhase() {
        plantingPhase.startPhase(mockPlayer, mockGameView);
        assertEquals(mockPlayer, plantingPhase.player);
        assertEquals(mockGameView, plantingPhase.gameView);
        assertEquals(cardObjectToCardMap, plantingPhase.cardObjectToCardMap);
    }

    @Test
    void testEndPhase() {
        // endPhase does not have any logic, just verify it can be called
        plantingPhase.endPhase(mockPlayer);
    }

    @Test
    void testIsMoveValid_MaximumCardsPlanted() {
        plantingPhase.numOfPlantedCard = 2;
        assertFalse(plantingPhase.isMoveValid(mockCardMoveEvent));
    }

    @Test
    void testIsMoveValid_InvalidCoordinates() {
        when(mockPlayerView.fromInHand(any(Coordinate.class))).thenReturn(false);
        when(mockPlayerView.toInBeanField(any(Coordinate.class))).thenReturn(false);

        assertFalse(plantingPhase.isMoveValid(mockCardMoveEvent));
    }

    @Test
    void testIsMoveValid_NotFirstCardInHand() {
        when(mockPlayerView.fromInHand(any(Coordinate.class))).thenReturn(true);
        when(mockPlayerView.toInBeanField(any(Coordinate.class))).thenReturn(true);
        when(mockPlayerView.getPlantingSpotIdx(any(Coordinate.class))).thenReturn(0);
        when(mockPlayer.getHandCards()).thenReturn(List.of(mock(Card.class), mockCard));

        cardObjectToCardMap.put(mockCardObject, mockCard);

        assertFalse(plantingPhase.isMoveValid(mockCardMoveEvent));
    }

    @Test
    void testIsMoveValid_CannotPlantCard() {
        when(mockPlayerView.fromInHand(any(Coordinate.class))).thenReturn(true);
        when(mockPlayerView.toInBeanField(any(Coordinate.class))).thenReturn(true);
        when(mockPlayerView.getPlantingSpotIdx(any(Coordinate.class))).thenReturn(0);
        when(mockPlayer.getHandCards()).thenReturn(List.of(mockCard));

        cardObjectToCardMap.put(mockCardObject, mockCard);
        when(mockBeanField.canPlant(0, mockCard)).thenReturn(false);

        assertFalse(plantingPhase.isMoveValid(mockCardMoveEvent));
    }

    @Test
    void testIsMoveValid_SuccessfulPlant() {
        when(mockPlayerView.fromInHand(any(Coordinate.class))).thenReturn(true);
        when(mockPlayerView.toInBeanField(any(Coordinate.class))).thenReturn(true);
        when(mockPlayerView.getPlantingSpotIdx(any(Coordinate.class))).thenReturn(0);
        when(mockPlayer.getHandCards()).thenReturn(List.of(mockCard));

        cardObjectToCardMap.put(mockCardObject, mockCard);
        when(mockBeanField.canPlant(0, mockCard)).thenReturn(true);

        assertTrue(plantingPhase.isMoveValid(mockCardMoveEvent));
        verify(mockBeanField).plant(0, mockCard);
        verify(mockPlayer).popFromHand(0);
        verify(mockPlayerView).updateBeanFieldView(0);
        assertEquals(1, plantingPhase.numOfPlantedCard);
    }

    @Test
    void testCanEnableNextPhase() {
        plantingPhase.numOfPlantedCard = 0;
        assertFalse(plantingPhase.canEnableNextPhase());

        plantingPhase.numOfPlantedCard = 1;
        assertTrue(plantingPhase.canEnableNextPhase());
    }

    @Test
    void testGetNextPhase() {
        assertTrue(plantingPhase.getNextPhase() instanceof TradingPhase);
    }

    @Test
    void testToString() {
        assertEquals("PlantingPhase", plantingPhase.toString());
    }
}
