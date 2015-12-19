package Physigraph;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by cfy on 15-11-25.
 */
public class FVertex implements Selectable{
    protected Paint mpaint;

    protected float x,y;

    protected float radius;
    protected ArrayList<FLine> lines;
    public FVertex(){
        this(0,0);
    }

    public FVertex(float x,float y){
        this.x = x;
        this.y = y;
        radius = 4;
        mpaint = new Paint();
        mpaint.setARGB(255,0,0,0);
        mpaint.setStyle(Paint.Style.FILL);
        lines = new ArrayList<>();
    }
    public FVertex(float[] pos){
        this(pos[0], pos[1]);
    }
    public void setPos(float x,float y){
        this.x = x;
        this.y = y;
    }
    public void Draw(Canvas canvas){
        canvas.drawCircle(x,y,radius,mpaint);
    }
    public boolean addLine(FLine line){
        if(!containsLine(line)){
            this.lines.add(line);
            return true;
        }
        return false;
    }
    public Paint getPaint(){
        return mpaint;
    }
    public float getX() {
        return x;
    }
    public float getY(){
        return y;
    }

    @Override
    public void DrawSelection(Canvas canvas) {
        Paint mp = new Paint();
        mp.setARGB(150,157,177,241);
        mp.setStyle(Paint.Style.STROKE);
        mp.setStrokeWidth(8);
        canvas.drawCircle(x,y,radius + 4,mp);
    }

    public void removeLine(FLine line){
        this.lines.remove(line);
    }

    public boolean containsLine(FLine line){
        for(FLine a : this.lines){
            if(a == line) return true;
        }
        return false;
    }
}
