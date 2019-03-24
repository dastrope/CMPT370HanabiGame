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

public class main extends Application {
    public double[] xHandPositions = {350, 100, 100, 600, 600}; //client is first in array, clockwise order after
    public double[] yHandPositions = {600, 450, 300, 300, 450}; //client is first in array, clockwise order after

    public static void main(String[] args) {
        launch(args);
    }

    public Scene createTable(Table table) {
        Pane root = new Pane() ;
        Scene scene = new Scene(root, 1200, 800 );

        /*create background and groups for buttons*/
        scene.setFill( Color.LAWNGREEN ) ;
        root.setBackground( null );

        Button button1   = new Button( "TEST1" ) ;
        Button  button2 = new Button( "TEST2" ) ;
        HBox pane_for_buttons = new HBox( 16 ) ;
        pane_for_buttons.getChildren().addAll(button1,button2);
        root.getChildren().add(pane_for_buttons);

        button1.setOnMouseClicked((MouseEvent event) -> {

        });


        int i = 0;
        for (Hand hand : table.playerHands) {
            HBox h = createHandBox(hand);
            root.getChildren().add(h);
            h.setLayoutX(xHandPositions[i]);
            h.setLayoutY(yHandPositions[i]);
            i++;
        }
        return scene;
    }

    public HBox createHandBox(Hand hand){
        HBox h = new HBox(20);
        for (Card card : hand.cards) {
            CardButton c = createCardButton(card);
            h.getChildren().add(c);
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

        /*set event for clicking the card to generate a play event*/
//        c.setOnMouseClicked((MouseEvent event) ->
//        {
//            c.getParent().getChildrenUnmodifiable().stream().
//
//            /*play event just simple print output with useful info*/
//            System.out.println("Player 1 played " + card1.getColour() +card1.getNumber() +" from position "+position);
//        });
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
//        Pane root_stack = new Pane() ;
//
//        /*create background and groups for buttons*/
//        Scene scene = new Scene( root_stack, 1200, 800 ) ;
//        scene.setFill( Color.LAWNGREEN ) ;
//        root_stack.setBackground( null );

//        HBox hand = new HBox(20);           // space between buttons is 20

        /*create a list to contain cards for tracking card position*/
//        ArrayList<Button> cardList = new ArrayList<>();

        /*loop to create all the cards with hardcoded information*/

        Table table1 = new Table(2);
        for (int i = 1 ; i<3 ; i++){
            table1.giveCard(i, 'r', '1');
            table1.giveCard(i, 'r', 'u');
            table1.giveCard(i);
            table1.giveCard(i, 'w', '5');
            table1.giveCard(i, 'u', '5');
        }

        Scene scene = createTable(table1);



//        for (int i = 1 ; i<6 ; i++){
//            char suit;
//            char rank;
//            if (i % 2 == 0) {
//                suit = 'b';
//            } else{
//                suit = 'r';
//            }
//            rank = Character.forDigit(i, 10);         //convert rank to char
//            Card temp = new Card(suit,rank);
//            CardButton card1 = new CardButton(temp);          //create a card
//
//            /*set the cards image and add the card to the cardlist*/
//            System.out.println(card1.card.toString());
//
//            /*set event for when the mouse hovers over the card*/
//            card1.setOnMouseEntered(( MouseEvent event ) ->
//            {
//                /*get all siblings of the card, raise any that match its suit*/
//                ObservableList<Node> cards = card1.getParent().getChildrenUnmodifiable();
//                for (Node card : cards){
//                    CardButton cd = (CardButton) card;
//                    if (cd.getColour() == card1.getColour()){
//                        card.setTranslateY(-15);
//                    }
//                }
//            });
//
//            /*set event for when the stops hovering over the card*/
//            card1.setOnMouseExited(( MouseEvent event ) ->
//            {
//                /*get all siblings of the card, lower any that match its suit*/
//                ObservableList<Node> cards = card1.getParent().getChildrenUnmodifiable();
//                for (Node card : cards){
//                    CardButton cd = (CardButton) card;
//                    if (cd.getColour() == card1.getColour()){
//                        card.setTranslateY(0);
//                    }
//                }
//            });

//            /*set event for clicking the card to generate a play event*/
//            card1.setOnMouseClicked((MouseEvent event) ->
//            {
//                int position = cardList.indexOf(card1) +1; //position in the hand based on when it was added to cardlist
//
//                /*play event just simple print output with useful info*/
//                System.out.println("Player 1 played " + card1.getColour() +card1.getNumber() +" from position "+position);
//            });
//        }
//        /*add the cardlist to the hand horizontal box object so the cards will be drawn*/
//        hand.getChildren().addAll(cardList);
//        pane_for_buttons.getChildren().addAll( button1, button2 ) ;
//        pane_for_buttons.setAlignment( Pos.CENTER ) ;
//        pane_for_buttons.setPadding( new Insets( 0, 0, 20, 0 ) ) ;

//        /*create a borderpane to allow some layout changes and add the hand to it*/
//        BorderPane border_pane = new BorderPane() ;
//        border_pane.setTop( pane_for_buttons ) ;
//        border_pane.setBottom(hand);

        /*add that borderpane to the main pane and set its position in it*/
//        root_stack.getChildren().addAll( border_pane);
//        root_stack.setLayoutX(350);
//        root_stack.setLayoutY(600);

        /*draw the window and scene*/
        stage.setScene( scene ) ;
        stage.show() ;
    }
}
