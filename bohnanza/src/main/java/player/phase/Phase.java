package player.phase;

import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import player.Player;
import game.CardMoveEvent;
import view.GameView;
import view.PlayerView;

public interface Phase {
    void startPhase(Player player, GameView gameView);
    void endPhase(Player player);
    boolean isMoveValid(CardMoveEvent cardMoveEvent);
    Phase getNextPhase();
    @Override
    public String toString();
}
