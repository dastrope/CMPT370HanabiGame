import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DiscardPileTest {

    @Test
    void getDiscards() { // tests all three functions
        DiscardPile pile = new DiscardPile();
        HashMap newpile = pile.getDiscards();

        assertNull(newpile.get("r1"));
        assertEquals(0, pile.getDiscardCount());

        Card r1 = new Card('r', '1');
        Card b1 = new Card('b', '1');

        pile.addDiscard(r1);
        pile.addDiscard(r1);

        newpile = pile.getDiscards();

        assertEquals(2, newpile.get("r1"));
        assertEquals(2, pile.getDiscardCount());

        pile.addDiscard(b1);

        newpile = pile.getDiscards();

        assertEquals(2, newpile.get("r1"));
        assertEquals(1, newpile.get("b1"));
        assertEquals(3, pile.getDiscardCount());

    }
/*
    @Test
    void addDiscard() {
    }

    @Test
    void getDiscardCount() {
    }
*/
}