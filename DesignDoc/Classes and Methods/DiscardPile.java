public class DiscardPile {
    private int discardCount;
    private Collection<Card,int> discards;

    public DiscardPile();

    public Collection<Card,int> getDiscards();

    public void addDiscard(Card discardedCard);

    public int getDiscardCount();
}
