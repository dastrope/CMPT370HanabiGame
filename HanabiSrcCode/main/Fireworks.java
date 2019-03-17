import java.util.HashMap;

public class Fireworks {
    public HashMap<String,Integer> Fireworks;
    //a container for individual fireworks

    public Fireworks();
    //initializes all fireworks with height set to 0 for each

    public void addFirework(char colour);
    //increments a fierwork by 1 on its height

    public Boolean checkBuildValid(Card tryMe);
    //checks to see if a play is valid and a firework can be successfully increased

    public HashMap getFireworks();
    //returns the collection of fireworks
}
