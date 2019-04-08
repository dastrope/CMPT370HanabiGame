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

public class GameView implements GameModelObserver {
    private GameController cont;    //Controller for sending moves to
    private GameModel model;    //Model to retrieve info from
    private int state;  //State variable for switching between action states
    private final int STATE_WAITING = 0;    //waiting for client turn
    private final int STATE_READY = 1;      //ready to make an action
    private final int STATE_PLAYING = 2;    //play button selected
    private final int STATE_DISCARDING = 3; //discard button selected
    private final int STATE_INFORMING_COLOUR = 4;   //inform colour selected
    private final int STATE_INFORMING_NUMBER = 5;   //inform number selected
    private double canvasHeight = 800;  //initial scene height
    private double canvasWidth = 1200;  //initial scene width
    private double cardHeight = canvasHeight/10;    //height of all cardbuttons
    private double cardWidth = cardHeight*0.66;     //width of all cardbuttons
    private double handSpacing = 5;     //spacing between cards in hand
    private double handSize;
    private double[] xHandPositions = {
            canvasWidth*0.02,
            canvasWidth*0.02,
            canvasWidth*0.73,
            canvasWidth*0.73,
    };  //x layout positions for other players hands
    private double[] yHandPositions = {
            canvasHeight*0.60,
            canvasHeight*0.25,
            canvasHeight*0.25,
            canvasHeight*0.60
    }; //y layout positions for other players hands
    private double userPositionX = canvasWidth*0.355;    //x position of client
    private double userPositionY = canvasHeight*0.80;   //y position of client
    private ArrayList<HandBox> handList;    //list of hands on the scene
    private ArrayList<Circle> tokenList;    //list of tokens on scene
    private ArrayList<FireworkRectangle> fireworkList;  //list of fireworks
    private ArrayList<Button> actionButtons;    //list of action buttons
    private ArrayList<Circle> fuseList;   //list of fuses on scene
    private ArrayList<CardButton> discardedCardList;    //list of discarded
    private Label fuseLabel;    //label for fuse count
    private Label tokenLabel;   //label for token count
    private Label turnLabel;
    private Stage discardStage; //stage for the discard view
    private Pane root;          //main pane on the scene


//    public static void main(String[] args) {
//        launch(args);
//    }

    /**
     * Purpose: Instantiates the game view and its components
     *
     * @param aModel sets the GameModel field.
     * @param aController sets the controller field.
     */
    public GameView(GameModel aModel, GameController aController){
        this.model = aModel;
        this.cont = aController;
        this.handList = new ArrayList<>();
        this.tokenList = new ArrayList<>();
        this.fireworkList = new ArrayList<>();
        this.actionButtons = new ArrayList<>();
        this.fuseList = new ArrayList<>();
        this.discardedCardList = new ArrayList<>();
        this.root = new Pane();
        this.setState();    //sets the initial state based on whose turn it is
    }

    public void setCont(GameController cont) { this.cont = cont; }

    public void setModel(GameModel model){
        this.model = model;
    }

    @Override
    /**
     * Purpose: Allows the model to notify the view of an update
     *
     */
    public void modelChanged() {
        this.update();
    }

    /**
     * Purpose: Creates all the elements for the game table and draws them on the scene
     * @return the scene with all the view elements on it
     */
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
        turnLabel = new Label("Player " + model.currentTurn() + "'s turn!");
        turnLabel.setFont(Font.font("Century Gothic", FontWeight.THIN, canvasHeight/50));
        turnLabel.setTextFill(Color.WHITE);
        top_pane.getChildren().add(turnLabel);
        pane_for_buttons.getChildren().addAll(playButton, discardButton, informColourButton,
                informNumberButton);
        root.getChildren().addAll(pane_for_buttons, top_pane);

        /*sets the onclick methods for the action buttons*/
        playButton.setOnMouseClicked((MouseEvent event) -> this.setPlayState(handList, table));

        discardButton.setOnMouseClicked((MouseEvent event) -> this.setDiscardState(handList, table));

        informColourButton.setOnMouseClicked((MouseEvent event) ->this.setInformColourState(handList, table));

        informNumberButton.setOnMouseClicked((MouseEvent event) ->this.setInformNumberState(handList, table));

        discardPileButton.setOnMouseClicked((MouseEvent event) -> this.drawDiscardPile());

        /*calls the methods for drawing game elements*/
        createTable(root, table);

        createInfoTokens(top_pane);

        createFireworks(root, ((int)((canvasWidth/2)-canvasWidth/20*3)));

        createFuses(top_pane );

        update();

        return scene;
    }

    /**
     * Purpose: Draws the hands from the game table
     * @param root pane to draw the hands on
     * @param table table to get the hands from
     */
    private void createTable(Pane root, Table table){
        int positionsIndex = 0;
        int seat = 1;
        /*Logic to draw the hands in their correct positions*/
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

    /**
     * Purpose: Draws the fireworks from the model
     * @param root Pane to draw the fireworks on
     * @param panePosition x position on the layout to draw the fireworks
     */
    private void createFireworks(Pane root, int panePosition) {
        int x = panePosition;   //beginning x position of the fireworks
        int y = 450;    //y position of fireworks
        double fworkWidth = canvasWidth/20;     //width of the firework bars
        double fworkHeightMult = canvasHeight/20;   //height multiplier for bars

        /*Logic for drawing the firework and its count label*/
        for (Object colour : model.getFireworks().getFireworks().keySet()){
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

    /**
     * Purpose: Draws the info tokens from the model
     * @param root a box to add the tokens to
     */
    private void createInfoTokens(HBox root) {
        int tokenCount = 0;
        HBox tokenBox = new HBox(5);
        /*Logic for drawing the tokens and the count label*/
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

    /**
     * Purpose: Draws the fuse tokens from the model
     * @param root a box to add the fuses to
     */
    private void createFuses(HBox root) {
        int fuseCount = 0;
        HBox fuseBox = new HBox(5);
        /*Logic for drawing the fuses and the count label*/
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

    /**
     * Purpose: Creates the discardPile view, then draws the cards and counts
     */
    private void drawDiscardPile() {
        LinkedHashMap<String, Integer> discards = model.getDiscards().getDiscards();
        Pane root = new Pane();
        VBox reds = new VBox();
        VBox blues = new VBox();
        VBox greens = new VBox();
        VBox whites = new VBox();
        VBox yellows = new VBox();
        HBox discardGrid = new HBox();

        /*Draws the grid of discarded cards by colour and number*/
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


    /**
     * Purpose: Sets the view state to the play state
     * Play state disables other players cards and allows client cards to be played
     * @param handList the list of hands to modify
     * @param table the table to compare the hands to
     */
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

    /**
     * Purpose: Sets the view state to the discard state
     * Discard state disables other players cards and allows client cards to be discarded
     * @param handList the list of hands to modify
     * @param table the table to compare the hands to
     */
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

    /**
     * Purpose: Sets the view state to the inform colour state
     * Inform colour state disables clients cards and sets other cards to be informable
     * @param handList the list of hands to modify
     * @param table the table to compare the hands to
     */
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

    /**
     * Purpose: Sets the view state to the inform number state
     * Inform number state disables clients cards and sets other cards to be informable
     * @param handList the list of hands to modify
     * @param table the table to compare the hands to
     */
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

    /**
     * Purpose: removes methods on cards and disables them
     * @param hands The list of hands to reset the cards of
     */
    public void resetAllCards(ArrayList<HandBox> hands){
        for (HandBox hand : hands){
            hand.setDisable(false);
            for (CardButton card : hand.getCardList()){
                resetCard(card);
            }
        }
    }

    /**
     * Purpose: removes methods on a card
     * @param card the card to modify
     */
    public void resetCard(CardButton card){
        card.setOnMouseClicked((MouseEvent e) ->{});
        card.setOnMouseEntered((MouseEvent e) ->{});
        card.setOnMouseEntered((MouseEvent e) ->{});
    }

    /**
     * Purpose: Draws the hand provided
     * @param hand the hand to be drawn
     * @param seat the owner of the hand
     * @return the newly created handBox
     */
    public HandBox createHandBox(Hand hand, int seat){
        HandBox h = new HandBox(hand, seat, 5);
        for (Card card : hand.cards) {
            CardButton c = createCardButton(card);
            h.getChildren().add(c);
            h.addCardButton(c);
        }
        return h;
    }

    /**
     * Purpose: Draws the card provided
     * @param card the card to be drawn
     * @return the newly created cardbutton
     */
    public CardButton createCardButton(Card card) {
        CardButton c = new CardButton(card);
        ImageView image = new ImageView(new Image(c.getImageString()));
        image.setFitHeight(this.cardHeight);
        image.setFitWidth(this.cardWidth);
        c.setGraphic(image);
//        c.setBackground(Background.EMPTY);
        return c;
    }

    /**
     * Purpose: Sets the card provided to a new card
     * @param cb the cardbutton to modify
     * @param newCard the new card to set the button to
     */
    public void changeCardButton(CardButton cb, Card newCard) {
        cb.setCard(newCard);
        ImageView image = new ImageView(new Image(cb.getImageString()));
        image.setFitHeight(75);
        image.setFitWidth(50);
        cb.setGraphic(image);
    }

    /**
     * Purpose: Redraws the hands with updated info from the model
     */
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

    /**
     * Purpose: Redraws the tokens with updated info from the model
     */
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

    /**
     * Purpose: Redraws the fireworks with updated info from the model
     */
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

    /**
     * Purpose: Redraws the discarded cards with updated info from the model
     */
    private void redrawDiscards() {
        if (discardStage != null){
            LinkedHashMap<String, Integer> discards = model.getDiscards().getDiscards();
            for (CardButton cb : discardedCardList) {
                int maxPossible = getTotalPossibleOfCard(Character.getNumericValue(cb.card.getNumber()));
                cb.setText(discards.get(cb.card.toString()).toString()+"/"+maxPossible);
            }
        }
    }

    private void redrawTurnLabel() {
        this.turnLabel.setText("Player " + model.currentTurn() + "'s turn!");
    }

    /**
     * Purpose: Checks the given number to see how many of that card are in the deck
     * @param cardNumber the number to be checked
     * @return the number of possible cards of that number
     */
    private int getTotalPossibleOfCard(int cardNumber) {
        if (cardNumber == 1){
            return 3;
        } else if (cardNumber == 5) {
            return 1;
        } else{
            return 2;
        }
    }


    /**
     * Purpose: Sets the view state based on whose turn it is in the model
     */
    public void setState() {
        if (this.model.playerSeat() != this.model.currentTurn()){
            this.state = STATE_WAITING;
            actionsToggle();
        } else {
            this.state = STATE_READY;
            actionsToggle();
        }
    }


    /**
     * Purpose: Toggles the action buttons based on the current state
     */
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

    /**
     * Purpose: Sends the given move to the controller
     * @param move the move generated by the view
     */
    public void sendMove(String[] move) {
        this.cont.setMove(move);
    }

    /**
     * Purpose: Calls the redraw methods for all view elements
     */
    public void update(){
        setState();
        redrawHands();
        redrawTokens();
        redrawFireworks();
        redrawDiscards();
        redrawTurnLabel();
    }
}
