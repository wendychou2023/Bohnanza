package card;

import io.bitbucket.plt.sdp.bohnanza.gui.CardType;

import java.util.Objects;

public abstract class Card {
    private final CardType cardType;
    public Card(CardType cardType) {
        this.cardType = cardType;
    }

    public CardType getCardType() {
        return cardType;
    }
    public abstract int getHarvestRevenue(int nCardsHarvested);

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
