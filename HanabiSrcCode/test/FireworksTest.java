import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class FireworksTest {

    @Test
    void addFirework() { //tests addFirework and getFireworks

        Fireworks fwork = new Fireworks("none");

        fwork.addFirework('b');

        fwork.addFirework('r');
        fwork.addFirework('r');
        fwork.addFirework('r');

        fwork.addFirework('g');
        fwork.addFirework('g');
        fwork.addFirework('g');
        fwork.addFirework('g');
        fwork.addFirework('g');

        assertEquals(1,fwork.stackSize('b'));
        assertEquals(3,fwork.stackSize('r'));
        assertEquals(5,fwork.stackSize('g'));

    }

    @Test
    void checkBuildValid() {
        Fireworks fwork = new Fireworks("none");

        fwork.addFirework('b');

        fwork.addFirework('r');
        fwork.addFirework('r');
        fwork.addFirework('r');

        fwork.addFirework('g');
        fwork.addFirework('g');
        fwork.addFirework('g');
        fwork.addFirework('g');
        fwork.addFirework('g');

        Card validCard = new Card('r','4');
        Card invalidCard = new Card('w','4');

        assertTrue(fwork.checkBuildValid(validCard));

        assertFalse(fwork.checkBuildValid(invalidCard));

    }
/*  //tested by addFirework
    @Test
    void getFireworks() {

    }
    */
}