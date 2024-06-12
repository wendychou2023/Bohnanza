package card;

public class ChiliBean extends Card {
    public ChiliBean() {
        super(CardType.FEUER_BOHNE);
    }

    @Override
    public int getHarvestRevenue(int nCardsHarvested) {
        if (nCardsHarvested >= 9) {
            return 4;
        } else if (nCardsHarvested >= 8) {
            return 3;
        } else if (nCardsHarvested >= 6) {
            return 2;
        } else if (nCardsHarvested >= 4) {
            return 1;
        } else {
            return 0;
        }
    }
}
