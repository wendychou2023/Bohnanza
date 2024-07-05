package game;

import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;

public class CardMoveEvent {
    public Coordinate to, from;
    public CardObject card;

    public CardMoveEvent(Coordinate to, Coordinate from, CardObject card) {
        this.to = to;
        this.from = from;
        this.card = card;
    }
}
