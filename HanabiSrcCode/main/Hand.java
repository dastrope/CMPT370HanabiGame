/**
 * The class representing a player's current hand of cards in the model.
 */
public class Hand {
    /**
     * the amount of cards in the player hands
     */
    private int handSize;

    /**
     * a collecton that will store the current cards in the hand
     */
    protected Card[] cards;


    /**
     * Contructor for Hand. Initializes handSize and cards.
     * @param handSize amount of cards in the player hands
     */
    public Hand(int handSize){
        this.handSize = handSize;
        this.cards = new Card[this.handSize];
    }

    /**
     * adds a card with known colour and number to cards at the earliest indexed null
     * @param colour the colour of the card
     * @param number the rank of the card
     */
    public void addCard(char colour, char number){
        Card newCard = new Card(colour,number);
        for (int i = 0; i < this.cards.length; i++){
            if ((this.cards[i] == null) ){

                this.cards[i] = newCard;
                break;
            }
        }
        // TODO: make an Exception package
        //throw new HandFullException("Attempted to addCard to a full hand");
    }

    /**
     * adds a card with unknown colour and number to cards at the leftmost null
     */
    public void addCard(){
        Card newCard = new Card();
        for (int i = 0; i < this.cards.length; i++){
            if ((this.cards[i] == null) ){
                this.cards[i] = newCard;
                break;
            }
        }
        // TODO: make an Exception package
        //throw new HandFullException("Attempted to addCard to a full hand");
    }

    /**
     * remove the card at a given position in cards
     * @param position the postion of the card to be removed
     */
    public void removeCard(int position){
        if (this.cards[position] == null){
            // TODO: make an Exception package
            //throw new CardNotFoundException("There was no card at" + position);
        }
        this.cards[position] = null;

    }

    /**
     * Sets the colourKnown to true to each card object in cards that belongs to input colour
     * @param colour the colour to search for within the card
     */
    public void informColour(char colour){
        for (int i = 0; i < this.cards.length; i++){
            if (this.cards[i].getColour() == colour){
                this.cards[i].colourInformed();
            }
        }
    }

    /**
     * Sets the numberKnown to true to each card object in cards that belongs to input colour
     * @param number the number to search for within the card
     */
    public void informNumber(char number){
        for (int i = 0; i < this.cards.length; i++){
            if (this.cards[i].getNumber() == number){
                this.cards[i].numberInformed();
            }
        }
    }

    /**
     * Sets the user's card at the given index to the given colour
     * @param index the index of the card that has been informed
     * @param colour the colour that was informed
     */
    public void informColourUser(int index, char colour){
        this.cards[index].setColour(colour);
    }

    /**
     * Sets the user's card at the given index to the given number
     * @param index the index of the card that has been informed
     * @param number the number that was informed
     */
    public void informNumberUser(int index, char number){
        this.cards[index].setNumber(number);
    }

}
