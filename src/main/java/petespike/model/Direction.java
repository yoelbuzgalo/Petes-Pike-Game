package petespike.model;

public enum Direction {
    UP(-1 , 0),
    DOWN( 1 , 0),
    LEFT(0 , -1),
    RIGHT(0 , 1);

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
