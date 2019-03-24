/**
 * Class representing the all player hands in the game in the model.
 */
public class Table {
    /**
     * playerHands: an array of player hands, representing player seat numbers and turn order.
     */
    protected Hand[] playerHands;

    /**
     * Purpose: builds Table with the given number of player hands.
     *
     * @param players the number of players in the game.
     */
    public Table(int players) {
        this.playerHands = new Hand[players];
        int handSize;
        if (players >= 4) {
            handSize = 4;
        } else {
            handSize = 5;
        }
        for (Hand hand : this.playerHands) {
            hand = new Hand(handSize);
        }
    }

    /**
     * Purpose: Adds a card with the given rank and suit to the chosen player's hand.
     *
     * @param playerID the player to receive the card.
     * @param colour   the colour of the new card.
     * @param number   the number of the new card.
     */
    public void giveCard(int playerID, char colour, char number) {
        this.playerHands[playerID - 1].addCard(colour, number);
    }

    /**
     * Purpose: Adds a card with the given rank and suit to the user's hand.
     *
     * @param playerID the player to receive the card.
     */
    public void giveCard(int playerID) {
        this.playerHands[playerID - 1].addCard();
    }

    /**
     * Purpose: Removes a card at the given position from the given player hand.
     *
     * @param playerID     the player hand to remove the card from.
     * @param cardPosition the position of the card in the player's hand.
     */
    public void removeCard(int playerID, int cardPosition) {
        this.playerHands[playerID - 1].removeCard(cardPosition - 1);
    }

    /**
     * Purpose: Set the inform flags on the given player's cards.
     *
     * @param playerID the player who was informed.
     * @param type     the type of the inform done.
     * @param info     the information that was informed.
     */
    public void informCard(int playerID, String type, char info) {
        if (type == "colour") {
            this.playerHands[playerID - 1].informColour(info);
        } else if (type == "number") {
            this.playerHands[playerID - 1].informNumber(info);
        } else {
            //TODO: make exception class.
            //throw new InvalidInformTypeException("Invalid inform type: " + type);
        }
    }

    /**
     * Purpose: Set the card information and inform flags on the user's cards.
     *
     * @param playerID the player who was informed.
     * @param type     the type of the inform done.
     * @param info     the information that was informed.
     * @param index    the position of the card that was informed.
     */
    public void informCard(int playerID, String type, char info, int index) {
        if (type == "colour") {
            this.playerHands[playerID - 1].informColourUser(index, info);
        } else if (type == "number") {
            this.playerHands[playerID - 1].informNumberUser(index, info);
        } else {
            //TODO: make exception class.
            //throw new InvalidInformTypeException("Invalid inform type: " + type);
        }
    }

    /**
     * Purpose: Returns the card from the given player's hand at the given index.
     *
     * @param playerID the player whose hand contains the desired card.
     * @param index the position of the card in the hand.
     * @return the card.
     */
    public Card getPlayersCard(int playerID, int index) {
        return (this.playerHands[playerID].getCard(index));
    }
}
