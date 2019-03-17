public class Table {
    private Collection<int,Hand> playerHands;   // collection of player hands, indexed by player seat numbers

    public Table();
        // contstructs player hands by giving each player their cards.  constructs own player's hand by giving them blank cards.

    public void giveCard(int playerID, char colour, int number);
        // adds a card with the given rank and suit to the chosen player's hand.

    public void removeCard(int playerID, int cardPosition);
        // removes the card in the chosen player's hand from the chosen hand position.

    public void informCard(int playerID, String type, char info);
        // informs the chosen player about cards in their hand of the chosen type.
}
