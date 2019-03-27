import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class HandBox extends HBox {

    public Hand hand;
    public ArrayList<CardButton> children;

    public HandBox(Hand aHand, double spacing){
        this.hand = aHand;
        this.children = new ArrayList<>();
        this.setSpacing(spacing);
    }

    public void addCardButton(CardButton cb) {
        this.children.add(cb);
    }

    public Hand getHand(){
        return this.hand;
    }
}
