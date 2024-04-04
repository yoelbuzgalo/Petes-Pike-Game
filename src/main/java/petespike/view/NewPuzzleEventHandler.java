package petespike.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;

public class NewPuzzleEventHandler implements EventHandler<ActionEvent> {
    private PetesPike game;
    private final String fileName;

    public NewPuzzleEventHandler(PetesPike game, String fileName){
        this.game = game;
        this.fileName = fileName;
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            this.game = new PetesPike(this.fileName);
        } catch (PetesPikeException e) {
            throw new RuntimeException(e);
        }
    }
}
