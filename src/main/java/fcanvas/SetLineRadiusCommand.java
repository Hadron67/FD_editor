package fcanvas;

import physigraph.FLine;

/**
 * Created by cfy on 15-12-1.
 */
public class SetLineRadiusCommand extends BasicCommand{
    private FLine line;
    private float oldradius,newradius;
    public SetLineRadiusCommand(FLine line,float oldradius){
        this.line = line;
        this.oldradius = oldradius;
        this.newradius = line.getRadiusVector();
    }
    public void setNewradius(float newradius){
        this.newradius = newradius;
    }
    @Override
    protected void Do(FeynmanCanvas fcanvas) {
        line.setRadiusVector(newradius);
    }

    @Override
    protected void Undo(FeynmanCanvas feynmanCanvas) {
        line.setRadiusVector(oldradius);
    }
}
