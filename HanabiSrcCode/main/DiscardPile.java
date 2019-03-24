import java.util.HashMap;

public class DiscardPile {
    private int discardCount;
    //tracks the number of cards in the discard pile
    private HashMap<String,Integer> discards;
    //A collection to store the discarded cards and how many of each card has been discarded

    public DiscardPile()
    {

    }
    //Constructor method instantiates the collection and sets the discardcount to 0

    public HashMap<String,Integer> getDiscards(){
        return null;
    }
    //returns the collection of discarded cards

    public void addDiscard(Card discardedCard){

    }
    //adds a newly discarded card to the collection
 
    public int getDiscardCount(){
        return 0;
    }
    //returns the amount of cards in the discard pile
}
