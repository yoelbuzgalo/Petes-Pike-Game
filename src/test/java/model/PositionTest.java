package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import petespike.model.Position;

public class PositionTest {
    @Test
    public void testEqualsTrue(){
        Position p1 = new Position(1, 1);
        Position p2 = new Position(1, 1);

        Boolean expected = true;
        Boolean actual = p1.equals(p2);

        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsFalse(){
        Position p1 = new Position(1, 1);
        Position p2 = new Position(0, 0);

        Boolean expected = false;
        Boolean actual = p1.equals(p2);

        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsFalse2(){
        Position p1 = new Position(1, 1);
        Position p2 = new Position(1, 0);

        Boolean expected = false;
        Boolean actual = p1.equals(p2);

        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsFalse3(){
        Position p1 = new Position(1, 0);
        Position p2 = new Position(0, 1);

        Boolean expected = false;
        Boolean actual = p1.equals(p2);

        assertEquals(expected, actual);
    }
}
