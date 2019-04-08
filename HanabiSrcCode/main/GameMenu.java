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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 *  The class that contains the Game Menu. This menu contains a GUI, and launches the game for users.
 */
public class GameMenu {

    /**
     * A stage that will contain the elements of game menu.
     */
    private Stage stage;

    /**
     * The HanabiClient that this GameMenu belongs to.
     */
    private HanabiClient parent;

    /**
     * The number of players still needed to start a game.
     */
    private int needed;

    /**
     *
     * A string representation of a U of S student's NSID. This is required to connect to the server and is input by the user.
     */
    private String nsid;

    /**
     * The number of players that will be in the game. THis is input by the user.
     */
    private int numOfPlayers;

    /**
     * A number returned by the server that represent a unique game instance.
     */
    private int gameId;

    /**
     * The time for each turn during the Hanabi. This must be input by the user and is passed to the server.
     */
    private int timeout;

    /**
     * A unique token provided by the server that must be passed to users to join the game.
     */
    private String token;

    /**
     * A secret hash unique to every NSID, and must be entered in order to join the game.
     */
    private String hash;

    /**
     * The game type chosen by the user. This can be se to default, wild, and extended.
     */
    private String gameType;

        // These fields are user entered data used for establishing a server connection.

       // These fields will be set by createGame() or joinGame(), these are the GameView instances of our in-game program.

    /**
     * The contructor of the game menu.
     * @param parent A valid HanabiClient to set as the parent.
     * @param aStage A valid state to set as the stage for the GameMenu.
     */
    public GameMenu(HanabiClient parent, Stage aStage) {
        this.parent = parent;
        this.stage = aStage;
    }

    /**
     * A function that will auto fill NSID and secret hash for the user in order to simplify the ability to play the game.
     */
    public void getProfile(){
        try{
            String content = new String(Files.readAllBytes(Paths.get("./HanabiSrcCode/main/userprofile.txt")));
            String[] profileInfo = content.split(",");
            this.nsid = profileInfo[0];
            this.hash = profileInfo[1];
        } catch (IOException e){
        }
    }

    /**
     * A function to start and display the game menu to the user.
     */
    public void start() {
        final double CANVAS_WIDTH = 1376;
        final double CANVAS_HEIGHT = 768;
        stage.setTitle( "HANABI" );
        getProfile();
        Text t = new Text (CANVAS_WIDTH*0.375,CANVAS_HEIGHT*0.25,"HANABI");
        t.setId("MenuTitle");
        t.setFont(Font.font("Century Gothic",FontWeight.THIN,CANVAS_HEIGHT/5));

        Label createLabel = new Label("create game");
        Label joinLabel = new Label("join game");
        Label howToPlayLabel = new Label("how to play");
        Label profileLabel = new Label("set profile info");
        Label exitLabel = new Label("exit");

        createLabel.setFont(new Font("Century Gothic",CANVAS_HEIGHT/18));
        joinLabel.setFont(new Font("Century Gothic",CANVAS_HEIGHT/18));
        howToPlayLabel.setFont(new Font("Century Gothic",CANVAS_HEIGHT/18));
        profileLabel.setFont(new Font("Century Gothic",CANVAS_HEIGHT/18));
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

        profileLabel.setOnMouseEntered((MouseEvent e) -> {
            profileLabel.setScaleX(1.1);
            profileLabel.setScaleY(1.1);
        });

        profileLabel.setOnMouseExited((MouseEvent e) -> {
            profileLabel.setScaleX(1);
            profileLabel.setScaleY(1);
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
        selectionsBox.getChildren().addAll(createLabel,joinLabel,howToPlayLabel, profileLabel, exitLabel);
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
        if (this.nsid != null && this.hash != null) {
            createNsid.setText(this.nsid);
            createSecret.setText(this.hash);
        }

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
        if (this.nsid != null && this.hash != null) {
            joinNsid.setText(this.nsid);
            joinHash.setText(this.hash);
        }
        joinInfoBox.getChildren().addAll(joinNsid,joinHash,joinGameID,joinToken,joinOK);

        HBox profileInfoBox = new HBox();
        profileInfoBox.setStyle("-fx-spacing: 10;" +
                "-fx-padding: 20;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;");
        profileInfoBox.setLayoutX(CANVAS_WIDTH*0.35);
        profileInfoBox.setLayoutY((CANVAS_HEIGHT*0.49)+225);
        profileInfoBox.setVisible(false);

        TextField profileNsid = new TextField();
        TextField profileHash = new TextField();
        Button profileOK = new Button("OK");

        profileNsid.setPromptText("Enter your NSID.");
        profileNsid.setPrefSize(100,joinNsid.getPrefHeight());
        profileHash.setPromptText("Enter your secret hash.");
        if (this.nsid != null && this.hash != null) {
            profileNsid.setText(this.nsid);
            profileHash.setText(this.hash);
        }

        profileInfoBox.getChildren().addAll(profileNsid,profileHash, profileOK);

        createLabel.setOnMouseClicked(event -> {
            joinInfoBox.setVisible(false);
            profileInfoBox.setVisible(false);
            createInfoBox.setVisible(true);
        });

        joinLabel.setOnMouseClicked(event -> {
            createInfoBox.setVisible(false);
            profileInfoBox.setVisible(false);
            joinInfoBox.setVisible(true);
        });

        profileLabel.setOnMouseClicked(event -> {
            createInfoBox.setVisible(false);
            joinInfoBox.setVisible(false);
            profileInfoBox.setVisible(true);
        });

        howToPlayLabel.setOnMouseClicked(event -> howToPlay());

        exitLabel.setOnMouseClicked(event -> exit());

        profileOK.setOnMouseClicked(event -> {
            try {
                this.nsid = profileNsid.getText();
                this.hash = profileHash.getText();
                if (this.nsid.equals("") || this.hash.equals("")){
                    System.out.println("Profile creation failed, please enter valid inputs in both fields.");
                } else {
                    byte info[] = (this.nsid+","+this.hash).getBytes();
                    FileOutputStream out = new FileOutputStream("./HanabiSrcCode/main/userprofile.txt");
                    out.write(info);
                    out.close();
                    System.out.println("Profile successfully created! NSID: " + this.nsid + " Secret: " + this.hash);
                }
            } catch (NullPointerException | IOException e) {
                Alert nullAlert =  new Alert(Alert.AlertType.WARNING);
                nullAlert.setTitle("Error!");
                nullAlert.setHeaderText("All fields must have values.");
                nullAlert.setContentText(null);
                nullAlert.show();
                return;
            }
        });

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
        root.getChildren().addAll(t,selectionsBox,createInfoBox,joinInfoBox, profileInfoBox);

        Scene aScene = new Scene( root, CANVAS_WIDTH, CANVAS_HEIGHT, Color.WHITESMOKE );
        this.stage.setScene( aScene );
        this.stage.show();
    }
        // Constructs the GameView game menu

    /**
     * A function that will display the How To Play window to the user
     */
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

    /**
     * A function that will let the user exit the game.
     */
    public void exit(){
        System.exit(0);
    }
}