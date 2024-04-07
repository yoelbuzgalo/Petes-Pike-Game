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
import petespike.model.Move;

public class HintEventHandler implements EventHandler<ActionEvent> {
    private Label goatlabel;
    private Label directionlabel;
    private PetesPikeUI UI;


    public HintEventHandler(Label goatlabel , Label directionLabel , PetesPikeUI UI){
        this.UI = UI;
        this.directionlabel = directionLabel;
        this.goatlabel = goatlabel;

    }

    @Override
    public void handle(ActionEvent event) {
        Move hinted = UI.getHint();
        System.out.println(hinted);
        System.out.println(goatlabel.getBackground());
        directionlabel.setText(hinted.getDirection().name());
        goatlabel.setBackground(new Background(new BackgroundImage( UI.getImage(hinted.getPosition()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER , BackgroundSize.DEFAULT)));
        System.out.println(goatlabel.getBackground());

    }
    
}
