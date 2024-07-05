package player.phase;

import card.Card;
import game.CardMoveEvent;
import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import player.BeanField;
import player.PlantingSpot;
import player.Player;
import view.PlayerView;

public class PlantingPhase implements Phase {
    Phase nextPhase = new TradingPhase();
    PlayerView playerView;
    Player player;

    public PlantingPhase(PlayerView playerView){
        this.playerView = playerView;
    }

    @Override
    public void startPhase(Player player) {
        this.player = player;
    }

    @Override
    public void endPhase(Player player) {

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
        if (!(playerView.fromInHand(cardMoveEvent.from) && playerView.toInBeanField(cardMoveEvent.to))){
            return false;
        }

        int plantingSpot = playerView.getPlantingSpotIdx(cardMoveEvent.to);
//        if (player.getBeanField().canPlant(plantingSpot, cardMoveEvent.card.card)){
//              player.getBF.plant()
//        }

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
