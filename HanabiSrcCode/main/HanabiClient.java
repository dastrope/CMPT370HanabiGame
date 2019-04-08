import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.stage.Stage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A class representing an instance of the HanabiClient Application.
 */
public class HanabiClient extends Application {

    /**
     * The number of players needed to start the game.
     */
    private int needed;

    /**
     * The user's NSID.
     */
    private String nsid;

    /**
     * The total number of players in the game.
     */
    private int numOfPlayers;

    /**
     * The gameID provided by the server.
     */
    private int gameId;

    /**
     * The timeout length of each turn.
     */
    private int timeout;

    /**
     * The game token provided by the server.
     */
    private String token;

    /**
     * The secret hash associated with an NSID.
     */
    private String hash;

    /**
     * The game type, this can be default, wild, and extended.
     */
    private String gameType;

    /**
     * The server instance.
     */
    private Server server;

    /**
     * The JSON interpreter.
     */
    private Gson gson;

    /**
     * A valid Hanabi gameModel
     */
    private GameModel aModel;

    /**
     * A valid Hanabi GameView.
     */
    private GameView aView;

    /**
     * A valid Hanabi gameController
     */
    private GameController aController;

    /**
     * A function that start the gameMenu and sets up the GSON.
     * @param primaryStage The stage for the gameMenu.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.gson = new Gson();
        GameMenu menu = new GameMenu(this,primaryStage);
        menu.start();
    }

    /**
     * A function that will create a create game request.
     * @param nsid A valid U of S NSID.
     * @param hash A valid hash associated with the NSID.
     * @param numOfPlayers The number of player in the game selected by the user.
     * @param timeout The time for the timeout for one move selected by the user.
     * @param gameType The game type selected by the user.
     */
    public void createGameRequest(String nsid, String hash, int numOfPlayers, int timeout, String gameType) {
        server = new Server(this);
        server.run();
        server.createGame(nsid,hash,numOfPlayers,timeout,gameType);
    }

    /**
     * A function that will create a join game request.
     * @param nsid A valid U of S NSID.
     * @param hash A valid hash associated with the NSID.
     * @param token A secret token provided by the server to the game host.
     * @param gameId The gameID provided by the server to the host.
     */
    public void joinGameRequest(String nsid, String hash, int gameId, String token) {
        server = new Server(this);
        server.run();
        server.joinGame(nsid,hash,gameId,token);
    }

    /**
     * A function that will send a message to the server.
     * @param msg The message to send to the server.
     */
    public void sendMsgToServer(String msg) {
        server.sendMessage(msg);
    }

    /**
     * A function that starts the game once a server connection has been established and all players have joined..
     * @param startHands The cards of the other Hanabi players.
     */
    public void startGame(JsonArray startHands) {
        ArrayList<String[]> hands = new ArrayList<>();
        Type type = new TypeToken<ArrayList<String[]>>(){}.getType();
        hands = gson.fromJson(startHands,type);

        this.aModel = new GameModel(this.timeout, this.gameType, hands);
        this.aController = new GameController(this);
        this.aView = new GameView(this.aModel, this.aController);

        this.aController.setModel(this.aModel);
        this.aController.setView(this.aView);
        this.aModel.addSubscriber(this.aView);

        Stage stage = new Stage();
        stage.setScene(this.aView.createGame());
        stage.show();
    }

    /**
     * A function that will parse and handle the JSON message received from the server.
     * @param jsonMessage A valid json message sent by the server..
     */
    public void handleJSON (JsonElement jsonMessage){
        System.out.println(jsonMessage);
        Set<Map.Entry<String,JsonElement>> messageMap = jsonMessage.getAsJsonObject().entrySet();
        Iterator<Map.Entry<String,JsonElement>> iter = messageMap.iterator();
        Map.Entry<String,JsonElement> entry = iter.next();

        switch(entry.getKey()){
            case "notice":
                handleNotifyMessage(messageMap);
                break;

            case "reply":
                handleReplyMessage(messageMap);
                break;
        }
    }

    /**
     * A function that handles how the Client should proceed with the received JSON message.
     * @param messageMap A valid parsed JSON message.
     */
    public void handleNotifyMessage(Set<Map.Entry<String,JsonElement>> messageMap) {
        Iterator<Map.Entry<String,JsonElement>> iter = messageMap.iterator();
        Map.Entry<String,JsonElement> entry = iter.next();
        System.out.println(entry.getValue().getAsString());
        switch (entry.getValue().getAsString()){
            case "player joined":
                this.needed = iter.next().getValue().getAsInt();
                break;
            case "player left":
                this.needed = iter.next().getValue().getAsInt();
                break;
            case "game starts":
                this.startGame(iter.next().getValue().getAsJsonArray());
                break;
            case "game cancelled":
                this.aController.handleNotifyMessage(messageMap);
                break;
            case "game ends":
                this.aController.handleNotifyMessage(messageMap);
                break;
            default:
                this.aController.handleNotifyMessage(messageMap);
                break;
        }
    }

    /**
     * A function that will dictate how the client should proceed after getting a reply about a message that was sent.
     * @param messageMap A valid parsed JSON message.
     */
    public void handleReplyMessage(Set<Map.Entry<String,JsonElement>> messageMap) {
        Iterator<Map.Entry<String,JsonElement>> iter = messageMap.iterator();
        Map.Entry<String,JsonElement> entry = iter.next();

        switch (entry.getValue().getAsString()) {
            case "created":
                this.gameId = iter.next().getValue().getAsInt();
                this.token = iter.next().getValue().getAsString();
                break;
            case "joined":
                this.needed = iter.next().getValue().getAsInt();
                this.timeout = iter.next().getValue().getAsInt();
                this.gameType = iter.next().getValue().getAsString();
                break;
            case "extant":
                this.gameId = iter.next().getValue().getAsInt();
                this.token = iter.next().getValue().getAsString();
                break;
            case "no such game":
                break;
            case "game full":
                break;
            case "invalid":
                break;
            default:
                this.aController.handleReplyMessage(messageMap);
                break;
        }
    }

    /**
     * The main method to start the game.
     * @param args Arguments provided by the command line.
     */
    public static void main(String[] args){
        launch();
    }
}
