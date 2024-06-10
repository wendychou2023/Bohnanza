package player;

import card.Card;
import java.util.Collection;

public class TradeOffer {
    //offered to active player
    private final Collection<Card> cardsOfferedToActive;

    //received from active player//
    private final Collection<Card> cardsReceivedFromActive;

    private final Player activePlayer;
    private final Player inactivePlayer;

    public TradeOffer(
            Collection<Card> cardsReceivedFromActive,
            Collection<Card> cardsOfferedToActive,
            Player activePlayer,
            Player inactivePlayer
    ) {
        this.activePlayer = activePlayer;
        this.inactivePlayer = inactivePlayer;
        this.cardsOfferedToActive = cardsOfferedToActive;
        this.cardsReceivedFromActive = cardsReceivedFromActive;
    }

    public Collection<Card> getCardsOfferedToActive() {
        return cardsOfferedToActive;
    }

    public Collection<Card> getCardsReceivedFromActive() {
        return cardsReceivedFromActive;
    }

    public void accept() throws NotEnoughBeanFieldException {

        for (Card card : cardsOfferedToActive) {
            activePlayer.getBeanField().plant(card);
            inactivePlayer.getHandCards().remove(card);
        }
        for (Card card : cardsReceivedFromActive) {
            inactivePlayer.getBeanField().plant(card);
            activePlayer.getHandCards().remove(card);
        }
    }
}
