package petespike.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;

public class NewPuzzleEventHandler implements EventHandler<ActionEvent> {
    private PetesPike game;
    private final TextField fileNameInput;

    public NewPuzzleEventHandler(PetesPike game, TextField fileNameInput){
        this.game = game;
        this.fileNameInput = fileNameInput;
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            this.game = new PetesPike(this.fileNameInput.getText());
        } catch (PetesPikeException e) {
            System.out.println(e.getMessage());
        }
    }
}
