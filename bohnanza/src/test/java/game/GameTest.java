package game;

import card.Card;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void startGameForThree() {
        Game game = Game.getInstance();
        for (int i = 0; i < 3; i++) {
            game.addPlayer(new Player());
        }
        assertDoesNotThrow(game::startGame);
    }

    void startGameForMoreThanthree() {
        Game game = Game.getInstance();
        for (int i = 0; i < 4; i++) {
            game.addPlayer(new Player());
        }
        assertDoesNotThrow(game::startGame);
    }
}