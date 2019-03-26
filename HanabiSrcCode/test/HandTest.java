import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    @Test
    void addCard() {
        //tests for hand size = 4
        Hand aHand = new Hand(4);

        aHand.addCard('b','3');
        aHand.addCard('g','4');
        aHand.addCard('r','5');
        aHand.addCard('w','3');

        assertEquals('b', aHand.getCard(1).getColour());
        assertEquals('g', aHand.getCard(2).getColour());
        assertEquals('r', aHand.getCard(3).getColour());
        assertEquals('w', aHand.getCard(4).getColour());

        //tests a card will go in the right spot when one was removed

        aHand.removeCard(2);
        aHand.addCard('g','1');
        assertEquals('1', aHand.getCard(2).getNumber());

        //tests for hand size = 5

        Hand bHand = new Hand(5);

        bHand.addCard('b','3');
        bHand.addCard('g','4');
        bHand.addCard('r','5');
        bHand.addCard('w','3');
        bHand.addCard('y','2');

        assertEquals('b', bHand.getCard(1).getColour());
        assertEquals('g', bHand.getCard(2).getColour());
        assertEquals('r', bHand.getCard(3).getColour());
        assertEquals('w', bHand.getCard(4).getColour());
        assertEquals('y', bHand.getCard(5).getColour());
    }

    @Test
    void removeCard() {
        Hand aHand = new Hand(4);

        aHand.addCard('b','3');
        aHand.addCard('g','4');
        aHand.addCard('r','5');
        aHand.addCard('w','3');

        aHand.removeCard(2);
        assertNull(aHand.getCard(2));
        aHand.addCard('g','4');

        aHand.removeCard(1);
        assertNull(aHand.getCard(1));
        aHand.addCard('b','3');

        aHand.removeCard(4);
        assertNull(aHand.getCard(4));


    }

    @Test
    void informColour() {
        Hand aHand = new Hand(4);

        aHand.addCard('b','3');
        aHand.addCard('g','4');
        aHand.addCard('g','5');
        aHand.addCard('w','3');

        assertFalse(aHand.cards[0].checkColourKnown());
        assertFalse(aHand.cards[1].checkColourKnown());
        assertFalse(aHand.cards[2].checkColourKnown());
        assertFalse(aHand.cards[3].checkColourKnown());

        aHand.informColour('g');

        assertFalse(aHand.cards[0].checkColourKnown());
        assertTrue(aHand.cards[1].checkColourKnown()); //TRUE
        assertTrue(aHand.cards[2].checkColourKnown()); //TRUE
        assertFalse(aHand.cards[3].checkColourKnown());


    }

    @Test
    void informNumber() {
        Hand aHand = new Hand(5);

        aHand.addCard('b','3');
        aHand.addCard('g','4');
        aHand.addCard('g','5');
        aHand.addCard('w','3');
        aHand.addCard('r','3');

        assertFalse(aHand.cards[0].checkNumberKnown());
        assertFalse(aHand.cards[1].checkNumberKnown());
        assertFalse(aHand.cards[2].checkNumberKnown());
        assertFalse(aHand.cards[3].checkNumberKnown());
        assertFalse(aHand.cards[4].checkNumberKnown());

        aHand.informNumber('3');

        assertTrue(aHand.cards[0].checkNumberKnown());
        assertFalse(aHand.cards[1].checkNumberKnown());
        assertFalse(aHand.cards[2].checkNumberKnown());
        assertTrue(aHand.cards[3].checkNumberKnown());
        assertTrue(aHand.cards[4].checkNumberKnown());


    }
}