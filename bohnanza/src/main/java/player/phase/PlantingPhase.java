package player.phase;

import card.Card;
import game.CardMoveEvent;
import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.CardType;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import player.BeanField;
import player.PlantingSpot;
import player.Player;
import view.DeckView;
import view.GameView;
import view.PlayerView;

import java.util.HashMap;
import java.util.Map;

public class PlantingPhase implements Phase {
    Phase nextPhase = new TradingPhase();
    PlayerView playerView;
    GameView gameView;
    Player player;
    Map<CardObject, Card> cardObjectToCardMap;
    int numOfPlantedCard = 0;

    public PlantingPhase(PlayerView playerView){
        this.playerView = playerView;
    }

    @Override
    public void startPhase(Player player, GameView gameView) {
        this.player = player;
        this.gameView = gameView;
        this.cardObjectToCardMap = gameView.getCardObjectToCardMap();
    }

    @Override
    public void endPhase(Player player) {

    }
    private Card convertToCard(CardObject cardObject) {
        // Access some property or method of cardObject to get the card type
        // This is just a placeholder, replace with your actual logic
        CardType cardType = cardObject.getCardType();
        Card card = new Card(cardType) {
            @Override
            public int getHarvestRevenue(int nCardsHarvested) {
                return 0;
            }
        };
        return card;
    }

    /**
     * @param cardMoveEvent contains from coordinate, to coordinate, and cardObject
     * @return boolean
     * isMoveValid checks the following:
     * Case 1: player can only plant maximum 2 cards
     * Case 2: from coordinate is in player's compartment & to coordinate belongs to player's beanfield compartment
     * Case 3: Planted card is the first card in player's handcard
     * Case 4: if the card can be planted in the selected planting spot
     */
    @Override
    public boolean isMoveValid(CardMoveEvent cardMoveEvent) {
        if (numOfPlantedCard == 2){
            return false;
        }

        if (!(playerView.fromInHand(cardMoveEvent.from) && playerView.toInBeanField(cardMoveEvent.to))) {
            return false;
        }

        int plantingSpot = playerView.getPlantingSpotIdx(cardMoveEvent.to);
        Card cardToPlant = cardObjectToCardMap.get(cardMoveEvent.card);

        if (cardToPlant != player.getHandCards().get(0)){
            return false;
        }

        if (player.getBeanField().canPlant(plantingSpot, cardToPlant)){
            player.getBeanField().plant(plantingSpot, cardToPlant);
            player.popFromHand();
            numOfPlantedCard++;
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean canEnableNextPhase() {
        return numOfPlantedCard >= 1;
    }


    @Override
    public Phase getNextPhase() {
        return nextPhase;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
