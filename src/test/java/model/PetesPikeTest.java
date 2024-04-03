package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;
import petespike.model.GameState;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.Position;

public class PetesPikeTest {
    @Test
    public void testgetMoveCount(){
        try{
            PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
            game.makeMove(new Move(new Position(0, 4) , Direction.LEFT));

            int expected = 1;
            int actual = game.getMoveCount();

            assertEquals(expected, actual);
    
        }
        catch(PetesPikeException e){
            assert false;
        }    
    }

    @Test
    public void testgetState(){
        try{
            PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
            game.makeMove(new Move(new Position(0, 4) , Direction.LEFT));

            GameState expected = GameState.IN_PROGRESS;
            GameState actual = game.getState();

            assertEquals(expected, actual);
    
        }
        catch(PetesPikeException e){
            assert false;
        }    
    }

    @Test
    public void testgetStateWon(){
        try{
            PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
            game.makeMove(new Move(new Position(0, 4) , Direction.LEFT));
            game.makeMove(new Move(new Position(0, 2) , Direction.DOWN));

            GameState expected = GameState.WON;
            GameState actual = game.getState();

            assertEquals(expected, actual);
    
        }
        catch(PetesPikeException e){
            assert false;
        }    
    }

    @Test
    public void testSymbolAt(){
        try{
            PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
            game.makeMove(new Move(new Position(0, 4) , Direction.LEFT));

            char expected = 'P';
            char actual = game.getSymbolAt(new Position(0, 2));

            assertEquals(expected, actual);
    
        }
        catch(PetesPikeException e){
            assert false;
        }    
    }

    @Test
    public void testMakeMoveExceptionBellowZero(){
        try{
            PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
            game.makeMove(new Move(new Position(-1, 4) , Direction.LEFT));



            assert false;
    
        }
        catch(PetesPikeException e){
            assert true;
        }    
    }

    @Test
    public void testMakeMoveExceptionAboveRange(){
        try{
            PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
            game.makeMove(new Move(new Position(5, 4) , Direction.LEFT));



            assert false;
    
        }
        catch(PetesPikeException e){
            assert true;
        }    
    }

    @Test
    public void testMakeMoveExceptionWrongDirection(){
        try{
            PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
            game.makeMove(new Move(new Position(5, 4) , Direction.RIGHT));



            assert false;
    
        }
        catch(PetesPikeException e){
            assert true;
        }    
    }

    @Test
    public void testMakeMoveExceptionEmptySymbol(){
        try{
            PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
            game.makeMove(new Move(new Position(0, 0) , Direction.RIGHT));



            assert false;
    
        }
        catch(PetesPikeException e){
            assert true;
        }    
    }

    @Test
    public void testGoatPos(){
        try{
            PetesPike game = new PetesPike("data/petes_pike_4_8_5_no_solution.txt");
            game.makeMove(new Move(new Position(3, 2) , Direction.UP));



            assert game.getGoatPos().contains(new Position(2, 2));
    
        }
        catch(PetesPikeException e){
            assert false;
        }    
    }

    @Test
    public void testPossibleMoves(){
        try{
            PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");

            int expected = 12;
            int actual = game.getPossibleMoves().size();
            assert expected == actual;
        }
        catch(PetesPikeException e){
            assert false;
        }
    }

    @Test
    public void testValidMoveInvalid(){
        try{
            PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");

            assert !game.validMove(new Move(new Position(0, 4), Direction.UP));
        }
        catch(PetesPikeException e){
            assert false;
        }
    }

    @Test
    public void testValidMoveValid(){
        try{
            PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");

            assert game.validMove(new Move(new Position(0, 4), Direction.LEFT));
        }
        catch(PetesPikeException e){
            assert false;
        }
    }
        
}
