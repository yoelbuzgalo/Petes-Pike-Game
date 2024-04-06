package petespike.view;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import petespike.model.Move;
import petespike.model.PetesPikeException;
import petespike.model.Position;

public class GridEventHandler implements EventHandler<ActionEvent>{

    private final int row;
    private final int col;
    private final Position position;
    private final PetesPikeUI UI;

    public GridEventHandler(int row , int col, PetesPikeUI UI){
        this.row = row;
        this.col = col;
        this.UI = UI;
        this.position = new Position(row, col);
        

    }

    @Override
    public void handle(ActionEvent event) {
        UI.setClickedPosition(this.position);
        if(UI.getClickDirection() != null && UI.getClickedPosition() != null && UI.getEngine().validMove(new Move(position, UI.getClickDirection()))){
            try{
            UI.getEngine().makeMove(new Move(position, UI.getClickDirection()));
            }
            catch(PetesPikeException e){
                System.out.println(e.getMessage());
            }
        }
        
    }
    
}
