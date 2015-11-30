package Physigraph;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

/**
 * Created by cfy on 15-11-23.
 */
public class PhotonLine extends FLine{

    private float period_divizer;
    private float amplitude;

    private int acurraty;

    public PhotonLine(){
        this(0,0,0,0);
    }

    public PhotonLine(float x1,float y1,float x2,float y2){
        super(x1,y1,x2,y2);
        amplitude = 14;
        period_divizer = 40;
        acurraty = 80;

        selector_width = 19;
    }
    @Override
    protected void GeneratePath(float scale){
        p = new Path();
        float length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        if(!isArc) {
            p.moveTo(x1, y1);
            float periods = Math.round(length / period_divizer);
            float k1 = -(y2 - y1) / length;
            float k2 = (x2 - x1) / length;
            float l = acurraty * length / 100;
            for (float i = 0; i <= l; i++) {
                float phase = amplitude * (float) Math.sin(i / l * Math.PI * 2 * periods);
                p.lineTo(x1 + (x2 - x1) * i / l + k1 * phase, y1 + (y2 - y1) * i / l + k2 * phase);
            }
        }
        else{
            //TODO:arc photon line
            float radius = (float)Math.sqrt(this.radius * this.radius + length * length / 4);
            float mtheta = (float)Math.PI * 2 - 2 * (float)Math.atan2(length / 2,this.radius);
            float t1x = -(y2 - y1) / length;
            float t1y = (x2 - x1) / length;
            float centreX = (x1 + x2) / 2 + t1x * this.radius;
            float centreY = (y1 + y2) / 2 + t1y * this.radius;

            float periods = Math.round(mtheta * radius / period_divizer);
            float uvectorX = (x1 - centreX) / radius;
            float uvectorY = (y1 - centreY) / radius;

            float l = acurraty * mtheta * radius / period_divizer;
            p.moveTo(x1,y1);
            for(float i = 0;i <= l;i++){
                float phase = i/l * (float)Math.PI * 2 * periods;
                float angle = i/l * mtheta;
                float uvectorX2 = uvectorX * (float)Math.cos(angle) + uvectorY * (float)Math.sin(angle);
                float uvectorY2 =-uvectorX * (float)Math.sin(angle) + uvectorY * (float)Math.cos(angle);

                p.lineTo(centreX + uvectorX2 * (radius + amplitude * (float)Math.sin(phase)),centreY + uvectorY2 * (radius + amplitude * (float)Math.sin(phase)));

            }
        }
    }

    public float getAmplitude() {
        return amplitude;
    }
    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
        this.p = null;
    }

    public float getPeriod_divizer() {
        return period_divizer;
    }

    public void setPeriod_divizer(float period_divizer) {
        this.period_divizer = period_divizer;
        this.p = null;
    }

    public int getAcurraty() {
        return acurraty;
    }

    public void setAcurraty(int acurraty) {
        this.acurraty = acurraty;
        p = null;
    }
}
