import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.stage.Stage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HanabiClient extends Application {

    private int needed;
    private String nsid;
    private int numOfPlayers;
    private int gameId;
    private int timeout;
    private String token;
    private String hash;
    private String gameType;

    private Server server;
    private Gson gson;

    private GameModel aModel;
    private GameView aView;
    private GameController aController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.gson = new Gson();
        GameMenu menu = new GameMenu(this,primaryStage);
        menu.start();
    }

    public void createGameRequest(String nsid, String hash, int numOfPlayers, int timeout, String gameType) {
        server = new Server(this);
        server.run();
        server.createGame(nsid,hash,numOfPlayers,timeout,gameType);
        //handleJSON(server.getMessage());
    }

    public void joinGameRequest(String nsid, String hash, int gameId, String token) {
        server = new Server(this);
        server.run();
        server.joinGame(nsid,hash,gameId,token);
        //handleJSON(server.getMessage());
    }

    public void sendMsgToServer(String msg) {
        server.sendMessage(msg);
    }

    public void startGame(JsonArray startHands) {
        ArrayList<String[]> hands = new ArrayList<>();
        Type type = new TypeToken<ArrayList<String[]>>(){}.getType();
        hands = gson.fromJson(startHands,type);

        this.aModel = new GameModel(this.timeout, this.gameType, hands);
        this.aController = new GameController(this);
        this.aView = new GameView(this.aModel, this.aController);

        this.aController.setModel(this.aModel);
        this.aController.setView(this.aView);

        Stage stage = new Stage();
        stage.setScene(this.aView.createGame());
        stage.show();
    }

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
        //handleJSON(server.getMessage());
    }

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
        //handleJSON(server.getMessage());
    }

    public static void main(String[] args){
        launch();
    }
}
