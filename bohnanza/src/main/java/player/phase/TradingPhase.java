package player.phase;

import card.Card;
import game.CardMoveEvent;
import game.Deck;
import game.Game;
import io.bitbucket.plt.sdp.bohnanza.gui.CardObject;
import io.bitbucket.plt.sdp.bohnanza.gui.Coordinate;
import player.Player;
import view.GameView;
import view.PlayerView;
import view.TradingAreaView;

import java.util.List;

public class TradingPhase implements Phase {
    Phase nextPhase = new DrawPhase();
    Player player;
    Game game;
    Deck deck;
    List<Card> tradingCards;
    TradingAreaView tradingAreaView;
    PlayerView[] playerViews;
    GameView gameView;

    @Override
    public void startPhase(Player player, GameView gameView) {
        this.player = player;
        this.game = Game.getInstance();
        this.deck = game.getDeck();

        this.gameView = gameView;
        this.tradingAreaView = gameView.getTradingAreaView();
        this.playerViews = gameView.getPlayerViews();

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
    /**
     * isMoveValid checks the following:
     * Case 1: from coordinate is one of the two trading cards & to coordinate is the first trading compartment
     * Case 2: from coordinate is one of the player compartment & to coordinate is the second trading compartment
     * Only allows players to trade one card at a time.
     *
     * @param cardMoveEvent contains from coordinate, to coordinate, and cardObject
     * @return boolean
     */
    @Override
    public boolean isMoveValid(CardMoveEvent cardMoveEvent) {
        if (!tradingCardSelected && tradingAreaView.tradingCardMoved(cardMoveEvent.from)
                && tradingAreaView.placeAtFirstTradingCompartment(cardMoveEvent.to)){
            tradingCardSelected = true;
            return true;
        }

        boolean fromNonActivePlayerHand = false;
        for (int i = 0; i < playerViews.length; i++){
            if (i != player.getPlayerId()){
                fromNonActivePlayerHand = playerViews[i].fromInHand(cardMoveEvent.from);
            }

            if (fromNonActivePlayerHand){
                break;
            }
        }

        if (!nonActivePlayerOfferCard && tradingCardSelected && fromNonActivePlayerHand &&
                tradingAreaView.placeAtSecondTradingCompartment(cardMoveEvent.to)){
            nonActivePlayerOfferCard = true;
            return true;
        }

        return false;
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
