package fcanvas;

import physigraph.FLine;

/**
 * Created by cfy on 15-12-1.
 */
public class FlipLineCommand extends BasicCommand{
    private FLine line;
    public FlipLineCommand(FLine line){
        this.line = line;
    }
    @Override
    protected void Do(FeynmanCanvas fcanvas) {
        line.flip();
    }

    @Override
    protected void Undo(FeynmanCanvas feynmanCanvas) {
        line.flip();
    }
}
