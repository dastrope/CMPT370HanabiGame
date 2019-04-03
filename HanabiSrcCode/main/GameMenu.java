import com.google.gson.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.text.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.Socket;
import java.security.*;
import java.math.*;
import java.time.Instant;

public class GameMenu extends Application{
    private Button createGameButton;
    private Button joinGameButton;
    private Button howToPlayButton;
    private Button exitButton;
        // These buttons are used to call their respective methods in the class.

    private int gameId;
    private String token;
        // These fields are user entered data used for establishing a server connection.
    
    private Socket serverSocket;
    private GameModel aModel;
    private GameInteractionModel iModel;
    private GameView aView;
    private GameController aController;
        // These fields will be set by createGame() or joinGame(), these are the main instances of our in-game program.


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

        TextField createNsid = new TextField();
        TextField createPlayers = new TextField();
        TextField createTimeout = new TextField();
        ChoiceBox rainboxBox = new ChoiceBox(FXCollections.observableArrayList("Default","Extended","Wild"));
        Button createOK = new Button("OK");

        createNsid.setPromptText("Enter your NSID.");
        createPlayers.setPromptText("Enter the # of players.");
        createTimeout.setPromptText("Enter the timeout limit.");
        rainboxBox.setValue("Default");
        rainboxBox.setTooltip(new Tooltip("Select a game type."));

        createInfoBox.getChildren().addAll(createNsid,createPlayers,createTimeout,rainboxBox,createOK);

        HBox joinInfoBox = new HBox();
        joinInfoBox.setStyle("-fx-spacing: 10;" +
                             "-fx-padding: 20;" +
                             "-fx-border-color: black;" +
                             "-fx-border-width: 2;");
        joinInfoBox.setLayoutX(CANVAS_WIDTH*0.35);
        joinInfoBox.setLayoutY((CANVAS_HEIGHT*0.49)+75);

        TextField joinNsid = new TextField();
        TextField joinGameID = new TextField();
        TextField joinToken = new TextField();
        Button joinOK = new Button("OK");

        joinNsid.setPromptText("Enter your NSID.");
        joinGameID.setPromptText("Enter the game-id.");
        joinToken.setPromptText("Enter the secret token.");

        joinInfoBox.getChildren().addAll(joinNsid,joinGameID,joinToken,joinOK);

        Group root = new Group();
        root.getChildren().addAll(t,selectionsBox,createInfoBox,joinInfoBox);

        Scene theScene = new Scene( root, CANVAS_WIDTH, CANVAS_HEIGHT, Color.WHITESMOKE );
        aStage.setScene( theScene );
        aStage.show();
    }
        // Constructs the main game menu
    
    public void createGame(){

    }
        // Creates a connection with the server and creates a game as well as instantiates aModel, iModel, aView, aController, game-id and token.

    public void joinGame(){

    }
        // Creates a connection with the server as well as instantiates aModel, iModel, aView and aController.

    public void howToPlay(){

    }
        // Presents a window with the game rules.

    public void exit(){

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
        private int timestamp;

        public CreateGameEvent(String cmd, String nsid, int players, int timeout, boolean force, String rainbow) {
            this.cmd = cmd;
            this.nsid = nsid;
            this.players = players;
            this.timeout = timeout;
            this.force = force;
            this.rainbow = rainbow;
            this.timestamp = (int) Instant.now().getEpochSecond();
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
        private int timestamp;

        private JoinGameEvent(String cmd, String nsid, int gameid, String token) {
            this.cmd = cmd;
            this.nsid = nsid;
            this.gameid = gameid;
            this.token = token;
            this.timestamp = (int) Instant.now().getEpochSecond();
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

        String msg = "Troy";
        CreateGameEvent cge = new CreateGameEvent("create","twb431",4,60,false,"default");
        JoinGameEvent jge = new JoinGameEvent("join","twb431",1234,"somethingSecret");

        Gson gson = new Gson();
        String json1 = gson.toJson(cge);
        System.out.println(json1);

        String json2 = gson.toJson(jge);
        json2 = json2.replace("gameid","game-id");
        System.out.println(json2);

        //System.out.println(computeHash(msg));
    }

    // Closes the entire program.
}