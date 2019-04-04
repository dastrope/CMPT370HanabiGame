import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.awt.*;
import java.io.PrintStream;
import java.util.*;

public class GameController {
    private GameModel model;        // link to game model
    private GameView view;          // link to game view
    private GameInteractionModel iModel; // link to interaction model
    private PrintStream outToServer;

    private int state; // current game state, whether or not it is the user's turn
    private final int STATE_INACTIVE = 0;   // not the user's turn
    private final int STATE_ACTIVE = 1;     // the user's turn
    private String[] userMove;
    public Gson gson;

    public GameController(PrintStream ps){
        gson = new Gson();
        this.outToServer = ps;
    }

//         constructor.  will set up the model, view, and interaction model.

    public void handleMessage(Set<Map.Entry<String,JsonElement>> jsonMessage){
        Map.Entry<String,JsonElement> entry = jsonMessage.iterator().next();

        switch(entry.getKey()){
            case "notice":
                handleNotifyMessage(jsonMessage);
                break;

            case "reply":
                handleReplyMessage(jsonMessage);
                break;
        }
    }

    public void handleReplyMessage(Set<Map.Entry<String,JsonElement>> messageMap){
        Iterator<Map.Entry<String,JsonElement>> iter = messageMap.iterator();
        Map.Entry<String,JsonElement> entry = iter.next();
        switch (entry.getValue().toString()){
            case "invalid":
                break;
            case "accepted":
                this.model.userDiscardCard(Integer.parseInt(this.userMove[1]),iter.next().getValue().getAsString());
                this.model.giveCard("uu");
                break;
            case "built":
                this.model.userPlayCardSuccess(Integer.parseInt(this.userMove[1]),iter.next().getValue().getAsString());
                break;
            case "burned":
                this.model.userDiscardCard(Integer.parseInt(this.userMove[1]),iter.next().getValue().getAsString());
                break;
        }

    }

    public void handleNotifyMessage(Set<Map.Entry<String,JsonElement>> messageMap){
        Iterator<Map.Entry<String,JsonElement>> iter = messageMap.iterator();
        Map.Entry<String,JsonElement> entry = iter.next();
        switch (entry.getValue().getAsString()){
            case "your move":
                model.nextTurn();
                userMove = this.view.getMove();
                sendJSON(userMove);
                break;
            case "discarded":
                model.discardCard(iter.next().getValue().getAsInt());
                model.giveCard(iter.next().getValue().getAsString());
                model.nextTurn();
                break;
            case "played":
                model.playCardSuccess(iter.next().getValue().getAsInt());
                model.giveCard(iter.next().getValue().getAsString());
                model.nextTurn();
                break;
            case "inform":
                System.out.println(messageMap);
                Map.Entry<String,JsonElement> infoChar = iter.next();
                Map.Entry<String,JsonElement> cardBools = iter.next();

                if (model.currentTurn() == model.playerSeat()){
                    model.informSelf(infoChar.getValue().getAsCharacter(),  cardBools.getValue().getAsString());
                } else {
                    model.informOther(infoChar.getValue().getAsCharacter(), cardBools.getValue().getAsCharacter());
                }
                model.nextTurn();
                break;
            case "game ends":
                break;
        }

    }
        // handles a message from the server, adjusting the model as needed.

    public Set<Map.Entry<String,JsonElement>> parseJSON(String jsonMessage){
        JsonParser parser = new JsonParser();
        JsonObject array = parser.parse(jsonMessage).getAsJsonArray().get(0).getAsJsonObject();

        Set<Map.Entry<String,JsonElement>> messageMap = array.entrySet();

        return messageMap;
    }
        // parses a message from the server into a collection of usable information.
        
    public void sendJSON(String[] move){
        JsonArray item = new JsonArray();
        JsonObject e = new JsonObject();
        if (move[0].equals("play")) {
            e.addProperty("action", "play");
            e.addProperty("position",Integer.parseInt(move[1]));
        } else if (move[0].equals("discard")) {
            e.addProperty("action", "discard");
            e.addProperty("position",Integer.parseInt(move[1]));
        } else if (move[0].equals("informNumber")) {
            e.addProperty("action","inform");
            e.addProperty("player",Integer.parseInt(move[1]));
            e.addProperty("rank",Integer.parseInt(move[2]));
        } else if (move[0].equals("informColour")) {
            e.addProperty("action","inform");
            e.addProperty("player",Integer.parseInt(move[1]));
            e.addProperty("suit",move[2]);
        }
        item.add(e);
        String json = gson.toJson(item);
        outToServer.println(json);
    }
        // converts a collection of information into a json message and sends that message to the server

    public void setModel(GameModel model){
        this.model = model;
    }
        // sets up the game model
    
    public void setiModel(GameInteractionModel iModel){
        this.iModel = iModel;
    }
        // sets up the interaction model
    
    public void setView(GameView newView){
        this.view = newView;
    }
        // sets up the game view
        
//    public void endGame(){
//
//    }
        // ends the game, and creates a new window with endgame information

    static class gameStartEvent {
        private String notice;
        private String[][] startHands;

        private gameStartEvent(String notice, String[][] startHands) {
            this.notice = notice;
            this.startHands = startHands;
        }

        @Override
        public String toString() {
            return String.format("(notice=%s, startHands=%s)", notice, startHands.toString());
        }
    }

    static class SelfInformEvent {
        private Boolean[] cardArr;

        private SelfInformEvent(Boolean[] cardArr) {
            this.cardArr = cardArr;
        }

        @Override
        public String toString() {
            return Arrays.toString(cardArr);
        }
    }


    public static void main(String[] args){
//        GameController c = new GameController(new PrintStream(System.out.format()));
        Card card = new Card('b', '2');

        String[][] initHands = {{},{"b1","b3","b5","g2"},{"b1","b3","g1","g2"},{"b2","b4","g1","g3"}};

        Gson gson = new Gson();
        Collection collection = new ArrayList();

        collection.add(new gameStartEvent("gameStart", initHands));
        String json1 = gson.toJson(collection);

        JsonArray item = new JsonArray();
        JsonObject e = new JsonObject();

        Boolean[] cardArr = {true,false,false,true};
        SelfInformEvent sie = new SelfInformEvent(cardArr);

        e.addProperty("notice","inform");
        e.addProperty("rank",1);
        e.addProperty("cards","[true,false,false,true]");

        item.add(e);

        String json2 = gson.toJson(item);

        System.out.println("Using Gson.toJson() on a raw collection: " + json1);

        //c.handleMessage(json1);
        //c.handleMessage(json2);
    }
}