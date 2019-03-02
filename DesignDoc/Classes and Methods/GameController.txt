public class GameController {
    private GameModel model;
    private GameView view;
    private GameInteractionModel iModel;

    private state;
    private final int STATE_INACTIVE;
    private final int STATE_ACTIVE;

    double modelX, modelY;
    double touchX, touchY;

    public GameController();

    public void handleMessage(String jsonMessage);

    public Collection parseJSON(String jsonMessage);

    public void sendJSON(Collection move);

    public void handleMousePressed(MouseEvent event);

    public void handleMouseHover(MouseEvent event);
}