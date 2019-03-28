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
        for (int i = 0; i < players; i++) {
            playerHands[i] = new Hand(handSize);
        }
    }

    /**
     * Purpose: Adds a card with the given rank and suit to the chosen player's hand.
     *
     * @param seat the player to receive the card.
     * @param colour   the colour of the new card.
     * @param number   the number of the new card.
     */
    public void giveCard(int seat, char colour, char number) {
        this.playerHands[seat-1].addCard(colour, number);
    }

    /**
     * Purpose: Adds a card with the given rank and suit to the user's hand.
     *
     * @param seat the player to receive the card.
     */
    public void giveCard(int seat) {
        this.playerHands[seat - 1].addCard();
    }

    /**
     * Purpose: Removes a card at the given position from the given player hand.
     *
     * @param seat     the player hand to remove the card from.
     * @param cardPosition the position of the card in the player's hand.
     */
    public void removeCard(int seat, int cardPosition) {
        this.playerHands[seat - 1].removeCard(cardPosition);
    }

    /**
     * Purpose: Set the inform flags on the given player's cards.
     *
     * @param seat     the player who was informed.
     * @param type     the type of the inform done.
     * @param info     the information that was informed.
     */
    public void informCard(int seat, String type, char info) {
        if (type.equals("colour")) {
            this.playerHands[seat - 1].informColour(info);
        } else if (type.equals("number")) {
            this.playerHands[seat - 1].informNumber(info);
        } else {
            //TODO: make exception class.
            //throw new InvalidInformTypeException("Invalid inform type: " + type);
        }
    }

    /**
     * Purpose: Set the card information and inform flags on the user's cards.
     *
     * @param seat the player who was informed.
     * @param type     the type of the inform done.
     * @param info     the information that was informed.
     * @param positions    the position of the card that was informed.
     */
    public void informCard(int seat, String type, char info, boolean[] positions) {
        for (int i = 0; i < positions.length; i++){
            if (positions[i]) {
                if (type.equals("colour")) {
                    this.playerHands[seat - 1].informColourUser(i + 1, info);
                } else {
                    this.playerHands[seat - 1].informNumberUser(i + 1, info);
                }
            }
        }
    }

    /**
     * Purpose: Returns the card from the given player's hand at the given index.
     *
     * @param seat the player whose hand contains the desired card.
     * @param position the position of the card in the hand.
     * @return the card.
     */
    public Card getPlayersCard(int seat, int position) {
        return (this.playerHands[seat - 1].getCard(position));
    }
}
