public class GameController {
    private GameModel model;        // link to game model
    private GameView view;          // link to game view
    private GameInteractionModel iModel; // link to interaction model

    private int state; // current game state, whether or not it is the user's turn
    private final int STATE_INACTIVE;   // not the user's turn
    private final int STATE_ACTIVE;     // the user's turn

    public GameController();
        // constructor.  will set up the model, view, and interaction model.

    public void handleMessage(String jsonMessage);
        // handles a message from the server, adjusting the model as needed.

    public Collection parseJSON(String jsonMessage);
        // parses a message from the server into a collection of usable information.
        
    public void sendJSON(Collection move);
        // converts a collection of information into a json message and sends that message to the server

    public void setModel(GameModel model);
        // sets up the game model
    
    public void setiModel(GameInteractionModel iModel);
        // sets up the interaction model
    
    public void setView(GameView newView);
        // sets up the game view
        
    public void endGame();
        // ends the game, and creates a new window with endgame information
}