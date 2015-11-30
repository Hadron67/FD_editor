package Physigraph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by cfy on 15-11-23.
 */
public class ArrowedLine extends FLine{
    protected float arrowPosition;
    protected float arrowHeight,arrowWidth;
    public ArrowedLine(){
        this(0,0,0,0);
    }
    public ArrowedLine(float x1,float y1,float x2,float y2){
        super(x1,y1,x2,y2);
        arrowPosition = 0.5f;
        arrowHeight = 10f;
        arrowWidth = 20;
    }

    @Override
    protected void GeneratePath(float scale) {
        super.GeneratePath(scale);
        if(!isArc){
            float distance = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
            float k1x = (x2 - x1) / distance;
            float k1y = (y2 - y1) / distance;
            float t1x = -(y2 - y1) / distance;
            float t1y = (x2 - x1) / distance;
            p.moveTo(x1,y1);
            p.lineTo(x2, y2);
            p.moveTo(x1 + (x2 - x1) * arrowPosition, y1 + (y2 - y1) * arrowPosition);
            p.lineTo(x1 + (x2 - x1) * arrowPosition - (k1x * arrowWidth + t1x * arrowHeight), y1 + (y2 - y1) * arrowPosition - (k1y * arrowWidth + t1y * arrowHeight));
            p.moveTo(x1 + (x2 - x1) * arrowPosition, y1 + (y2 - y1) * arrowPosition);
            p.lineTo(x1 + (x2 - x1) * arrowPosition - (k1x * arrowWidth - t1x * arrowHeight), y1 + (y2 - y1) * arrowPosition - (k1y * arrowWidth - t1y * arrowHeight));
        }
        else{
            float length = distance(x1,y1,x2,y2);
            float radius = (float)Math.sqrt(this.radius * this.radius + length * length / 4);
            float t1x = -(y2 - y1) / length;
            float t1y = (x2 - x1) / length;
            float centreX = (x2 + x1) / 2 + this.radius * t1x;
            float centreY = (y2 + y1) / 2 + this.radius * t1y;
            float mtheta = (float)Math.PI * 2 - 2 * (float)Math.atan2(length / 2,this.radius);
            float vectorX = (x1 - centreX) * (float)Math.cos(mtheta * arrowPosition) + (y1 - centreY) * (float)Math.sin(mtheta * arrowPosition);
            float vectorY =-(x1 - centreX) * (float)Math.sin(mtheta * arrowPosition) + (y1 - centreY) * (float)Math.cos(mtheta * arrowPosition);
            vectorX /= radius;
            vectorY /= radius;
            float v2x = vectorY;
            float v2y = -vectorX;
            p.moveTo(centreX + vectorX * radius,centreY + vectorY * radius);
            p.lineTo(centreX + vectorX * (radius - arrowHeight) - v2x * arrowWidth, centreY + vectorY * (radius - arrowHeight) - v2y * arrowWidth);
            p.moveTo(centreX + vectorX * radius, centreY + vectorY * radius);
            p.lineTo(centreX + vectorX * (radius + arrowHeight) - v2x * arrowWidth,centreY + vectorY * (radius + arrowHeight) - v2y * arrowWidth);

        }
    }

//    @Override
//    protected void draw_extra(float x1, float y1, float x2, float y2, float scale, Canvas canvas) {
//        Path p = new Path();
//        Paint mp = new Paint();
//        mp.setStyle(Paint.Style.FILL);
//        mp.setColor(Color.argb(255,0,0,0));
//        mp.setAntiAlias(true);
//        if(!isArc) {
//            float distance = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
//            float k1x = (x2 - x1) / distance;
//            float k1y = (y2 - y1) / distance;
//            float t1x = -(y2 - y1) / distance;
//            float t1y = (x2 - x1) / distance;
//            p.moveTo(x1 + (x2 - x1) * arrowPosition, y1 + (y2 - y1) * arrowPosition);
//            p.lineTo(x1 + (x2 - x1) * arrowPosition + (-k1x * arrowWidth + t1x * arrowHeight) * scale, y1 + (y2 - y1) * arrowPosition + (-k1y * arrowWidth + t1y * arrowHeight) * scale);
//            p.lineTo(x1 + (x2 - x1) * arrowPosition + (-k1x * arrowWidth *3/4 ) * scale, y1 + (y2 - y1) * arrowPosition + (-k1y * arrowWidth *3/ 4) * scale);
//            p.lineTo(x1 + (x2 - x1) * arrowPosition + (-k1x * arrowWidth - t1x * arrowHeight) * scale, y1 + (y2 - y1) * arrowPosition + (-k1y * arrowWidth - t1y * arrowHeight) * scale);
//            p.lineTo(x1 + (x2 - x1) * arrowPosition, y1 + (y2 - y1) * arrowPosition);
//            canvas.drawPath(p,mp);
//        }
//    }
}
