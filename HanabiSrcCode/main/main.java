import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        StackPane root_stack = new StackPane() ;
        Scene scene = new Scene( root_stack, 910, 600 ) ;
        scene.setFill( Color.LAWNGREEN ) ;
        root_stack.setBackground( null );

        Button button1   = new Button( "TEST1" ) ;
        Button  button2 = new Button( "TEST2" ) ;
        HBox pane_for_buttons = new HBox( 16 ) ; // space between buttons is 16

        pane_for_buttons.getChildren().addAll( button1, button2 ) ;

        pane_for_buttons.setAlignment( Pos.CENTER ) ;

        pane_for_buttons.setPadding( new Insets( 0, 0, 20, 0 ) ) ;

        BorderPane border_pane = new BorderPane() ;

        border_pane.setBottom( pane_for_buttons ) ;
        root_stack.getChildren().addAll( border_pane);

        stage.setScene( scene ) ;
        stage.show() ;
    }
}
