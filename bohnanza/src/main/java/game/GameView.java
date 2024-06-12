package game;

import io.bitbucket.plt.sdp.bohnanza.gui.*;

public class GameView implements Runnable {
    private GameController gameController;
    private final GUI gui;
    @SuppressWarnings("unused")
    private final String[] args;
    private Label gameInfoLabel; // Label to display game information
    private Label playerInfoLabel; // Label to display player information

    public GameView(GUI gui, String[] args) {
        super();
        this.gui = gui;
        this.args = args;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void run() {
        setupGUI();
    }

    private void setupGUI(){
        // create card objects for all supported card types
        // and display them distributed over the GUI

        final int X_DIFF = 40;
        final int Y_DIFF = 40;
        final int X_ORIGIN = 305;
        final int Y_ORIGIN = 5;
        final int COLS = 11;
        final int ROWS = 9;

        int x = X_ORIGIN;
        int y = Y_ORIGIN;
        for (CardType cardType : CardType.values()) {
            gui.addCard(cardType, new Coordinate(x, y)).flip();

            x += X_DIFF;
            if (x > X_ORIGIN + X_DIFF * ROWS) {
                x = X_ORIGIN;
                y += Y_DIFF;
            }
            if (y > Y_ORIGIN + Y_DIFF * COLS) {
                y = Y_ORIGIN;
                x = X_ORIGIN;
            }
        }

        // create compartments that are used to demo the convenience functions
        // for aligning cards within compartments

        Compartment vDistCompartment = setupDemoCompartment(0, "vert. distr.");
        setupDemoCompartmentButton(0, button -> {
            vDistCompartment.distributeVertical(gui.getCardObjectsInCompartment(vDistCompartment));
        });


        Compartment hDistCompartment = setupDemoCompartment(1, "hor. distr.");
        setupDemoCompartmentButton(1, button -> {
            hDistCompartment.distributeHorizontal(gui.getCardObjectsInCompartment(vDistCompartment));
        });


        Compartment vCentCompartment = setupDemoCompartment(2, "vert. center");
        setupDemoCompartmentButton(2, button -> {
            vCentCompartment.centerVertical(gui.getCardObjectsInCompartment(vCentCompartment));
        });


        Compartment hCentCompartment = setupDemoCompartment(3, "hor. center");
        setupDemoCompartmentButton(3, button -> {
            hCentCompartment.centerHorizontal(gui.getCardObjectsInCompartment(hCentCompartment));
        });

        // a label that will be used to show information on a dragged'n'dropped card
        final Label label = gui.addLabel(new Coordinate(10, 100), "<none>");

        // a demo of a compartment with empty background
        gui.addCompartment(new Coordinate(1, 1), new Size(140, 140), "Handkarten");

        // a demo of a compartment with an image as background
        gui.addCompartment(new Coordinate(1, 200), new Size(300, 200), "Bohnenfelder", "BOHNENFELD_ALLE");

        // a button to explicitly terminate the demo. This closes the window.
        gui.addButton("exit", new Coordinate(100, 200), new Size(80, 25), button -> {
            gui.stop();
        });

        // set the handler for drag'n'drop events. With this handler:
        // - whenever a d'n'd action finishes, the dropped card is flipped (toggle whether the front or back is shown)
        // - information on the dropped card is shown in the dedicated label
        // - the card is moved to the front, i.e., displayed top-most
        gui.setCardDnDHandler((CardObject card, Coordinate mouseCoordinate, Coordinate newCoordinate) -> {
            card.flip();
            label.updateLabel(card.toString());
            gui.moveToTop(card);
            return newCoordinate;
        });

        // Initialize labels for displaying information
        gameInfoLabel = gui.addLabel(new Coordinate(100, 500), "Game Information");
        playerInfoLabel = gui.addLabel(new Coordinate(100, 600), "Player Information");

        // Add "Next" button
        gui.addButton("Next", new Coordinate(100, 650), new Size(200, 50), button -> {
            gameController.userActionCompleted(); // Notify the controller
        });

    }

    final int Y = 800;
    final int WIDTH = 250;
    final int HEIGHT = 300;
    final int V_SPACING = 5;
    final int H_SPACING = 5;
    final Size BUTTON_SIZE = new Size(100, 25);

    private Compartment setupDemoCompartment(int pos, String label) {
        return gui.addCompartment(new Coordinate(WIDTH * pos, Y), new Size(WIDTH, HEIGHT), label);
    }

    private Button setupDemoCompartmentButton(int pos, ButtonHandler handler) {
        return gui.addButton("arrange", new Coordinate(WIDTH * (pos + 1) - BUTTON_SIZE.width - H_SPACING, Y + V_SPACING), BUTTON_SIZE, handler);
    }

    public void updateGameInfo(String info) {
        gameInfoLabel.updateLabel(info);
    }

    public void updatePlayerInfo(String info) {
        playerInfoLabel.updateLabel(info);
    }

    public void update() {
        // Implement logic to update the GUI based on the current game state
    }

}
