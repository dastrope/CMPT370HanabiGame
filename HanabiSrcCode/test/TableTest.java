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

        assertEquals(1, board.playerHands[0].cards[0].getNumber());
        assertEquals(4, board.playerHands[0].cards[2].getNumber());
        assertEquals(5, board.playerHands[2].cards[0].getNumber());
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
        assertNull(board.playerHands[0].cards[0]);

        board.removeCard(1, 4);
        assertNull(board.playerHands[0].cards[3]);
        assertNotNull(board.playerHands[0].cards[2]);
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

        assertTrue(board.playerHands[0].cards[1].checkNumberKnown());
        assertTrue(board.playerHands[0].cards[3].checkNumberKnown());
        assertFalse(board.playerHands[0].cards[0].checkNumberKnown());
        assertFalse(board.playerHands[0].cards[2].checkNumberKnown());

        board.informCard(1, "colour", 'r');

        assertTrue(board.playerHands[0].cards[0].checkColourKnown());
        assertFalse(board.playerHands[0].cards[1].checkColourKnown());
        assertFalse(board.playerHands[0].cards[2].checkColourKnown());
        assertTrue(board.playerHands[0].cards[3].checkColourKnown());
    }
}