package petespike.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import backtracker.Backtracker;
import backtracker.Configuration;

public class PetesPikeSolver implements Configuration<PetesPikeSolver>{
    private final List<Move> moves = new ArrayList<>();
    private final PetesPike engine;
    private static int MAX_MOVES = 7;

    /**
     * Static method that returns a solution instance
     * @param engine pass in an engine you want to solve
     * @return returns an instance of a valid solution or null if there is no solution
     */
    public static PetesPikeSolver solve (PetesPike engine) {
        PetesPikeSolver configuration = new PetesPikeSolver(engine, null);
        Backtracker<PetesPikeSolver> solver = new Backtracker<>(false);
        return solver.solve(configuration);
    }

    /**
     * Constructor
     * @param original pass in an original configuration
     */
    public PetesPikeSolver(PetesPike original, List<Move> moves){
        this.engine = original;
        if (moves != null){
            this.moves.addAll(moves);
        }
    }

    public void makeMove(Move move) throws PetesPikeException{
        engine.makeMove(move);
    }

    public char[][] getBoard(){
        return engine.getBoard();
    }

    /**
     * Getter method that gets the moves of a current configuration
     * @return returns a list of moves
     */
    public List<Move> getMoves() {
        return this.moves;
    }

    @Override
    public Collection<PetesPikeSolver> getSuccessors() {
        List<PetesPikeSolver> successors = new ArrayList<>();

    
            for(Move move: this.engine.getPossibleMoves()) {
                if(moves.size()!= 0 && move.equals(moves.getLast())){
                    continue;
                }
                if(moves.size()> 6 && move.equals(moves.get(moves.size()- 3)) && move.equals(moves.get(moves.size()- 5))){
                    continue;
                }
                PetesPikeSolver successor = new PetesPikeSolver(new PetesPike(this.engine), this.moves);
                try{
                successor.makeMove(move);
                } catch(PetesPikeException e){continue;}
                // if(engine.getBoard().equals(successor.getBoard())){
                //     continue;
                // }
                if(Arrays.deepEquals(engine.getBoard(), successor.getBoard())){
                    continue;
                }

                successor.moves.add(move);
                successors.add(successor);
            }
    

        return successors;
    }

    @Override
    public boolean isValid() {
         if(moves.size() > MAX_MOVES){
             return false;
         }
        // try{
        //     this.engine.makeMove(this.getMoves().getLast());
        //     return true;
        // }
        // catch(PetesPikeException e){
        //     return false;
        // }

        return true;
    }

    @Override
    public boolean isGoal() {
        return this.engine.getState() == GameState.WON;
    }

    public static void main(String[] args) throws PetesPikeException {
        // , "data/petes_pike_9_9_9_0.txt"
        String[] files = new String[]{"data/petes_pike_4_8_5_no_solution.txt" , "data/petes_pike_5_5_2_0.txt" , "data/petes_pike_5_5_4_0.txt" , "data/petes_pike_5_5_4_1.txt" , "data/petes_pike_5_5_5_0.txt" , "data/petes_pike_5_7_4_0.txt"};
        for(String filename : files){
            System.out.println(filename);
            PetesPikeSolver petesPikeSolver = solve(new PetesPike(filename));
            if (petesPikeSolver != null){
                System.out.println(petesPikeSolver.getMoves().toString());
            }
            else{
                System.out.println("null");
            }
        }
    }

}
