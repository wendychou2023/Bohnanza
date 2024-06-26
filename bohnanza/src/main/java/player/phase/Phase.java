package player.phase;

import player.Player;
import game.CardMoveEvent;

public interface Phase {
    void startPhase(Player player);
    void endPhase(Player player);
    boolean isMoveValid(CardMoveEvent cardMoveEvent);
    Phase getNextPhase();
}
