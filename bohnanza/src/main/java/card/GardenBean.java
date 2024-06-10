package card;

public class GardenBean extends Card {

    @Override
    public int getHarvestRevenue(int nCardsHarvested) {
        if (nCardsHarvested >= 3) {
            return 3;
        } else if (nCardsHarvested >= 2) {
            return 2;
        } else {
            return 0;
        }
    }

}
