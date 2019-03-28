import javafx.application.Application;
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
    private double[] xHandPositions = {100, 100, 600, 600}; //client is first in array, clockwise order after
    private double[] yHandPositions = {450, 300, 300, 450}; //client is first in array, clockwise order after
    private double userPositionX = 350;
    private double userPositionY = 600;
    private GameModel model;
    private int state;
    private final int STATE_WAITING = 0;
    private final int STATE_READY = 1;
    private final int STATE_PLAYING = 2;
    private final int STATE_DISCARDING = 3;
    private final int STATE_INFORMING_COLOUR = 4;
    private final int STATE_INFORMING_NUMBER = 5;

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

        playButton.setOnMouseClicked((MouseEvent event) -> this.setPlayState(handList, table));

        discardButton.setOnMouseClicked((MouseEvent event) -> this.setDiscardState(handList, table));

        informColourButton.setOnMouseClicked((MouseEvent event) ->this.setInformColourState(handList, table));

        informNumberButton.setOnMouseClicked((MouseEvent event) ->this.setInformNumberState(handList, table));

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

    public void disableAllCards(ArrayList<HandBox> hands){
        for (HandBox hand : hands){
            hand.setDisable(true);
            for (CardButton card : hand.getCardList()){
                resetCard(card);
            }
        }
    }

    public void setPlayState(ArrayList<HandBox> handList, Table table){
        this.resetAllCards(handList);
        if (this.state == STATE_PLAYING) {
            this.state = STATE_READY;
        } else {
            this.state = STATE_PLAYING;
            for (HandBox hand : handList) {
                if (hand.getHand() != table.playerHands[model.playerSeat() - 1]) {
                    hand.setDisable(true);
                } else {
                    for (int i = 0; i < hand.getCardList().size(); i++) {
                        CardButton cb = hand.getCardList().get(i);
                        int position = i + 1;
                        cb.setOnMouseClicked((MouseEvent e) -> {
                            System.out.println("Player " + model.playerSeat() + " played " + cb.card +
                                    " from position " + position);
                        });
                    }
                }
            }
        }
    }

    public void setDiscardState(ArrayList<HandBox> handList, Table table) {
        this.resetAllCards(handList);
        if (this.state == STATE_DISCARDING) {
            this.state = STATE_READY;
        } else {
            this.state = STATE_DISCARDING;
            for (HandBox hand : handList) {
                if (hand.getHand() != table.playerHands[model.playerSeat() - 1]) {
                    hand.setDisable(true);
                } else {
                    for (int i = 0; i < hand.getCardList().size(); i++) {
                        CardButton cb = hand.getCardList().get(i);
                        int position = i + 1;
                        cb.setOnMouseClicked((MouseEvent e) -> {
                            System.out.println("Player " + model.playerSeat() + " discarded " + cb.card +
                                    " from position " + position);
                        });
                    }
                }
            }
        }
    }

    public void setInformColourState(ArrayList<HandBox> handList, Table table){
        this.resetAllCards(handList);
        if (this.state == STATE_INFORMING_COLOUR){
            this.state = STATE_READY;
        } else {
            this.state = STATE_INFORMING_COLOUR;
            ArrayList<CardButton> raised = new ArrayList<>();
            for (HandBox hand : handList) {
                if (hand.getHand() == table.playerHands[model.playerSeat() - 1]) {
                    hand.setDisable(true);
                } else {
                    for (int i = 0; i < hand.getCardList().size(); i++) {
                        CardButton cb = hand.getCardList().get(i);
                        cb.setOnMouseEntered((MouseEvent e) -> {
                            for (CardButton c : hand.getCardList()) {
                                if (c.card.getColour() == cb.card.getColour()) {
                                    c.setTranslateY(-15);
                                    raised.add(c);
                                }
                            }
                        });

                        cb.setOnMouseExited((MouseEvent e) -> {
                            for (CardButton c : hand.getCardList()) {
                                c.setTranslateY(0);
                            }
                            raised.clear();
                        });

                        cb.setOnMouseClicked((MouseEvent e) -> {
                            int informedPlayer = handList.indexOf(hand) + 1;
                            String informedCards = " colour " + cb.card.getColour();
                            for (CardButton c : raised) {
                                informedCards += " position " + (hand.getCardList().indexOf(c) + 1);
                            }
                            System.out.println("Player " + model.playerSeat() + " informed player " + informedPlayer +
                                    informedCards);
                        });
                    }
                }
            }
        }
    }

    public void setInformNumberState(ArrayList<HandBox> handList, Table table){
        this.resetAllCards(handList);
        if (this.state == STATE_INFORMING_NUMBER){
            this.state = STATE_READY;
        } else {
            this.state = STATE_INFORMING_NUMBER;
            ArrayList<CardButton> raised = new ArrayList<>();
            for (HandBox hand : handList) {
                if (hand.getHand() == table.playerHands[model.playerSeat() - 1]) {
                    hand.setDisable(true);
                } else {
                    for (int i = 0; i < hand.getCardList().size(); i++) {
                        CardButton cb = hand.getCardList().get(i);
                        cb.setOnMouseEntered((MouseEvent e) -> {
                            for (CardButton c : hand.getCardList()) {
                                if (c.card.getNumber() == cb.card.getNumber()) {
                                    c.setTranslateY(-15);
                                    raised.add(c);
                                }
                            }
                        });

                        cb.setOnMouseExited((MouseEvent e) -> {
                            for (CardButton c : hand.getCardList()) {
                                c.setTranslateY(0);
                            }
                            raised.clear();
                        });

                        cb.setOnMouseClicked((MouseEvent e) -> {
                            int informedPlayer = handList.indexOf(hand) + 1;
                            String informedCards = " number " + cb.card.getNumber();
                            for (CardButton c : raised) {
                                informedCards += " position " + (hand.getCardList().indexOf(c) + 1);
                            }
                            System.out.println("Player " + model.playerSeat() + " informed player " + informedPlayer +
                                    informedCards);
                        });
                    }
                }
            }
        }
    }

    public void resetAllCards(ArrayList<HandBox> hands){
        for (HandBox hand : hands){
            hand.setDisable(false);
            for (CardButton card : hand.getCardList()){
                resetCard(card);
            }
        }
    }

    public void resetCard(CardButton card){
        card.setOnMouseClicked((MouseEvent e) ->{});
        card.setOnMouseEntered((MouseEvent e) ->{});
        card.setOnMouseEntered((MouseEvent e) ->{});
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
        this.state = STATE_READY;
        model = new GameModel(15,5,"default");

        String[][] data = {{"b1", "b2", "b4", "g1"}
                , {}
                , {"b1", "b3", "g1", "g2"}
                , {"b2", "b4", "g1", "g3"}
                , {"r2", "w4", "y5", "g1"}
        };

        model.dealTable(data);

        Scene scene = createTable(model.getGameTable());

        /*draw the window and scene*/
        stage.setScene( scene ) ;
        stage.show() ;
    }
}
