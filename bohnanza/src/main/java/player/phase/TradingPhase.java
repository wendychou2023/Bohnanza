package player.phase;

import card.Card;
import game.CardMoveEvent;
import game.Game;
import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import player.Player;

import java.util.List;

public class TradingPhase implements Phase {
    Phase nextPhase = new DrawPhase();

    @Override
    public void startPhase(Player player) {

    }

    @Override
    public void endPhase(Player player) {

    }

    @Override
    public boolean isMoveValid(Coordinate from, Coordinate to, CardObject card) {
        return false;
    }

    @Override
    public Phase getNextPhase() {
        return nextPhase;
    }
}
