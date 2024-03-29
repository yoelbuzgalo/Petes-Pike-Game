package petespike.model;

public class Position {
    private int row;
    private int col;

    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Position){
            Position other = (Position) obj;
            if (this.row == other.row && this.col == other.col){
                return true;
            }
        }
        return false;
    }
}
