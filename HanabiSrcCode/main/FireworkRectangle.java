import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class FireworkRectangle extends Rectangle {
    public char colour;
    public int height;

    public FireworkRectangle(char colour, int height){
        this.colour = colour;
        this.height = height;
    }

    public Paint getPaint(){
        Paint fill = Color.BLACK;
        switch (this.colour) {
            case 'r':
                fill = Color.RED;
                break;
            case 'b':
                fill = Color.BLUE;
                break;
            case 'g':
                fill = Color.GREEN;
                break;
            case 'w':
                fill = Color.WHITE;
                break;
            case 'y':
                fill = Color.YELLOW;
                break;
        }
        return fill;
    }

}
