import java.io.PrintStream;
/**
 * The class extending the GameController into a usable AI.
 */
public class AIController extends GameController {

    /**
     * Represents the AI player's ability to Inform,
     * true if there are >0 info tokens
     */
    private Boolean canInform;

    /**
     *  Represents the AI player's ability to discard.
     *   true if there are <8 info tokens
     */
    private Boolean canDiscard;

    /**
     *  The game model that will store game data for the AI player.
     */
    private GameModel model;

    /**
     * A class constructor, will initialize canInform, canDiscard.
     * @param client The Hanabi client that the AI is started with.
     */
    public AIController(HanabiClient client){
        super(client);
    }

    /**
     *  Function that finds the best play card move, returns info about the play and null if no good plays are found.
     *  @return A String representing a move or null.
     */
    public String bestPlay(){
        Hand hand = model.getGameTable().playerHands[model.playerSeat()-1];
        int handPosition = 1;
        for (Card card : hand.cards){
            if (card.checkColourKnown() && card.checkNumberKnown()) {
                if (model.getFireworks().checkBuildValid(card)){
                    return "play " + handPosition;
                }
                else {
                    if (model.getInfoTokens() < 8){
                        return "discard " + handPosition;
                    }
                }
            } else if (Character.getNumericValue(card.getNumber()) == 1){
                for (Object height : model.getFireworks().getFireworks().entrySet()) {
                    if ((int)height == 0){
                        return "play " + handPosition;
                    }
                }
            }
            handPosition++;
        }
        return null;
    }

    /**
     *  Function that finds the best inform move, returns info about the inform and null if no good informs are found.
     *  @return A String representing a move or null.
     */
    public String bestInform(){
        if (model.getInfoTokens() == 0){
            return null;
        }
        int seat = 1;
        for (Hand hand : model.getGameTable().playerHands){
            if (seat != model.playerSeat()){
                int handPosition = 1;
                for (Card card : hand.cards){
                    boolean knowsNumber = card.checkNumberKnown();
                    boolean knowsColour = card.checkColourKnown();
                    if (!knowsColour || !knowsNumber){
                        if (Character.getNumericValue(card.getNumber()) == 5 && !knowsNumber) {
                            return "inform number player " + seat + " about position " + handPosition;
                        }
                        else if (model.getFireworks().checkBuildValid(card)){
                            if (knowsColour){
                                return "inform number player " + seat + " about position " + handPosition;
                            } else{
                                return "inform colour player " + seat + " about position " + handPosition;
                            }
                        }
                    }
                    handPosition++;
                }
            }
            seat++;
        }
        return null;
    }

    /**
     *  Function that finds the best discaed move, returns info about the discard and null if no good discards are found.
     *  @return A String representing a move or null.
     */
    public String bestDiscard(){
        if (model.getInfoTokens() == 8) {
            return null;
        }
        Hand hand = model.getGameTable().playerHands[model.playerSeat()-1];
        int handPosition = 1;
        for (Card card : hand.cards){
            if (card.checkNumberKnown()){
                if (!model.getFireworks().getFireworks().containsValue(Character.getNumericValue(card.getNumber())-1)){
                    return "discard " + handPosition;
                }
            }
            handPosition++;
        }
        return null;
    }

    /**
     *  Function that finds a valid move when no good moves were found.
     * @return A String representing a move or null.
     */
    public String findFirstMove(){
        if (model.getInfoTokens() > 0){
            int seat = 1;
            for (Hand hand : model.getGameTable().playerHands){
                int handPosition = 1;
                for (Card card : hand.cards){
                    if (!card.checkNumberKnown() && !card.checkColourKnown()){
                        if (card.checkColourKnown()){
                            return "inform number player " + seat + " about position " + handPosition;
                        }else{
                            return "inform colour player " + seat + " about position " + handPosition;
                        }
                    }
                    handPosition++;
                }
                seat++;
            }
        } else {
            if (model.getInfoTokens() < 8) {
                Hand hand = model.getGameTable().playerHands[model.playerSeat()-1];
                int handPosition = 1;
                for (Card card : hand.cards){
                    if (card.checkNumberKnown() && Character.getNumericValue(card.getNumber()) != 5){
                        return "discard " + handPosition;
                    }
                    handPosition++;
                }
            }
        }
        return null;
    }

    /**
     *   Finds and executes the best move between inform, discard, and play card.
     */
    public void performBestMove(){
        String action;
        action = bestPlay();
        if (action == null){
            action = bestInform();
            if (action == null){
                action = bestDiscard();
                if (action == null){
                    action = findFirstMove();
                }
            }
        }
        System.out.println(action);
    }

    /**
     *  Function that sets the current model to the input.
     * @param model A valid Hanabi game Model.
     */
    public void setModel(GameModel model){
        this.model = model;
    }

}
