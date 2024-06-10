package card;

public class StinkBean extends Card {

    @Override
    public int getHarvestRevenue(int nCardsHarvested) {
        if (nCardsHarvested >= 8) {
            return 4;
        } else if (nCardsHarvested >= 7) {
            return 3;
        } else if (nCardsHarvested >= 5) {
            return 2;
        } else if (nCardsHarvested >= 3) {
            return 1;
        } else {
            return 0;
        }
    }
}