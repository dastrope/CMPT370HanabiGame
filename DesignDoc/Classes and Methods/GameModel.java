public class GameModel {
    private int infoTokens;
    private int fuses;
    private int turn;
    private int cardsInDeck;
    private Table gameTable;
    private DiscardPile discards;
    private Fireworks fireworks;

    public GameModel();

    public void dealTable();

    public void addToken();

    public void removeToken();

    public int getInfoTokens();

    public void removeFuse();

    public int getFuses();

    public void giveCard();

    public void discardCard();

    public void informCard();

    public void buildFirework();

    public void getFireworks();
}
