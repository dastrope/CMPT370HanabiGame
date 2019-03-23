/**
 * The class representing a single Hanabi card.
 */
public class Card {

    /**
     * number rank of the card
     */
    private char number;

    /**
     * colour suit of the card
     */
    private char colour;

    /**
     * marker for whether the colour of the card is known
     */
    private Boolean colourKnown;

    /**
     * marker for whether the rank of the card is known
     */
    private Boolean numberKnown;


    /**
     * Purpose: Card constructor for other players' cards, takes color and number value
     *          and sets both colourKnown and numberKnown to false.
     *
     * @param colour the colour of the card.
     * @param number the rank of the card.
     */
    public Card(char colour, char number) {

        this.colour = colour;
        this.number = number;

        this.colourKnown = false;
        this.numberKnown = false;
    }


    /**
     * Purpose: Card constructor for the user cards,
     *          sets both colourKnown and numberKnown to false.
     *
     */
    public Card() {

        /* Assumes colour and number are instantiated to null. */

        this.colourKnown = false;
        this.numberKnown = false;
    }

    /**
     * Purpose: sets numberKnown to true, when a number is revealed to the card's owner
     */
    public void numberInformed() {
        this.numberKnown = true;
    }

    /**
     * Purpose: sets colourKnown to true, when a number is revealed to the card's owner
     */
    public void colourInformed() {
        this.numberKnown = true;
    }

    /**
     * Purpose: returns the rank of the card.
     * @return the char representing the rank.
     */
    public char getNumber() {
        return this.number;
    }

    /**
     * Purpose: returns the colour of the card.
     * @return the char representing the colour.
     */
    public char getColour() {
        return this.colour;
    }

    /**
     * Purpose: returns whether the colour of the card has been given as information.
     * @return true if it has been informed, false otherwise.
     */
    public boolean checkColourKnown() {
        return this.colourKnown;
    }

    /**
     * Purpose: returns whether the number of the card has been given as information.
     * @return true if it has been informed, false otherwise.
     */
    public boolean checkNumberKnown() {
        return this.numberKnown;
    }

    /**
     * Purpose: sets the number field of a users card after it has been revealed.
     * @param number the char representing the rank of the card.
     */
    public void setNumber(char number) {
        this.number = number;
        this.numberInformed();
    }

    /**
     * Purpose: sets the colour field of a users card after it has been revealed.
     * @param colour the char representing the colour of the card.
     */
    public void setColour(char colour) {
        this.colour = colour;
        this.colourInformed();
    }
}
