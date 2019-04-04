import java.io.PrintStream;
import java.util.Collection;

public class AIController extends GameController {
    private Boolean canInform; // true if there are >0 info tokens
    private Boolean canDiscard;// true if there are <8 info tokens
    private GameModel model;
    
    public AIController(PrintStream outputStream){
        super(outputStream);
    } //constructor, will initialize canInform, canDiscard

    public void checkAvailableMoves(){

    } //updates the booleans

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
    } //finds the best play card move, returns info about the play

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
    } //finds the best Inform move

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
    } //finds the best Discard move

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
    } //finds the best move between info, discard, and play card.

    public void setModel(GameModel model){
        this.model = model;
    }
}
