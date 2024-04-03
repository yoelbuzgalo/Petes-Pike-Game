package petespike.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PetesPikeUI extends Application {
    // Factory methods
    private static Button factoryButton(String text){
        Button button = new Button(text);
        return button;
    };

    private static GridPane createPuzzleLayout(){
        GridPane puzzleLayout = new GridPane();
        // TODO: Create puzzle grid here
        return puzzleLayout;
    }

    private static GridPane createMoveButtons(){
        GridPane moveButtonsGrid = new GridPane();
        // TODO: Create move buttons here
        return moveButtonsGrid;
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Top Part of the UI (Reset, File Address and New Puzzle)
        HBox puzzleInputBox = new HBox();
        Button resetButton = factoryButton("Reset");
        TextField fileAddressInput = new TextField();
        Button newPuzzleButton = factoryButton("New Puzzle");
        puzzleInputBox.getChildren().addAll(resetButton, fileAddressInput, newPuzzleButton);

        // Grid Box
        GridPane puzzleLayout = createPuzzleLayout();

        // Side Box
        VBox sideBox = new VBox();
        GridPane moveButtonsGrid = createMoveButtons();
        Button getHintButton = factoryButton("Get Hint");
        HBox hintBox = new HBox();
        // TODO: Add here images into the hint box

        sideBox.getChildren().addAll(moveButtonsGrid, getHintButton, hintBox);

        // Bottom Box
        HBox movesBox = new HBox();
        Label label = new Label("Moves: 0");
        movesBox.getChildren().addAll(label);

        BorderPane bp = new BorderPane();
        bp.setTop(puzzleInputBox);
        bp.setCenter(puzzleLayout);
        bp.setRight(sideBox);
        bp.setBottom(movesBox);
        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.setTitle("Petes Pike Game");
        stage.show();
    }
}
