package card;

import io.bitbucket.plt.sdp.bohnanza.gui.CardType;

public class GardenBean extends Card {
    public GardenBean() {
        super(CardType.GARTEN_BOHNE);
    }

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
