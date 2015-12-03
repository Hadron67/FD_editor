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
        fcanvas.setDiagram(new Diagram());
        fcanvas.setOrigin(diag.getCX(),diag.getCY());
        fcanvas.setScale(diag.getScale());
        fcanvas.Deselect();
        fcanvas.update();
    }

    @Override
    protected void Undo(FeynmanCanvas feynmanCanvas) {
        diag.setOrigin(feynmanCanvas.getCX(),feynmanCanvas.getCY());
        diag.setScale(feynmanCanvas.getScale());
        feynmanCanvas.setDiagram(diag);
        feynmanCanvas.Deselect();
        feynmanCanvas.update();
    }
}
