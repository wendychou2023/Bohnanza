package player.phase;

import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Compartment;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import player.Player;
import game.CardMoveEvent;
import view.GameView;
import view.PlayerView;

public interface Phase {
    void startPhase(Player player, GameView gameView);
    void endPhase(Player player);
    Compartment isMoveValid(CardMoveEvent cardMoveEvent);
    boolean canEnableNextPhase();
    Phase getNextPhase();
    @Override
    public String toString();
}
