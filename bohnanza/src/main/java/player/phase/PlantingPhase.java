package player.phase;

import card.Card;
import player.BeanField;
import player.PlantingSpot;
import player.Player;

public class PlantingPhase implements Phase {
    Phase nextPhase = new TradingPhase();

    @Override
    public void doPhase(Player player) {
        if (player.getHandCards().isEmpty()) {
            return;
        }

        // plant first card from hand
//        Card card = player.popFromHand();
//        boolean cardPlanted = false;
//        BeanField beanField = player.getBeanField();


        //ToDo: UI Request: let player choose where to plant the card
        // and here just check if chosen spot is valid
        plantCard(player);
//        int selectedField = player.getGameController().requestUserPlantAction("Choose which field to plant first card: " + card.toString(), player);
//        if (beanField.canPlant(selectedField, card)) {
//            beanField.plant(selectedField, card);
//            player.getGameController().updatePlayerHandCard(player);
//            player.getGameController().updatePlayerBeanfield(player);
//            cardPlanted = true;
//        }

        /*for (int i = 0; i < beanField.getNumberOfFields(); i++) {
            if (beanField.canPlant(i, card)) {
                beanField.plant(i, card);
                cardPlanted = true;
                break;
            }
        }*/
//        if (!cardPlanted) {
//            for (int i = 0; i < beanField.getNumberOfFields(); i++) {
//                if (beanField.canHarvest(i)) {
//                    beanField.harvest(i);
//                    beanField.plant(i, card);
//                }
//            }
//        }


        //ToDo: UI Request: Plant second card?
        boolean plantSecondCard = player.getGameController().requestUserYesOrNo("Do you want to plant second card: " + player.getHandCards().get(0).toString() + "?", player);
        if(plantSecondCard){
            plantCard(player);
        }
    }

    private void plantCard(Player player){
        // plant first card from hand
        Card card = player.popFromHand();
        boolean cardPlanted = false;
        BeanField beanField = player.getBeanField();

        int selectedField = player.getGameController().requestUserPlantAction("Choose which field to plant card: " + card.toString(), player);
        if (beanField.canPlant(selectedField, card)) {
            beanField.plant(selectedField, card);
            player.getGameController().updatePlayerHandCard(player);
            player.getGameController().updatePlayerBeanfield(player);
            cardPlanted = true;
        }

        if (!cardPlanted) {
            for (int i = 0; i < beanField.getNumberOfFields(); i++) {
                if (beanField.canHarvest(i)) {
                    beanField.harvest(i);
                    beanField.plant(i, card);
                }
            }
        }
    }

    @Override
    public Phase getNextPhase() {
        return nextPhase;
    }
}
