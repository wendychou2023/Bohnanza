package card;

import java.util.Objects;

public abstract class Card {
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
