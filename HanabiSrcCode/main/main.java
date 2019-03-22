import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Pane root_stack = new Pane() ;

        Scene scene = new Scene( root_stack, 1200, 800 ) ;
        scene.setFill( Color.LAWNGREEN ) ;
        root_stack.setBackground( null );

        Button button1   = new Button( "TEST1" ) ;
        Button  button2 = new Button( "TEST2" ) ;
        HBox pane_for_buttons = new HBox( 16 ) ; // space between buttons is 16
        HBox hand = new HBox(20);
        ArrayList<Button> buttonList = new ArrayList<>();
        for (int i = 0 ; i<5 ; i++){
            char suit;
            char rank;
            if (i % 2 == 0) {
                suit = 'b';
            } else{
                suit = 'r';
            }
            CardButton card1 = new CardButton(suit, (char)i);
            ImageView unknown = new ImageView(new Image("cards_set1/unknown.png"));
            unknown.setFitHeight(100);
            unknown.setFitWidth(66);
            card1.setGraphic(unknown);
            card1.setOnMouseEntered(( MouseEvent event ) ->
            {
                double clicked_point_x = event.getSceneX() ;
                double clicked_point_y = event.getSceneY() ;
                System.out.println(clicked_point_x + clicked_point_y);
                card1.setTranslateY(-15);
            });
            card1.setOnMouseExited(( MouseEvent event ) ->
            {
                double clicked_point_x = event.getSceneX() ;
                double clicked_point_y = event.getSceneY() ;
                System.out.println(clicked_point_x + clicked_point_y);
                card1.setTranslateY(0);
            });
            buttonList.add(card1);
        }
        hand.getChildren().addAll(buttonList);

        pane_for_buttons.getChildren().addAll( button1, button2 ) ;

        pane_for_buttons.setAlignment( Pos.CENTER ) ;

        pane_for_buttons.setPadding( new Insets( 0, 0, 20, 0 ) ) ;

        BorderPane border_pane = new BorderPane() ;

        border_pane.setTop( pane_for_buttons ) ;
        border_pane.setBottom(hand);
        root_stack.getChildren().addAll( border_pane);
        root_stack.setLayoutX(350);
        root_stack.setLayoutY(600);

        stage.setScene( scene ) ;
        stage.show() ;
    }
}
