package petespike.model;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    @Override
    public String toString(){
        return this.name();
    }
}
