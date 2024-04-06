package petespike.view;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import petespike.model.Position;

public class GridEventHandler implements EventHandler<ActionEvent>{

    private final int row;
    private final int col;
    private final PetesPikeUI UI;

    public GridEventHandler(int row , int col , PetesPikeUI UI){
        this.row = row;
        this.col = col;
        this.UI = UI;

    }

    @Override
    public void handle(ActionEvent event) {
        // UI.setSelectedPosition(new Position(row , col));
        // if(UI.getSelectedDirection() != null && UI.getGame().isValid(new Position(row, col) , UI.getSelectedDirection())){
        //     UI.getGame().makeMove(new Position(row, col) , UI.getSelectedDirection());
        // }
    }
    
}
