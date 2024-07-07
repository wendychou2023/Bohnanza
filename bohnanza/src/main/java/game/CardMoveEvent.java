package game;

import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;

public class CardMoveEvent {
    public Coordinate to, from;
    public CardObject card;

    public CardMoveEvent(Coordinate from, Coordinate to, CardObject card) {
        this.from = from;
        this.to = to;
        this.card = card;
    }
}
