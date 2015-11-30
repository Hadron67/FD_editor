package Physigraph;

import android.graphics.Path;

/**
 * Created by cfy on 15-11-23.
 */
public class GluonLine extends FLine{
    protected float amplitude;
    protected float period_divizer;
    protected float acurraty;

    public GluonLine(float x1, float y1, float x2, float y2) {
        super(x1, y1, x2, y2);
        amplitude = 15;
        period_divizer = 40;
        acurraty = 50;

        selector_width = 2 * amplitude + lineWidth / 2;
    }
    public GluonLine(){
        this(0,0,0,0);
    }

    @Override
    protected void GeneratePath(float scale) {
        p = new Path();
        float length = distance(x1,y1,x2,y2);
        if(!isArc){
            float periods = Math.round(length / period_divizer);
            float t1x = -(y2 - y1) / length;
            float t1y = (x2 - x1) / length;

            float l = acurraty * length / 100;
            p.moveTo(x1,y1);
            for(float i = 0;i <= l;i++){
                float phase = i/l * 2 * (float)Math.PI * periods;
                float vectorX = -t1x * amplitude + (t1x * (float)Math.cos(phase) + t1y * (float)Math.sin(phase)) * amplitude;
                float vectorY = -t1y * amplitude + (-t1x * (float)Math.sin(phase) + t1y * (float)Math.cos(phase)) * amplitude;
                p.lineTo(x1 + (x2 - x1) * i/l + vectorX,y1 + (y2 - y1) * i/l + vectorY);
            }
        }
        else{
            float radius = (float)Math.sqrt(this.radius * this.radius + length * length / 4);
            float mtheta = (float)Math.PI * 2 - 2 * (float)Math.atan2(length / 2,this.radius);
            float t1x = -(y2 - y1) / length;
            float t1y = (x2 - x1) / length;
            float centreX = (x1 + x2) / 2 + t1x * this.radius;
            float centreY = (y1 + y2) / 2 + t1y * this.radius;

            float periods = (float) Math.ceil(mtheta * radius / period_divizer);
            float uvectorX = (x1 - centreX) / radius;
            float uvectorY = (y1 - centreY) / radius;

            float l = acurraty * mtheta * radius / period_divizer;
            p.moveTo(x1,y1);

            for(float i = 0;i <= l;i++){
                float phase = i/l * (2 * (float)Math.PI * periods + mtheta);
                float angle = i/l * mtheta;
                float vectorX = uvectorX * (float)Math.cos(angle) + uvectorY * (float)Math.sin(angle);
                float vectorY = -uvectorX * (float)Math.sin(angle) + uvectorY * (float)Math.cos(angle);
                float tX = uvectorX * (float)Math.cos(phase) + uvectorY * (float)Math.sin(phase);
                float tY =-uvectorX * (float)Math.sin(phase) + uvectorY * (float)Math.cos(phase);

                p.lineTo(centreX + vectorX * (radius - amplitude) + tX * amplitude,centreY + vectorY * (radius - amplitude) + tY * amplitude);
            }
        }
    }
}
