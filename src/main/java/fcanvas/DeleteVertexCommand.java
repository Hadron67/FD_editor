package fcanvas;

import physigraph.FVertex;

/**
 * Created by cfy on 15-12-1.
 */
public class DeleteVertexCommand extends BasicCommand{
    private FVertex deletedVertex;
    public DeleteVertexCommand(FVertex vertex){
        deletedVertex = vertex;
    }
    @Override
    protected void Do(FeynmanCanvas fcanvas) {
        fcanvas.fdiagram.DeleteVertexWithLines(deletedVertex);
    }

    @Override
    protected void Undo(FeynmanCanvas feynmanCanvas) {
        feynmanCanvas.fdiagram.AddVertexWithLines(deletedVertex);
    }
}
