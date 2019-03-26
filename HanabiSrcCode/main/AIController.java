import java.util.Collection;

public class AIController extends GameController {
    private Boolean canInform; // true if there are >0 info tokens
    private Boolean canDiscard;// true if there are <8 info tokens
    
    public AIController(){

    } //constructor, will initialize canInform, canDiscard

    public void checkAvailableMoves(){

    } //updates the booleans

    public Collection<String> bestPlay(){
        return null;
    } //finds the best play card move, returns info about the play

    public Collection<String> bestInform(){
        return null;
    } //finds the best Inform move

    public Collection<String> bestDiscard(){
        return null;
    } //finds the best Inform move

    public void performBestMove(){

    } //finds the best move between info, discard, and play card.
}
