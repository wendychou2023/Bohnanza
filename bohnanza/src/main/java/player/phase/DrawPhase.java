package player.phase;

import game.Game;
import player.Player;

public class DrawPhase implements Phase {
    Phase nextPhase = null; // no next phase

    @Override
    public void doPhase(Player player) {
        player.addToHand(Game.getInstance().getDeck().drawN(3));
    }

    @Override
    public Phase getNextPhase() {
        return nextPhase;
    }
}
