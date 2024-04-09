package petespike.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
    private boolean gameIsDisabled;
    private PetesPike engine;
    private Position clickedPosition;
    private GridPane puzzleLayout;
    private GridPane moveButtonsGrid;
    private Button hintButton;
    private Label pieceLabel;
    private Label directionLabel;
    private Label moveCount;
    private Label messageLabel;

    static {
        CHARACTER_IMAGES.put('T', new Image("file:data/images/mountaintop.png"));
        CHARACTER_IMAGES.put('P', new Image("file:data/images/pete.png"));
        CHARACTER_IMAGES.put('0', new Image("file:data/images/blue_goat.png"));
        CHARACTER_IMAGES.put('1', new Image("file:data/images/orange_goat.png"));
        CHARACTER_IMAGES.put('2', new Image("file:data/images/green_goat.png"));
        CHARACTER_IMAGES.put('3', new Image("file:data/images/yellow_goat.png"));
        CHARACTER_IMAGES.put('4', new Image("file:data/images/red_goat.png"));
        CHARACTER_IMAGES.put('5', new Image("file:data/images/gold_goat.png"));
        CHARACTER_IMAGES.put('6', new Image("file:data/images/purple_goat.png"));
        CHARACTER_IMAGES.put('7', new Image("file:data/images/magenta_goat.png"));
        CHARACTER_IMAGES.put('8', new Image("file:data/images/cyan_goat.png"));
    }

    /**
     * Factory method that creates a standard button with event handler
     * @param text
     * @param eventHandler
     * @return
     */
    private static Button factoryButton(String text, EventHandler<ActionEvent> eventHandler){
        Button button = new Button(text);
        button.setOnAction(eventHandler);
        return button;
    };

    /**
     * Helper function to create a background for buttons
     * @param image
     * @return
     */
    private static Background createBackground(Image image){
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
    private static Button createGridButtons(char gridCharacter , EventHandler<ActionEvent> handler){
        Button button = new Button();
        button.setPrefHeight(100);
        button.setPrefWidth(100);
        if (!CHARACTER_IMAGES.containsKey(gridCharacter) || gridCharacter == 'T'){
            button.setDisable(true);
        }
        button.setBackground(createBackground(CHARACTER_IMAGES.get(gridCharacter)));
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
            for(int j = 0; j < board[i].length; j++){
                Button gridElement = createGridButtons(board[i][j] , new GridEventHandler(i, j , this));
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
        if(this.clickedPosition == null){
            messageLabel.setText("Please press piece to move first");
        }
        else if(!engine.validMove(new Move(this.clickedPosition, direction))){
            this.messageLabel.setText(GameState.NO_MOVES.toString());
        } else{
            try {
                this.engine.makeMove(new Move(this.clickedPosition, direction));
            } catch (PetesPikeException e){
                this.messageLabel.setText(e.getMessage());
            }
        }
    }

    /**
     * Creates a grid of move buttons
     * @return Returns a grid of move buttons
     */
    private GridPane createMoveButtons(){
        this.moveButtonsGrid = new GridPane();
        moveButtonsGrid.add(factoryButton("Up", (x) -> handleMoveButtonClick(Direction.UP)), 1, 0);
        moveButtonsGrid.add(factoryButton("Left", (x) -> handleMoveButtonClick(Direction.LEFT)), 0, 1);
        moveButtonsGrid.add(factoryButton("Right", (x) -> handleMoveButtonClick(Direction.RIGHT)),2, 1);
        moveButtonsGrid.add(factoryButton("Down", (x) -> handleMoveButtonClick(Direction.DOWN)), 1, 2);
        return moveButtonsGrid;
    }

    /**
     * Setter method to set the clicked position of grid buttons
     * @param clickedPosition
     */
    public void setClickedPosition(Position clickedPosition) {
        this.clickedPosition = clickedPosition;
    }

    /**
     * Helper function that will disable/enable a user's access to move controls
     * this will also change the grid's opacity
     */
    private void setOpacityAndDisableControls(boolean enable){
        if (enable && !gameIsDisabled) {
            for(Button button : this.gridButtons.values()){
                button.setOpacity(0.5);
                button.setDisable(true);
            }
            for (Node node : this.moveButtonsGrid.getChildren()){
                Button castedNode = (Button) node;
                castedNode.setDisable(true);
            }
            this.hintButton.setDisable(true);
            this.gameIsDisabled = true;
        } else if (!enable && gameIsDisabled) {
            for(Button button : this.gridButtons.values()){
                button.setOpacity(1);
                button.setDisable(false);
            }
            for (Node node : this.moveButtonsGrid.getChildren()){
                Button castedNode = (Button) node;
                castedNode.setDisable(false);
            }
            this.hintButton.setDisable(false);
            this.gameIsDisabled = false;
        }
    }

    public void newGame(String fileName) throws PetesPikeException {
        this.clickedPosition = null;
        this.engine = new PetesPike(fileName);
        this.engine.registerObserver(this);
        this.puzzleLayout.getChildren().clear();
        this.gridButtons.clear();
        for(int i = 0; i < this.engine.getRows(); i++){
            for(int j = 0; j < this.engine.getCols(); j++){
                Button gridElement = createGridButtons(this.engine.getBoard()[i][j] , new GridEventHandler(i , j , this));
                puzzleLayout.add(gridElement, j , i );
                // store each button in a map with a position key
                this.gridButtons.put(new Position(i, j), gridElement);
            }
        }
        this.moveCount.setText("Moves: " + this.engine.getMoveCount());
    }

    @Override
    public void pieceMoved(Position from, Position to) {
        this.moveCount.setText("Moves: " + engine.getMoveCount());
        Button fromElement = this.gridButtons.get(from);
        Button toElement = this.gridButtons.get(to);
        fromElement.setBackground(createBackground(null));
        fromElement.setDisable(true);
        try {
            toElement.setBackground(createBackground(CHARACTER_IMAGES.get(this.engine.getSymbolAt(to))));
            toElement.setDisable(false);
        } catch (PetesPikeException e) {
            this.messageLabel.setText(e.getMessage());
        }

    }

    @Override
    public void reset() {
        for(int i = 0; i < this.engine.getBoard().length; i++){
            for (int j = 0; j < this.engine.getBoard()[i].length; j++){
                try {
                    Position targetPosition = new Position(i, j);
                    Button target = this.gridButtons.get(targetPosition);
                    target.setDisable(!CHARACTER_IMAGES.containsKey(this.engine.getSymbolAt(targetPosition)));
                    target.setBackground(createBackground(CHARACTER_IMAGES.get(this.engine.getSymbolAt(targetPosition))));
                    moveCount.setText("Moves: " + this.engine.getMoveCount());
                } catch (PetesPikeException e) {
                    this.messageLabel.setText(e.getMessage());
                }
            }
        }
        this.setOpacityAndDisableControls(false);
    }

    @Override
    public void updateStatus(GameState status) {
        if (status == GameState.WON){
            this.setOpacityAndDisableControls(true);
            this.setMessage(status.toString());
        } else {
            this.setOpacityAndDisableControls(false);
            this.setMessage(status.toString());
        }
    }

    @Override
    public void displayHint(Move move){
        if (move == null){
            this.pieceLabel.setText("");
            this.directionLabel.setText("");
            return;
        }
        try {
            char piece = this.engine.getSymbolAt(move.getPosition());
            this.pieceLabel.setText(""+piece);
            this.directionLabel.setText(move.getDirection().toString());
        } catch (PetesPikeException ppe){
            this.messageLabel.setText(ppe.getMessage());
        }
    }

    @Override
    public void setMessage(String message) {
        this.messageLabel.setText(message);
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
        Button resetButton = factoryButton("Reset",(x) -> this.engine.reset());
        TextField fileAddressInput = new TextField();
        Button newPuzzleButton = factoryButton("New Puzzle", new NewPuzzleEventHandler(fileAddressInput, this));
        puzzleInputBox.getChildren().addAll(resetButton, fileAddressInput, newPuzzleButton);

        // Grid Box
        this.puzzleLayout = createPuzzleLayout(engine.getBoard());

        // Side Box
        VBox sideBox = new VBox();
        GridPane moveButtonsGrid = createMoveButtons();


        HBox hintBox = new HBox();
        this.pieceLabel = new Label("");
        this.directionLabel = new Label("");
        this.hintButton = factoryButton("Get Hint", (x) -> {
            try {
                this.engine.getHint();
            } catch (PetesPikeException e) {
                this.setMessage(e.getMessage());
            }
        });
        hintBox.getChildren().addAll(pieceLabel, this.directionLabel);
        // TODO: Add here images into the hint box

        sideBox.getChildren().addAll(moveButtonsGrid, this.hintButton, hintBox);

        // Bottom Box
        BorderPane messagepane = new BorderPane();
        messageLabel = new Label("New Game");
        this.moveCount = new Label("Moves: " + engine.getMoveCount());
        messagepane.setRight(moveCount);
        messagepane.setLeft(messageLabel);

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
