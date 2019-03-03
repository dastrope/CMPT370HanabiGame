public class Hand {
    private int handSize; // the amount of cards in the player hands
    private Collection<Card> cards; //a collecton that will store the current cards in the hand

    public Hand(int handSize); // constructor.  Will initialize collection cards, handSize.

    public void addCard(char colour, char number); // adds a card with colour and number to collection cards

    public void removeCard(int position); // remove the card at position in collection cards

    public void informColour(char colour); // will set the colourKnown to true to each card object in collection cards that belongs to input colour

    public void informNumber(char number); // will set the numberKnown to true to each card object in collection cards that belongs to input number
}
