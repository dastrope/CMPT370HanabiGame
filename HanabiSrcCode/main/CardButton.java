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
    public String getImageString(){
        String address = "cards_set1/basic/";
        switch(suit){
            case 'b':
                address+="blue "+rank+".png";
                break;

            case 'r':
                address+="red "+rank+".png";
                break;

            default:
                address = "cards_set1/unknown.png";
        }
        return address;
    }
}
