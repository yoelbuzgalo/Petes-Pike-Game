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
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import petespike.model.*;
import java.util.HashMap;
import java.util.Map;

public class PetesPikeUI extends Application implements PetesPikeObserver {
    private static final Image EMPTY_IMAGE = new Image("file:data/images/empty.png");
    private static final Image ARROW_IMAGE = new Image("file:data/images/arrow.png");
    private static final Map<Character, Image> CHARACTER_IMAGES = new HashMap<>();
    private final Map<Position, Button> gridButtons = new HashMap<>();
    private Button mountainTopButton;
    private boolean gameIsDisabled;
    private PetesPike engine;
    private Position clickedPosition;
    private GridPane puzzleLayout;
    private GridPane moveButtonsGrid;
    private Button hintButton;
    private ImageView hintPieceImage;
    private ImageView hintDirectionImage;
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
        CHARACTER_IMAGES.put('5', new Image("file:data/images/gray_goat.jpg"));
        CHARACTER_IMAGES.put('6', new Image("file:data/images/purple_goat.png"));
        CHARACTER_IMAGES.put('7', new Image("file:data/images/orange_goat.png"));
        CHARACTER_IMAGES.put('8', new Image("file:data/images/blue_goat.png"));
    }

    /**
     * Factory function that creates a standard button with event handler
     * @param text A string that will be the button's text
     * @param eventHandler Pass in an event handler
     * @return returns a button UI object
     */
    private static Button factoryButton(String text, EventHandler<ActionEvent> eventHandler){
        Button button = new Button(text);
        button.setOnAction(eventHandler);
        return button;
    }

    /**
     * Factory function that creates a standard grid with borders
     */
    private static GridPane factoryGrid(){
        GridPane gridLayout = new GridPane();
        Border blackBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2)));
        gridLayout.setBorder(blackBorder);
        return gridLayout;
    }

    /**
     * Helper class to rotate arrows of any object
     * @param node pass in the node object
     * @param direction pass in the direction enum
     */
    private static void rotateArrowDirection(Node node, Direction direction){
        if (direction == Direction.UP){
            node.setRotate(90);
        } else if (direction == Direction.DOWN) {
            node.setRotate(270);
        } else if (direction == Direction.LEFT){
            node.setRotate(0);
        } else if (direction == Direction.RIGHT){
            node.setRotate(180);
        }
    }

    /**
     * Creates a button with arrows depending on its direction
     * @param direction pass in the direction that will be for the arrow button
     * @param eventHandler pass in an event handler when the button is clicked
     * @return returns an arrow button
     */
    private static Button arrowButton(Direction direction, EventHandler<ActionEvent> eventHandler){
        Button button = new Button();
        button.setRotate(90);
        button.setPrefWidth(45);
        button.setPrefHeight(45);
        rotateArrowDirection(button, direction);
        button.setBackground(new Background(new BackgroundImage(ARROW_IMAGE, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, false, false, true, false))));
        button.setOnAction(eventHandler);
        return button;
    }

    /**
     * Helper function to create a background for buttons
     * @param image pass in an image
     * @return returns a background with image
     */
    private static Background createBackground(Image image){
        if (image == null){
            return new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, new Insets(0)));
        }
        return new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, false, false, true, false)));
    }

    /**
     * Creates elements in a grid
     * @param gridCharacter pass in a grid character
     * @param handler pass in a handler for click event
     * @return returns a button for every grid element/character
     */
    private static Button createGridButtons(char gridCharacter , EventHandler<ActionEvent> handler){
        Button button = new Button();
        button.setPrefHeight(100);
        button.setPrefWidth(100);
        if (!CHARACTER_IMAGES.containsKey(gridCharacter) || gridCharacter == 'T'){
            button.setDisable(true);
        }
        button.setBackground(createBackground(CHARACTER_IMAGES.get(gridCharacter)));
        Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1), new Insets(0)));
        button.setBorder(border);
        button.setOnAction(handler);
        return button;
    }

    /**
     * Helper function to load the grid in
     */
    public void loadGridFromEngine() {
        for (int i = 0; i < this.engine.getRows(); i++) {
            for (int j = 0; j < this.engine.getCols(); j++) {
                Button gridElement = createGridButtons(this.engine.getBoard()[i][j], new GridEventHandler(i, j, this));
                gridElement.setOpacity(1);
                this.puzzleLayout.add(gridElement, j, i);
                Position key = new Position(i, j);
                if (key.equals(engine.getMountainTopPosition())) {
                    this.mountainTopButton = gridElement;
                    this.mountainTopButton.setDisable(true);
                }
                // store each button in a map with a position key
                this.gridButtons.put(new Position(i, j), gridElement);
            }
        }
    }

    /**
     * Helper function to handle a move button click
     * @param direction pass in the direction of move
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
     * @return returns a grid of move buttons
     */
    private GridPane createMoveButtons(){
        this.moveButtonsGrid = new GridPane();
        moveButtonsGrid.add(arrowButton(Direction.UP, (x) -> handleMoveButtonClick(Direction.UP)), 1, 0);
        moveButtonsGrid.add(arrowButton(Direction.LEFT, (x) -> handleMoveButtonClick(Direction.LEFT)), 0, 1);
        moveButtonsGrid.add(arrowButton(Direction.RIGHT, (x) -> handleMoveButtonClick(Direction.RIGHT)),2, 1);
        moveButtonsGrid.add(arrowButton(Direction.DOWN, (x) -> handleMoveButtonClick(Direction.DOWN)), 1, 2);
        return moveButtonsGrid;
    }

    /**
     * Setter method to set the clicked position of grid buttons
     * @param clickedPosition pass in the position that was just clicked
     */
    public void setClickedPosition(Position clickedPosition) {
        this.clickedPosition = clickedPosition;
    }

    /**
     * Helper function to set the opacity and disable controls
     * @param opacity pass in desired opacity
     * @param disable pass in desired true/false for disabling controls
     */
    private void setOpacityAndDisable(double opacity, boolean disable){
        for(Button button : this.gridButtons.values()){
            button.setOpacity(opacity);
            button.setDisable(disable);
        }
        for (Node node : this.moveButtonsGrid.getChildren()){
            if (node instanceof Button){
                Button castedNode = (Button) node;
                castedNode.setDisable(disable);
            }
        }
        this.hintButton.setDisable(disable);
        this.gameIsDisabled = disable;
    }

    /**
     * Helper function that will disable/enable a user's access to move controls
     * this will also change the grid's opacity
     */
    private void setOpacityAndDisableControls(boolean enable){
        if (enable && !gameIsDisabled) {
            setOpacityAndDisable(0.5, true);
        } else if (!enable && gameIsDisabled) {
            setOpacityAndDisable(1, false);
        }
    }

    /**
     * Creates a new game with a file path argument
     * @param fileName pass in a string of the file path
     * @throws PetesPikeException throws an exception when file isn't found
     */
    public void newGame(String fileName) throws PetesPikeException {
        this.clickedPosition = null;
        this.engine = new PetesPike(fileName);
        this.engine.registerObserver(this);
        this.updateStatus(this.engine.getState());
        this.puzzleLayout.getChildren().clear();
        this.gridButtons.clear();
        this.loadGridFromEngine();
        this.moveCount.setText("Moves: " + this.engine.getMoveCount());
        this.setOpacityAndDisableControls(false);
        this.setHintToEmpty();
    }

    @Override
    public void pieceMoved(Position from, Position to) {
        this.moveCount.setText("Moves: " + engine.getMoveCount());
        Button fromElement = this.gridButtons.get(from);
        Button toElement = this.gridButtons.get(to);
        toElement.setBackground(fromElement.getBackground());
        toElement.setDisable(false);
        fromElement.setBackground(createBackground(CHARACTER_IMAGES.get(engine.getBoard()[from.getRow()][from.getCol()])));
        fromElement.setDisable(true);
        this.setHintToEmpty();
    }

    @Override
    public void reset() {
        for(int i = 0; i < this.engine.getBoard().length; i++){
            for (int j = 0; j < this.engine.getBoard()[i].length; j++){
                try {
                    Position targetPosition = new Position(i, j);
                    Button target = this.gridButtons.get(targetPosition);
                    if (target != this.mountainTopButton){
                        target.setDisable(!CHARACTER_IMAGES.containsKey(this.engine.getSymbolAt(targetPosition)));
                    }
                    target.setBackground(createBackground(CHARACTER_IMAGES.get(this.engine.getSymbolAt(targetPosition))));
                    moveCount.setText("Moves: " + this.engine.getMoveCount());
                } catch (PetesPikeException e) {
                    this.messageLabel.setText(e.getMessage());
                }
            }
        }
        this.setOpacityAndDisableControls(false);
        this.setHintToEmpty();
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

    /**
     * Helper function to set the hint to empty
     */
    private void setHintToEmpty(){
        this.hintPieceImage.setImage(EMPTY_IMAGE);
        this.hintDirectionImage.setImage(EMPTY_IMAGE);
    }

    @Override
    public void displayHint(Move move){
        if (move == null){
            this.setHintToEmpty();
            return;
        }
        try {
            char piece = this.engine.getSymbolAt(move.getPosition());
            this.hintPieceImage.setImage(CHARACTER_IMAGES.get(piece));
            this.hintDirectionImage.setImage(ARROW_IMAGE);
            rotateArrowDirection(this.hintDirectionImage, move.getDirection());
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
        this.puzzleLayout = factoryGrid();
        this.loadGridFromEngine();

        // Side Box
        VBox sideBox = new VBox();
        this.moveButtonsGrid = createMoveButtons();


        HBox hintBox = new HBox();
        this.hintPieceImage = new ImageView(new Image("file:data/images/empty.png"));
        this.hintPieceImage.setFitWidth(30);
        this.hintPieceImage.setFitHeight(30);
        this.hintDirectionImage = new ImageView(new Image("file:data/images/empty.png"));
        this.hintDirectionImage.setFitWidth(30);
        this.hintDirectionImage.setFitHeight(30);
        this.hintButton = factoryButton("Get Hint", (x) -> {
            try {
                this.engine.getHint();
            } catch (PetesPikeException e) {
                this.setMessage(e.getMessage());
            }
        });
        hintBox.getChildren().addAll(this.hintPieceImage, this.hintDirectionImage);

        sideBox.getChildren().addAll(moveButtonsGrid, this.hintButton, hintBox);

        // Bottom Box
        BorderPane messagePane = new BorderPane();
        messageLabel = new Label("New Game");
        this.moveCount = new Label("Moves: " + engine.getMoveCount());
        messagePane.setRight(moveCount);
        messagePane.setLeft(messageLabel);

        BorderPane bp = new BorderPane();
        bp.setTop(puzzleInputBox);
        bp.setCenter(puzzleLayout);
        bp.setRight(sideBox);
        bp.setBottom(messagePane);
        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Petes Pike Game");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
