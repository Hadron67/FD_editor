package fcanvas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by cfy on 15-12-23.
 */
public class VertexIndicatorLine {
    private Paint mPaint;
    private float x1,y1,x2,y2;
    public VertexIndicatorLine(float x1,float y1,float x2,float y2){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.argb(125,228,50,234));
        mPaint.setStrokeWidth(5);

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    public void draw(Canvas canvas){
        canvas.drawLine(x1,y1,x2,y2,mPaint);
        canvas.drawCircle((x1 + x2) / 2,(y1 + y2) / 2,10,mPaint);
    }
    public void setEndPos(float x,float y){
        x2 = x;
        y2 = y;
    }
}
