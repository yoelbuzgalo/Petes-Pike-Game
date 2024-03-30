package petespike.model;

public enum Direction {
    UP(0 , 1),
    DOWN( 0 , -1),
    LEFT(-1 , 0),
    RIGHT(1 , 0);

    private int row;
    private int col;

    private Direction(int row , int col){
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }

    @Override
    public String toString(){
        return this.name();
    }
}
