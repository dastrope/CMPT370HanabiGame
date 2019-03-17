import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class FireworksTest {

    @Test
    void addFirework() { //tests addFirework and getFireworks

        Fireworks fwork = new Fireworks();

        fwork.addFirework('b');

        fwork.addFirework('r');
        fwork.addFirework('r');
        fwork.addFirework('r');

        fwork.addFirework('g');
        fwork.addFirework('g');
        fwork.addFirework('g');
        fwork.addFirework('g');
        fwork.addFirework('g');

        HashMap<String,Integer> currentFwork = fwork.getFireworks();

        assertEquals(1,currentFwork.get("b"));
        assertEquals(3,currentFwork.get("r"));
        assertEquals(5,currentFwork.get("g"));


    }

    @Test
    void checkBuildValid() {
        Fireworks fwork = new Fireworks();

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