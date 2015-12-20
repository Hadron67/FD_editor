package fcanvas;

import physigraph.FLine;

/**
 * Created by cfy on 15-12-20.
 */
public class SetLoopLineRadiusCommand extends BasicCommand{
    private FLine line;
    private float oldRVectorX,oldRVectorY;
    private float newRVectorX,newRVectorY;

    public SetLoopLineRadiusCommand(FLine line,float oldx,float oldy){
        this.line = line;
        newRVectorX = line.getRadiusVectorX();
        newRVectorY = line.getRadiusVectorY();
        oldRVectorX = oldx;
        oldRVectorY = oldy;
    }

    @Override
    protected void Do(FeynmanCanvas fcanvas) {
        line.setArcVector(newRVectorX,newRVectorY);
    }

    @Override
    protected void Undo(FeynmanCanvas feynmanCanvas) {
        line.setArcVector(oldRVectorX,oldRVectorY);
    }
}
