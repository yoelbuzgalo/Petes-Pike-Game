package petespike.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import backtracker.Backtracker;
import backtracker.Configuration;

public class PetesPikeSolver implements Configuration<PetesPikeSolver>{
    private List<Move> moves;
    private PetesPike engine;

    public PetesPikeSolver(PetesPike engine){
        this.engine = new PetesPike(engine);
        moves = new ArrayList<>();
    }

    public PetesPikeSolver(PetesPikeSolver other){
        this.moves = new ArrayList<>(other.moves);
        this.engine = new PetesPike(other.engine);
    }

    public void addMove(Move move){
        moves.addLast(move);
    }

    

   @Override
   public Collection<PetesPikeSolver> getSuccessors() {
       Collection<PetesPikeSolver> collection = new HashSet<>();

       for(Move move: engine.getPossibleMoves()){
        PetesPikeSolver solver = new PetesPikeSolver(this);
        solver.addMove(move);
        collection.add(solver);

       }

       return collection;
   }

   @Override
   public boolean isValid() {
       try{
        engine.makeMove(moves.get(moves.size() - 1));

        return true;
       }

       catch(PetesPikeException e){
            return false;
       }
   }

   @Override
   public boolean isGoal() {
       return engine.getState() == GameState.WON;
   }

   public static void main(String[] args) throws PetesPikeException {
    PetesPikeSolver solver = new PetesPikeSolver(new PetesPike("data/petes_pike_5_5_2_0.txt"));
    Backtracker<PetesPikeSolver> back = new Backtracker<>(false);
    PetesPikeSolver solution = back.solve(solver);
    System.out.println(solution.moves);
   }

}
