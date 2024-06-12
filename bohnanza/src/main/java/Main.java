import game.Game;
import game.GameController;
import game.GameView;
import io.bitbucket.plt.sdp.bohnanza.gui.Color;
import io.bitbucket.plt.sdp.bohnanza.gui.GUI;
import io.bitbucket.plt.sdp.bohnanza.gui.Size;

public class Main {

    public static void main(String[] args) {
        Game game = Game.getInstance();
        GUI gui = new GUI(new Size(800, 600), new Size(60, 90), new Size(240, 360), new Color(0,0,0), new Color(255,255,255));
        GameView gameView = new GameView(gui, args);
        GameController controller = new GameController(game, gameView, gui);

        controller.start();
    }
}
