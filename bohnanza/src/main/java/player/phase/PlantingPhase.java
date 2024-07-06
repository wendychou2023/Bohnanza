package player.phase;

import card.Card;
import game.CardMoveEvent;
import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.CardType;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import player.BeanField;
import player.PlantingSpot;
import player.Player;
import view.GameView;
import view.PlayerView;

public class PlantingPhase implements Phase {
    Phase nextPhase = new TradingPhase();
    PlayerView playerView;
    GameView gameView;
    Player player;

    public PlantingPhase(PlayerView playerView){
        this.playerView = playerView;
    }

    @Override
    public void startPhase(Player player, GameView gameView) {
        this.player = player;
        this.gameView = gameView;
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
     * 1. from coordinate is in player's compartment
     * 2. to coordinate belongs to player's beanfield compartment
     * 3. if the card can be planted in the selected planting spot
     */
    @Override
    public boolean isMoveValid(CardMoveEvent cardMoveEvent) {
        if (!(playerView.fromInHand(cardMoveEvent.from) && playerView.toInBeanField(cardMoveEvent.to))) {
            return false;
        }

        int plantingSpot = playerView.getPlantingSpotIdx(cardMoveEvent.to);
        Card cardToPlant = convertToCard(cardMoveEvent.card);

        PlantingSpot spot = player.getBeanField().getPlantingSpots().get(plantingSpot);
        Card plantedCard = spot.getPlantedCard();


        BeanField beanField = player.getBeanField();
        //System.out.println(beanField.getPlantedBeans());

        if (!beanField.isEmpty(plantingSpot)) {
            Card existingCard = plantedCard;
            //System.out.println(existingCard.getCardType() + " " + cardToPlant.getCardType());
            if (!existingCard.getCardType().equals(cardToPlant.getCardType())) {
                return false;
            }
        }
        beanField.plant(plantingSpot, cardToPlant);
        //System.out.println(beanField.getPlantedBeans());
        return true;
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
