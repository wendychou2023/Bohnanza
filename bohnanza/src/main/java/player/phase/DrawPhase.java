package player.phase;

import game.CardMoveEvent;
import game.Game;
import player.Player;

public class DrawPhase implements Phase {
    Phase nextPhase = null; // no next phase

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
