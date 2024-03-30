package petespike.view;


import petespike.model.PetesPike;
import petespike.model.PetesPikeException;


import java.util.Scanner;
import java.util.stream.IntStream;

public class PetesPikeCLI {
    private final static String[] COMMANDS = {
            "help - displays list of commands",
            "board - displays the current board",
            "reset - resets the current puzzle to the initial board configuration and move count to 0",
            "new <puzzle_filename> - starts a new puzzle",
            "move - <row> <col> <direction> - move the piece at <row>, <col> in the <direction>.\n\t <direction> is one of u(p), d(own), l(eft), r(ight).",
            "hint - displays a valid move given the current board configuration",
            "quit - quits the game"
    };

    private static void printCommands(){
        System.out.println("Commands: ");
        for(String command : COMMANDS){
            System.out.println("\t" + command);
        }
    }

    private static void printBoard(PetesPike engine){
        char[][] board = engine.getBoard();
        int cols = engine.getCols();
        int rows = engine.getRows();

        System.out.print(" ");
        IntStream.range(0, (cols)).forEach((colNum) -> System.out.print(" " + colNum));
        System.out.println();
        for(int i = 0; i < rows; i++){
            System.out.print(i);
            for (int j = 0; j < cols; j++){
                System.out.print(" " + board[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.print("Puzzle filename: ");
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.next();
        try{
            PetesPike ppEngine = new PetesPike(filename);
            printCommands();
            printBoard(ppEngine);
        } catch (PetesPikeException ppe){
            System.err.println((ppe.getMessage()));
        }
    }
}
