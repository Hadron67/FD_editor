package Physigraph;

import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

/**
 * Created by cfy on 15-11-24.
 */
public class Diagram {

    protected float originx,originy,scale;

    private Selectable selected;

    private ArrayList<FLine> lines;
    private ArrayList<FVertex> vertices;
    public Diagram(){
        this.lines = new ArrayList<>();
        this.vertices = new ArrayList<>();
        scale = 1;
        originx = originy = 0;
        selected = null;
    }
    public void addLine(FLine line){
        this.lines.add(line);
    }
    public boolean addVertex(FVertex vertex){
        for(FVertex a : vertices){
            if(a.x == vertex.x && a.y == vertex.y) return false;
        }
        this.vertices.add(vertex);
        return true;
    }
    public void moveOrigin(float x,float y){
        this.originx += x;
        this.originy += y;
    }
    public float getCX(){
        return this.originx;
    }
    public float getCY(){
        return this.originy;
    }
    public float[] transform(float originx,float originy){
        return new float[]{(originx - this.originx) / scale,(originy - this.originy) / scale};
    }
    public float[] transform(float[] origin){
        return new float[]{(origin[0] - this.originx) / scale,(origin[1] - this.originy) / scale};
    }
    public float[] inverseTransform(float originx,float originy){
        return new float[]{originx * scale + this.originx,originy * scale + this.originy};
    }
    public void rescale(float scaler,float x,float y){
        this.scale *= scaler;
        this.originx += (this.originx - x) * (scaler - 1);
        this.originy += (this.originy - y) * (scaler - 1);
    }
    public void Draw(Canvas canvas){
        canvas.translate(this.originx, this.originy);
        canvas.scale(scale,scale);
        for(FLine a : lines){
            a.Draw(canvas,scale);
        }
        for(FVertex a : vertices){
            a.Draw(canvas,scale);
        }
        if(selected != null){
            selected.DrawSelection(canvas);
        }

    }
    public FVertex getNearestVertex(float x,float y,float criticalRadius){
        float rx = (x - originx) / scale;
        float ry = (y - originy) / scale;
        for(FVertex a : vertices){
            if((rx - a.x) * (rx - a.x) + (ry - a.y) * (ry - a.y) <= criticalRadius * criticalRadius * scale * scale){
                return a;
            }
        }
        return null;
    }
    public float getScale(){
        return this.scale;
    }
    public void removeLine(FLine line){
        this.lines.remove(line);
    }
    /*
    * @param x,y : coordinate of the point to select line , NOT TRANSFORMED ONES
    *
    * */
    public FLine selectLine(float x,float y,float citicaldiatance){
        float rx = (x - originx) / scale;
        float ry = (y - originy) / scale;
        for(FLine a : lines){
            if(a.Touched(rx,ry,citicaldiatance * scale)) {
                selected = a;
                return a;
            }
        }
        return null;
    }
    public FVertex selectVertex(float x,float y,float criticaldistance){
        float rx = (x - originx) / scale;
        float ry = (y - originy) / scale;
        for(FVertex a : vertices){
            if(distance(a.x,a.y,rx,ry) - a.radius <= criticaldistance * scale){
                selected = a;
                return a;
            }
        }
        return null;
    }
    public void Deselect(){
        this.selected = null;
    }
    public void DeleteLine(FLine line){
        line.Delete();
    }

    public void DeleteVertex(FVertex vertex){
        vertex.Delete();
    }
    private static float distance(float x1,float y1,float x2,float y2){
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float)Math.sqrt(dx*dx + dy*dy);
    }
}
