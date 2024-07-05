package player;

import card.Card;
import game.GameController;
import org.apache.commons.lang3.NotImplementedException;
import player.phase.Phase;
import player.phase.PlantingPhase;
import view.PlayerView;

import java.util.LinkedList;
import java.util.List;

public class Player {
    private Phase currentPhase = null;
    private GameController gameController;
    private final List<Card> handCards;
    private final BeanField beanField;
    private int coins = 0;
    private PlayerView playerView;

    public Player() {
        this.handCards = new LinkedList<>();
        this.beanField = new BeanField(this);
    }

    public void setPlayerView(PlayerView playerView){
        this.playerView = playerView;
    }

    public PlayerView getPlayerView(){
        return playerView;
    }

    public List<Card> getHandCards() {
        return handCards;
    }

    public BeanField getBeanField() {return beanField;}

    public Phase getCurrentPhase(){
        return currentPhase;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return gameController;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public void removeCoins(int coins) {
        this.coins -= coins;
    }

    public void addToHand(Card card) {
        handCards.add(card);
        playerView.updateHandView(card);
    }

    public void addToHand(List<Card> cards) {
        for (Card card: cards) {
            addToHand(card);
        }
    }

    public Card popFromHand() {
        return handCards.remove(0);
    }

    /**
     * starts the planting phase
     */
    public void startPlantingPhase() {
        // inactive players usually have phase == null
        if (currentPhase == null) {
            currentPhase = new PlantingPhase(playerView);
        }
        currentPhase.startPhase(this);
    }

    /**
     * ends the current phase and starts the next phase if there is one
     */
    public boolean endPhaseAndStartNext() {
        currentPhase.endPhase(this);

        // start next phase
        currentPhase = currentPhase.getNextPhase();
        if (currentPhase != null) {
            currentPhase.startPhase(this);
            return currentPhase != null;
        } else {
            throw new NotImplementedException("End of turn not implemented");
            // notify game that turn has ended
            // game.endTurn(); or sth like that
            // if using game: add game field, remove gamecontroller field from player

            // possibly use gamecontroller but this really is a game logic thing
        }
    }

    // use endPhaseAndStartNext() instead
/*
    public boolean executeNextPhase() {
        if (currentPhase != null) {
            currentPhase.doPhase(this);
            currentPhase = currentPhase.getNextPhase();
            return currentPhase != null;
        }
        return false;
    }
*/
}
