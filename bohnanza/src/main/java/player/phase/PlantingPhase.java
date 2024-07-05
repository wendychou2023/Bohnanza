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

    @Override
    public boolean isMoveValid(CardMoveEvent cardMoveEvent) {
        // we get from and to coordinates from the game controller
        // we need to check if
        // 1. the from coordinate is user's compartment
        // 2. to coordinate belongs to user's beanfield compartment
        // 3. check which planting spot the bean is planted -> return boolean
        //      if that planting spot of the user is available to be planted
        if (!(playerView.fromInHand(cardMoveEvent.from) && playerView.toInBeanField(cardMoveEvent.to))){
            return false;
        }






        return false;
    }

    @Override
    public Phase getNextPhase() {
        return nextPhase;
    }
}
