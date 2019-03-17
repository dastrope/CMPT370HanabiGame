public class Card {
    private char number;     // number rank of the card
    private char colour;    // colour suit of the card
    private Boolean colourKnown;    // marker for whether the colour of the card is known
    private Boolean numberKnown;    // marker for whether the rank of the card is known

    public Card(char colour, char number);
        // object contstructor, takes color and number value and sets both
        // colourKnown and numberKnown to false.

    public void numberGiven();
        // sets numberKnown to true.

    public void colourGiven();
        // sets colourKnown to true.

    public int getNumber();
        // returns the ranks of the card.

    public char getColour();
        // returns the suit of the card.

    public Boolean checkColourKnown();
        // returns whether the colour of the card has been given as information.

    public Boolean checkNumberKnown();
        // returns whether the colour of the card has been given as information.

}
