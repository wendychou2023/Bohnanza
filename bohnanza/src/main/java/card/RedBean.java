package card;

public class RedBean extends Card {
    public RedBean() {
        super(CardType.ROTE_BOHNE);
    }

    @Override
    public int getHarvestRevenue(int nCardsHarvested) {
        if (nCardsHarvested >= 5) {
            return 4;
        } else if (nCardsHarvested >= 4) {
            return 3;
        } else if (nCardsHarvested >= 3) {
            return 2;
        } else if (nCardsHarvested >= 2) {
            return 1;
        } else {
            return 0;
        }
    }

}
