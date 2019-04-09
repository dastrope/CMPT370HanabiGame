public class Hand {
    private int cardCount;
    private Collection<Card> cards;

    public Hand();

    public void addCard(char colour, int number);

    public void removeCard(int position);

    public void informColour(char colour);

    public void informNumber(int number);
}
