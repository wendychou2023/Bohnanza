package player.phase;

import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import player.Player;
import game.CardMoveEvent;
import view.PlayerView;

public interface Phase {
    void startPhase(Player player);
    void endPhase(Player player);
    boolean isMoveValid(Coordinate from, Coordinate to, CardObject card);
    Phase getNextPhase();
}
