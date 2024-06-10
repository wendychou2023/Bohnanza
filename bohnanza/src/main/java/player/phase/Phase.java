package player.phase;

import player.Player;

public interface Phase {
    void doPhase(Player player);
    Phase getNextPhase();
}
