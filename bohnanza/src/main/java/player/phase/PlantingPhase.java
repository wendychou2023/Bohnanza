package player.phase;

import card.Card;
import game.CardMoveEvent;
import player.BeanField;
import player.PlantingSpot;
import player.Player;

public class PlantingPhase implements Phase {
    Phase nextPhase = new TradingPhase();

    @Override
    public void startPhase(Player player) {

    }

    @Override
    public void endPhase(Player player) {

    }

    @Override
    public boolean isMoveValid(CardMoveEvent cardMoveEvent) {
        return false;
    }

    @Override
    public Phase getNextPhase() {
        return nextPhase;
    }
}
