package Physigraph;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * Created by cfy on 15-11-28.
 */
public class LineRadiusSetter {
    protected float x,y;
    protected Diagram diag;
    private float raduis;
    protected FLine line = null;
    private Paint mpaint;
    public LineRadiusSetter(Diagram d){
        this.diag = d;

        this.raduis = 40;

        this.x = this.y = 0;
        mpaint = new Paint();
        mpaint.setStyle(Paint.Style.FILL);
        mpaint.setARGB(125,208,122,76);
    }
    public void Draw(Canvas canvas){
        update();
        //canvas.drawRect(x - width / 2, y - height / 2, x + width / 2, y + height / 2, mpaint);
        canvas.drawCircle(x, y, raduis, mpaint);
    }

    public void update(){
        if(this.line == null) return;
        if(line.isArc) {
            float[] c = line.getCentre();
            this.x = c[0] * diag.scale + diag.originx;
            this.y = c[1] * diag.scale + diag.originy;
        }
    }
    public boolean Touched(float x,float y,float critical){
        return this.line != null && line.isArc && Math.sqrt((x - this.x) * (x - this.x) - (y - this.y) * (y - this.y)) - raduis <=critical;
    }
    public void setLine(FLine line){
        this.line = line;
        this.update();
    }
    public boolean hasLine(){
        return line != null && line.isArc;
    }

    public void setRadiusByCoordinates(float x, float y){
        float distance = (float)Math.sqrt((line.x2 - line.x1) * (line.x2 - line.x1) + (line.y2 - line.y1) * (line.y2 - line.y1)) * diag.scale;
        float prj = ((line.y1 - line.y2) * (x - this.x) + (line.x2 - line.x1) * (y - this.y)) / distance;
        line.radius += prj;
        line.refresh();
        update();
    }
}
