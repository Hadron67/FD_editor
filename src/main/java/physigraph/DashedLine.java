package physigraph;

import android.graphics.Path;

/**
 * Created by cfy on 15-11-29.
 */
public class DashedLine extends FLine{
    protected float seglength;

    public DashedLine(float x1,float y1,float x2,float y2){
        super(x1,y1,x2,y2);
        seglength = 20;
    }
    public DashedLine(){
        this(0,0,0,0);
    }

    @Override
    protected void GeneratePath() {
        p = new Path();
        float length = distance(x1,y1,x2,y2);

        switch (shape){
            case LINE:
                dashedLineTo(p,x1,y1,x2,y2,(int)Math.ceil(length / seglength));

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
                float l = (int)Math.ceil(2 * radius * mtheta / seglength) / 2;
                p.moveTo(x1, y1);
                for(float i = 0;i <= 2 * l - 2;i += 2){
                    float phase1 = i/l / 2 * mtheta;
                    float phase2 = (i + 1)/l / 2 * mtheta;
                    p.moveTo(centreX + vectorX * (float)Math.cos(phase1) + vectorY * (float)Math.sin(phase1),centreY - vectorX * (float)Math.sin(phase1) + vectorY * (float)Math.cos(phase1));
                    p.lineTo(centreX + vectorX * (float)Math.cos(phase2) + vectorY * (float)Math.sin(phase2),centreY - vectorX * (float)Math.sin(phase2) + vectorY * (float)Math.cos(phase2));
                }

                break;
            case LOOP:

                break;
        }
    }
}
