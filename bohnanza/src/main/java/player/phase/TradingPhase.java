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
    PlayerView playerView;
    GameView gameView;

    @Override
    public void startPhase(Player player, GameView gameView) {
        this.player = player;
        this.game = Game.getInstance();
        this.deck = game.getDeck();

        this.gameView = gameView;
        this.tradingAreaView = gameView.getTradingAreaView();

        prepareForTrade();
    }

    private void prepareForTrade(){
        tradingCards = deck.drawN(2); //draw 2 cards for trading
        tradingAreaView.displayTradeCards(tradingCards);
    }

    @Override
    public void endPhase(Player player) {

    }

    @Override
    public boolean isMoveValid(CardMoveEvent cardMoveEvent) {
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
