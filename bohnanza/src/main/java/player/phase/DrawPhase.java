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
    Game game;

    @Override
    public void startPhase(Player player, GameView gameView) {
        game = Game.getInstance();
        player.addToHand(game.getDeck().drawN(3));
    }

    @Override
    public void endPhase(Player player) {
    }

    @Override
    public boolean isMoveValid(CardMoveEvent cardMoveEvent) {
        return false;
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
