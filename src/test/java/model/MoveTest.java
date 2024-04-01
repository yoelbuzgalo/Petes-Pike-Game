package model;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;
import petespike.model.Move;
import petespike.model.Position;

public class MoveTest {
    @Test
    public void testMove(){
        Move m = new Move(new Position(0, 0) , Direction.UP);
       
        assert (m.getDirection().equals(Direction.UP) && m.getPosition().getRow() == 0);
    }
}
