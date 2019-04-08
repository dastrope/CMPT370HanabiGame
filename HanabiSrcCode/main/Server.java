import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.CharArrayReader;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Timer;

/**
 * A class that represents the server. It will establish a connection and communicate with the server.
 */
public class Server implements Runnable {

    /**
     * The GSON instance, used to create JSON messages.
     */
    private Gson gson = new Gson();

    /**
     * The server socket.
     */
    private Socket serversSocket;

    /**
     * The data that is incoming from the server.
     */
    private DataInputStream inFromServer;

    /**
     * The data that is outgoing to the server.
     */
    private PrintStream outToServer;

    /**
     * A linked list of JSON messages.
     */
    private LinkedList<JsonElement> messages;

    /**
     * A Reference to the parent HanabiClient that this server belongs to.
     */
    private HanabiClient parent;

    /**
     * A constructor for Server.
     * @param parent The parent HanabiClient.
     */
    public Server(HanabiClient parent) {
        this.parent = parent;
    }

    /**
     * A function that starts Server and establishes a connection.
     */
    @Override
    public void run() {
        establishConnection();
        messages = new LinkedList<>();
    }

    /**
     * A function a waits for messages and handles them.
     */
    public void watchForMessages() {
        char[] incomingMsg = new char[256];
        int i = 0;
        try {
            while (inFromServer.available() != 0) {
                incomingMsg[i] = (char) inFromServer.readByte();
                if (incomingMsg[i] == '}') {
                    JsonStreamParser parser = new JsonStreamParser(new CharArrayReader(incomingMsg));
                    JsonElement msg = parser.next();
                    messages.add(msg);
                    incomingMsg = new char[256];
                    i = 0;
                } else {
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A function and sends JSON messages
     * @param msg A valid JSON message.
     */
    public void sendMessage(String msg){
        outToServer.println(msg);
    }

    /**
     * A function that will get the message if there is one.
     * @return A JSON message.
     */
    public JsonElement getMessage(){
        if (messages.isEmpty()) {
            return null;
        } else {
            return messages.poll();
        }
    }

    /**
     * A function that establishes a connection to the server.
     */
    public void establishConnection() {
        try {
            this.serversSocket = new Socket("gpu2.usask.ca", 10219);
            this.inFromServer = new DataInputStream(serversSocket.getInputStream());
            this.outToServer = new PrintStream(serversSocket.getOutputStream());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Error");
            alert.setHeaderText("Cannot establish connection with Server");
            alert.setContentText(null);
        }
        System.out.println("Success");
        System.out.println(this.serversSocket.getRemoteSocketAddress());
    }

    /**
     * A function that creates a connection with the server and creates a game as well as instantiates aModel, iModel, aView, aController, game-id and token.
     * @param nsid A valid U of S NSID.
     * @param hash A valid hash associated with the NSID.
     * @param numOfPlayers he number of player in the game selected by the user.
     * @param timeout The time for the timeout for one move selected by the user.
     * @param gameType The game type selected by the user.
     */
    public void createGame(String nsid, String hash, int numOfPlayers, int timeout, String gameType){
        CreateGameEvent cge = new CreateGameEvent("create", nsid, numOfPlayers, timeout, true, gameType.toLowerCase(), hash);
        cge.setMd5hash(computeHash(gson.toJson(cge)));
        attemptToCreate(cge);
        System.out.println(gson.toJson(cge));
    }

    /**
     * A function that creates a connection with the server and joins a game as well as instantiates aModel, iModel, aView, aController, game-id and token.
     * @param nsid A valid U of S NSID.
     * @param hash A valid hash associated with the NSID.
     * @param token A secret token provided by the server to the game host.
     * @param gameId The gameID provided by the server to the host.
     */
    public void joinGame(String nsid, String hash, int gameId, String token){
        JoinGameEvent jge = new JoinGameEvent("join",nsid,gameId,token,hash);
        String json = gson.toJson(jge);
        json = json.replace("gameid","game-id");
        jge.setMd5hash(computeHash(json));
        attemptToJoin(jge);
    }

    /**
     * A function that attempts to create a game by sending create game data to the server.
     * @param cge A valid createGameEvent that contains data about the new game.
     */
    public void attemptToCreate(CreateGameEvent cge) {
        outToServer.println(gson.toJson(cge));
    }

    /**
     * A function that attempts to create a game by sending join game data to the server.
     * @param jge A valid joinGameEvent containing info about the join game command.
     */
    public void attemptToJoin(JoinGameEvent jge){
        String json = gson.toJson(jge);
        json = json.replace("gameid","game-id");
        System.out.println(json);
        outToServer.println(json);
    }

    /**
     * A function that computes the MD5 hash.
     * @param msg A message encoded in MD5.
     * @return The MD5 hash or an error String.
     */
    private static String computeHash(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());
            return new BigInteger(1,md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return ("MD5 ... what's MD5?");
        }
    }

    /**
     * A class representing a CreateGameEvent.
     */
    static class CreateGameEvent {

        /**
         * The command of for create game.
         */
        private String cmd;

        /**
         * A valid U of S NSID.
         */
        private String nsid;

        /**
         * The number of player in the game selected by the user.
         */
        private int players;

        /**
         * The time for the timeout for one move selected by the user.
         */
        private int timeout;

        /**
         * A boolean value where true will cancel the game.
         */
        private boolean force;

        /**
         * A string that represents the rainbow status that represents the game mode.
         */
        private String rainbow;

        /**
         * A timestamp of the current time.
         */
        private long timestamp;

        /**
         * The MD5 hash.
         */
        private String md5hash;


        /**
         * A constructor that instantiate a CreateGameEvent
         * @param cmd The command of for create game.
         * @param nsid The number of player in the game selected by the user.
         * @param players The time for the timeout for one move selected by the user.
         * @param timeout A boolean value where true will cancel the game.
         * @param force  A boolean value where true will cancel the game.
         * @param rainbow A string that represents the rainbow status that represents the game mode.
         * @param md5hash  The MD5 hash.
         */
        public CreateGameEvent(String cmd, String nsid, int players, int timeout, boolean force, String rainbow, String md5hash) {
            this.cmd = cmd;
            this.nsid = nsid;
            this.players = players;
            this.timeout = timeout;
            this.force = force;
            this.rainbow = rainbow;
            this.timestamp = (int) Instant.now().getEpochSecond();
            this.md5hash = md5hash;
        }

        /**
         * A function that updates the MD5 hash.
         * @param newMd5 The new MD5 hash.
         */
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

    /**
     * A class representing a JoinGameEvent.
     */
    static class JoinGameEvent {

        /**
         * The command of for create game.
         */
        private String cmd;

        /**
         * A valid U of S NSID.
         */
        private String nsid;

        /**
         * A valid gameID, that is provided to the host by the server.
         */
        private int gameid;

        /**
         * A secret token that is provided to the host by the server.
         */
        private String token;

        /**
         * A timestamp of the current time.
         */
        private long timestamp;

        /**
         * The MD5 hash.
         */
        private String md5hash;

        /**
         *  A constructor of the JoinGameEvent
         * @param cmd A command of for create game.
         * @param nsid A valid U of S NSID.
         * @param gameid  A valid gameID, that is provided to the host by the server.
         * @param token A secret token that is provided to the host by the server.
         * @param md5hash The MD5 hash.
         */
        private JoinGameEvent(String cmd, String nsid, int gameid, String token, String md5hash) {
            this.cmd = cmd;
            this.nsid = nsid;
            this.gameid = gameid;
            this.token = token;
            this.timestamp = (int) Instant.now().getEpochSecond();
            this.md5hash = md5hash;
        }

        /**
         * A function that updates the MD5 hash.
         * @param newMd5 The new MD5 hash.
         */
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
}
