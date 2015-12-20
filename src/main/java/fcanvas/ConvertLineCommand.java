package fcanvas;

import physigraph.FLine;

/**
 * Created by cfy on 15-12-1.
 *
 */
public class ConvertLineCommand extends BasicCommand{
    private FLine line;
    private boolean isarc;
    public ConvertLineCommand(FLine line){
        this.line = line;
        isarc = line.IsArc();
    }

    @Override
    protected void Do(FeynmanCanvas fcanvas) {
        if(isarc){
            line.ConvertToLine();
        }
        else{
            line.ConvertToArc();
        }
    }

    @Override
    protected void Undo(FeynmanCanvas feynmanCanvas) {
        if(!isarc){
            line.ConvertToLine();
        }
        else{
            line.ConvertToArc();
        }
    }
}
