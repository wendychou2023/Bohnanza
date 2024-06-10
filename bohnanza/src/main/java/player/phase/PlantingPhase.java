package player.phase;

import card.Card;
import player.Player;

public class PlantingPhase implements Phase {
    Phase nextPhase = new TradingPhase();

    @Override
    public void doPhase(Player player) {
        if (player.getHandCards().isEmpty()) {
            return;
        }

        // plant first card from hand
        Card card = player.popFromHand();
        if (player.getBeanField().canPlant(card)) {
            player.getBeanField().plant(card);
        } else {
            //ToDo: UI Request: which field to harvest?
            player.getBeanField().harvest(player.getBeanField().getHarvestableBeans().get(0));
            player.getBeanField().plant(card);
        }

        //ToDo: UI Request: Plant second card?
    }

    @Override
    public Phase getNextPhase() {
        return nextPhase;
    }
}
