package Physigraph;

import android.graphics.Canvas;

import java.lang.reflect.Constructor;
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
    public void removeLine(FLine line){
        this.lines.remove(line);
    }
    public boolean addVertex(FVertex vertex){
        for(FVertex a : vertices){
            if(a.x == vertex.x && a.y == vertex.y) return false;
        }
        this.vertices.add(vertex);
        return true;
    }
    public void removeVertex(FVertex vertex){
        vertices.remove(vertex);
    }
    public void AddLineAndConnectToVertices(FLine line){
        lines.add(line);
        if(line.v1 != null){
            line.v1.addLine(line);
        }
        if(line.v2 != null){
            line.v2.addLine(line);
        }
    }
    public void moveOrigin(float x,float y){
        this.originx += x;
        this.originy += y;
    }
    public void setOrigin(float x,float y){
        this.originx = x;
        this.originy = y;
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
    public void setScale(float scale){
        this.scale = scale;
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
        this.lines.remove(line);
        if(selected == line){
            selected = null;
        }
    }

    public void DeleteVertexWithLines(FVertex vertex){
        for(FLine a : vertex.lines){
            FVertex anotherVertex = a.v1 == vertex ? a.v2 : a.v1;
            anotherVertex.lines.remove(a);
            this.lines.remove(a);
        }
        vertices.remove(vertex);
        if(selected == vertex){
            selected = null;
        }
    }

    public void AddVertexWithLines(FVertex vertex){
        for(FLine a : vertex.lines){
            FVertex anotherVertex = a.v1 == vertex ? a.v2 : a.v1;
            anotherVertex.lines.add(a);
            this.lines.add(a);
        }
        vertices.add(vertex);
    }
    private static float distance(float x1,float y1,float x2,float y2){
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float)Math.sqrt(dx*dx + dy*dy);
    }
    public boolean IsEmpty(){
        return this.lines.isEmpty() && this.vertices.isEmpty();
    }
    private boolean containsLine(FVertex vertex,FLine line){
        for(FLine l : vertex.lines){
            if(l == line) return true;
        }
        return false;
    }
}
