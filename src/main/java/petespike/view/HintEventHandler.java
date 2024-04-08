package petespike.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import petespike.model.GameState;
import petespike.model.Move;
import petespike.model.PetesPike;

public class HintEventHandler implements EventHandler<ActionEvent> {
    private final Label goatlabel;
    private final Label directionlabel;
    private final PetesPike engine;


    public HintEventHandler(Label goatlabel , Label directionLabel , PetesPike engine){
        this.engine = engine;
        this.directionlabel = directionLabel;
        this.goatlabel = goatlabel;

    }

    @Override
    public void handle(ActionEvent event) {
            if(engine.getState() != GameState.WON){
            Move hinted = this.engine.getHint();
            System.out.println(hinted);
            this.goatlabel.setText(hinted.toString());
        }
        else{
            System.out.println("Cannot hint , game won");
        }
//        System.out.println(goatlabel.getBackground());
//        directionlabel.setText(hinted.getDirection().name());
//        goatlabel.setBackground(new Background(new BackgroundImage( UI.getImage(hinted.getPosition()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER , BackgroundSize.DEFAULT)));
//        System.out.println(goatlabel.getBackground());

    }
    
}
