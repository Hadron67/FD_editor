package FCanvas;


import Physigraph.FLine;

/**
 * Created by cfy on 15-12-1.
 *
 */
public class DeleteLineCommand extends BasicCommand{
    private FLine deletedLine;
    public DeleteLineCommand(FLine deletedLine){
        this.deletedLine = deletedLine;
    }
    @Override
    protected void Do(FeynmanCanvas fcanvas) {

        fcanvas.fdiagram.DeleteLine(deletedLine);
    }

    @Override
    protected void Undo(FeynmanCanvas feynmanCanvas) {
        feynmanCanvas.fdiagram.AddLineAndConnectToVertices(deletedLine);
    }
}
