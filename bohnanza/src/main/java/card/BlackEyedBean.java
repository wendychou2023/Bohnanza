package card;

import io.bitbucket.plt.sdp.bohnanza.gui.CardType;

public class BlackEyedBean extends Card {
    public BlackEyedBean() {
        super(CardType.AUGEN_BOHNE);
    }

    @Override
    public int getHarvestRevenue(int nCardsHarvested) {
        if (nCardsHarvested >= 6) {
            return 4;
        } else if (nCardsHarvested >= 5) {
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
