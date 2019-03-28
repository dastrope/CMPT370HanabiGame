import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class main extends Application {
    public double[] xHandPositions = {100, 100, 600, 600}; //client is first in array, clockwise order after
    public double[] yHandPositions = {450, 300, 300, 450}; //client is first in array, clockwise order after
    public double userPositionX = 350;
    public double userPositionY = 600;
    public GameModel model;

    public static void main(String[] args) {
        launch(args);
    }

    public Scene createTable(Table table) {
        Pane root = new Pane() ;
        Scene scene = new Scene(root, 1200, 800 );
        ArrayList<HandBox> handList= new ArrayList<>();

        /*create background and groups for buttons*/
        scene.setFill( Color.LAWNGREEN ) ;
        root.setBackground( null );

        Button playButton = new Button("Play a card");
        Button discardButton = new Button("Discard a card");
        Button informColourButton = new Button("Inform colour");
        Button informNumberButton = new Button("Inform number");
        HBox pane_for_buttons = new HBox( 16 ) ;
        pane_for_buttons.getChildren().addAll(playButton,discardButton,informColourButton,informNumberButton);
        root.getChildren().add(pane_for_buttons);

        playButton.setOnMouseClicked((MouseEvent event) -> {
            enableUserCards(table,handList);
        });

        discardButton.setOnMouseClicked((MouseEvent event) -> {
            enableUserCards(table,handList);
        });

        informColourButton.setOnMouseClicked((MouseEvent event) -> {
            enableOtherCards(table,handList);
        });

        informNumberButton.setOnMouseClicked((MouseEvent event) -> {
            enableOtherCards(table,handList);
        });

        int i = 0;
        for (Hand hand : table.playerHands) {
            HandBox h = createHandBox(hand);
            root.getChildren().add(h);
            handList.add(h);
            if (hand == table.playerHands[model.playerSeat()-1]) {
                h.setLayoutX(userPositionX);
                h.setLayoutY(userPositionY);
            } else {
                h.setLayoutX(xHandPositions[i]);
                h.setLayoutY(yHandPositions[i]);
                i++;
            }
        }
        return scene;
    }

    public void enableUserCards(Table table, ArrayList<HandBox> handList) {
        for (HandBox hand : handList) {
            if (hand.getHand() !=  table.playerHands[model.playerSeat()-1]) {
                hand.setDisable(true);
            } else {
                hand.setDisable(false);
            }
        }
    }

    public void enableOtherCards(Table table, ArrayList<HandBox> handList) {
        for (HandBox hand : handList) {
            if (hand.getHand() ==  table.playerHands[model.playerSeat()-1]) {
                hand.setDisable(true);
            } else {
                hand.setDisable(false);
            }
        }
    }

    public HandBox createHandBox(Hand hand){
        HandBox h = new HandBox(hand,15);
        for (Card card : hand.cards) {
            CardButton c = createCardButton(card);
            h.getChildren().add(c);
            h.addCardButton(c);
        }
        return h;
    }

    public CardButton createCardButton(Card ncard) {
        CardButton c = new CardButton(ncard);
        ImageView image = new ImageView(new Image(c.getImageString()));
        image.setFitHeight(100);
        image.setFitWidth(66);
        c.setGraphic(image);

        /*set event for when the mouse hovers over the card*/
        c.setOnMouseEntered(( MouseEvent event ) ->
        {
            /*get all siblings of the card, raise any that match its suit*/
            ObservableList<Node> cards = c.getParent().getChildrenUnmodifiable();
            for (Node card : cards){
                CardButton cd = (CardButton) card;
                if (cd.getColour() == c.getColour()){
                    card.setTranslateY(-15);
                }
            }
        });

        /*set event for when the stops hovering over the card*/
        c.setOnMouseExited(( MouseEvent event ) ->
        {
            /*get all siblings of the card, lower any that match its suit*/
            ObservableList<Node> cards = c.getParent().getChildrenUnmodifiable();
            for (Node card : cards){
                CardButton cd = (CardButton) card;
                if (cd.getColour() == c.getColour()){
                    card.setTranslateY(0);
                }
            }
        });


        return c;
    }



    @Override
    public void start(Stage stage) {
        /*
         * This is all just code for testing certain functionality, much of it is janky and should not be replicated
         * for the final product. No states are present so things are always drawn/done the same way, and objects are all
         * hardcoded in and completely exist only in the view itself, so of course that will require changing.
         * Some of the event handlers will also need to be moved to controller, although for hovering events
         * I don't think that is necessary at the moment, as those are entirely clientside view only changes.
         * Optimization may also be possible for things like image drawing, should look at that
         */

        model = new GameModel(15,5,"default");

        String[][] data = {{"b1", "b2", "b4", "g1"}
                , {}
                , {"b1", "b3", "g1", "g2"}
                , {"b2", "b4", "g1", "g3"}
                , {"r2", "w4", "y5", "g1"}
        };

        model.dealTable(data);

        Scene scene = createTable(model.gameTable);

        /*draw the window and scene*/
        stage.setScene( scene ) ;
        stage.show() ;
    }
}
