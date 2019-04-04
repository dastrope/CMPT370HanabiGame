import java.util.ArrayList;
import java.util.HashMap;

public class GameModel {
    private int infoTokens; // must be an integer between 0 and 8, these tokens are a resource used to give other players hints.
    private int fuses;      // initialized to 3, the team loses one for each mistake.  once fuses reaches 0, the game ends.
    private int turn;       // marks the player number of the player whose turn it currently is.
    private int userID;         // this client's player position at the table
    private int playerCount;    // number of players in the current game
    private int cardsInDeck;    // counts the remaining cards in the deck.  If the deck runs out, each player gets one more turn before the game ends.
    private Table gameTable;    // holds the table state, containing hands, cards, and revealed information for each player.
    private DiscardPile discards;   // keeps track of which cards have been discarded over the course of the game.
    private Fireworks fireworks;    // maintains the firework stacks.
    private long timeLimit;          // turn time limit time for this game
    private long turnStart;          // clock time at start of turn

    /**
     * Purpose: Instantiates the game model to the appropriate start state based on timeout, numOfPlayer and gameType.
     *
     * @param timeout the turn time limit in seconds for each player. Must be 1 <=.
     * @param startingHands hands of cards for all the current players.
     * @param gameType the type of game to be played. Must be "default", "extended", and "wild".
     */
    public GameModel(int timeout, String gameType, ArrayList<String[]> startingHands){
        this.infoTokens = 8;
        this.fuses = 3;
        this.timeLimit = timeout * 1000;
        this.playerCount = startingHands.size();
        this.turn = 1;

        this.gameTable = new Table(playerCount);
        this.dealTable(startingHands);
        this.discards = new DiscardPile();
        this.fireworks = new Fireworks(gameType);

        if (gameType.equals("default")){
            this.cardsInDeck = 50;
        } else {
            this.cardsInDeck = 60;
        }
    }

    /**
     * Purpose: Adds all the cards from the deal to each player's respective hand.
     *
     * @param startinghands Array of Array of strings representing each player's hand.
     */
    public void dealTable(ArrayList<String[]> startinghands){
        for (int i = 0; i < this.playerCount; i++){
            if (startinghands.get(i).length == 0){
                for (int j = 0; j < 4; j++) {
                    this.gameTable.giveCard(i+1);
                }
                if (this.playerCount <= 3){
                    this.gameTable.giveCard(i+1);
                }
                this.userID = i + 1;
            } else{
                for( String card : startinghands.get(i)){
                    this.gameTable.giveCard(i+1, card.charAt(0), card.charAt(1));
                }
            }
        }
    }

    /**
     * Purpose: returns the table for the given game
     * @return the game table
     */
    public Table getGameTable(){
        return this.gameTable;
    }

    /**
     * Purpose: adds a token to the information token pile
     */
    public void addToken(){
        if (this.infoTokens == 8){
            // throw new TokensException("tried to add a 9th info token");
            // TODO create a TokensException
        }
        this.infoTokens += 1;
    }

    /**
     * Purpose: removes a token from the information token pile
     */
    public void removeToken(){
        if (this.infoTokens == 0){
            // throw new TokensException("tried to remove a nonexistent token");
            // TODO create a TokensException
        }
        this.infoTokens -= 1;
    }

    /**
     * Purpose: returns the current number of available information tokens.
     *
     * @return the number of info tokens
     */
    public int getInfoTokens(){
        return this.infoTokens;
    }

    /**
     * Purpose: removes a fuse from the stack of fuses.  If none are left, game should end.
     */
    public void removeFuse(){
        this.fuses -= 1;
    }

    /**
     * Purpose: returns the current number of available fuses.
     *
     * @return the number of fuses remaining
     */
    public int getFuses(){
        return this.fuses;
    }
        // returns the current number of fuses remaining.

    /**
     * Purpose: adds a new Card to the hand of the current player
     *
     * @param newcard a two character string of the card to be added
     */
    public void giveCard(String newcard){
        if(this.playerSeat() == this.currentTurn()){
            this.gameTable.giveCard(this.playerSeat());
        } else {
            this.gameTable.giveCard(this.currentTurn(), newcard.charAt(0), newcard.charAt(1));
        }
    }

    /**
     * Purpose: remove a card from the current player's hand from the specified location
     *
     * @param handPosition the position of the card to be removed
     */
    public void discardCard(int handPosition) {
        this.discards.addDiscard(this.gameTable.getPlayersCard(this.currentTurn(), handPosition));
        this.gameTable.removeCard(this.currentTurn(), handPosition);
        this.addToken();
    }

    /**
     * Purpose: remove a card from the users player's hand from the specified location
     *
     * @param handPosition the position of the card to be removed
     */
    public void userDiscardCard(int handPosition,String card){
        this.gameTable.getPlayersCard(this.currentTurn(),handPosition).setColour(card.charAt(0));
        this.gameTable.getPlayersCard(this.currentTurn(),handPosition).setColour(card.charAt(1));
        this.discards.addDiscard(this.gameTable.getPlayersCard(this.currentTurn(),handPosition));
        this.gameTable.removeCard(this.currentTurn(), handPosition);
        this.addToken();
    }

    /**
     * Purpose: Update the markers for informed cards in other players' hands.
     *
     * @param player the player being informed
     * @param info the information being given
     */
    public void informOther(int player, char info){
        String numarray = "12345";
        if (numarray.contains(Character.toString(info))){
            this.gameTable.informCard(player, "number", info);
        }else{
            this.gameTable.informCard(player, "colour", info);
        }
        this.removeToken();
    }

    /**
     * Purpose: Update the user's cards at the given indexes with the given information.
     *
     * @param info the information the player was informed about.
     * @param positionString the positions of the cards that were informed.
     */
    public void informSelf(char info, String positionString) {
        String numarray = "12345";
        String[] positions = positionString.split(",");
        if (numarray.contains(Character.toString(info))){
            this.gameTable.informCard(this.playerSeat(), "number", info, positions);
        } else {
            this.gameTable.informCard(this.playerSeat(), "colour", info, positions);
        }
        this.removeToken();
    }

    /**
     * Purpose: Update a player's card after a successful play.
     *
     * @param handPosition the Position of the card that was played
     */
    public void playCardSuccess(int handPosition){
        Card played = this.gameTable.getPlayersCard(this.currentTurn(), handPosition);
        this.fireworks.addFirework(played.getColour());
        this.gameTable.removeCard(this.currentTurn(), handPosition);
    }

    /**
     * Purpose: Update a user's card after a successful play.
     *
     * @param handPosition the Position of the card that was played
     */
    public void userPlayCardSuccess(int handPosition,String card){
        this.gameTable.getPlayersCard(this.currentTurn(),handPosition).setColour(card.charAt(0));
        this.gameTable.getPlayersCard(this.currentTurn(),handPosition).setColour(card.charAt(1));
        Card played = this.gameTable.getPlayersCard(this.currentTurn(), handPosition);
        this.fireworks.addFirework(played.getColour());
        this.gameTable.removeCard(this.currentTurn(), handPosition);
    }

    /**
     * Purpose: returns the height of the requested firework stack as an integer
     * @param colour the colour of the fireworks stack in question
     * @return the height of the firework stack
     */
    public int getFireworkHeight(char colour){
        return this.fireworks.stackSize((colour));
    }
        // returns the current height of the firework stack of the given color.

    /**
     * Purpose: Returns
     * @return
     */
    public Fireworks getFireworks() {
        return fireworks;
    }

    /**
     * Purpose: progresses the turn counter and resets the turn timer
     */
    public void nextTurn(){
        this.turn += 1;
        this.turnStart = System.currentTimeMillis();
    }
        // increments the current player counter.  must only be called after all results from current turn are completed.

    /**
     * Purpose: returns the player number whose turn is currently being taken
     *
     * @return the current player's seat number
     */
    public int currentTurn(){
        return (this.turn % this.playerCount)+1;
    }
        // returns the seat number of the player whose turn it is currently.

    /**
     * Purpose: returns the time remaining of the current player's turn, in milliseconds
     *
     * @return a number of milliseconds remaining in the current turn
     */
    public long showTime(){
        return this.timeLimit - (System.currentTimeMillis() - this.turnStart);
    }
        // returns the remaining time for the current turn.

    /**
     * Purpose: returns the number of the user's seat at the game table
     *
     * @return the user's seat number
     */

    /**
     * Purpose: returns the discard pile of the current game
     * @return the DiscardPile object
     */
    public int playerSeat(){
        return this.userID;
    }
        // returns the userID seat number

    public DiscardPile getDiscards(){
        return this.discards;
    }

    @Override
    public String toString() {
        return "GameModel{" +
                "infoTokens=" + infoTokens +
                ", fuses=" + fuses +
                ", turn=" + turn +
                ", userID=" + userID +
                ", playerCount=" + playerCount +
                ", cardsInDeck=" + cardsInDeck +
                ", gameTable=" + gameTable +
                ", discards=" + discards +
                ", fireworks=" + fireworks +
                ", timeLimit=" + timeLimit +
                ", turnStart=" + turnStart +
                '}';
    }
}
