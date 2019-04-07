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

public class Server implements Runnable {

    private Gson gson = new Gson();
    private Socket serversSocket;
    private DataInputStream inFromServer;
    private PrintStream outToServer;
    private LinkedList<JsonElement> messages;
    private HanabiClient parent;

    @Override
    public void run() {
        establishConnection();
        messages = new LinkedList<>();
    }

    public void watchForMessages() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                char[] incomingMsg = new char[256];
                int i = 0;

                try {
                    while (messages.isEmpty()) {
                        if (inFromServer.available() != 0) {
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void sendMessage(String msg){
        outToServer.println(msg);
    }

    public JsonElement getMessage(){
        watchForMessages();
        return messages.poll();
    }

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

    public void createGame(String nsid, String hash, int numOfPlayers, int timeout, String gameType){
        CreateGameEvent cge = new CreateGameEvent("create", nsid, numOfPlayers, timeout, true, gameType.toLowerCase(), hash);
        cge.setMd5hash(computeHash(gson.toJson(cge)));
        attemptToCreate(cge);
        System.out.println(gson.toJson(cge));
    }
    // Creates a connection with the server and creates a game as well as instantiates aModel, iModel, aView, aController, game-id and token.

    public void joinGame(String nsid, String hash, int gameId, String token){
        JoinGameEvent jge = new JoinGameEvent("join",nsid,gameId,token,hash);
        String json = gson.toJson(jge);
        json = json.replace("gameid","game-id");
        jge.setMd5hash(computeHash(json));
        attemptToJoin(jge);
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
}
