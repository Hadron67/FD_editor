package FCanvas;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import Physigraph.Diagram;
import Physigraph.FLine;
import Physigraph.FVertex;

/**
 * Created by cfy on 15-12-3.
 */
public class SplitLineCommand extends BasicCommand{
    private FLine originLine;
    private ArrayList<FLine> newlines;
    private ArrayList<FVertex> newvertices;

    public SplitLineCommand(Diagram diag,FLine originLine,int segs){
        this.originLine = originLine;
        newlines = new ArrayList<>();
        newvertices = new ArrayList<>();
        try {
            Constructor<? extends FLine> lineConstructor = originLine.getClass().getConstructor();
            FVertex lastvertex = originLine.getStartVertex();
            if(!originLine.IsArc()) {
                for (float i = 1; i <= segs; i++) {
                    float x = originLine.getStartVertex().getX() * (1 - i / segs) + originLine.getEndVertex().getX() * i / segs;
                    float y = originLine.getStartVertex().getY() * (1 - i / segs) + originLine.getEndVertex().getY() * i / segs;
                    FVertex newvertex = i != segs ? new FVertex(x, y) : originLine.getEndVertex();
                    if (i != segs) newvertices.add(newvertex);
                    FLine newline = lineConstructor.newInstance();
                    newline.setStartVertex(lastvertex);
                    newline.setEndVertex(newvertex);
                    newlines.add(newline);
                    lastvertex = newvertex;
                }
            }
            else{
                float[] c = originLine.getCentre();
                float vectorX = originLine.getStartVertex().getX() - c[0];
                float vectorY = originLine.getStartVertex().getY() - c[1];

                float mtheta = originLine.getMAngle();

                float newradiusv = -originLine.getRadius() * (float)Math.cos(mtheta / 2 / segs);


                for (float i = 1; i <= segs; i++) {

                    float angle = mtheta * i/segs;

                    float x = vectorX * (float)Math.cos(angle) + vectorY * (float)Math.sin(mtheta) + c[0];
                    float y =-vectorX * (float)Math.sin(angle) + vectorY * (float)Math.cos(mtheta) + c[1];

                    FVertex newvertex = i != segs ? new FVertex(x, y) : originLine.getEndVertex();
                    if (i != segs) newvertices.add(newvertex);
                    FLine newline = lineConstructor.newInstance();
                    newline.setRadiusVector(newradiusv);
                    newline.setStartVertex(lastvertex);
                    newline.setEndVertex(newvertex);
                    newlines.add(newline);
                    lastvertex = newvertex;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void Do(FeynmanCanvas fcanvas) {
        fcanvas.fdiagram.DeleteLine(originLine);
        for(FVertex a : newvertices){
            fcanvas.fdiagram.addVertex(a);
        }
        for(FLine line : newlines){
            fcanvas.fdiagram.addLine(line);
        }
        originLine.getStartVertex().addLine(newlines.get(0));
        originLine.getEndVertex().addLine(newlines.get(newlines.size() - 1));
        fcanvas.Deselect();
        fcanvas.update();
    }

    @Override
    protected void Undo(FeynmanCanvas fcanvas) {
        for(FLine line : newlines){
            fcanvas.fdiagram.removeLine(line);
        }
        for(FVertex v : newvertices){
            fcanvas.fdiagram.removeVertex(v);
        }
        originLine.getStartVertex().removeLine(newlines.get(0));
        originLine.getEndVertex().removeLine(newlines.get(newlines.size() - 1));
        fcanvas.fdiagram.AddLineAndConnectToVertices(originLine);
        fcanvas.Deselect();
        fcanvas.update();
    }
}
