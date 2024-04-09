package petespike.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
//import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
//import java.util.Map;
import java.util.Set;

import petespike.view.PetesPikeObserver;

public class PetesPike {
    private final static char MOUNTAINTOP_SYMBOL = 'T';
    private final static char EMPTY_SYMBOL = '-';
    private final static char PETE_SYMBOL = 'P';
    private final static Set<Character> GOAT_SYMBOLS = new HashSet<>(Arrays.asList('0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' , '8'));

    private final String filename;
    private final int rows;
    private final int cols;
    private final char[][] initialBoard;
    private final Position mountainTopPosition;
    private final Position peteInitialPosition;

    private Set<Position> goatPositions;
    private char[][] board;
    private int moveCount;
    private Position petePosition;
    private GameState state;

    private PetesPikeObserver observer;

    /**
     * Main constructor of the game engine
     * @param filename
     * @throws PetesPikeException
     */
    public PetesPike(String filename) throws PetesPikeException{
        this.filename = filename;
        this.state = GameState.NEW;
        this.moveCount = 0;
        this.goatPositions = new HashSet<>();

        // read the file and set the puzzle board
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] firstLine = reader.readLine().split(" ");
            this.rows = Integer.parseInt(firstLine[0]);
            this.cols = Integer.parseInt(firstLine[1]);
            initialBoard = new char[this.rows][this.cols];
            board = new char[this.rows][this.cols];

            Position tempMountainTopPosition = null;
            // reads file into board
            for (int i = 0; i < this.rows; i++) {
                String line = reader.readLine();
                for (int j = 0; j < this.cols; j++) {
                    initialBoard[i][j] = line.charAt(j);
                    if (line.charAt(j) == MOUNTAINTOP_SYMBOL) {
                        tempMountainTopPosition = new Position(i, j);
                    } else if (line.charAt(j) == PETE_SYMBOL) {
                        this.petePosition = new Position(i, j);
                    } else if (GOAT_SYMBOLS.contains(line.charAt(j))) {
                        goatPositions.add(new Position(i, j));
                    }
                }
            }

            if (tempMountainTopPosition == null){
                throw new IOException("File does not contain mountain top symbol, fatal error");
            }

            this.mountainTopPosition = tempMountainTopPosition;
            System.out.println(this.mountainTopPosition);

            // store initial position of pete
            this.peteInitialPosition = petePosition;
            // copy the initial board setting to the playing board
            this.copyBoard();

        } catch (IOException ioe) {
            throw new PetesPikeException(ioe.getMessage());
        }
    }

    /**
     * Helper function to copy the initial board layout to playing board
     */
    private void copyBoard(){
        // deep copy the initial board to playing board
        for(int i = 0; i < this.initialBoard.length; i++){
            this.board[i] = Arrays.copyOf(this.initialBoard[i], this.initialBoard[i].length);
        }
    }

    /**
     * Reset functionality to reset the game to initial condition
     */
    public void reset() {
        this.moveCount = 0;
        this.copyBoard();
        this.goatPositions.clear();
        this.petePosition = this.peteInitialPosition;
        this.state = GameState.NEW;
        if (this.observer != null){
            this.observer.reset();
            this.observer.updateStatus(this.state);
        }
    }

    public int getMoveCount() {
        return this.moveCount;
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public String getFilename() {
        return this.filename;
    }

    public Set<Position> getGoatPositions() {
        return this.goatPositions;
    }

    public GameState getState() {
        return this.state;
    }

    //checks if valid position and returns char
    public char getSymbolAt(Position position) throws PetesPikeException{
        if(position.getCol() >= this.cols || position.getRow() >= this.rows){
            throw new PetesPikeException("Position out of range");
        }
        return board[position.getRow()][position.getCol()];
    }

    public Position getMountainTopPosition(){
        return this.mountainTopPosition;
    }

    public char[][] getBoard() {
        return board;
    }

    //loops through all moves for all goats and Pete
    public List<Move> getPossibleMoves(){
        List<Move> moves = new LinkedList<>();
        
        for(Direction direction : Direction.values()){
            moves.add(new Move(petePosition, direction));
        }  

        for(Position position : goatPositions){
            for(Direction direction : Direction.values()){
                moves.add(new Move(position, direction));
            }  
        }

        return moves;
    }

    public void registerObserver(PetesPikeObserver observer){
        this.observer = observer;
    }

    private void notifyObserver(Position from , Position to){
        if (this.observer != null){
            this.observer.pieceMoved(from, to);
            this.observer.updateStatus(this.state);
        }
    }

    /**
     * This returns a possible move based on any current board configuration
     * @return
     */
    public Move getHint() throws PetesPikeException {
        for(Move move : this.getPossibleMoves()){
            if(this.validMove(move)){
                if (this.observer != null){
                    this.observer.displayHint(move);
                }
                return move;
            }
        }
        throw new PetesPikeException("There are no possible moves!");
    }

    /**
     * This function checks whether a move is valid or not
     * @param move
     * @return
     */
    public boolean validMove(Move move){
        if (this.getMountainTopPosition().equals(move.getPosition())){
            return false;
        }

        int row = move.getPosition().getRow();
        int column = move.getPosition().getCol();

        row += move.getDirection().getRow();
        column += move.getDirection().getCol();
        while(row >= 0 && row < this.rows && column >= 0 && column < this.cols){

            if(this.board[row][column] != EMPTY_SYMBOL && this.board[row][column] != MOUNTAINTOP_SYMBOL){
                this.state = GameState.IN_PROGRESS;
                return true;
            }

            row += move.getDirection().getRow();
            column += move.getDirection().getCol();
        }
        this.state = GameState.NO_MOVES;
        return false;
    }

    /**
     * This function proceeds to make a move
     * @param move
     * @throws PetesPikeException
     */
    public void makeMove(Move move) throws PetesPikeException{
        //checks if move is possible
        if(!validMove(move)){
            throw new PetesPikeException("Move not valid");
        }
        if(this.state == GameState.WON){
            throw new PetesPikeException("game already won");
        }

        //updates counters
        moveCount ++;
        if(this.state == GameState.NEW){
            this.state = GameState.IN_PROGRESS; 
        }

        //new col and row for moved char
        int newRow = move.getPosition().getRow();
        int newCol = move.getPosition().getCol();

        //holds moving char and changes that position to an empty symbol
        char moving = board[move.getPosition().getRow()][move.getPosition().getCol()];
        board[move.getPosition().getRow()][move.getPosition().getCol()] = EMPTY_SYMBOL;

        //loops until out of range of board
        while((newRow >= 0 && newRow < this.rows) && (newCol >= 0 && newCol < this.cols)){
            //checks if there is a piece to stop moving char
            if(this.board[newRow][newCol] != EMPTY_SYMBOL && this.board[newRow][newCol] != MOUNTAINTOP_SYMBOL){

                newRow -= move.getDirection().getRow();
                newCol -= move.getDirection().getCol();
                //sets new position to char
                board[newRow][newCol] = moving;

                if(moving == PETE_SYMBOL){
                    petePosition = new Position(newRow, newCol);
                    
                    //checks if game won
                    if(petePosition.equals(mountainTopPosition)){
                        this.state = GameState.WON;
                    }
                    notifyObserver(move.getPosition(), petePosition);
                }
                else{
                    goatPositions.add(new Position(newRow, newCol));
                    goatPositions.remove(move.getPosition());
                    notifyObserver(move.getPosition(), new Position(newRow, newCol));
                }
                return;
            }
            newRow += move.getDirection().getRow();
            newCol += move.getDirection().getCol();
        }
        //throws error based off which piece fell off
        if(moving == PETE_SYMBOL){
            throw new PetesPikeException(this.state.toString());
        }
        else{
            throw new PetesPikeException(this.state.toString());
        }
    }
}
