package fcanvas;

import physigraph.FVertex;

/**
 * Created by cfy on 15-12-1.
 */
public class AddVertexCommand extends BasicCommand{
    private FVertex addedVertex;

    public AddVertexCommand(FVertex vertex){
        addedVertex = vertex;
    }
    @Override
    protected void Do(FeynmanCanvas fcanvas) {
        fcanvas.fdiagram.AddVertexWithLines(addedVertex);
    }

    @Override
    protected void Undo(FeynmanCanvas feynmanCanvas) {
        feynmanCanvas.fdiagram.DeleteVertexWithLines(addedVertex);
    }
}
