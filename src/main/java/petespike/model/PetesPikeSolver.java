package petespike.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import backtracker.Backtracker;
import backtracker.Configuration;

public class PetesPikeSolver implements Configuration<PetesPikeSolver>{
    private final List<Move> moves = new ArrayList<>();
    private final PetesPike engine;
    private static int MAX_MOVES = 10;

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
                PetesPikeSolver successor = new PetesPikeSolver(new PetesPike(this.engine), this.moves);
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
        try{
            this.engine.makeMove(this.getMoves().getLast());
            return true;
        }
        catch(PetesPikeException e){
            return false;
        }
    }

    @Override
    public boolean isGoal() {
        return this.engine.getState() == GameState.WON;
    }

    public static void main(String[] args) throws PetesPikeException {
        PetesPikeSolver petesPikeSolver = solve(new PetesPike("data/petes_pike_5_5_2_0.txt"));
        if (petesPikeSolver != null){
            System.out.println(petesPikeSolver.getMoves().toString());
        }
    }

}
