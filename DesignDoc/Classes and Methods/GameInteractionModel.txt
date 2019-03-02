public class GameInteractionModel {
    private Collection observers;
    private Card selected;
    private int state;

    private final int STATE_INACTIVE;
    private final int STATE_ACTIVE;
    private final int STATE_PLAY;
    private final int STATE_DISCARD;
    private final int STATE_INFORM_COLOUR;
    private final int STATE_INFORM_NUMBER;

    public GameInteractionModel();

    public void setSelected(Card selectedCard);

    public void addObserver(Observer anObserver);

    public void removeObserver(Observer anObserver);

    public void notifyObserver(Observer anObserver);
}
