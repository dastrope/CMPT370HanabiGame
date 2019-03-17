public class GameMenu {
    private Button createGameButton;
    private Button joinGameButton;
    private Button howToPlayButton;
    private Button exitButton;
        // These buttons are used to call their respective methods in the class.
    
    
    private int game-id;
    private String token;
        // These fields are user entered data used for establishing a server connection.
    
    private Socket serverSocket;
    private Model aModel;
    private InteractionModel iModel;
    private View aView;
    private Controller aController;
        // These fields will be set by createGame() or joinGame(), these are the main instances of our in-game program.
    
    public GameMenu(); 
        // Constructs the main game menu
    
    public void createGame(); 
        // Creates a connection with the server and creates a game as well as instantiates aModel, iModel, aView, aController, game-id and token.

    public void joinGame(); 
        // Creates a connection with the server as well as instantiates aModel, iModel, aView and aController.

    public void howToPlay(); 
        // Presents a window with the game rules.

    public void exit(); 
        // Closes the entire program.
}