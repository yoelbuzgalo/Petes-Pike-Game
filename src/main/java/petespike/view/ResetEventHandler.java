package petespike.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;


public class ResetEventHandler implements EventHandler<ActionEvent> {
    private PetesPike game;

    public ResetEventHandler(PetesPike game){
        this.game = game;
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            this.game = new PetesPike(this.game.getFilename());
        } catch (PetesPikeException e) {
            throw new RuntimeException(e);
        }
    }
}
