public class GameView {
    private GameModel model;
    private GameInteractionModel iModel;

    public GameView();

    public void setModel(GameModel aModel);

    public void setInteractionModel(GameInteractionModel aModel);

    public void drawTable();

    public void drawHand();

    public void drawCard();

    public void drawInfoTokens();

    public void drawFireworks();

    public void drawFuses();

    public void drawStatus();

}
