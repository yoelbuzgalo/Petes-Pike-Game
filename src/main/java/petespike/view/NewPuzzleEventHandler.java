package petespike.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;

public class NewPuzzleEventHandler implements EventHandler<ActionEvent> {
    private final TextField fileNameInput;
    private final PetesPikeUI display;

    public NewPuzzleEventHandler(TextField fileNameInput, PetesPikeUI display){
        this.fileNameInput = fileNameInput;
        this.display = display;
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            this.display.newGame(this.fileNameInput.getText());
        } catch (PetesPikeException ppe){
            System.out.println(ppe.getMessage());
        }
    }
}
