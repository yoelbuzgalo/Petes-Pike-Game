package petespike.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import petespike.model.PetesPike;

public class PetesPikeUI extends Application {
    // Factory methods
    private static Button factoryButton(String text, EventHandler<ActionEvent> eventHandler){
        Button button = new Button(text);
        button.setOnAction(eventHandler);
        return button;
    };

    private static GridPane createPuzzleLayout(){
        GridPane puzzleLayout = new GridPane();
        return puzzleLayout;
    }

    private static GridPane createMoveButtons(){
        GridPane moveButtonsGrid = new GridPane();
        // TODO: Create move buttons here
        moveButtonsGrid.add(factoryButton("Up", (x) -> System.out.println("Unsupported command")), 1, 0);
        moveButtonsGrid.add(factoryButton("Left", (x) -> System.out.println("Unsupported command")), 0, 1);
        moveButtonsGrid.add(factoryButton("Right", (x) -> System.out.println("Unsupported command")),2, 1);
        moveButtonsGrid.add(factoryButton("Down", (x) -> System.out.println("Unsupported command")), 1, 2);
        return moveButtonsGrid;
    }

    @Override
    public void start(Stage stage) throws Exception {
        String filePath = "data/petes_pike_5_5_2_0.txt";
        PetesPike engine = new PetesPike(filePath);
        // Top Part of the UI (Reset, File Address and New Puzzle)
        HBox puzzleInputBox = new HBox();
        Button resetButton = factoryButton("Reset", new ResetEventHandler(engine));
        TextField fileAddressInput = new TextField();
        Button newPuzzleButton = factoryButton("New Puzzle", new NewPuzzleEventHandler(engine, fileAddressInput.getText()));
        puzzleInputBox.getChildren().addAll(resetButton, fileAddressInput, newPuzzleButton);

        // Grid Box
        GridPane puzzleLayout = createPuzzleLayout();

        // Side Box
        VBox sideBox = new VBox();
        GridPane moveButtonsGrid = createMoveButtons();
        Button getHintButton = factoryButton("Get Hint", (x) -> System.out.println("Unsupported command"));
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
