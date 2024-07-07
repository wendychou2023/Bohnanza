package player.phase;

import card.Card;
import game.CardMoveEvent;
import game.Deck;
import game.Game;
import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Compartment;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import player.Player;
import view.GameView;
import view.PlayerView;
import view.TradingAreaView;

import java.util.List;
import java.util.Map;

public class TradingPhase implements Phase {
    Phase nextPhase = new DrawPhase();
    Player player;
    Game game;
    Deck deck;
    List<Card> tradingCards;
    TradingAreaView tradingAreaView;
    PlayerView[] playerViews;
    GameView gameView;
    Map<CardObject, Card> cardObjectToCardMap;

    @Override
    public void startPhase(Player player, GameView gameView) {
        this.player = player;
        this.game = Game.getInstance();
        this.deck = game.getDeck();

        this.gameView = gameView;
        this.tradingAreaView = gameView.getTradingAreaView();
        this.playerViews = gameView.getPlayerViews();

        this.cardObjectToCardMap = gameView.getCardObjectToCardMap();

        tradingAreaView.setTradedCard();

        prepareForTrade();
    }

    private void prepareForTrade(){
        tradingCards = deck.drawN(2); //draw 2 cards for trading
        tradingAreaView.displayTradeCards(tradingCards);
    }

    @Override
    public void endPhase(Player player) {

    }

    boolean tradingCardSelected = false;
    boolean nonActivePlayerOfferCard = false;
    int trackNumOfTradedCard = 0;
    int nonActivePlayerId;
    int offeredCardIdx;
    /**
     * isMoveValid checks the following:
     * Case 1: Players place trading card
     *    a. from coordinate is one of the two trading cards & to coordinate is the first trading compartment
     *    b. from coordinate is one of the player compartment & to coordinate is the second trading compartment
     * Only allows players to trade one card at a time.
     * Case 2: Players plant card after trading
     *
     * @param cardMoveEvent contains from coordinate, to coordinate, and cardObject
     * @return boolean
     */
    @Override
    public boolean isMoveValid(CardMoveEvent cardMoveEvent) {
        if(trackNumOfTradedCard == tradingAreaView.getNumOfTradedCard() - 1
                && tradingAreaView.compartmentsAreEmpty()){
            trackNumOfTradedCard++;
            tradingCardSelected = false;
            nonActivePlayerOfferCard = false;
        }

        // Case 1a
        if (!tradingCardSelected && tradingAreaView.tradingCardMoved(cardMoveEvent.from)
                && tradingAreaView.withinFirstTradingCompartment(cardMoveEvent.to)){
            tradingCardSelected = true;
            return true;
        }

        boolean fromNonActivePlayerHand = false;
        for (int i = 0; i < playerViews.length; i++){
            if (i != player.getPlayerId()){
                fromNonActivePlayerHand = playerViews[i].fromInHand(cardMoveEvent.from);
            }

            if (fromNonActivePlayerHand){
                nonActivePlayerId = i;
                break;
            }
        }

        // Case 1b
        if (!nonActivePlayerOfferCard && tradingCardSelected && fromNonActivePlayerHand &&
                tradingAreaView.withinSecondTradingCompartment(cardMoveEvent.to, true)){
            nonActivePlayerOfferCard = true;

            Player nonActivePlayer = game.getPlayers().get(nonActivePlayerId);
            Card cardMoved = cardObjectToCardMap.get(cardMoveEvent.card);
            Card handCard;
            for (int j = 0; j < nonActivePlayer.getHandCards().size(); j++){
                handCard = nonActivePlayer.getHandCards().get(j);
                if (cardMoved == handCard){
                    offeredCardIdx = j;
                    break;
                }
            }

            return true;
        }

        // Case 2
        if (trackNumOfTradedCard == tradingAreaView.getNumOfTradedCard() - 1
                && !tradingAreaView.compartmentsAreEmpty()){
            if (tradingAreaView.withinFirstTradingCompartment(cardMoveEvent.from)
                    && playerViews[player.getPlayerId()].toInBeanField(cardMoveEvent.to)) {
                return canPlant(player, playerViews[player.getPlayerId()], cardMoveEvent);
            } else if (tradingAreaView.withinSecondTradingCompartment(cardMoveEvent.from, false)
                    && playerViews[nonActivePlayerId].toInBeanField(cardMoveEvent.to)) {
                Player nonActivePlayer = game.getPlayers().get(nonActivePlayerId);
                boolean result = canPlant(nonActivePlayer, playerViews[nonActivePlayerId], cardMoveEvent);
                if (result){
                    nonActivePlayer.popFromHand(offeredCardIdx);
                }
                return result;
            }
        }

        return false;
    }

    private boolean canPlant(Player plantingPlayer, PlayerView playerView, CardMoveEvent cardMoveEvent){
        int plantingSpot = playerView.getPlantingSpotIdx(cardMoveEvent.to);
        Card cardToPlant = cardObjectToCardMap.get(cardMoveEvent.card);

        if (plantingSpot >= plantingPlayer.getBeanField().getNumberOfFields()){
            return false;
        }

        if (plantingPlayer.getBeanField().canPlant(plantingSpot, cardToPlant)){
            plantingPlayer.getBeanField().plant(plantingSpot, cardToPlant);
            playerView.updateBeanFieldView(plantingSpot);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean canEnableNextPhase() {
        return (trackNumOfTradedCard == 1 || trackNumOfTradedCard == 2) && tradingAreaView.compartmentsAreEmpty();
    }

    @Override
    public Phase getNextPhase() {
        return nextPhase;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
