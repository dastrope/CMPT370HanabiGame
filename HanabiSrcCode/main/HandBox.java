import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class HandBox extends HBox {
    public int seat;
    public Hand hand;
    public ArrayList<CardButton> cardList;

    public HandBox(Hand aHand, int seat, double spacing){
        this.hand = aHand;
        this.seat = seat;
        this.cardList = new ArrayList<>();
        this.setSpacing(spacing);
    }

    public void addCardButton(CardButton cb) {
        this.cardList.add(cb);
    }

    public Hand getHand(){
        return this.hand;
    }

    public ArrayList<CardButton> getCardList(){ return this.cardList; }
}
