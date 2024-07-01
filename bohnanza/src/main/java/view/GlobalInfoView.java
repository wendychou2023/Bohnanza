package view;

import io.bitbucket.plt.sdp.bohnanza.gui.*;

public class GlobalInfoView {
    private final GUI gui;
    private Label gameInfoLabel;
    private Label playerInfoLabel;

    public GlobalInfoView(GUI gui) {
        this.gui = gui;
        setupGlobalInfoView();
    }

    private void setupGlobalInfoView() {
        gameInfoLabel = gui.addLabel(new Coordinate(10, 50), "Game Information");
        playerInfoLabel = gui.addLabel(new Coordinate(10, 250), "Player Information");
    }

    public void updateGameInfo(String info) {
        gameInfoLabel.updateLabel(info);
    }

    public void updatePlayerInfo(String info) {
        playerInfoLabel.updateLabel(info);
    }
}
