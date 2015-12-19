package Physigraph;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.cfy.project2.BuildConfig;

/**
 * Created by cfy on 15-11-19.
 */
public class Lattice {
    public static final float POINT_RADIUS = 5;
    protected float k1,k2,k3,k4,a = 0,b = 0;
    protected float scale = 1;

    Paint latticepaint;

    public Lattice(float k1,float k2,float k3,float k4){
        if(BuildConfig.DEBUG) assert(k1 >= 0 && k2 <= 0 && k3 >= 0 && k4 <= 0);
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;
        this.k4 = k4;
        latticepaint = new Paint();
        latticepaint.setAntiAlias(true);
        latticepaint.setStyle(Paint.Style.FILL);
        latticepaint.setARGB(255,200,175,224);
        //latticepaint.setARGB(255,0,0,0);

    }
    public void draw(Canvas canvas){
        float p1 = k1 * scale;
        float p2 = k2 * scale;
        float p3 = k3 * scale;
        float p4 = k4 * scale;
        float w = canvas.getWidth();
        float h = canvas.getHeight();
        float lambda2 = (float)Math.floor(((w - a) * p4 - (h - b) * p3) / (p1 * p4 - p2 * p3));
        float lambda1 = (float)Math.floor(((0 - a) * p4 - (0 - b) * p3) / (p1 * p4 - p2 * p3));
        for(float lambda = lambda1;lambda <= lambda2;lambda++){
            float mu1 = (float)Math.floor(MIN((-a-lambda*p1)/p3,(h-b-lambda*p2)/p4));
            float mu2 = (float)Math.floor(MAX((w - a - lambda * p1) / p3, (-b - lambda * p2) / p4));
            for(float mu = mu1;mu <= mu2;mu++){
                canvas.drawCircle(p1*lambda + p3*mu + a,p2*lambda + p4*mu + b,POINT_RADIUS * scale,latticepaint);
            }
        }
    }
    private float MAX(float a,float b){
        return a > b ? a : b;
    }
    private float MIN(float a,float b){
        return a > b ? b : a;
    }
    public void moveCentre(float dx,float dy){
        this.a += dx;
        this.b += dy;
    }
    public float getCX(){
        return a;
    }
    public float getCY(){
        return b;
    }
    public void setCentre(float x,float y){
        this.a = x;
        this.b = y;
    }
    public void setScale(float scale){
        this.scale = scale;
    }
    public void rescale(float scaler,float x,float y){
        assert(scaler > 0);
        this.scale *= scaler;
        this.a += (this.a - x) * (scaler - 1);
        this.b += (this.b - y) * (scaler - 1);
    }
    public float getScale(){
        return this.scale;
    }
    public float[] getNearestPoint(float rx,float ry,float criticalRadius){
        float x = (rx - a) / scale;
        float y = (ry - b) / scale;
        int[] output = new int[]{Math.round((x * k4 - y * k3)/(k1 * k4 - k2 * k3)),Math.round((y * k1 - x * k2)/(k1 * k4 - k2 * k3))};
        float x1 = output[0] * this.k1 + output[1] * this.k3;
        float y1 = output[0] * this.k2 + output[1] * this.k4;
        if((x - x1) * (x - x1) + (y - y1) * (y - y1) <= criticalRadius * criticalRadius * scale * scale){
            return inverseTransform(output);
        }
        else return null;
    }
    public float[] getCurrentVector1(){
        return new float[]{k1,k2};
    }
    public float[] getCurrentVector2(){
        return new float[]{k3,k4};
    }
    public float[] inverseTransform(int[] coordinate){
        float[] V1 = this.getCurrentVector1();
        float[] V2 = this.getCurrentVector2();
        return new float[]{(coordinate[0] * V1[0] + coordinate[1] * V2[0]) * scale + a,(coordinate[0] * V1[1] + coordinate[1] * V2[1]) * scale + b};
    }
}
