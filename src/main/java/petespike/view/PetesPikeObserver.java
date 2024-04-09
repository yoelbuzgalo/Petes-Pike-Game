package petespike.view;

import petespike.model.GameState;
import petespike.model.Move;
import petespike.model.Position;

public interface PetesPikeObserver {

    public abstract void pieceMoved(Position from , Position to);
    public abstract void reset();
    public abstract void updateStatus(GameState status);
    public abstract void displayHint(Move move);
    public abstract void setMessage(String message);
}
