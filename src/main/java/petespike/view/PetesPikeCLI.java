package petespike.view;


import petespike.model.PetesPike;
import petespike.model.PetesPikeException;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * This class is the command line interface to run Pete's Pike game
 * @author Yoel Buzgalo
 */
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

    private final static Map<Character, String> characterColors = new HashMap<>();

    static {
        // Associate each unique symbol to its color piece
        characterColors.put('P', AsciiColorCodes.RED);
        characterColors.put('0', AsciiColorCodes.BLUE);
        characterColors.put('1', AsciiColorCodes.ORANGE);
        characterColors.put('2', AsciiColorCodes.GREEN);
        characterColors.put('3', AsciiColorCodes.YELLOW);
        characterColors.put('4', AsciiColorCodes.MAGENTA);
        characterColors.put('5', AsciiColorCodes.GOLD);
        characterColors.put('6', AsciiColorCodes.PURPLE);
        characterColors.put('7', AsciiColorCodes.LT_GRAY);
        characterColors.put('8', AsciiColorCodes.CYAN);
    }

    /**
     * Prints a list of all commands
     */
    private static void printCommands(){
        System.out.println("Commands: ");
        for(String command : COMMANDS){
            System.out.println("\t" + command);
        }
    }

    /**
     * Helper function to print colorized characters for the printBoard function
     * @param board
     * @param row
     * @param col
     * @return
     */
    private static String colorizeCharacter(char[][] board, int row, int col){
        if (board[row][col] == 'T') {
            return "+";
        } else if (board[row][col] == 'P'){
            return characterColors.get(board[row][col]) + 'P' + AsciiColorCodes.RESET;
        } else if (Character.isDigit(board[row][col])) {
            return characterColors.get(board[row][col]) + 'G' + AsciiColorCodes.RESET;
        }
        return "-";
    }

    /**
     * This function prints the board
     * @param engine
     */
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
                System.out.print(" " + colorizeCharacter(board, i, j));
            }
            System.out.println();
        }
    }

    /**
     * Main entry to start the game using CLI
     * @param args
     */
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
