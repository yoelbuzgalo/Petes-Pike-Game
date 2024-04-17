package petespike.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import backtracker.Configuration;

public class PetesPikeSolver implements Configuration<PetesPikeSolver>{
    private List<Move> moves;
    private PetesPike engine;

    public PetesPikeSolver(PetesPike engine){
        this.engine = new PetesPike(engine);
        moves = new ArrayList<>();
    }

   @Override
   public Collection<PetesPikeSolver> getSuccessors() {
       Collection<PetesPikeSolver> collection = new Collection<PetesPikeSolver>();
   }

   @Override
   public boolean isValid() {
       try{
        engine.makeMove(moves.get(-1));

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

}
