package petespike.view;

import petespike.model.Position;

public interface PetesPikeObserver {

    public abstract void pieceMoved(Position from , Position to);
    public abstract void reset();
    public abstract void displayMessage(String message);
}
