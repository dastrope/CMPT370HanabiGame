import javafx.scene.control.Button;

public class CardButton extends Button {
    char suit;
    char rank;
    public CardButton(char suit, char rank){
        this.suit = suit;
        this.rank = rank;
    }
    public char getSuit(){
        return this.suit;
    }
}
