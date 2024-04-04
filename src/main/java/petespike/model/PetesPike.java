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

//import petespike.view.AsciiColorCodes;

public class PetesPike {
    private final static char MOUNTAINTOP_SYMBOL = 'T';
    private final static char EMPTY_SYMBOL = '-';
    private final static char PETE_SYMBOL = 'P';
    private final static Set<Character> GOAT_SYMBOLS = new HashSet<>(Arrays.asList('0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' , '8'));
    private final String filename;
    private final Set<Position> goatPositions;
    private final int rows;
    private final int cols;
    private final char[][] initialBoard;
    private final Position peteInitialPosition;
    private char[][] board;
    private int moveCount;
    private Position mountainTopPosition;
    private Position petePosition;
    private GameState state;


    
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
            // reads file into board
            for (int i = 0; i < this.rows; i++) {
                String line = reader.readLine();
                for (int j = 0; j < this.cols; j++) {
                    initialBoard[i][j] = line.charAt(j);
                    if (line.charAt(j) == MOUNTAINTOP_SYMBOL) {
                        this.mountainTopPosition = new Position(i, j);
                    } else if (line.charAt(j) == PETE_SYMBOL) {
                        this.petePosition = new Position(i, j);
                    } else if (GOAT_SYMBOLS.contains(line.charAt(j))) {
                        goatPositions.add(new Position(i, j));
                    }
                }
            }

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
            this.board[i] = Arrays.copyOf(this.initialBoard[i], this.initialBoard.length);
        }
    }

    public void reset() throws PetesPikeException{
        System.out.println("Ran here");
        this.moveCount = 0;
        this.copyBoard();
        this.goatPositions.clear();
        this.petePosition = this.peteInitialPosition;
        this.state = GameState.NEW;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public String getFilename() {
        return filename;
    }

    public Set<Position> getGoatPositions() {
        return goatPositions;
    }

    public GameState getState() {
        return state;
    }

    //checks if valid position and returns char
    public char getSymbolAt(Position position)throws PetesPikeException{
        if(position.getCol() >= this.cols || position.getRow() >= this.rows){
            throw new PetesPikeException("Position out of range");
        }
        return board[position.getRow()][position.getCol()];
    }

    public Position getMountaintop(){
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

    public boolean validMove(Move move){
        int row = move.getPosition().getRow();
        int column = move.getPosition().getCol();

        row += move.getDirection().getRow();
        column += move.getDirection().getCol();
        while(row >= 0 && row < this.rows && column >= 0 && column < this.cols){

            if(board[row][column] != EMPTY_SYMBOL && board[row][column] != MOUNTAINTOP_SYMBOL){
                return true;
            }

            row += move.getDirection().getRow();
            column += move.getDirection().getCol();
        }

        return false;
    }

    public void makeMove(Move move) throws PetesPikeException{
        //checks if mov is possible
        if(move.getPosition().getRow() >= this.rows || move.getPosition().getCol() >= this.cols || 
        move.getPosition().getRow() < 0 || move.getPosition().getCol() < 0 ||
        board[move.getPosition().getRow()][move.getPosition().getCol()] == EMPTY_SYMBOL){
            throw new PetesPikeException("Invalid move");
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
            if(board[newRow][newCol] != EMPTY_SYMBOL && board[newRow][newCol] != MOUNTAINTOP_SYMBOL){

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
                    return;
                }
                else{
                    goatPositions.add(new Position(newRow, newCol));
                    goatPositions.remove(move.getPosition());
                    return;
                }
            }
            newRow += move.getDirection().getRow();
            newCol += move.getDirection().getCol();

        }

        //throws error based off which piece fell off
        if(moving == PETE_SYMBOL){
            state = GameState.NO_MOVES;
            throw new PetesPikeException("Pete fell off!!");
            
        }
        else{
            throw new PetesPikeException("Goat fell off!");
            
        }


    }
}
