import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void numberInformed() {
        Card aCard = new Card('b', '2');
        assertFalse(aCard.checkNumberKnown());
        aCard.numberInformed();
        assertTrue(aCard.checkNumberKnown());
    }

    @Test
    void colourInformed() {
        Card bCard = new Card('g', '4');
        assertFalse(bCard.checkColourKnown());
        bCard.colourInformed();
        assertTrue(bCard.checkColourKnown());
    }

    @Test
    void getNumber() {
        Card cCard = new Card ('w', '1');
        assertEquals('1', cCard.getNumber());
    }

    @Test
    void getColour() {
        Card cCard = new Card ('r', '1');
        assertEquals('r', cCard.getColour());
    }

    @Test
    void checkColourKnown() {
        Card bCard = new Card('g', '4');
        assertFalse(bCard.checkColourKnown());
        bCard.colourInformed();
        assertTrue(bCard.checkColourKnown());
    }

    @Test
    void checkNumberKnown() {
        Card aCard = new Card('b', '2');
        assertFalse(aCard.checkNumberKnown());
        aCard.numberInformed();
        assertTrue(aCard.checkNumberKnown());
    }

    @Test
    void setNumber() {
        Card nCard = new Card();
        assertNull(nCard.getNumber());
        assertFalse(nCard.checkNumberKnown());
        nCard.setNumber('3');
        assertTrue(nCard.checkNumberKnown());
        assertEquals('3', nCard.getNumber());
        try{
            nCard.setNumber('1');
            assertEquals('3', nCard.getNumber());
        }
        catch(Exception E){
            assertEquals('3', nCard.getNumber());
        }
    }

    @Test
    void setColour() {
        Card nCard = new Card();
        assertNull(nCard.getColour());
        assertFalse(nCard.checkColourKnown());
        nCard.setColour('r');
        assertTrue(nCard.checkColourKnown());
        assertEquals('r', nCard.getColour());
        try{
            nCard.setColour('w');
            assertEquals('r', nCard.getColour());
        }
        catch(Exception E){
            assertEquals('r', nCard.getColour());
        }
    }
}