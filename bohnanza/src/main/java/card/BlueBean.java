package card;

import io.bitbucket.plt.sdp.bohnanza.gui.CardType;

public class BlueBean extends Card {
    public BlueBean() {
        super(CardType.BLAUE_BOHNE);
    }

    @Override
    public int getHarvestRevenue(int nCardsHarvested) {
        if (nCardsHarvested >= 10) {
            return 4;
        } else if (nCardsHarvested >= 8) {
            return 3;
        } else if (nCardsHarvested >= 6) {
            return 2;
        } else if (nCardsHarvested >= 3) {
            return 1;
        } else {
            return 0;
        }
    }
}
