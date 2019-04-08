import com.google.gson.*;
import java.util.*;
/**
 * A class that acts as the game controller. It controls the in-game state.
 */
public class GameController {

    /**
     * A reference to the model of the player.
     */
    private GameModel model;

    /**
     * A reference to the view that the user will see.
     */
    private GameView view;

    /**
     * A reference to the interaction model.
     */
    private GameInteractionModel iModel; // link to interaction model

    /**
     * The current game state, that represents if it is the user's turn.
     * 0: not the user's turn.
     * 1: the user's turn.
     */
    private int state;

    /**
     * A constant to be assigned to state
     * Inactive represents it is not the user's turn.
     */
    private final int STATE_INACTIVE = 0;

    /**
     * A constant to be assigned to state.
     * Active represents the user's turn.
     */
    private final int STATE_ACTIVE = 1;

    /**
     * A string reprenting a user move to be passed to the server.
     */
    private String[] userMove;

    /**
     * The GSON instance, used to create JSON messages.
     */
    public Gson gson;

    /**
     * A reference to the Hanabi Client that this controller belongs to.
     */
    private HanabiClient parent;

    /**
     * A constructor for the Game Controller. It will instantiate gson and patent.
     * The constructor will also set up the model, view, and interaction model.
     * @param parent A reference to the Hanabi Client that this Game Controller is linked to.
     */
    public GameController(HanabiClient parent){
        this.gson = new Gson();
        this.parent = parent;
    }

    /**
     * A function that handles messages returning from the server after performing a move.
     * @param messageMap A valid parsed JSON message.
     */
    public void handleReplyMessage(Set<Map.Entry<String,JsonElement>> messageMap){
        Iterator<Map.Entry<String,JsonElement>> iter = messageMap.iterator();
        Map.Entry<String,JsonElement> entry = iter.next();
        System.out.println("ControllerR: " + entry.getValue().getAsString());
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

    /**
     * A function that will interpret server messages and notify the model and update all required fields.
     * @param messageMap A valid parsed JSON message.
     */
    public void handleNotifyMessage(Set<Map.Entry<String,JsonElement>> messageMap){
        Iterator<Map.Entry<String,JsonElement>> iter = messageMap.iterator();
        Map.Entry<String,JsonElement> entry = iter.next();
        System.out.println("ControllerN: " + entry.getValue().getAsString());
        switch (entry.getValue().getAsString()){
            case "your move":
                model.nextTurn();
                view.update();
                waitForMove();
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

    /**
     * A function that will parse incoming JSON messages into a collection of usable and interpretable information.
     * @param jsonMessage A JSON message received from the server.
     * @return return a parsed message map.
     */
    public Set<Map.Entry<String,JsonElement>> parseJSON(String jsonMessage){
        JsonParser parser = new JsonParser();
        JsonObject array = parser.parse(jsonMessage).getAsJsonArray().get(0).getAsJsonObject();

        Set<Map.Entry<String,JsonElement>> messageMap = array.entrySet();

        return messageMap;
    }

    /**
     * A function that will create a JSON message and send it to the server.
     * @param move A String array representing a move to perform.
     */
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
        System.out.println(e);
        parent.sendMsgToServer(e);
    }

    /**
     * A function that sets the current move to the input move.
     * @param move A representaion of a move as an array of Strings
     */
    public void setMove(String[] move) {
        this.userMove = move;
        sendJSON(this.userMove);
    }

    /**
     * A function that sets the current move to null, indicating that the controller is waiting for a move.
     */
    public void waitForMove() {
        while (this.userMove == null) {
        }
    }

    /**
     * A function that will set up the game model.
     * @param model A valid Hanabi GameModel.
     */
    public void setModel(GameModel model){
        this.model = model;
    }

    /**
     * A function that will set up the game view.
     * @param newView A valid Hanabi GameView.
     */
    public void setView(GameView newView){
        this.view = newView;
    }

//    /**
//     * A function that will end the game and show an end game window with statistics.
//     */
//    public void endGame(){
//
//    }

    /**
     * A class that declares the game start even and will set the GameController accordingly.
     */
    static class gameStartEvent {

        /**
         * A String that represents the notice of a game start event.
         */
        private String notice;

        /**
         * An array of arrays of Strings that represents the starting hands of the other players.
         */
        private String[][] startHands;

        /**
         * A constructor for gameStartEvent.
         * @param notice A valid notice String
         * @param startHands A valid set of starting hands.
         */
        private gameStartEvent(String notice, String[][] startHands) {
            this.notice = notice;
            this.startHands = startHands;
        }

        @Override
        public String toString() {
            return String.format("(notice=%s, startHands=%s)", notice, startHands.toString());
        }
    }

    /**
     * A class that keeps track of which cards can be informed.
     */
    static class SelfInformEvent {
        /**
         * An array of booleans representing each card..
         */
        private Boolean[] cardArr;

        /**
         * A function that updates the card array.
         * @param cardArr The updated card array
         */
        private SelfInformEvent(Boolean[] cardArr) {
            this.cardArr = cardArr;
        }

        @Override
        public String toString() {
            return Arrays.toString(cardArr);
        }
    }


//    public static void main(String[] args){
////        GameController c = new GameController(new PrintStream(System.out.format()));
//        Card card = new Card('b', '2');
//
//        String[][] initHands = {{},{"b1","b3","b5","g2"},{"b1","b3","g1","g2"},{"b2","b4","g1","g3"}};
//
//        Gson gson = new Gson();
//        Collection collection = new ArrayList();
//
//        collection.add(new gameStartEvent("gameStart", initHands));
//        String json1 = gson.toJson(collection);
//
//        JsonArray item = new JsonArray();
//        JsonObject e = new JsonObject();
//
//        Boolean[] cardArr = {true,false,false,true};
//        SelfInformEvent sie = new SelfInformEvent(cardArr);
//
//        e.addProperty("notice","inform");
//        e.addProperty("rank",1);
//        e.addProperty("cards","[true,false,false,true]");
//
//        item.add(e);
//
//        String json2 = gson.toJson(item);
//
//        System.out.println("Using Gson.toJson() on a raw collection: " + json1);
//
//        //c.handleMessage(json1);
//        //c.handleMessage(json2);
//    }
}