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
    char MOUNTAINTOP_SYMBOL = 'T';
    char EMPTY_SYMBOL = '-';
    char PETE_SYMBOL = 'P';
    Set<Character> GOAT_SYMBOLS = new HashSet<>(Arrays.asList('0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' , '8'));

    private final int rows;
    private final int cols;
    private char[][] board;

    private int moveCount;

    private Position mountaintopPos;
    private Position petePos;
    private Set<Position> goatPos;

    private GameState state;

    private String filename;

    
    public PetesPike(String filename) throws PetesPikeException{
        
        moveCount = 0; 
        goatPos = new HashSet<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){

        this.filename = filename;

        String[] line = reader.readLine().split(" ");
        board = new char[Integer.parseInt(line[0])][Integer.parseInt(line[1])];
        rows = Integer.parseInt(line[0]);
        cols = Integer.parseInt(line[1]);

        //reads file into board
        for(int row = 0 ; row < rows; row++){
            String l = reader.readLine();

            for(int col = 0 ; col < cols ; col++){
                board[row][col] = l.charAt(col);
                if(l.charAt(col)== MOUNTAINTOP_SYMBOL){
                    mountaintopPos = new Position(row, col);
                }
                else if(l.charAt(col)== PETE_SYMBOL){
                    petePos = new Position(row, col);
                }
                else if(GOAT_SYMBOLS.contains(l.charAt(col))){
                    goatPos.add(new Position(row, col));
                }
            }
        }
    }
    catch(IOException e){
        throw new PetesPikeException(e.getMessage());
    }

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

    public Set<Position> getGoatPos() {
        return goatPos;
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
        return this.mountaintopPos;
    }

    public char[][] getBoard() {
        return board;
    }

    //loops through all moves for all goats and Pete
    public List<Move> getPossibleMoves(){
        List<Move> moves = new LinkedList<>();
        
        for(Direction direction : Direction.values()){
            moves.add(new Move(petePos, direction));
        }  

        for(Position position : goatPos){
            for(Direction direction : Direction.values()){
                moves.add(new Move(position, direction));
            }  
        }

        return moves;
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
        int newrow = move.getPosition().getRow();
        int newcol = move.getPosition().getCol();

        //holds moving char and changes that position to an empty symbol
        char moving = board[move.getPosition().getRow()][move.getPosition().getCol()];
        board[move.getPosition().getRow()][move.getPosition().getCol()] = EMPTY_SYMBOL;

        //loops until out of range of board
        while((newrow >= 0 && newrow < this.rows) && (newcol >= 0 && newcol < this.cols)){
            //checks if there is a piece to stop moving char
            if(board[newrow][newcol] != EMPTY_SYMBOL && board[newrow][newcol] != MOUNTAINTOP_SYMBOL){

                newrow -= move.getDirection().getRow();
                newcol -= move.getDirection().getCol();
                //sets new position to char
                board[newrow][newcol] = moving;

                if(moving == PETE_SYMBOL){
                    petePos = new Position(newrow, newcol);
                    //checks if game won
                    if(petePos.equals(mountaintopPos)){
                        this.state = GameState.WON;
                    }
                    return;
                }
                else{
                    goatPos.add(new Position(newrow, newcol));
                    goatPos.remove(move.getPosition());
                    return;
                }
            }
            newrow += move.getDirection().getRow();
            newcol += move.getDirection().getCol();

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

    public static void main(String[] args) {
        try{
            PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
            game.makeMove(new Move(game.petePos, Direction.LEFT));
            game.makeMove(new Move(game.petePos, Direction.DOWN));
            System.out.println(game.getState());
            System.out.println(game.petePos);
            System.out.println(game.mountaintopPos);
            System.out.println();
        }
        catch(PetesPikeException e){
            System.out.println(e.getMessage());
        }
    }
}
