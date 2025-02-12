package petespike.model;

public class Position {
    private final int row;
    private final int col;

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
            return this.hashCode() == other.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + this.col;
        result = prime * result + this.row;
        return result ;
    }

    @Override
    public String toString() {
        return "Position: (" + this.row + ", " + this.col + ")" ;
    }
}
