package petespike.view;


import petespike.model.*;


import java.util.HashMap;
import java.util.List;
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

    private final static Scanner input = new Scanner(System.in);

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
     * @return Returns a colorized character piece of the board
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
        System.out.println("Moves: " + engine.getMoveCount());
    }

    /**
     * Helper function to get the direction based on the parsed input
     * @param parsedInput
     * @return
     * @throws PetesPikeException
     */
    private static Direction getDirection(String[] parsedInput) throws PetesPikeException {
        Direction direction;

        if (parsedInput[3].equals("u")){
            direction = Direction.UP;
        } else if (parsedInput[3].equals("d")){
            direction = Direction.DOWN;
        } else if (parsedInput[3].equals("l")){
            direction = Direction.LEFT;
        } else if (parsedInput[3].equals("r")){
            direction = Direction.RIGHT;
        } else {
            throw new PetesPikeException("Invalid input for direction");
        }
        return direction;
    }

    private static void checkIfActiveGame(PetesPike game) throws PetesPikeException {
        if (game.getState() == GameState.WON) {
            throw new PetesPikeException("There must be an active game to use this command!");
        }
    }

    /**
     * Helper function to prompt and get the user's actions
     * @param game
     */
    private static void promptUser(PetesPike game){
        while(true){
            System.out.print("Command: ");
            String stringInput = input.nextLine();
            String[] parsedInput = stringInput.split(" ");
            try {
                if (parsedInput[0].equals("help")){
                    printCommands();
                } else if (parsedInput[0].equals("board")){
                    // do nothing, it prints every command anyways
                } else if (parsedInput[0].equals("reset")){
                    game = new PetesPike(game.getFilename());
                } else if (parsedInput[0].equals("new") && parsedInput.length == 2 && !parsedInput[1].isEmpty()){
                    game = new PetesPike(parsedInput[1]);
                } else if (parsedInput[0].equals("move") && parsedInput.length == 4 && !parsedInput[1].isEmpty() && !parsedInput[2].isEmpty() && !parsedInput[3].isEmpty()){
                    // If the game was already won, reject the move
                    checkIfActiveGame(game);
                    Direction direction = getDirection(parsedInput);

                    // Make the move
                    game.makeMove(new Move(new Position(Integer.parseInt(parsedInput[1]), Integer.parseInt(parsedInput[2])), direction));
                    // If the move changed the game's state to won - the CLI will print out
                    if (game.getState() == GameState.WON){
                        System.out.println("Congratulations, you have scaled the mountain!");
                    }
                } else if (parsedInput[0].equals("hint")){

                    checkIfActiveGame(game);
                    Move hintedMove = getHint(game);
                    if (hintedMove != null) {
                        System.out.println("Try: " + hintedMove);
                    } else {
                        System.out.println("There is no possible moves!");
                    }

                } else if (parsedInput[0].equals("quit")){
                    return;
                } else {
                    throw new PetesPikeException(stringInput);
                }
                printBoard(game);
            } catch (PetesPikeException ppe) {
                System.out.println("Invalid command: " + ppe.getMessage());
            }
            
        }
    }

    private static Move getHint(PetesPike game){
        //PetesPike copy = game;
        //List<Move> moves = game.getPossibleMoves();
        for(Move move : game.getPossibleMoves()){
            if(game.validMove(move)){
                return move;
            }
        }
        return null;
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
            PetesPike game = new PetesPike(filename);
            printCommands();
            printBoard(game);
            promptUser(game);
        } catch (PetesPikeException ppe){
            System.err.println((ppe.getMessage()));
        }
    }
}
