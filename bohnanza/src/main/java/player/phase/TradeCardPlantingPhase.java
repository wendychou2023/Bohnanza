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
        Game.getInstance().getPlayers().forEach(p -> {
            for (Card card : p.getTradingArea()) {
                BeanField field = p.getBeanField();

                if (field.canPlant(card)) {
                    field.plant(card);
                } else {
                    // harvest first harvestable field if necessary
                    // could be optimized
                    field.harvest(field.getHarvestableBeans().get(0));
                    field.plant(card);
                }
            }
            p.getTradingArea().clear();
        });
    }

    @Override
    public Phase getNextPhase() {
        return nextPhase;
    }
}
