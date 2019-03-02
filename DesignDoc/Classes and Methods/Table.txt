public class Table {
    private int userID;
    private int playerCount;
    private Collection<int,Hand>;

    public Table();

    public void giveCard(int playerID, char colour, int number);

    public void removeCard(int playerID, int cardPosition);

    public void informCard(int playerID, String type, String info);
}
