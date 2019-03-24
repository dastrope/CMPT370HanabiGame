import javafx.scene.control.Button;

public class CardButton extends Button {
    /**
     * A simple extension of the button class to allow for suit/rank setting and image address generation for a card
     */
    char suit;
    char rank;

    public CardButton(char suit, char rank){
        /**
         * Basic constructor at the moment just sets field values to those given.
         */
        this.suit = suit;
        this.rank = rank;
    }
    public char getSuit(){
        /**
         * Returns the character in the card's suit field
         */
        return this.suit;
    }
    public String getImageString(){
        /**
         * Generates an image address based on the suit and rank of the card, if not available sets it to the unknown card
         */
        String address = "cards_set1/basic/";
        switch(suit){
            case 'b':
                address+="blue "+rank+".png";
                break;

            case 'r':
                address+="red "+rank+".png";
                break;

            default:
                address = "cards_set1/unknown.png";
        }
        return address;
    }
}
