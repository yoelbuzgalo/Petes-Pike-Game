package petespike.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PetesPike {
    char MOUNTAINTOP_SYMBOL = 'T';
    char EMPTY_SYMBOL = '-';
    char PETE_SYMBOL = 'P';
    Set<Character> GOAT_SYBOLS = new HashSet<>(Arrays.asList('0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' , '8'));

    private final int rows;
    private final int cols;
    private char[][] board;

    private int moveCount;

    private Position mountaintopPos;
    private Position petePos;
    private Set<Position> goatPos;

    private GameState state;

    public PetesPike(String filename) throws PetesPikeException{
        moveCount = 0; 
        goatPos = new HashSet<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){

        String[] line = reader.readLine().split(" ");
        board = new char[Integer.parseInt(line[0])][Integer.parseInt(line[1])];
        rows = Integer.parseInt(line[0]);
        cols = Integer.parseInt(line[1]);

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
                else if(GOAT_SYBOLS.contains(l.charAt(col))){
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

    public GameState getState() {
        return state;
    }

    public char getSymbolAt(Position position){
        return board[position.getRow()][position.getCol()];
    }

    public Position getMountaintop(){
        return this.mountaintopPos;
    }

    public char[][] getBoard() {
        return board;
    }

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
}
