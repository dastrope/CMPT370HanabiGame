import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameMenu {

    private Stage stage;
    private HanabiClient parent;
    private int needed;
    private String nsid;
    private int numOfPlayers;
    private int gameId;
    private int timeout;
    private String token;
    private String hash;
    private String gameType;
        // These fields are user entered data used for establishing a server connection.

       // These fields will be set by createGame() or joinGame(), these are the GameView instances of our in-game program.

    public GameMenu(HanabiClient parent, Stage aStage) {
        this.parent = parent;
        this.stage = aStage;
    }

    public void start() {
        final double CANVAS_WIDTH = 1376;
        final double CANVAS_HEIGHT = 768;

        stage.setTitle( "HANABI" );

        Text t = new Text (CANVAS_WIDTH*0.375,CANVAS_HEIGHT*0.25,"HANABI");
        t.setId("MenuTitle");
        t.setFont(Font.font("Century Gothic",FontWeight.THIN,CANVAS_HEIGHT/5));

        Label createLabel = new Label("create game");
        Label joinLabel = new Label("join game");
        Label howToPlayLabel = new Label("how to play");
        Label exitLabel = new Label("exit");

        createLabel.setFont(new Font("Century Gothic",CANVAS_HEIGHT/18));
        joinLabel.setFont(new Font("Century Gothic",CANVAS_HEIGHT/18));
        howToPlayLabel.setFont(new Font("Century Gothic",CANVAS_HEIGHT/18));
        exitLabel.setFont(new Font("Century Gothic",CANVAS_HEIGHT/18));

        createLabel.setOnMouseEntered((MouseEvent e) -> {
            createLabel.setScaleX(1.1);
            createLabel.setScaleY(1.1);
        });

        createLabel.setOnMouseExited((MouseEvent e) -> {
            createLabel.setScaleX(1);
            createLabel.setScaleY(1);
        });

        joinLabel.setOnMouseEntered((MouseEvent e) -> {
            joinLabel.setScaleX(1.1);
            joinLabel.setScaleY(1.1);
        });

        joinLabel.setOnMouseExited((MouseEvent e) -> {
            joinLabel.setScaleX(1);
            joinLabel.setScaleY(1);
        });

        howToPlayLabel.setOnMouseEntered((MouseEvent e) -> {
            howToPlayLabel.setScaleX(1.1);
            howToPlayLabel.setScaleY(1.1);
        });

        howToPlayLabel.setOnMouseExited((MouseEvent e) -> {
            howToPlayLabel.setScaleX(1);
            howToPlayLabel.setScaleY(1);
        });

        exitLabel.setOnMouseEntered((MouseEvent e) -> {
            exitLabel.setScaleX(1.1);
            exitLabel.setScaleY(1.1);
        });

        exitLabel.setOnMouseExited((MouseEvent e) -> {
            exitLabel.setScaleX(1);
            exitLabel.setScaleY(1);
        });

        VBox selectionsBox = new VBox();
        selectionsBox.getChildren().addAll(createLabel,joinLabel,howToPlayLabel,exitLabel);
        selectionsBox.setAlignment(Pos.CENTER_RIGHT);
        selectionsBox.setLayoutY(CANVAS_HEIGHT*0.45);
        selectionsBox.setStyle("-fx-spacing: 20;" +
                               "-fx-padding: 30;" +
                               "-fx-border-color: black;" +
                               "-fx-border-width: 5;");

        HBox createInfoBox = new HBox();
        createInfoBox.setStyle("-fx-spacing: 10;" +
                               "-fx-padding: 20;" +
                               "-fx-border-color: black;" +
                               "-fx-border-width: 2;");
        createInfoBox.setLayoutX(CANVAS_WIDTH*0.35);
        createInfoBox.setLayoutY(CANVAS_HEIGHT*0.49);
        createInfoBox.setVisible(false);

        TextField createNsid = new TextField();
        TextField createSecret = new TextField();
        Label playersText = new Label("Players:");
        ChoiceBox createPlayers = new ChoiceBox(FXCollections.observableArrayList(2,3,4,5));
        Label timeoutText = new Label("Timeout:");
        ChoiceBox createTimeout = new ChoiceBox(FXCollections.observableArrayList(30,60,90,120,150));
        ChoiceBox rainboxBox = new ChoiceBox(FXCollections.observableArrayList("Default","Extended","Wild"));
        Button createOK = new Button("OK");

        createNsid.setPromptText("Enter your NSID.");
        createNsid.setPrefSize(100,createNsid.getPrefHeight());
        createSecret.setPromptText("Enter your secret hash.");
        createPlayers.setTooltip(new Tooltip("# of players."));
        createTimeout.setTooltip(new Tooltip("Timeout limit."));
        rainboxBox.setValue("Default");
        rainboxBox.setTooltip(new Tooltip("Select a game type."));

        createInfoBox.getChildren().addAll(createNsid,createSecret,playersText,createPlayers,timeoutText,createTimeout,rainboxBox,createOK);

        HBox joinInfoBox = new HBox();
        joinInfoBox.setStyle("-fx-spacing: 10;" +
                             "-fx-padding: 20;" +
                             "-fx-border-color: black;" +
                             "-fx-border-width: 2;");
        joinInfoBox.setLayoutX(CANVAS_WIDTH*0.35);
        joinInfoBox.setLayoutY((CANVAS_HEIGHT*0.49)+75);
        joinInfoBox.setVisible(false);

        TextField joinNsid = new TextField();
        TextField joinHash = new TextField();
        TextField joinGameID = new TextField();
        TextField joinToken = new TextField();
        Button joinOK = new Button("OK");

        joinNsid.setPromptText("Enter your NSID.");
        joinNsid.setPrefSize(100,joinNsid.getPrefHeight());
        joinHash.setPromptText("Enter your secret hash.");
        joinGameID.setPromptText("Enter the game-id.");
        joinGameID.setPrefSize(143,joinGameID.getPrefHeight());
        joinToken.setPromptText("Enter the secret token.");

        joinInfoBox.getChildren().addAll(joinNsid,joinHash,joinGameID,joinToken,joinOK);

        createLabel.setOnMouseClicked(event -> {
            joinInfoBox.setVisible(false);
            createInfoBox.setVisible(true);
        });

        joinLabel.setOnMouseClicked(event -> {
            createInfoBox.setVisible(false);
            joinInfoBox.setVisible(true);
        });

        howToPlayLabel.setOnMouseClicked(event -> howToPlay());

        exitLabel.setOnMouseClicked(event -> exit());

        createOK.setOnMouseClicked(event -> {
            try {
                this.nsid = createNsid.getText();
                this.hash = createSecret.getText();
                this.numOfPlayers = (Integer) createPlayers.getValue();
                this.timeout = (Integer) createTimeout.getValue();
                if (rainboxBox.getValue() == "Default") {
                    this.gameType = "none";
                } else if (rainboxBox.getValue() == "Extended"){
                    this.gameType = "firework";
                } else {
                    this.gameType = "wild";
                }
            } catch (NullPointerException e) {
                Alert nullAlert =  new Alert(Alert.AlertType.WARNING);
                nullAlert.setTitle("Error!");
                nullAlert.setHeaderText("All fields must have values.");
                nullAlert.setContentText(null);
                nullAlert.show();
                return;
            }
            parent.createGameRequest(this.nsid,this.hash,this.numOfPlayers,this.timeout,this.gameType);
        });

        joinOK.setOnMouseClicked(event -> {
            try {
                this.gameId = Integer.parseInt(joinGameID.getText());
            } catch (NumberFormatException e) {
                Alert numberAlert =  new Alert(Alert.AlertType.WARNING);
                numberAlert.setTitle("Error!");
                numberAlert.setHeaderText("Game-id must be a proper integer.");
                numberAlert.setContentText(null);
                numberAlert.show();
                return;
            }
            try {
                this.nsid = joinNsid.getText();
                this.token = joinToken.getText();
                this.hash = joinHash.getText();
            } catch (NullPointerException e) {
                Alert nullAlert =  new Alert(Alert.AlertType.WARNING);
                nullAlert.setTitle("Error!");
                nullAlert.setHeaderText("All fields must have values.");
                nullAlert.setContentText(null);
                nullAlert.show();
                return;
            }
            parent.joinGameRequest(this.nsid,this.hash,this.gameId,this.token);
        });

        Group root = new Group();
        root.getChildren().addAll(t,selectionsBox,createInfoBox,joinInfoBox);

        Scene aScene = new Scene( root, CANVAS_WIDTH, CANVAS_HEIGHT, Color.WHITESMOKE );
        this.stage.setScene( aScene );
        this.stage.show();
    }
        // Constructs the GameView game menu


    public void howToPlay() {
        Stage stage = new Stage();

        VBox box = new VBox();

        Label labelTitle = new Label("How to Play");
        labelTitle.setAlignment(Pos.TOP_CENTER);

        Button exitButton = new Button("Close");
        exitButton.setOnMouseClicked(event -> stage.close());
        exitButton.setAlignment(Pos.BOTTOM_CENTER);

        TabPane helpTabs = new TabPane();

        Tab goalTab = new Tab();
        Tab giveTab = new Tab();
        Tab playTab = new Tab();
        Tab discardTab = new Tab();

        goalTab.setText("Goal of Hanabi");
        giveTab.setText("Giving info");
        playTab.setText("Playing cards");
        discardTab.setText("Discarding cards");

        helpTabs.getTabs().addAll(goalTab,giveTab,playTab,discardTab);

        box.getChildren().addAll(labelTitle,helpTabs,exitButton);

        Scene tabScene = new Scene(box,600,450, Color.WHITESMOKE);

        stage.setTitle("How to Play");
        stage.setScene(tabScene);
        stage.show();
    }

    public void exit(){
        System.exit(0);
    }
}