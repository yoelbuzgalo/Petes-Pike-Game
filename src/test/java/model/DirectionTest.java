package model;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;

public class DirectionTest {
    @Test
    public void testUpValues(){
        assert (Direction.UP.getRow() == -1 && Direction.UP.getCol() == 0);
    }

    @Test
    public void testDownValues(){
        assert (Direction.DOWN.getRow() == 1 && Direction.DOWN.getCol() == 0);
    }

    @Test
    public void testRightValues(){
        assert (Direction.RIGHT.getRow() == 0 && Direction.RIGHT.getCol() == 1);
    }

    @Test
    public void testLeftValues(){
        assert (Direction.LEFT.getRow() == 0 && Direction.LEFT.getCol() == -1);
    }

    
}
