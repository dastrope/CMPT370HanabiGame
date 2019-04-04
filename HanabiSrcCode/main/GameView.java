import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GameView {
    private double[] xHandPositions = {50, 50, 850, 850}; //client is first in array, clockwise order after
    private double[] yHandPositions = {500, 200, 200, 500}; //client is first in array, clockwise order after
    private double userPositionX = 450;
    private double userPositionY = 600;
    private GameController cont;
    private GameModel model;
    private int state;
    private final int STATE_WAITING = 0;
    private final int STATE_READY = 1;
    private final int STATE_PLAYING = 2;
    private final int STATE_DISCARDING = 3;
    private final int STATE_INFORMING_COLOUR = 4;
    private final int STATE_INFORMING_NUMBER = 5;
    private double cardHeight = 75;
    private double cardWidth = 50;
    private double canvasHeight;
    private double canvasWidth;
    private ArrayList<HandBox> handList;
    private ArrayList<Circle> tokenList;
    private ArrayList<FireworkRectangle> fireworkList;
    private ArrayList<Button> actionButtons;
    private Pane root;

//    public static void main(String[] args) {
//        launch(args);
//    }

    public GameView(GameModel aModel, GameController aController){
        this.model = aModel;
        this.cont = aController;
        this.handList = new ArrayList<>();
        this.tokenList = new ArrayList<>();
        this.fireworkList = new ArrayList<>();
        this.actionButtons = new ArrayList<>();
        this.root = new Pane();
        this.canvasHeight = 800;
        this.canvasWidth = 1200;
    }

    public void setCont(GameController cont) { this.cont = cont; }

    public void setModel(GameModel model){
        this.model = model;
    }

    public Scene createGame() {
        Table table = model.getGameTable();
        Scene scene = new Scene(root, canvasWidth, canvasHeight );
        /*create background and groups for buttons*/
        scene.setFill( Color.DARKGREEN ) ;
        root.setBackground( null );
        Button playButton = new Button("Play a Card");
        Button discardButton = new Button("Discard a Card");
        Button informColourButton = new Button("Inform Colour");
        Button informNumberButton = new Button("Inform Number");
        Button discardPileButton = new Button("View Discarded Cards");
        playButton.setFont(Font.font("Century Gothic", FontWeight.THIN, canvasHeight/50));
        discardButton.setFont(Font.font("Century Gothic", FontWeight.THIN, canvasHeight/50));
        informColourButton.setFont(Font.font("Century Gothic", FontWeight.THIN, canvasHeight/50));
        informNumberButton.setFont(Font.font("Century Gothic", FontWeight.THIN, canvasHeight/50));
        discardPileButton.setFont(Font.font("Century Gothic", FontWeight.THIN, canvasHeight/50));
        actionButtons.add(playButton);
        actionButtons.add(discardButton);
        actionButtons.add(informColourButton);
        actionButtons.add(informNumberButton);
        HBox pane_for_buttons = new HBox( 10 ) ;
        pane_for_buttons.setLayoutX(300);
        pane_for_buttons.getChildren().addAll(playButton, discardButton, informColourButton,
                informNumberButton, discardPileButton);
        root.getChildren().add(pane_for_buttons);

        playButton.setOnMouseClicked((MouseEvent event) -> this.setPlayState(handList, table));

        discardButton.setOnMouseClicked((MouseEvent event) -> this.setDiscardState(handList, table));

        informColourButton.setOnMouseClicked((MouseEvent event) ->this.setInformColourState(handList, table));

        informNumberButton.setOnMouseClicked((MouseEvent event) ->this.setInformNumberState(handList, table));

        discardPileButton.setOnMouseClicked((MouseEvent event) -> this.drawDiscardPile());

        createTable(root, table);

        createInfoTokens(root,480);

        createFireworks(root, 470);
        return scene;
    }

    private void createTable(Pane root, Table table){
        int positionsIndex = 0;
        int seat = 1;
        for (Hand hand : table.playerHands) {
            HandBox h = createHandBox(hand, seat);
            seat++;
            root.getChildren().add(h);
            handList.add(h);
            if (hand == table.playerHands[model.playerSeat()-1]) {
                h.setLayoutX(userPositionX);
                h.setLayoutY(userPositionY);
            } else {
                h.setLayoutX(xHandPositions[positionsIndex]);
                h.setLayoutY(yHandPositions[positionsIndex]);
                positionsIndex++;
            }
        }
    }
    private void createFireworks(Pane root, int panePosition) {
        int x = panePosition;
        for (Object colour : model.getFireworks().getFireworks().keySet()){
            char c = (char) colour;
            int height = model.getFireworkHeight(c);
            FireworkRectangle fireworkMeter = new FireworkRectangle(c, height);
            Paint fill = fireworkMeter.getPaint();
            fireworkMeter.setX(x);
            fireworkMeter.setY(450);
            fireworkMeter.setWidth(30);
            fireworkMeter.setHeight(5+(50*height));
            fireworkMeter.setFill(fill);
            fireworkMeter.setStroke(Color.BLACK);
            root.getChildren().add(fireworkMeter);
            x+= 40;
            fireworkList.add(fireworkMeter);
        }
    }

    private void createInfoTokens(Pane root, int panePosition) {
        int x = panePosition;
        for (int i = 0 ; i < model.getInfoTokens() ; i++){
            Circle token = new Circle();

            //Setting the properties of the token;
            token.setCenterX(x);
            token.setCenterY(500);
            token.setRadius(10);
            token.setFill(Color.WHITE);
            token.setStroke(Color.BLACK);
            root.getChildren().add(token);
            x += 25;
            tokenList.add(token);
        }
    }

    private void drawDiscardPile() {
        LinkedHashMap<String, Integer> discards = model.getDiscards().getDiscards();
        Pane root = new Pane();
        VBox reds = new VBox();
        VBox blues = new VBox();
        VBox greens = new VBox();
        VBox whites = new VBox();
        VBox yellows = new VBox();
        HBox discardGrid = new HBox();

        for (String cName : discards.keySet()){
            Card card = new Card(cName.charAt(0), cName.charAt(1));
            CardButton cb = createCardButton(card);
            cb.setText(discards.get(cName).toString());
            switch(cName.charAt(0)) {
                case 'b':
                    blues.getChildren().add(cb);
                    break;
                case 'r':
                    reds.getChildren().add(cb);
                    break;
                case 'g':
                    greens.getChildren().add(cb);
                    break;
                case 'w':
                    whites.getChildren().add(cb);
                    break;
                case 'y':
                    yellows.getChildren().add(cb);
                    break;
            }
        }
        discardGrid.getChildren().addAll(reds, blues, greens, whites, yellows);
        root.getChildren().add(discardGrid);
        Stage secondStage = new Stage();
        secondStage.setScene(new Scene(root, 500, 500));
        secondStage.show();
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
                            String[] move = new String[2];
                            move[0] = "play";
                            move[1] = String.valueOf(position);
                            sendMove(move);
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
                            String[] move = new String[2];
                            move[0] = "discard";
                            move[1] = String.valueOf(position);
                            sendMove(move);
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
                            String[] move = new String[3];
                            move[0] = "informColour";
                            move[1] = String.valueOf(informedPlayer);
                            move[2] = String.valueOf(cb.card.getColour());
                            sendMove(move);
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
                            String[] move = new String[3];
                            move[0] = "informNumber";
                            move[1] = String.valueOf(informedPlayer);
                            move[2] = String.valueOf(cb.card.getNumber());
                            sendMove(move);
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

    public HandBox createHandBox(Hand hand, int seat){
        HandBox h = new HandBox(hand, seat, 5);
        for (Card card : hand.cards) {
            CardButton c = createCardButton(card);
            h.getChildren().add(c);
            h.addCardButton(c);
        }
        return h;
    }

    public CardButton createCardButton(Card card) {
        CardButton c = new CardButton(card);
        ImageView image = new ImageView(new Image(c.getImageString()));
        image.setFitHeight(this.cardHeight);
        image.setFitWidth(this.cardWidth);
        c.setGraphic(image);
//        c.setBackground(Background.EMPTY);
        return c;
    }

    public void changeCardButton(CardButton cb, Card newCard) {
        cb.setCard(newCard);
        ImageView image = new ImageView(new Image(cb.getImageString()));
        image.setFitHeight(75);
        image.setFitWidth(50);
        cb.setGraphic(image);
    }

    public void redrawHands(){
        for (HandBox hand : handList){
            int handPos = 1;
            for (CardButton cb : hand.getCardList()){
                if (cb.card != hand.getHand().getCard(handPos)){
                    this.changeCardButton(cb, hand.getHand().getCard(handPos));
                }
                handPos++;
            }
        }
    }

    public void redrawTokens(){
        int i = 0;
        for (Circle token : tokenList){
            if (i < model.getInfoTokens()){
                token.setVisible(true);
            } else{
                token.setVisible(false);
            }
            i++;
        }
    }

    public void redrawFireworks(){
        for (FireworkRectangle fireworkMeter : fireworkList){
            char colour = fireworkMeter.colour;
            double newHeight = 5+(50*model.getFireworkHeight(colour));
            fireworkMeter.setHeight(newHeight);
            fireworkMeter.setLayoutY(-newHeight);
        }
    }

    private void redrawDiscards() {

    }

    public void setState() {
        if (this.model.playerSeat() != this.model.currentTurn()){
            this.state = STATE_WAITING;
            actionsToggle();
        } else {
            this.state = STATE_READY;
            actionsToggle();
        }
    }

    public void actionsToggle() {
        if (state == STATE_WAITING) {
            for (Button btn : this.actionButtons) {
                btn.setDisable(true);
            }
        } else {
            for (Button btn : this.actionButtons) {
                btn.setDisable(false);
            }
        }
    }
    public void sendMove(String[] move) {
        this.cont.setMove(move);
    }

    public void update(){
        setState();
        redrawHands();
        redrawTokens();
        redrawFireworks();
//        redrawDiscards();
    }


    public void test(){
        model.removeToken();
        model.playCardSuccess(1);
        model.giveCard("uu");
        this.update();
    }
}
