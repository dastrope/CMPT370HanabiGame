import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.smartcardio.Card;
import java.util.ArrayList;

public class main extends Application {


    public static void main(String[] args) {
        launch(args);
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
        Pane root_stack = new Pane() ;

        /*create background and groups for buttons*/
        Scene scene = new Scene( root_stack, 1200, 800 ) ;
        scene.setFill( Color.LAWNGREEN ) ;
        root_stack.setBackground( null );
        Button button1   = new Button( "TEST1" ) ;
        Button  button2 = new Button( "TEST2" ) ;
        HBox pane_for_buttons = new HBox( 16 ) ;
        HBox hand = new HBox(20);           // space between buttons is 20

        /*create a list to contain cards for tracking card position*/
        ArrayList<Button> cardList = new ArrayList<>();

        /*loop to create all the cards with hardcoded information*/
        for (int i = 0 ; i<5 ; i++){
            char suit;
            char rank;
            if (i % 2 == 0) {
                suit = 'b';
            } else{
                suit = 'r';
            }
            rank = Character.forDigit(i+1, 10);         //convert rank to char
            CardButton card1 = new CardButton(suit, rank);          //create a card

            /*set the cards image and add the card to the cardlist*/
            ImageView image = new ImageView(new Image(card1.getImageString()));
            image.setFitHeight(100);
            image.setFitWidth(66);
            card1.setGraphic(image);
            cardList.add(card1);

            /*set event for when the mouse hovers over the card*/
            card1.setOnMouseEntered(( MouseEvent event ) ->
            {
                /*get all siblings of the card, raise any that match its suit*/
                ObservableList<Node> cards = card1.getParent().getChildrenUnmodifiable();
                for (Node card : cards){
                    CardButton cd = (CardButton) card;
                    if (cd.suit == card1.suit){
                        card.setTranslateY(-15);
                    }
                }
            });

            /*set event for when the stops hovering over the card*/
            card1.setOnMouseExited(( MouseEvent event ) ->
            {
                /*get all siblings of the card, lower any that match its suit*/
                ObservableList<Node> cards = card1.getParent().getChildrenUnmodifiable();
                for (Node card : cards){
                    CardButton cd = (CardButton) card;
                    if (cd.suit == card1.suit){
                        card.setTranslateY(0);
                    }
                }
            });

            /*set event for clicking the card to generate a play event*/
            card1.setOnMouseClicked((MouseEvent event) ->
            {
                int position = cardList.indexOf(card1); //position in the hand based on when it was added to cardlist

                /*play event just simple print output with useful info*/
                System.out.println("Player 1 played " + card1.suit +card1.rank +" from position "+position);
            });
        }
        /*add the cardlist to the hand horizontal box object so the cards will be drawn*/
        hand.getChildren().addAll(cardList);
        pane_for_buttons.getChildren().addAll( button1, button2 ) ;
        pane_for_buttons.setAlignment( Pos.CENTER ) ;
        pane_for_buttons.setPadding( new Insets( 0, 0, 20, 0 ) ) ;

        /*create a borderpane to allow some layout changes and add the hand to it*/
        BorderPane border_pane = new BorderPane() ;
        border_pane.setTop( pane_for_buttons ) ;
        border_pane.setBottom(hand);

        /*add that borderpane to the main pane and set its position in it*/
        root_stack.getChildren().addAll( border_pane);
        root_stack.setLayoutX(350);
        root_stack.setLayoutY(600);

        /*draw the window and scene*/
        stage.setScene( scene ) ;
        stage.show() ;
    }
}
