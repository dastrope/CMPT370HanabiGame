import java.util.HashMap;

/**
 * Class to store the firework data and info.
 */
public class Fireworks {
    /**
     * Stores the firework stacks level.
     */
    private HashMap<Character,Integer> fireworks;

    /**
     * The type of the currently ongoing game. Can be "default", "extended", and "wild".
     */
    private String gameType;

    /**
     * An ordered (alphabetically) array of the colour types that are valid.
     */
    private char[] cardTypes = {'b','g','r','w','y'};

    /**
     * Constructor for the firework stacks. Initializes all fireworks to 0, adds an extra if playing extended.
     * @param gameType the game type
     */
    public Fireworks(String gameType){
        this.fireworks = new HashMap<Character,Integer>();
        this.gameType = gameType;

        for (char colour: cardTypes){
            this.fireworks.put(colour,0);
        }

        if (gameType == "extended"){
            this.fireworks.put('m',0);
        }
    }

    /**
     * Increments a given firework by 1
     * @param colour The colour of the stack to increase
     */
    public void addFirework(char colour){
        int height = this.fireworks.get(colour);
        this.fireworks.replace(colour,++height) ;
    }

    /**
     * Checks if a given card can be added to the stack (valid for default and extended gameTypes)
     * @param inputCard The card to check against the stack
     * @return true if the card can be played. false otherwise.
     */
    public boolean checkBuildValid(Card inputCard){
        char colour = inputCard.getColour();
        int number = Character.getNumericValue(inputCard.getNumber());
        return (this.fireworks.get(colour) == (number -1) );
    }

    /**
     * Checks if a given card can be added to any of the stacks (valid for wild mode only)
     * @param inputCard The card to check against the stack
     * @return an Array of booleans that are true if the card can be played on the respective stack. false otherwise.
     */
    public boolean[] checkBuildValidWild(Card inputCard){
        boolean[] bArray = new boolean[5];
        int i = 0;
        int number = Character.getNumericValue(inputCard.getNumber());
        for (char colour: this.cardTypes){
            bArray[i] = (this.fireworks.get(colour) == (number -1) );
            i++;
        }
        return bArray;
    }


    /**
     * Returns the HashMap containing firework data
     * @return The firework data
     */
    public HashMap getFireworks(){
        return this.fireworks;
    }
}
