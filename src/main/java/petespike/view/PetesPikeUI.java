package petespike.view;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import petespike.model.*;
import java.util.HashMap;
import java.util.Map;

public class PetesPikeUI extends Application implements PetesPikeObserver {
    private static final Map<Character, Image> CHARACTER_IMAGES = new HashMap<>();
    private final Map<Position, Button> gridButtons = new HashMap<>();
    private PetesPike engine;
    private Position clickedPosition;
    private GridPane puzzleLayout;
    private Label movecount;
    private Label messagelabel;
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

    /**
     * Factory method that creates a standard button with event handler
     * @param text
     * @param eventHandler
     * @return
     */
    private static Button createButton(String text, EventHandler<ActionEvent> eventHandler){
        Button button = new Button(text);
        button.setOnAction(eventHandler);
        return button;
    };

    /**
     * Helper function to create a background for buttons
     * @param image
     * @return
     */
    private static Background createElementBackground(Image image){
        if (image == null){
            return new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, new Insets(0)));
        }
        return new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, false, false, false, false)));
    }

    /**
     * Creates elements in a grid
     * @param gridCharacter Pass in a grid character
     * @param handler Pass in
     * @return
     */
    private static Button createGridElement(char gridCharacter , EventHandler<ActionEvent> handler){
        Button button = new Button();
        button.setPrefHeight(100);
        button.setPrefWidth(100);
        if (!CHARACTER_IMAGES.containsKey(gridCharacter)){
            button.setDisable(true);
        }
        button.setBackground(createElementBackground(CHARACTER_IMAGES.get(gridCharacter)));
        Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1), new Insets(5)));
        button.setBorder(border);
        button.setOnAction(handler);
        return button;
    }

    /**
     * Creates a puzzle layout given a board configuration
     * @param board Pass in the board configuration
     * @return Returns a grid layout of the puzzle
     */
    private GridPane createPuzzleLayout(char[][] board){
        GridPane puzzleLayout = new GridPane();
        Border blackBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2)));
        puzzleLayout.setBorder(blackBorder);
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                Button gridElement = createGridElement(board[i][j] , new GridEventHandler(i, j , this));
                puzzleLayout.add(gridElement, j, i );
                // store each button in a map with a position key
                this.gridButtons.put(new Position(i, j), gridElement);
            }
        }
        return puzzleLayout;
    }

    /**
     * Helper function to handle a move button click
     * @param direction Pass in the direction of move
     */
    private void handleMoveButtonClick(Direction direction) {
        try {
            if(engine.validMove(new Move(this.clickedPosition, direction))){
            this.engine.makeMove(new Move(this.clickedPosition, direction));
            }
            else{
                this.messagelabel.setText("Invalid move");
            }
        } catch (PetesPikeException e){
            this.messagelabel.setText(e.getMessage());
        }
    }

    /**
     * Creates a grid of move buttons
     * @return Returns a grid of move buttons
     */
    private GridPane createMoveButtons(){
        GridPane moveButtonsGrid = new GridPane();
        moveButtonsGrid.add(createButton("Up", (x) -> handleMoveButtonClick(Direction.UP)), 1, 0);
        moveButtonsGrid.add(createButton("Left", (x) -> handleMoveButtonClick(Direction.LEFT)), 0, 1);
        moveButtonsGrid.add(createButton("Right", (x) -> handleMoveButtonClick(Direction.RIGHT)),2, 1);
        moveButtonsGrid.add(createButton("Down", (x) -> handleMoveButtonClick(Direction.DOWN)), 1, 2);
        return moveButtonsGrid;
    }

    /**
     * Setter method to set the clicked position of grid buttons
     * @param clickedPosition
     */
    public void setClickedPosition(Position clickedPosition) {
        this.clickedPosition = clickedPosition;
    }

    @Override
    public void pieceMoved(Position from, Position to) {
        System.out.println("Moving piece from: " + from + " ,to: " + to);
        this.messagelabel.setText("Piece moved from: " + from + " ,to: " + to);
        this.movecount.setText("Moves: " + engine.getMoveCount());
        Button fromElement = this.gridButtons.get(from);
        Button toElement = this.gridButtons.get(to);
        fromElement.setBackground(createElementBackground(null));
        fromElement.setDisable(true);
        try {
            toElement.setBackground(createElementBackground(CHARACTER_IMAGES.get(this.engine.getSymbolAt(to))));
            toElement.setDisable(false);
        } catch (PetesPikeException e) {
            this.messagelabel.setText(e.getMessage());
        }

    }

    @Override
    public void reset() {
        for(int i = 0; i < this.engine.getBoard().length; i++){
            for (int j = 0; j < this.engine.getBoard()[i].length; j++){
                try {
                    Position targetPosition = new Position(i, j);
                    Button target = this.gridButtons.get(targetPosition);
                    if (!CHARACTER_IMAGES.containsKey(this.engine.getSymbolAt(targetPosition))){
                        target.setDisable(true);
                    } else {
                        target.setDisable(false);
                    }
                    target.setBackground(createElementBackground(CHARACTER_IMAGES.get(this.engine.getSymbolAt(targetPosition))));
                } catch (PetesPikeException e) {
                    this.messagelabel.setText(e.getMessage());
                }
            }
        }
    }

    @Override
    public void displayMessage(String message) {
        this.messagelabel.setText(message);
    }

    public void newGame(String fileName) throws PetesPikeException {
        this.clickedPosition = null;
        this.engine = new PetesPike(fileName);
        System.out.println("Got here");
        this.puzzleLayout.getChildren().clear();
        this.gridButtons.clear();
        for(int i = 0; i < this.engine.getBoard().length; i++){
            for(int j = 0; j < this.engine.getBoard()[i].length; j++){
                Button gridElement = createGridElement(this.engine.getBoard()[i][j] , new GridEventHandler(i, j , this));
                puzzleLayout.add(gridElement, j, i );
                // store each button in a map with a position key
                this.gridButtons.put(new Position(i, j), gridElement);
            }
        }
    }

    public Image getImage(Position position){
        System.out.println(CHARACTER_IMAGES.get(engine.getChar(position)));
        return CHARACTER_IMAGES.get(engine.getChar(position));
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize the game engine
        String filePath = "data/petes_pike_5_5_2_0.txt"; // default starting puzzle
        this.engine = new PetesPike(filePath);
        this.clickedPosition = null;
        this.engine.registerObserver(this);

        // Top Part of the UI (Reset, File Address and New Puzzle)
        HBox puzzleInputBox = new HBox();
        Button resetButton = createButton("Reset",(x) -> this.engine.reset());
        TextField fileAddressInput = new TextField();
        Button newPuzzleButton = createButton("New Puzzle", new NewPuzzleEventHandler(fileAddressInput, this));
        puzzleInputBox.getChildren().addAll(resetButton, fileAddressInput, newPuzzleButton);

        // Grid Box
        this.puzzleLayout = createPuzzleLayout(engine.getBoard());

        // Side Box
        VBox sideBox = new VBox();
        GridPane moveButtonsGrid = createMoveButtons();
        

        HBox hintBox = new HBox();
        Label goatlabel = new Label("");
        Label directionlabel = new Label("");
        Button getHintButton = createButton("Get Hint", new HintEventHandler(goatlabel , directionlabel, engine));
        hintBox.getChildren().addAll(goatlabel , directionlabel);
        // TODO: Add here images into the hint box

        sideBox.getChildren().addAll(moveButtonsGrid, getHintButton, hintBox);

        // Bottom Box
        BorderPane messagepane = new BorderPane();
        messagelabel = new Label("Display message here");
        this.movecount = new Label("Moves: " + engine.getMoveCount());
        messagepane.setRight(movecount);
        messagepane.setLeft(messagelabel);

        BorderPane bp = new BorderPane();
        bp.setTop(puzzleInputBox);
        bp.setCenter(puzzleLayout);
        bp.setRight(sideBox);
        bp.setBottom(messagepane);
        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.setTitle("Petes Pike Game");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
