import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    GameModel game;
    @BeforeEach
    void setUp() {
        game = new GameModel(60, 4, "none");
        String[][] data = {{"b1", "b2", "b4", "g1"} , {} , {"b1", "b3", "g1", "g2"} , {"b2", "b4", "g1", "g3"}};
        game.dealTable(data);
    }

    @AfterEach
    void tearDown() {
        game = null;
    }

    @Test
    void dealTable() {
        assertEquals('b', game.getGameTable().getPlayersCard(1,3).getColour());
        assertEquals('u', game.getGameTable().getPlayersCard(2,4).getNumber());
        assertEquals("g1", game.getGameTable().getPlayersCard(4,3).toString());
    }

    @Test
    void addToken() {
        game.removeToken();
        assertEquals(7, game.getInfoTokens());
        game.addToken();
        assertEquals(8, game.getInfoTokens());
    }

    @Test
    void removeToken() {
        game.removeToken();
        assertEquals(7, game.getInfoTokens());
    }

    @Test
    void getInfoTokens() {
        assertEquals(8, game.getInfoTokens());
    }

    @Test
    void removeFuse() {
        game.removeFuse();
        assertEquals(2, game.getFuses());
    }

    @Test
    void getFuses() {
        assertEquals(3, game.getFuses());
    }

    @Test
    void giveCard() {
        game.playCardSuccess(2);
        game.giveCard("r4");
        assertEquals('r', game.getGameTable().getPlayersCard(1, 2).getColour());
        assertEquals('4', game.getGameTable().getPlayersCard(1, 2).getNumber());
    }

    @Test
    void discardCard() {
        game.discardCard(3);
        assertNull(game.getGameTable().getPlayersCard(1, 3));
        HashMap trash = game.getDiscards().getDiscards();
        assertEquals(1, trash.get("b4"));
        assertEquals(1, game.getDiscards().getDiscardCount());
    }

    @Test
    void informOther() {
        game.informOther(4, 'b');
        assertTrue(game.getGameTable().getPlayersCard(4, 1).checkColourKnown());
        assertTrue(game.getGameTable().getPlayersCard(4, 2).checkColourKnown());
        assertFalse(game.getGameTable().getPlayersCard(4, 3).checkColourKnown());
        assertFalse(game.getGameTable().getPlayersCard(4, 4).checkColourKnown());

        game.informOther(3, '2');
        assertFalse(game.getGameTable().getPlayersCard(3, 1).checkNumberKnown());
        assertFalse(game.getGameTable().getPlayersCard(3, 2).checkNumberKnown());
        assertFalse(game.getGameTable().getPlayersCard(3, 3).checkNumberKnown());
        assertTrue(game.getGameTable().getPlayersCard(3, 4).checkNumberKnown());
    }

    @Test
    void informSelf() {
        String pos = "true,false,false,false";
        game.informSelf('2', pos);

        assertEquals('2', game.getGameTable().getPlayersCard(2, 1).getNumber());
        assertTrue(game.getGameTable().getPlayersCard(2,1).checkNumberKnown());

        String pos2 = "false,true,false,false";

        game.informSelf('r', pos2);
        assertEquals("ru", game.getGameTable().getPlayersCard(2, 2).toString());
    }

    @Test
    void playCardSuccess() {
        game.playCardSuccess(1);
        assertEquals(1, game.getFireworkHeight('b'));
        assertNull(game.getGameTable().getPlayersCard(1,1));
    }

    @Test
    void getFireworkHeight() {
        assertEquals(0, game.getFireworkHeight('b'));
        game.playCardSuccess(1);
        assertEquals(1, game.getFireworkHeight('b'));
    }

    @Test
    void nextTurn() {
        assertEquals(1, game.currentTurn());
        game.nextTurn();
        assertEquals(2, game.currentTurn());
    }

    @Test
    void currentTurn() {
        assertEquals(1, game.currentTurn());
    }

    @Test
    void showTime() {
        //ignore temporarily
    }

    @Test
    void playerSeat() {
        assertEquals(2, game.playerSeat());
        game.nextTurn();
        assertEquals(2, game.playerSeat());
    }
}