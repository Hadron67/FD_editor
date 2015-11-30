package Physigraph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by cfy on 15-11-25.
 */
public class CounterVertex extends FVertex{
    private static Paint cover = new Paint();
    public CounterVertex(float x,float y){
        super(x,y);
        this.radius = 15;
        this.mpaint.setStyle(Paint.Style.STROKE);
        this.mpaint.setStrokeWidth(2);

        cover.setStyle(Paint.Style.FILL);
        cover.setColor(Color.argb(255,255,255,255));
    }
    public CounterVertex(float[] c){
        this(c[0],c[1]);
    }

    @Override
    protected void Draw(Canvas canvas, float scale) {
        super.Draw(canvas, scale);
        canvas.drawCircle(x,y,radius,cover);
        canvas.drawCircle(x, y, radius, mpaint);
        float r = radius / (float)Math.sqrt(2);
        canvas.drawLine(x - r,y - r,x + r,y + r,mpaint);
        canvas.drawLine(x + r,y - r,x - r,y + r,mpaint);
    }
}
