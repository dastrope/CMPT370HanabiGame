import java.util.ArrayList;
import java.util.Observer;

public class GameInteractionModel {
    private ArrayList observers;   // collection of observers to be informed of game state changes
    private Card selected;          // marker for which card is hovered over by the user
    private int state;              // marker for state variable for the view


// state information that will affect what UI components are active at any given time.

    private final int STATE_INACTIVE = 0;       // not the user's turn.  only the discard pile can be used.
    private final int STATE_ACTIVE = 1;         // user's turn base state.
    private final int STATE_PLAY = 2;           // user's turn, play action selected.
    private final int STATE_DISCARD = 3;        // user's turn, discard action selected.
    private final int STATE_INFORM_COLOUR = 4;  // user's turn, inform colour action selected.
    private final int STATE_INFORM_NUMBER = 5;  // user's turn, inform number action selected.

    public GameInteractionModel(){

    }

    public void setSelected(Card selectedCard){

    }
        // sets a card as being hovered over for the view's use.

    public void addObserver(Observer anObserver){

    }
        // add an observer to the list of obvservers

    public void removeObserver(Observer anObserver){

    }
        // remove an ovserver from the list of observers

    public void notifyObserver(Observer anObserver){

    }
        // notify all observers in the observer list of a change.
}
