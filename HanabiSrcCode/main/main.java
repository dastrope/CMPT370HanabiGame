import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class main extends Application implements GameModelObserver {
//    private GameController cont;
    private GameModel model;
    private int state;
    private final int STATE_WAITING = 0;
    private final int STATE_READY = 1;
    private final int STATE_PLAYING = 2;
    private final int STATE_DISCARDING = 3;
    private final int STATE_INFORMING_COLOUR = 4;
    private final int STATE_INFORMING_NUMBER = 5;
    private double canvasHeight = 800;
    private double canvasWidth = 1200;
    private double cardHeight = canvasHeight/10;
    private double cardWidth = cardHeight*0.66;
    private double handSpacing = 5;
    private double handSize;
    private double[] xHandPositions = {
            canvasWidth*0.02,
            canvasWidth*0.02,
            canvasWidth*0.73,
            canvasWidth*0.73,
    };
    private double[] yHandPositions = {
            canvasHeight*0.60,
            canvasHeight*0.25,
            canvasHeight*0.25,
            canvasHeight*0.60
    };
    private double userPositionX = canvasWidth*0.37;
    private double userPositionY = canvasHeight*0.80;
    private ArrayList<HandBox> handList = new ArrayList<>();
    private ArrayList<Circle> tokenList = new ArrayList<>();
    private ArrayList<FireworkRectangle> fireworkList = new ArrayList<>();
    private ArrayList<Button> actionButtons = new ArrayList<>();
    private ArrayList<Circle> fuseList = new ArrayList<>();
    private ArrayList<CardButton> discardedCardList = new ArrayList<>();
    private Label fuseLabel;
    private Label tokenLabel;
    private Stage discardStage;
    private Pane root = new Pane();


    public static void main(String[] args) {
        launch(args);
    }

//    public void setCont(GameController cont) { this.cont = cont; }

    public void setModel(GameModel model){
        this.model = model;
    }

    @Override
    public void modelChanged() {
        this.update();
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
        pane_for_buttons.setLayoutX(canvasWidth/4);
        pane_for_buttons.setPrefWidth(canvasWidth/2);
        pane_for_buttons.setMinWidth(canvasWidth/2);
        pane_for_buttons.setMaxHeight(canvasHeight/50);
        pane_for_buttons.setLayoutY(canvasHeight-(canvasHeight/20));
        HBox top_pane = new HBox(10);
        top_pane.getChildren().add(discardPileButton);
        top_pane.setLayoutY(10);
        top_pane.setLayoutX(canvasWidth/4);
        pane_for_buttons.getChildren().addAll(playButton, discardButton, informColourButton,
                informNumberButton);
        root.getChildren().addAll(pane_for_buttons, top_pane);

        playButton.setOnMouseClicked((MouseEvent event) -> this.setPlayState(handList, table));

        discardButton.setOnMouseClicked((MouseEvent event) -> this.setDiscardState(handList, table));

        informColourButton.setOnMouseClicked((MouseEvent event) ->this.setInformColourState(handList, table));

        informNumberButton.setOnMouseClicked((MouseEvent event) ->this.setInformNumberState(handList, table));

        discardPileButton.setOnMouseClicked((MouseEvent event) -> this.drawDiscardPile());

        createTable(root, table);

        createInfoTokens(top_pane);

        createFireworks(root, ((int)((canvasWidth/2)-canvasWidth/20*3)));

        createFuses(top_pane );

        return scene;
    }

    private void createTable(Pane root, Table table){
        int positionsIndex = 0;
        int seat = 1;
        this.handSize = table.playerHands[0].cards.length;
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
        int y = 450;
        double fworkWidth = canvasWidth/20;
        double fworkHeightMult = canvasHeight/20;

        for (Object colour : model.getFireworks().keySet()){
            char c = (char) colour;
            int height = model.getFireworkHeight(c);
            Label lbl = new Label(Integer.toString(height));
            lbl.setTextFill(Color.WHITE);
            lbl.setLayoutX(x+fworkWidth*0.45);
            lbl.setLayoutY(y);
            FireworkRectangle fireworkMeter = new FireworkRectangle(c, height, lbl);
            Paint fill = fireworkMeter.getPaint();
            fireworkMeter.setX(x);
            fireworkMeter.setY(y);
            fireworkMeter.setWidth(fworkWidth);
            fireworkMeter.setHeight(5+(fworkHeightMult*height));
            fireworkMeter.setFill(fill);
            fireworkMeter.setStroke(Color.BLACK);
            fireworkList.add(fireworkMeter);
            x += fworkWidth+5;
            root.getChildren().addAll(fireworkMeter, lbl);
        }
    }

    private void createInfoTokens(HBox root) {
        int tokenCount = 0;
        HBox tokenBox = new HBox(5);
        for (int i = 0 ; i < model.getInfoTokens() ; i++){
            tokenCount+=1;
            Circle token = new Circle();
            //Setting the properties of the token;
            token.setRadius(10);
            token.setFill(Color.LIGHTBLUE);
            token.setStroke(Color.BLACK);
            tokenBox.getChildren().add(token);
            tokenList.add(token);
        }
        tokenLabel = new Label("Info Tokens: " + tokenCount);
        tokenLabel.setFont(Font.font("Century Gothic", FontWeight.THIN, canvasHeight/50));
        tokenLabel.setTextFill(Color.WHITE);
        VBox tokenInfo = new VBox(5);
        tokenInfo.getChildren().addAll(tokenBox, tokenLabel);
        root.getChildren().add(tokenInfo);
    }

    private void createFuses(HBox root) {
        int fuseCount = 0;
        HBox fuseBox = new HBox(5);
        for (int i = 0 ; i < model.getFuses() ; i++){
            Circle fuse = new Circle();
            fuse.setRadius(10);
            fuse.setFill(Color.RED);
            fuse.setStroke(Color.BLACK);
            fuse.setStyle(Integer.toString(i));
            fuseList.add(fuse);
            fuseBox.getChildren().add(fuse);
            fuseCount+=1;
        }
        fuseLabel = new Label("Fuses: " + fuseCount);
        fuseLabel.setFont(Font.font("Century Gothic", FontWeight.THIN, canvasHeight/50));
        fuseLabel.setTextFill(Color.WHITE);
        VBox fuseInfo = new VBox(5);
        fuseInfo.getChildren().addAll(fuseBox, fuseLabel);
        root.getChildren().add(fuseInfo);

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
            int maxPossible = getTotalPossibleOfCard(Character.getNumericValue(cb.card.getNumber()));
            cb.setText(discards.get(cName).toString()+"/"+maxPossible);
            discardedCardList.add(cb);
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
        discardStage = new Stage();
        discardStage.setScene(new Scene(root, 500, 500));
        discardStage.show();
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
//                            sendMove(move);
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
//                            sendMove(move);
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
//                            sendMove(move);
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
//                            sendMove(move);
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
        HandBox h = new HandBox(hand, seat, this.handSpacing);
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
        image.setFitHeight(cardHeight);
        image.setFitWidth(cardWidth);
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
        int tokenCount = 0;
        for (Circle token : tokenList){
            if (i < model.getInfoTokens()){
                token.setVisible(true);
                tokenCount++;
            } else{
                token.setVisible(false);
            }
            i++;
        }
        tokenLabel.setText("Info Tokens: " + tokenCount);
    }

    public void redrawFireworks(){
        double fworkHeightMult = canvasHeight/20;

        for (FireworkRectangle fireworkMeter : fireworkList){
            char colour = fireworkMeter.colour;
            double newHeight = 5+(fworkHeightMult*model.getFireworkHeight(colour));
            fireworkMeter.setHeight(newHeight);
            fireworkMeter.setLayoutY(-newHeight);
            fireworkMeter.label.setText(Integer.toString(model.getFireworkHeight(colour)));
        }
    }

    private void redrawDiscards() {
        if (discardStage != null){
            LinkedHashMap<String, Integer> discards = model.getDiscards().getDiscards();
            for (CardButton cb : discardedCardList) {
                int maxPossible = getTotalPossibleOfCard(Character.getNumericValue(cb.card.getNumber()));
                cb.setText(discards.get(cb.card.toString()).toString()+"/"+maxPossible);
            }
        }
    }

    private int getTotalPossibleOfCard(int cardNumber) {
        if (cardNumber == 1){
            return 3;
        } else if (cardNumber == 5) {
            return 1;
        } else{
            return 2;
        }
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
//    public void sendMove(String[] move) {
//        this.cont.setMove(move);
//    }

    public void update(){
        setState();
        redrawHands();
        redrawTokens();
        redrawFireworks();
        redrawDiscards();
    }


    public void test(){
        System.out.println("PLayer Seat: " + model.playerSeat());
        System.out.println("Current Turn: " + model.currentTurn());
        model.removeToken();
        model.playCardSuccess(1);
        model.giveCard("uu");
        model.discardCard(3);
        model.giveCard("uu");
        model.nextTurn();
        model.nextTurn();
        model.nextTurn();
        model.nextTurn();
        model.nextTurn();
        model.nextTurn();
        System.out.println("PLayer Seat: " + model.playerSeat());
        System.out.println("Current Turn: " + model.currentTurn());
        this.update();
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
        this.model = new GameModel(15,5,"default");

        String[][] data = {{"b1", "b2", "b4", "g1"}
                , {}
                , {"r1", "b3", "g1", "g2"}
                , {"b2", "b4", "y2", "w3"}
                , {"r2", "w4", "y5", "g4"}
        };
        model.dealTable(data);
        Scene scene = createGame();
        /*draw the window and scene*/
        test();
        stage.setScene( scene ) ;
        stage.show() ;
    }
}
