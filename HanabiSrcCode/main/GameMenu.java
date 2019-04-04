import com.google.gson.*;

import javafx.application.Application;
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
import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.net.Socket;
import java.security.*;
import java.math.*;
import java.time.Instant;

public class GameMenu extends Application{

    private String nsid;
    private int numOfPlayers;
    private int gameId;
    private int timeout;
    private String token;
    private String hash;
    private String gameType;
        // These fields are user entered data used for establishing a server connection.

    private Gson gson = new Gson();
    private Socket serversSocket;
    private DataInputStream inFromServer;
    private PrintStream outToServer;
    private GameModel aModel;
    private GameInteractionModel iModel;
    private GameView aView;
    private GameController aController;



       // These fields will be set by createGame() or joinGame(), these are the GameView instances of our in-game program.

    public void start(Stage aStage) throws Exception {

        final double CANVAS_WIDTH = 1024;
        final double CANVAS_HEIGHT = 768;

        aStage.setTitle( "HANABI" );

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
            createGame();
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
            joinGame();
        });

        Group root = new Group();
        root.getChildren().addAll(t,selectionsBox,createInfoBox,joinInfoBox);

        Scene aScene = new Scene( root, CANVAS_WIDTH, CANVAS_HEIGHT, Color.WHITESMOKE );
        aStage.setScene( aScene );
        aStage.show();
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

    public void createGame(){
        try {
            establishConnection();
        } catch (Exception e) {
            return;
        }
        CreateGameEvent cge = new CreateGameEvent("create", this.nsid, this.numOfPlayers, this.timeout, true, this.gameType.toLowerCase(), this.hash);
        cge.setMd5hash(computeHash(gson.toJson(cge)));
        attemptToCreate(cge);
        System.out.println(gson.toJson(cge));
        runHanabi();
    }
        // Creates a connection with the server and creates a game as well as instantiates aModel, iModel, aView, aController, game-id and token.

    public void joinGame(){
        try {
            establishConnection();
        } catch (Exception e) {
            return;
        }
        JoinGameEvent jge = new JoinGameEvent("join",this.nsid,this.gameId,this.token,this.hash);
        String json = gson.toJson(jge);
        json = json.replace("gameid","game-id");
        jge.setMd5hash(computeHash(json));
        attemptToJoin(jge);
        runHanabi();
    }
        // Creates a connection with the server as well as instantiates aModel, iModel, aView and aController.

    public void establishConnection() throws Exception {
        try {
            this.serversSocket = new Socket("gpu2.usask.ca", 10219);
            this.inFromServer = new DataInputStream(serversSocket.getInputStream());
            this.outToServer = new PrintStream(serversSocket.getOutputStream());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Error");
            alert.setHeaderText("Cannot establish connection with Server");
            alert.setContentText(null);
            throw new Exception(e);
        }
        System.out.println("Success");
        System.out.println(this.serversSocket.getRemoteSocketAddress());
    }

    public void attemptToCreate(CreateGameEvent cge) {
        outToServer.println(gson.toJson(cge));
    }

    public void attemptToJoin(JoinGameEvent jge){
        String json = gson.toJson(jge);
        json = json.replace("gameid","game-id");
        System.out.println(json);
        outToServer.println(json);
    }

    public void runHanabi(){
        boolean inGame = true;
        char[] message = new char[256];
        int i = 0;

        try {
            while(inGame) {
                if (inFromServer.available() != 0) {
                    message[i] = (char)inFromServer.readByte();
                    if (message[i] == '}') {
                        System.out.println(new String(message));
                        message = new char[256];
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            inGame = false;
            System.out.println("Zoinks");
        }
    }

    private static String computeHash(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());
            return new BigInteger(1,md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return ("MD5 ... what's MD5?");
        }
    }

    static class CreateGameEvent {
        private String cmd;
        private String nsid;
        private int players;
        private int timeout;
        private boolean force;
        private String rainbow;
        private long timestamp;
        private String md5hash;


        public CreateGameEvent(String cmd, String nsid, int players, int timeout, boolean force, String rainbow, String md5hash) {
            this.cmd = cmd;
            this.nsid = nsid;
            this.players = players;
            this.timeout = timeout;
            this.force = force;
            this.rainbow = rainbow;
            //this.timestamp = System.currentTimeMillis()/1000L;
            this.timestamp = (int) Instant.now().getEpochSecond();
            this.md5hash = md5hash;
        }

        private void setMd5hash(String newMd5){
            this.md5hash = newMd5;
        }

        @Override
        public String toString() {
            return "createGameEvent{" +
                    "cmd='" + cmd + '\'' +
                    ", nsid='" + nsid + '\'' +
                    ", players=" + players +
                    ", timeout=" + timeout +
                    ", force=" + force +
                    ", rainbow='" + rainbow + '\'' +
                    '}';
        }
    }

    static class JoinGameEvent {
        private String cmd;
        private String nsid;
        private int gameid;
        private String token;
        private long timestamp;
        private String md5hash;

        private JoinGameEvent(String cmd, String nsid, int gameid, String token, String md5hash) {
            this.cmd = cmd;
            this.nsid = nsid;
            this.gameid = gameid;
            this.token = token;
            this.timestamp = (int) Instant.now().getEpochSecond();
            this.md5hash = md5hash;
        }

        private void setMd5hash(String newMd5){
            this.md5hash = newMd5;
        }

        @Override
        public String toString() {
            return "joinGameEvent{" +
                    "cmd='" + cmd + '\'' +
                    ", nsid='" + nsid + '\'' +
                    ", gameid=" + gameid +
                    ", token='" + token + '\'' +
                    '}';
        }
    }

    public static void main(String[] args){

        launch();
    }

    // Closes the entire program.
}