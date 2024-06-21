package game;

import card.Card;
import io.bitbucket.plt.sdp.bohnanza.gui.*;
import player.PlantingSpot;
import player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class GameView implements Runnable {
    private GameController gameController;
    private final GUI gui;
    @SuppressWarnings("unused")
    private final String[] args;
    private Label gameInfoLabel; // Label to display game information
    private Label playerInfoLabel; // Label to display player information
    private Label label;
    private Button startButton;
    private Button activePlayerButton;
    private Button nextPhaseButton;

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
        startButton = gui.addButton("start", new Coordinate(10, 100), BUTTON_SIZE, button -> {
            gameController.userClickStart();
            gameController.userActionCompleted();
        });

        label = gui.addLabel(new Coordinate(10, 50), "<none>");
        label.updateLabel("Click start to start the game.");

        // a button to explicitly terminate the game. This closes the window.
        gui.addButton("exit", new Coordinate(200, 100), BUTTON_SIZE, button -> {
            gui.stop();
        });

        // set the handler for drag'n'drop events. With this handler:
        // - whenever a d'n'd action finishes, the dropped card is flipped (toggle whether the front or back is shown)
        // - information on the dropped card is shown in the dedicated label
        // - the card is moved to the front, i.e., displayed top-most
        gui.setCardDnDHandler((CardObject card, Coordinate mouseCoordinate, Coordinate newCoordinate) -> {
//            card.flip();
            label.updateLabel(card.toString());
            gui.moveToTop(card);
            return newCoordinate;
        });

        // Initialize labels for displaying information
        gameInfoLabel = gui.addLabel(new Coordinate(10, 150), "Game Information");
        playerInfoLabel = gui.addLabel(new Coordinate(10, 250), "Player Information");

        activePlayerButton = gui.addButton("Active player takes turn", new Coordinate(10, 300), new Size(200, 25), button -> {
            gameController.userActionCompleted();
            gui.setButtonEnabled(activePlayerButton, false);
        });
        gui.setButtonEnabled(activePlayerButton, false);

        nextPhaseButton = gui.addButton("Next phase", new Coordinate(10, 350), new Size(200, 25), button -> {
            gameController.userActionCompleted();
            gui.setButtonEnabled(nextPhaseButton, false);
        });
        gui.setButtonEnabled(nextPhaseButton, false);


    }

    final int Y = 800;
    final int WIDTH = 250;
    final int HEIGHT = 300;
    final int V_SPACING = 5;
    final int H_SPACING = 5;
    final Size BUTTON_SIZE = new Size(100, 25);
    Compartment[] playerCompartments;
    Compartment[] playerBeanfield;

    public void updateGameInfo(String info) {
        gameInfoLabel.updateLabel(info);
    }

    public void updatePlayerInfo(String info) {
        playerInfoLabel.updateLabel(info);
    }


    int selectedOption;
    Coordinate requestLabelCoordinate = new Coordinate(100, 400);
    int requestButtonX = 100;
    int requestButtonXDiff = 105;
    int requestButtonY = 450;
    public void requestUserPlantAction(String message, Player player, int playerId){
        Label requestLabel = gui.addLabel(requestLabelCoordinate, message);
        Button[] buttons = new Button[player.getBeanField().getNumberOfFields()];
        int bound = player.getBeanField().getNumberOfFields();
        for (int i = 0; i < bound; i++) {
            int finalI = i;
            buttons[i] = gui.addButton("Field" + (i + 1), new Coordinate(requestButtonX + requestButtonXDiff * i, requestButtonY), BUTTON_SIZE,
                    button -> {
                        userRespondedtoRequest(requestLabel, buttons);
                        gameController.selectedOption(finalI);
                        gameController.userActionCompleted();
            });
        }
    }

    public void requestUserYesOrNo(String message, Player player, int playerId){
        Label requestLabel = gui.addLabel(requestLabelCoordinate, message);
        Button[] yesNoButtons = new Button[2];
        yesNoButtons[0] = gui.addButton("yes", new Coordinate(requestButtonX, requestButtonY), BUTTON_SIZE,
                button -> {
                    userRespondedtoRequest(requestLabel, yesNoButtons);
                    gameController.selectedOption(true);
                    gameController.userActionCompleted();
                });
        yesNoButtons[1] = gui.addButton("no", new Coordinate(requestButtonX + requestButtonXDiff, requestButtonY), BUTTON_SIZE,
                button -> {
                    userRespondedtoRequest(requestLabel, yesNoButtons);
                    gameController.selectedOption(false);
                    gameController.userActionCompleted();
                });
    }

    public void requestUserHarvestAction(String message, List<Card> harvestableBeans, Player player) {
        // Display a dialog or some UI element to ask the user for input
        Label requestLabel = gui.addLabel(new Coordinate(100, 400), message);
        int x = 100;
        int x_diff = 105;
        Button[] buttons = new Button[harvestableBeans.size()];
        int i = 0;
        for(Card bean: harvestableBeans) {
            int finalI = i;
            buttons[i] = gui.addButton(bean.toString(), new Coordinate(x + x_diff * i, 500), BUTTON_SIZE,
                    button -> {
//                        gameController.harvestField(finalI, player);
                        userRespondedtoRequest(requestLabel, buttons);
                    });
            i++;
        }
    }

    private void userRespondedtoRequest(Label requestLabel, Button[] buttons){
        gui.removeLabel(requestLabel);
        for(Button button: buttons){
            gui.removeButton(button);
        }
    }

    private void userRespondedtoRequest(Label requestLabel, Button button){
        gui.removeLabel(requestLabel);
        gui.removeButton(button);
    }

    private Compartment setupPlayerCompartment(int player, String label) {
        return gui.addCompartment(new Coordinate(WIDTH * player, Y), new Size(WIDTH, HEIGHT), label);
    }

    private Button setupPlayerCompartmentButton(int pos, ButtonHandler handler) {
        return gui.addButton("flip", new Coordinate(WIDTH * (pos + 1) - BUTTON_SIZE.width - H_SPACING, Y + V_SPACING), BUTTON_SIZE, handler);
    }

    private Compartment setupPlayerBeanfield(int player, String label) {
        return gui.addCompartment(new Coordinate(WIDTH * player, Y-HEIGHT/2), new Size(WIDTH, HEIGHT/2), label, "BOHNENFELD_ALLE");
    }

    public void updateInitialView(List<Card> drawPile, List<Player> players, int activePlayerId){
        // Disable the start button after start
        gui.setButtonEnabled(startButton, false);
        gui.setButtonEnabled(activePlayerButton, true);
        label.updateLabel("");

        // Display draw pile
        final int X_DRAW = 5;
        final int Y_DRAW = 400;

        for (Card card : drawPile) {
            gui.addCard(card.getCardType(), new Coordinate(X_DRAW, Y_DRAW));
        }

        // Display player compartments
        int i = 0;
        playerCompartments = new Compartment[players.size()];
        playerBeanfield = new Compartment[players.size()];

        for(Player player: players){
            playerCompartments[i] = setupPlayerCompartment(i, "Player " + i);
            int finalI = i;
            // Add a flip button in each player compartment
            setupPlayerCompartmentButton(i, button -> {
                // Retrieve the array of CardObject from the compartment
                CardObject[] cardObjects = gui.getCardObjectsInCompartment(playerCompartments[finalI]);

                // Check if cardObjects is not null to avoid NullPointerException
                if (cardObjects != null) {
                    // Use Arrays.stream() to iterate and flip each card
                    Arrays.stream(cardObjects).forEach(CardObject::flip);
                }
            });


            // Only found image for with 3 bean fields
            playerBeanfield[i] = setupPlayerBeanfield(i, "");

            updatePlayerHandCard(player, i);

            i++;
        }
    }
    public void updatePlayerHandCard(Player player, int playerId){
        final int X_DIFF = 20;
        final int Y_DIFF = 20;
        final int X_ORIGIN = WIDTH * playerId;
        final int Y_ORIGIN = Y;
        final int COLS = 10;
        final int ROWS = 5;

        int x = WIDTH * playerId;
        int y = Y;
        for (Card card : player.getHandCards()) {
            // Bean side facing up
            gui.addCard(card.getCardType(), new Coordinate(x + H_SPACING + BUTTON_SIZE.height, y-V_SPACING + BUTTON_SIZE.width));

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
    }

    public void updatePlayerBeanfield(Player player, int playerId){
        List<PlantingSpot> plantingSpots = player.getBeanField().getPlantingSpots();
        int fieldId = -1;
        for (PlantingSpot spot : plantingSpots) {
            fieldId++;
            if (spot.getPlantedCard() == null){
                continue;
            }
            Card card = spot.getPlantedCard();
            Integer numOfCards = spot.getNumberOfBeans();
            int finalFieldId = fieldId;
            IntStream.range(0, numOfCards).forEach(i -> {
                gui.addCard(card.getCardType(), new Coordinate(WIDTH * playerId + 10 + 80 * finalFieldId, Y-HEIGHT/2 + 50)).flip();
            });
        }
    }

    public void enableActivePlayerButton(){
        gui.setButtonEnabled(activePlayerButton, true);
    }

    public void enableNextPhaseButton(){
        gui.setButtonEnabled(nextPhaseButton, true);
    }
}
