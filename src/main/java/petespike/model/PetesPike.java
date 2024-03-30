package petespike.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PetesPike {
    String MOUNTAINTOP_SYMBOL = "T";
    char EMPTY_SYMBOL = '-';
    String PETE_SYMBOL = "P";
    Set<String> GOAT_SYBOLS = new HashSet<>();

    private final int rows;
    private final int cols;
    private String[][] board;

    private int moveCount;

    private Position mountaintopPos;
    private Position petePos;

    private 

    public PetesPike(String filename) throws IOException{
        moveCount = 0;
        FileReader file = new FileReader(filename);
        BufferedReader reader = new BufferedReader(file);

        String[] line = reader.readLine().split(" ");
        board = new String[Integer.parseInt(line[0])][Integer.parseInt(line[1])];
        rows = Integer.parseInt(line[0]);
        cols = Integer.parseInt(line[1]);

        for(int row = 0 ; row < rows; row++){
            line = reader.readLine().split("");
            for(int col = 0 ; col < cols ; col++){
                board[row][col] = line[col];
                if(line[col].equals(MOUNTAINTOP_SYMBOL)){
                    mountaintopPos = new Position(row, col);
                }
                else if(line[col].equals(PETE_SYMBOL)){
                    petePos = new Position(row, col);
                }
            }
        }



        reader.close();
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
}
