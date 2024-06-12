package card;

public class SoyBean extends Card {
    public SoyBean() {
        super(CardType.SOJA_BOHNE);
    }

    @Override
    public int getHarvestRevenue(int nCardsHarvested) {
        if (nCardsHarvested >= 7) {
            return 4;
        } else if (nCardsHarvested >= 6) {
            return 3;
        } else if (nCardsHarvested >= 4) {
            return 2;
        } else if (nCardsHarvested >= 2) {
            return 1;
        } else {
            return 0;
        }
    }
}
