package petespike.model;

public class Move {
    private final Position position;
    private final Direction direction;

    public Move(Position position, Direction direction){
        this.position = position;
        this.direction = direction;
    }

    public Position getPosition() {
        return this.position;
    }

    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public String toString() {
        return this.position + " " + this.direction;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Move){
            Move other = (Move)obj;

            return position.equals(other.getPosition()) && direction.equals(other.getDirection());
        }

        return false;
    }
}
