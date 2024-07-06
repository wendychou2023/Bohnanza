package player.phase;

import game.CardMoveEvent;
import game.Game;
import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Compartment;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import player.Player;
import view.GameView;

public class DrawPhase implements Phase {
    Phase nextPhase = null; // no next phase

    @Override
    public void startPhase(Player player, GameView gameView) {

    }

    @Override
    public void endPhase(Player player) {

    }

    @Override
    public Compartment isMoveValid(CardMoveEvent cardMoveEvent) {
        return null;
    }

    @Override
    public boolean canEnableNextPhase() {
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
