import javafx.scene.control.Button;

public class CardButton extends Button {
    /**
     * A simple extension of the button class to allow for suit/rank setting and image address generation for a card
     */
    Card card;

    /**
     * Basic constructor at the moment just sets field values to those given.
     * @param card A card that that will be clickable as a button
     */
    public CardButton(Card card){

        this.card = card;
    }

    /**
     * Returns the character in the card's colour field
     */
    public char getColour(){

        return this.card.getColour();
    }

    /**
     * Returns the character in the card's number field
     */
    public char getNumber(){

        return this.card.getNumber();
    }


    /**
     * Generates an image address based on the suit and rank of the card, if not available sets it to the unknown card
     */
    public String getImageString(){

        String address = "cards/";


        address+= this.card.toString() + ".png";
        System.out.println(address);



        return address;
    }
}
