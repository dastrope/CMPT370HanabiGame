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
    private int time;               // turn timer
    private int timeLimit;          // turn time limit time for this game

    /**
     * Purpose: Instantiates the game model to the appropriate start state based on timeout, numOfPlayer and gameType.
     *
     * @param timeout the turn time limit in seconds for each player. Must be 1 =<.
     * @param numOfPlayers the number of players in the game. Must be 2-5
     * @param gameType the type of game to be played. Must be "default", "extended", and "wild".
     */
    public GameModel(int timeout, int numOfPlayers, String gameType){
        this.infoTokens = 8;
        this.fuses = 3;
        this.timeLimit = timeout;
        this.playerCount = numOfPlayers;

        this.gameTable = new Table(numOfPlayers);
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
    public void dealTable(String[][] startinghands){
        for (int i = 0; i < this.playerCount; i++){
            if (startinghands[i].length == 0){
                for (int j = 0; j < 4; j++) {
                    this.gameTable.giveCard(i + 1);
                }
                if (this.playerCount <= 3){
                    this.gameTable.giveCard(i + 1);
                }
                this.userID = i + 1;
            } else{
                for( String card : startinghands[i]){
                    this.gameTable.giveCard(i + 1, card.charAt(0), card.charAt(1));
                }
            }
        }
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
    public void discardCard(int handPosition){
        this.gameTable.removeCard(this.currentTurn(), handPosition);
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
    }

    /**
     * Purpose: Update the user's cards at the given indexes with the given information.
     *
     * @param info the information the player was informed about.
     * @param index the index of the card that was informed.
     */
    public void informSelf(char info, int index) {
        String numarray = "12345";
        if (numarray.contains(Character.toString(info))){
            this.gameTable.informCard(this.playerSeat(), "number", info, index);
        } else {
            this.gameTable.informCard(this.playerSeat(), "colour", info, index);
        }
    }

    /**
     * @param player
     * @param handPosition
     */
    public void playCard(int player, int handPosition){

    }
        // attempts to play the card in the chosen position to the fireworks stack.

    public int getFireworkHeight(char color){
        return 0;
    }
        // returns the current height of the firework stack of the given color.
    
    public void nextTurn(){

    }
        // increments the current player counter.  must only be called after all results from current turn are completed.
        
    public int currentTurn(){
        return 0;
    }
        // returns the seat number of the player whose turn it is currently.
        
    public int showTime(){
        return 0;
    }
        // returns the remaining time for the current turn.
        
    public int playerSeat(){
        return userID;
    }
        // returns the userID seat number
}
