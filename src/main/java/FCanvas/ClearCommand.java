package FCanvas;

import Physigraph.Diagram;

/**
 * Created by cfy on 15-12-1.
 */
public class ClearCommand extends BasicCommand{
    private Diagram diag;
    public ClearCommand(Diagram d){
        this.diag = d;
    }
    @Override
    protected void Do(FeynmanCanvas fcanvas) {
        diag = fcanvas.fdiagram;
        fcanvas.fdiagram = new Diagram();
        fcanvas.fdiagram.setOrigin(diag.getCX(),diag.getCY());
        fcanvas.fdiagram.setScale(diag.getScale());
    }

    @Override
    protected void Undo(FeynmanCanvas feynmanCanvas) {
        diag.setOrigin(feynmanCanvas.fdiagram.getCX(),feynmanCanvas.fdiagram.getCY());
        diag.setScale(feynmanCanvas.fdiagram.getScale());
        feynmanCanvas.fdiagram = diag;
    }
}
