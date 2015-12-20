package physigraph;

import android.graphics.Path;

/**
 * Created by cfy on 15-11-30.
 */
public class DoubleLine extends FLine{
    protected float width;
    public DoubleLine(float x1,float y1,float x2,float y2){
        super(x1,y1,x2,y2);
        width = 2;
        mpaint.setStrokeWidth(2);

    }
    public DoubleLine(){
        this(0,0,0,0);
    }
    @Override
    protected void GeneratePath() {
        p = new Path();
        float length = distance(x1,y1,x2,y2);
        float tX = -(y2 - y1) / length;
        float tY = (x2 - x1) / length;

        switch (shape){
            case LINE:
                p.moveTo(x1 + tX * width, y1 + tY * width);
                p.lineTo(x2 + tX * width, y2 + tY * width);
                p.moveTo(x1 - tX * width, y1 - tY * width);
                p.lineTo(x2 - tX * width, y2 - tY * width);

                break;
            case ARC:
                float radius = (float)Math.sqrt(this.radius * this.radius + length * length / 4);
                float t1x = -(y2 - y1) / length;
                float t1y = (x2 - x1) / length;
                float centreX = (x1 + x2) / 2 + t1x * this.radius;
                float centreY = (y1 + y2) / 2 + t1y * this.radius;
                float vectorX = x1 - centreX;
                float vectorY = y1 - centreY;
                float mtheta = (float)Math.PI * 2 - 2 * (float)Math.atan2(length / 2,this.radius);
                p.moveTo(x1,y1);
                drawArc(p, centreX, centreY, vectorX * (1 + width / radius), vectorY * (1 + width / radius), mtheta, (int) Math.ceil(radius * mtheta / 20), false);
                p.moveTo(x1,y1);
                drawArc(p, centreX, centreY, vectorX * (1 - width / radius), vectorY * (1 - width / radius), mtheta, (int) Math.ceil(radius * mtheta / 20), false);

                break;
            case LOOP:
                radius = (float)Math.sqrt(arcVectorX * arcVectorX + arcVectorY * arcVectorY);
                centreX = x1 + arcVectorX;
                centreY = y1 + arcVectorY;
                vectorX = x1 - centreX;
                vectorY = y1 - centreY;
                mtheta = (float)Math.PI * 2 - 2 * (float)Math.atan2(length / 2,this.radius);
                p.moveTo(x1,y1);
                drawArc(p, centreX, centreY, vectorX * (1 + width / radius), vectorY * (1 + width / radius), mtheta, (int) Math.ceil(radius * mtheta / 20), false);
                p.moveTo(x1,y1);
                drawArc(p, centreX, centreY, vectorX * (1 - width / radius), vectorY * (1 - width / radius), mtheta, (int) Math.ceil(radius * mtheta / 20), false);

                break;
        }
    }
}
