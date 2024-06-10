package player.phase;

import card.Card;
import game.Game;
import player.Player;

import java.util.List;

public class TradingPhase implements Phase {
    Phase nextPhase = new TradeCardPlantingPhase();
    
    @Override
    public void doPhase(Player player) {
        // contains a simple strategy for trading with other players
        // behavior could be extracted to a strategy pattern

        // step 1 - draw two cards from deck
        List<Card> tradingCards = Game.getInstance().getDeck().drawN(2);

        // step 2 - get offeres from other players
        // ToDo: UI Request: get offers from other players
        // this method could wait for press of button "done" or similar while offers come in and
        // transactions are made

        // for now: no trading -> implies that drawn cards are moved to trading area
        player.getTradingArea().addAll(tradingCards);
    }

    @Override
    public Phase getNextPhase() {
        return nextPhase;
    }
}
