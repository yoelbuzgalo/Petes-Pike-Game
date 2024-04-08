package petespike.model;

public enum GameState {
    NEW("New Game"),
    IN_PROGRESS("Great move!"),
    NO_MOVES("There is no piece in that direction to stop you"),
    WON("Congratulations, you have scaled the mountain!");

    private final String message;

    private GameState(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
