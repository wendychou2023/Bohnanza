package player.phase;

import card.Card;
import game.Game;
import player.BeanField;
import player.Player;

public class TradeCardPlantingPhase implements Phase {
    Phase nextPhase;

    @Override
    public void doPhase(Player player) {
        //ToDo: UI Request: make each player plant traded cards

        // for now:
        for (Player playerIter : Game.getInstance().getPlayers()) {
            BeanField beanField = playerIter.getBeanField();

            for (Card card: playerIter.getTradingArea()) {
                if (beanField.canPlant(card)) {
                    for (int i = 0; i < beanField.getNumberOfFields(); i++) {
                        if (beanField.canPlant(i, card)) {
                            beanField.plant(i, card);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < beanField.getNumberOfFields(); i++) {
                        if (beanField.canHarvest(i)) {
                            beanField.harvest(i);
                            beanField.plant(i, card);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public Phase getNextPhase() {
        return nextPhase;
    }
}
