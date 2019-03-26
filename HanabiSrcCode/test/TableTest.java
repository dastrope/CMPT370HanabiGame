import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @Test
    void giveCard() {
        Table board = new Table(4);

        board.giveCard(1, 'r', '1');
        board.giveCard(1, 'b', '2');
        board.giveCard(1, 'g', '4');
        board.giveCard(1, 'r', '2');
        board.giveCard(2, 'r', '1');
        board.giveCard(3, 'y', '5');

        assertEquals('1', board.getPlayersCard(1,1).getNumber());
        assertEquals('4', board.getPlayersCard(1,3).getNumber());
        assertEquals('1', board.getPlayersCard(2,1).getNumber());
    }

    @Test
    void removeCard() {
        Table board = new Table(4);

        board.giveCard(1, 'r', '1');
        board.giveCard(1, 'b', '2');
        board.giveCard(1, 'g', '4');
        board.giveCard(1, 'r', '2');
        board.giveCard(2, 'r', '1');
        board.giveCard(3, 'y', '5');

        board.removeCard(1, 1);
        assertNull(board.getPlayersCard(1,1));

        board.removeCard(1, 4);
        assertNull(board.getPlayersCard(1,4));
        assertNotNull(board.getPlayersCard(1,3));
    }

    @Test
    void informCard() {
        Table board = new Table(4);

        board.giveCard(1, 'r', '1');
        board.giveCard(1, 'b', '2');
        board.giveCard(1, 'g', '4');
        board.giveCard(1, 'r', '2');
        board.giveCard(2, 'r', '1');
        board.giveCard(3, 'y', '5');

        board.informCard(1, "number", '2');

        assertFalse(board.getPlayersCard(1,1).checkNumberKnown());
        assertTrue(board.getPlayersCard(1,2).checkNumberKnown());
        assertFalse(board.getPlayersCard(1,3).checkNumberKnown());
        assertTrue(board.getPlayersCard(1,4).checkNumberKnown());

        board.informCard(1, "colour", 'r');

        assertTrue(board.getPlayersCard(1,1).checkColourKnown());
        assertFalse(board.getPlayersCard(1,2).checkColourKnown());
        assertFalse(board.getPlayersCard(1,3).checkColourKnown());
        assertTrue(board.getPlayersCard(1,4).checkColourKnown());
    }
}