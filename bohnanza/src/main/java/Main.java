import game.Game;
import io.bitbucket.plt.sdp.bohnanza.gui.Color;
import io.bitbucket.plt.sdp.bohnanza.gui.GUI;
import io.bitbucket.plt.sdp.bohnanza.gui.Size;
import player.Player;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        /*
        Game game = Game.getInstance();

        Scanner scanner = new Scanner(System.in);

        // Read number of players
        System.out.println("Enter number of players (3 to 5 players are allowed):");
        int numOfPlayers = scanner.nextInt();
        System.out.println("You entered: " + numOfPlayers);

        for(int i = 0; i < numOfPlayers; i++){
            game.addPlayer(new Player());
        }

        game.startGame();
        */


        GUI gui = new GUI(new Size(800, 600), new Size(60, 90), new Size(240, 360), new Color(0,0,0), new Color(255,255,255));
        
        new Thread(new GameView(gui, args)).start();
        
        gui.start();
    }
}
