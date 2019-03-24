import java.util.HashMap;

public class DiscardPile {
    private int discardCount;
    //tracks the number of cards in the discard pile
    private HashMap<String,Integer> discards;
    //A collection to store the discarded cards and how many of each card has been discarded

    /**
     * Purpose: initializes the DiscardPile object.  Sets the pile count to 0 and
     * initializes the hashmap object for holding the cards themselves.
     */
    public DiscardPile()
    {
        this.discardCount = 0;
        this.discards = new HashMap<>();
    }
    //Constructor method instantiates the collection and sets the discardcount to 0

    /**
     * Purpose: returns the current state of the discard pile.
     *
     * @return the discard pile hashmap
     */
    public HashMap<String,Integer> getDiscards(){
        return this.discards;
    }

    /**
     * Purpose: adds a card to the discard pile.
     *
     * @param discardedCard the card to be discarded.
     */
    public void addDiscard(Card discardedCard){
        String card = discardedCard.toString();

        Integer count = this.discards.get(card);
        this.discardCount += 1;

        if (count != null) {
            this.discards.replace(card, ++count);
        } else{
            this.discards.put(card, 1);
        }
    }

    /**
     * Purpose: return the current size of the discard pile.
     *
     * @return the current count of cards in the discard pile.
     */
    public int getDiscardCount(){
        return this.discardCount;
    }
}
