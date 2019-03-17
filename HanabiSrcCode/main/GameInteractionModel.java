public class GameInteractionModel {
    private Collection observers;   // collection of observers to be informed of game state changes
    private Card selected;          // marker for which card is hovered over by the user
    private int state;              // marker for state variable for the view


// state information that will affect what UI components are active at any given time.

    private final int STATE_INACTIVE;       // not the user's turn.  only the discard pile can be used.
    private final int STATE_ACTIVE;         // user's turn base state.
    private final int STATE_PLAY;           // user's turn, play action selected.
    private final int STATE_DISCARD;        // user's turn, discard action selected.
    private final int STATE_INFORM_COLOUR;  // user's turn, inform colour action selected.
    private final int STATE_INFORM_NUMBER;  // user's turn, inform number action selected.

    public GameInteractionModel();

    public void setSelected(Card selectedCard);
        // sets a card as being hovered over for the view's use.

    public void addObserver(Observer anObserver);
        // add an observer to the list of obvservers

    public void removeObserver(Observer anObserver);
        // remove an ovserver from the list of observers

    public void notifyObserver(Observer anObserver);
        // notify all observers in the observer list of a change.
}
