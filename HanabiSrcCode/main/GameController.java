import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class GameController {
    private GameModel model;        // link to game model
    private GameView view;          // link to game view
    private GameInteractionModel iModel; // link to interaction model

    private int state; // current game state, whether or not it is the user's turn
    private final int STATE_INACTIVE = 0;   // not the user's turn
    private final int STATE_ACTIVE = 1;     // the user's turn
    public Gson gson;

    public GameController(){
        gson = new Gson();
    }

    public String[] startGameNotice(JsonElement notice){
        JsonParser jreader = new JsonParser();
        String[] a = gson.fromJson(notice, String[].class);
        return a;
    }

    public String cardToJson(Card card){
        return gson.toJson(card);
    }


//         constructor.  will set up the model, view, and interaction model.

//    public void handleMessage(String jsonMessage){
//
//    }
        // handles a message from the server, adjusting the model as needed.

//    public Dictionary parseJSON(String jsonMessage){
//        return null;
//    }
        // parses a message from the server into a collection of usable information.
        
//    public void sendJSON(Collection move){
//
//    }
        // converts a collection of information into a json message and sends that message to the server

//    public void setModel(GameModel model){
//
//    }
        // sets up the game model
    
//    public void setiModel(GameInteractionModel iModel){
//
//    }
        // sets up the interaction model
    
//    public void setView(GameView newView){
//
//    }
        // sets up the game view
        
//    public void endGame(){
//
//    }
        // ends the game, and creates a new window with endgame information

    static class Event {
        private String name;
        private int source;

        private Event(String name, int source) {
            this.name = name;
            this.source = source;
        }

        @Override
        public String toString() {
            return String.format("(name=%s, source=%s)", name, source);
        }
    }

    public static void main(String[] args){
        GameController c = new GameController();
        Card card = new Card('b', '2');

        Gson gson = new Gson();
        Collection collection = new ArrayList();
        collection.add("hello");
        collection.add(5);
        collection.add(new Event("GREETINGS", 1));
        String json = gson.toJson(collection);
        System.out.println("Using Gson.toJson() on a raw collection: " + json);

        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        String message = gson.fromJson(array.get(0), String.class);
        int number = gson.fromJson(array.get(1), int.class);
        Event event = gson.fromJson(array.get(2), Event.class);
        System.out.printf("Using Gson.fromJson() to get: %s, %d, %s", message, number, event);

//        JsonElement j = new JsonParser().parse(c.cardToJson(card));
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("notice", "game starts");
////        System.out.println(c.cardToJson(card));
////        String json = c.cardToJson(card);
////        c.gson;
//        System.out.println(c.gson.fromJson(j, Card.class));

    }
}