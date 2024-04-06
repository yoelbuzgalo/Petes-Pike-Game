package petespike.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import petespike.model.*;

import java.util.HashMap;
import java.util.Map;

public class PetesPikeUI extends Application {
    private static final Map<Character, Image> CHARACTER_IMAGES = new HashMap<>();
    private PetesPike engine;
    private Position clickedPosition;
    static {
        CHARACTER_IMAGES.put('T', new Image("file:data/images/mountaintop.png"));
        CHARACTER_IMAGES.put('P', new Image("file:data/images/pete.png"));
        CHARACTER_IMAGES.put('0', new Image("file:data/images/blue_goat.png"));
        CHARACTER_IMAGES.put('1', new Image("file:data/images/orange_goat.png"));
        CHARACTER_IMAGES.put('2', new Image("file:data/images/green_goat.png"));
        CHARACTER_IMAGES.put('3', new Image("file:data/images/yellow_goat.png"));
        CHARACTER_IMAGES.put('4', new Image("file:data/images/magenta_goat.png"));
        CHARACTER_IMAGES.put('5', new Image("file:data/images/gold_goat.png"));
        CHARACTER_IMAGES.put('6', new Image("file:data/images/purple_goat.png"));
        CHARACTER_IMAGES.put('7', new Image("file:data/images/gray_goat.png"));
        CHARACTER_IMAGES.put('8', new Image("file:data/images/cyan_goat.png"));
    }


    // Factory methods
    private static Button createButton(String text, EventHandler<ActionEvent> eventHandler){
        Button button = new Button(text);
        button.setOnAction(eventHandler);
        return button;
    };

    private static Button createGridElement(Image image){
        Button button = new Button();
        button.setPrefHeight(100);
        button.setPrefWidth(100);
        if (image != null){
            button.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, false, false, false, false))));
        }
        Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1), new Insets(5)));
        button.setBorder(border);
        return button;
    }

    private static GridPane createPuzzleLayout(char[][] board){
        GridPane puzzleLayout = new GridPane();
        Border blackBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2)));
        puzzleLayout.setBorder(blackBorder);
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                puzzleLayout.add(createGridElement(CHARACTER_IMAGES.get(board[i][j])), j, i);
            }
        }
        return puzzleLayout;
    }

    private GridPane createMoveButtons(){
        GridPane moveButtonsGrid = new GridPane();
        moveButtonsGrid.add(createButton("Up", (x) -> {
            try {
                this.engine.makeMove(new Move(this.clickedPosition, Direction.UP));
            } catch (PetesPikeException e) {
                System.out.println(e.getMessage());
            }
        }), 1, 0);
        moveButtonsGrid.add(createButton("Left", (x) -> {
            try {
                this.engine.makeMove(new Move(this.clickedPosition, Direction.LEFT));
            } catch (PetesPikeException e) {
                System.out.println(e.getMessage());
            }
        }), 0, 1);
        moveButtonsGrid.add(createButton("Right", (x) -> {
            try {
                this.engine.makeMove(new Move(this.clickedPosition, Direction.RIGHT));
            } catch (PetesPikeException e) {
                System.out.println(e.getMessage());
            }
        }),2, 1);
        moveButtonsGrid.add(createButton("Down", (x) -> {
            try {
                this.engine.makeMove(new Move(this.clickedPosition, Direction.DOWN));
            } catch (PetesPikeException e) {
                System.out.println(e.getMessage());
            }
        }), 1, 2);
        return moveButtonsGrid;
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize the game engine
        String filePath = "data/petes_pike_5_5_2_0.txt"; // default starting puzzle
        this.engine = new PetesPike(filePath);
        this.clickedPosition = null;

        // Top Part of the UI (Reset, File Address and New Puzzle)
        HBox puzzleInputBox = new HBox();
        Button resetButton = createButton("Reset",(x) -> engine.reset());
        TextField fileAddressInput = new TextField();
        Button newPuzzleButton = createButton("New Puzzle", new NewPuzzleEventHandler(engine, fileAddressInput));
        puzzleInputBox.getChildren().addAll(resetButton, fileAddressInput, newPuzzleButton);

        // Grid Box
        GridPane puzzleLayout = createPuzzleLayout(engine.getBoard());

        // Side Box
        VBox sideBox = new VBox();
        GridPane moveButtonsGrid = createMoveButtons();
        Button getHintButton = createButton("Get Hint", (x) -> System.out.println("Unsupported command"));
        HBox hintBox = new HBox();
        // TODO: Add here images into the hint box

        sideBox.getChildren().addAll(moveButtonsGrid, getHintButton, hintBox);

        // Bottom Box
        HBox movesBox = new HBox();
        Label label = new Label("Moves: " + engine.getMoveCount());
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
