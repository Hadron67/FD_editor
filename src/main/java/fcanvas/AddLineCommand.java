package fcanvas;

import physigraph.FLine;

/**
 * Created by cfy on 15-12-1.
 */
public class AddLineCommand extends BasicCommand{
    private FLine addedline;
    public AddLineCommand(FLine line){
        this.addedline = line;
    }
    @Override
    protected void Do(FeynmanCanvas fcanvas) {
        fcanvas.fdiagram.AddLineAndConnectToVertices(addedline);
    }

    @Override
    protected void Undo(FeynmanCanvas feynmanCanvas) {
        feynmanCanvas.fdiagram.DeleteLineFromVertices(addedline);
    }
}
