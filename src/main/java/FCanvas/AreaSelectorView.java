package FCanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.cfy.project2.R;

/**
 * Created by cfy on 15-12-9.
 *
 */
public class AreaSelectorView {
    private float x1,y1,x2,y2;
    private Paint rectanglePaint = null;
    private Paint cornerPaint = null;

    private float radius;

    public AreaSelectorView(){
        radius = 10;

        x1 = y1 = 0;
        x2 = y2 = 100;
        rectanglePaint = new Paint();
        rectanglePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        rectanglePaint.setColor(Color.argb(100, 88, 145, 231));
        rectanglePaint.setAntiAlias(true);

        cornerPaint = new Paint();
        cornerPaint.setStyle(Paint.Style.FILL);
        cornerPaint.setColor(Color.argb(100, 243, 129, 158));
        cornerPaint.setAntiAlias(true);
    }

    public void setCornerRadius(float radius){
        this.radius = radius;
    }
    public void Draw(Canvas canvas){
        canvas.drawCircle(x1, y1, radius, cornerPaint);
        canvas.drawCircle(x2,y1,radius,cornerPaint);
        canvas.drawCircle(x1, y2, radius, cornerPaint);
        canvas.drawCircle(x2, y2, radius, cornerPaint);

        canvas.drawRect(x1, y1, x2, y2, rectanglePaint);
    }

    public int hitTest(float x,float y,float criticalradius){
        if(distance(x,y,x1,y1) - radius <= criticalradius) return 1;
        if(distance(x,y,x2,y1) - radius <= criticalradius) return 2;
        if(distance(x,y,x1,y2) - radius <= criticalradius) return 3;
        if(distance(x,y,x2,y2) - radius <= criticalradius) return 4;
        if(x1 < x && x2 > x && y1 < y && y2 > y) return 5;
        return 0;
    }

    public void setPos1(float x,float y){
        this.x1 = x;
        this.y1 = y;
    }

    public void setPos2(float x,float y){
        this.x2 = x;
        this.y2 = y;
    }

    private static float distance(float x1,float y1,float x2,float y2){
        float dx = x2 - x1,dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public void setPos(int which, float x, float y){
        switch (which){
            case 1:
                x1 = x;
                y1 = y;
                break;
            case 2:
                x2 = x;
                y1 = y;
                break;
            case 3:
                x1 = x;
                y2 = y;
                break;
            case 4:
                x2 = x;
                y2 = y;
                break;
        }
    }
    public void movePos(float dx,float dy){
        x1 += dx;
        x2 += dx;
        y1 += dy;
        y2 += dy;
    }

    public float getX1(){
        return x1;
    }
    public float getY1(){
        return y1;
    }
    public float getWidth(){
        return x2 - x1;
    }
    public float getHeight(){
        return y2 - y1;
    }
}
