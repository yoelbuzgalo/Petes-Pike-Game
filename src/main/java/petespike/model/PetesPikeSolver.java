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

    /**
     * Constructor
     * @param original
     */
    public PetesPikeSolver(PetesPike original, List<Move> moves){
        this.engine = original;
        if (moves != null){
            this.moves.addAll(moves);
        }
    }

    /**
     * Getter method that gets
     * @return
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
        PetesPikeSolver configuration = new PetesPikeSolver(new PetesPike("data/petes_pike_5_5_2_0.txt"), null);
        Backtracker<PetesPikeSolver> solver = new Backtracker<>(true);
        PetesPikeSolver solution = solver.solve(configuration);
        System.out.println(solution.getMoves());
   }

}
