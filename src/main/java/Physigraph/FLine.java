package Physigraph;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by cfy on 15-11-20.
 */
public class FLine implements Cloneable,Selectable{
    protected boolean isArc;
    protected float lineWidth;
    protected Path p = null;
    protected Path path_selector;
    protected Path path_selector_radiusIndicator;
    protected float selector_width;
    protected String Label;
    protected float LabelPosX;
    protected float LabelPosY;
    protected float LabelX;
    protected float LabelY;

    //this parametre only make sense when isArc = true
    protected float radius;
    protected float x1,y1,x2,y2;
    protected Paint mpaint;
    protected Paint labelPaint;
    //for topology only
    protected FVertex v1,v2;

    protected long Id;

    private static long IdCount = 0;
    public FLine(){
        this(0, 0, 0, 0);
    }
    public FLine(float x1,float y1,float x2,float y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.lineWidth = 3;
        this.radius = 0;
        this.isArc = false;
        this.mpaint = new Paint();
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setARGB(255, 0, 0, 0);
        mpaint.setStrokeWidth(lineWidth);
        mpaint.setAntiAlias(true);

        this.labelPaint = new Paint();
        labelPaint.setTextSize(40);
        labelPaint.setARGB(255, 0, 0, 0);
        labelPaint.setAntiAlias(true);

        selector_width = 12;

        this.Label = "";
        this.LabelPosX = 0;
        this.LabelPosY = 30;

        this.Id = IdCount++;
    }
    public Paint getPaint(){
        return mpaint;
    }
    public boolean IsArc(){
        return isArc;
    }
    public void Draw(Canvas canvas){
        if(p == null){
            GeneratePath();
        }
        canvas.drawPath(this.p,mpaint);

       // canvas.drawText(Label,LabelX,LabelY,labelPaint);
    }
    protected void GeneratePath(){
        p = new Path();
        if(!isArc) {
            p.moveTo(x1, y1);
            p.lineTo(x2, y2);
        }
        else{
            float length = distance(x1,y1,x2,y2);
            float radius = (float)Math.sqrt(this.radius * this.radius + length * length / 4);
            float t1x = -(y2 - y1) / length;
            float t1y = (x2 - x1) / length;
            float centreX = (x1 + x2) / 2 + t1x * this.radius;
            float centreY = (y1 + y2) / 2 + t1y * this.radius;
            float vectorX = x1 - centreX;
            float vectorY = y1 - centreY;
            float mtheta = (float)Math.PI * 2 - 2 * (float)Math.atan2(length / 2,this.radius);
            p.moveTo(x1, y1);
            drawArc(p, centreX, centreY, vectorX, vectorY, mtheta, (int)Math.ceil(radius * mtheta / 10), false);
        }
    }
    protected void updateLabelPos(){
        float distance = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        float k1x = (x2 - x1) / distance;
        float k1y = (y2 - y1) / distance;
        float t1x = -(y2 - y1) / distance;
        float t1y = (x2 - x1) / distance;
        this.LabelX = LabelPosX * k1x + LabelPosY * t1x + (x2 + x1) / 2;
        this.LabelY = LabelPosY * k1y + LabelPosY * t1y + (y2 + y1) / 2;
    }
    protected void GenerateSelectorPath(){
        path_selector = new Path();
        path_selector_radiusIndicator = new Path();
        float r = selector_width;
        if(!isArc){
            float distance = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
            float t1x = -(y2 - y1) / distance;
            float t1y = (x2 - x1) / distance;
            path_selector.moveTo(x1 + t1x * r, y1 + t1y * r);
            path_selector.lineTo(x2 + t1x * r, y2 + t1y * r);
            for(float i = 0;i <= 1;i += 0.1){
                float theta = (float)Math.PI * i;
                path_selector.lineTo(x2 + r * t1x * (float)Math.cos(theta) + r * t1y * (float)Math.sin(theta),y2 - r * t1x * (float)Math.sin(theta) + r * t1y * (float)Math.cos(theta));
            }
            path_selector.lineTo(x2 - t1x * r, y2 - t1y * r);
            path_selector.lineTo(x1 - t1x * r, y1 - t1y * r);
            for(float i = 0;i <= 1;i += 0.1){
                float theta = (float)Math.PI * i;
                path_selector.lineTo(x1 - r * t1x * (float)Math.cos(theta) - r * t1y * (float)Math.sin(theta),y1 + r * t1x * (float)Math.sin(theta) - r * t1y * (float)Math.cos(theta));
            }
            path_selector.lineTo(x1 + t1x * r, y1 + t1y * r);
        }
        else{
            float length = distance(x1,y1,x2,y2);
            float t1x = -(y2 - y1) / length;
            float t1y = (x2 - x1) / length;
            float centreX = (x1 + x2) / 2 + t1x * radius;
            float centreY = (y1 + y2) / 2 + t1y * radius;
            float vectorX = x1 - centreX;
            float vectorY = y1 - centreY;
            float uvectorX = vectorX / (float)Math.sqrt(radius * radius + length * length / 4);
            float uvectorY = vectorY / (float)Math.sqrt(radius * radius + length * length / 4);

            float mtheta = (float)Math.PI * 2 - 2 * (float)Math.atan2(length / 2,radius);

            float vectorX2 = x2 - centreX;
            float vectorY2 = y2 - centreY;
            float uvectorX2 = vectorX2 / (float)Math.sqrt(radius * radius + length * length / 4);
            float uvectorY2 = vectorY2 / (float)Math.sqrt(radius * radius + length * length / 4);

            path_selector.moveTo(centreX + vectorX - uvectorX * r, centreY + vectorY - uvectorY * r);
            drawArc(path_selector, centreX, centreY, vectorX - uvectorX * r, vectorY - uvectorY * r, mtheta, 40, false);
            drawArc(path_selector, x2, y2, -uvectorX2 * r, -uvectorY2 * r, (float) Math.PI, 20, true);
            drawArc(path_selector, centreX, centreY, vectorX2 + uvectorX2 * r, vectorY2 + uvectorY2 * r, mtheta, 40, true);
            drawArc(path_selector, x1, y1, uvectorX * r, uvectorY * r, (float) Math.PI, 20, true);

            dashedLineTo(path_selector_radiusIndicator, centreX, centreY, x1, y1, 10);
            dashedLineTo(path_selector_radiusIndicator, centreX, centreY, x2, y2, 10);
        }
    }
    public void setEndPoint(float[] p){
        this.x2 = p[0];
        this.y2 = p[1];
        refresh();
    }

    public void setEndPoint(float x,float y){
        this.x2 = x;
        this.y2 = y;
        refresh();
    }
    public void setStartPoint(float x,float y){
        this.x1 = x;
        this.y1 = y;
        refresh();
    }
    public void setStartVertex(FVertex v){
        this.v1 = v;
        this.x1 = v.x;
        this.y1 = v.y;
        if(!v.containsLine(this)){
            v.addLine(this);
        }
        refresh();
    }
    public void setEndVertex(FVertex v){
        this.v2 = v;
        this.x2 = v.x;
        this.y2 = v.y;
        if(!v.containsLine(this)){
            v.addLine(this);
        }
        refresh();
    }
    protected boolean Touched(float x,float y,float criticaldistance){
        float length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        float k1x = (x2 - x1) / length;
        float k1y = (y2 - y1) / length;
        float r1 = this.v1 == null ? 0 : v1.radius;
        float r2 = this.v2 == null ? 0 : v2.radius;
        if(!isArc){
            return ((x2 - x1) * (x - x1 - k1x * r1) + (y2 - y1) * (y - y1 - k1y * r1) >= 0 && (x2 - x1) * (x - x2 + k1x * r2) + (y2 - y1) * (y - y2 + k1y * r2) <=0 && Math.abs((y2 - y1) * (x - x1) - (x2 - x1) * (y - y1)) / distance(x1,y1,x2,y2) <= criticaldistance);
        }
        else{
            float radius = (float)Math.sqrt(this.radius * this.radius + length * length / 4);
            float t1x = -(y2 - y1) / length;
            float t1y = (x2 - x1) / length;
            float centreX = (x2 + x1) / 2 + this.radius * t1x;
            float centreY = (y2 + y1) / 2 + this.radius * t1y;
            float distance = distance(x,y,centreX,centreY);
            float epsilon1 = (float)Math.acos(1 - r1 * r1 / radius/radius / 2);
            float epsilon2 = (float)Math.acos(1 - r2 * r2 / radius/radius / 2);

            float vectorX = t1x * (float)Math.cos(epsilon2 - epsilon1) - t1y * (float)Math.sin(epsilon2 - epsilon1);
            float vectorY = t1x * (float)Math.sin(epsilon2 - epsilon1) + t1y * (float)Math.cos(epsilon2 - epsilon1);

            float cosine1 = (vectorX * (x - centreX) + vectorY * (y - centreY)) / distance;
            float cosine2 = -this.radius / radius * (float)Math.cos((epsilon1 + epsilon2) / 2) + length / 2 /radius * (float)Math.sin((epsilon1 + epsilon2) / 2);
            if(cosine1 >= cosine2){
                return Math.abs(distance - radius) <= criticaldistance;
            }
        }
        return false;
    }
    protected static float distance(float x1,float y1,float x2,float y2){
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float)Math.sqrt(dx*dx + dy*dy);
    }

    @Override
    public void DrawSelection(Canvas canvas) {
        Paint mp = new Paint();
        mp.setARGB(150,157,177,241);
        mp.setStyle(Paint.Style.FILL);
        mp.setStrokeWidth(5);
        mp.setAntiAlias(true);
        if(this.path_selector == null || this.path_selector_radiusIndicator == null){
            this.GenerateSelectorPath();
        }
        canvas.drawPath(path_selector,mp);
        mp.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path_selector_radiusIndicator,mp);
    }

    protected void drawArc(Path p,float cx,float cy,float vX,float vY,float endAngle,int segs,boolean clockwise){
        for(float i = 0;i <= segs;i++){
            float theta = i * endAngle / (float)segs;
            if(clockwise) theta = -theta;
            p.lineTo(cx + vX * (float)Math.cos(theta) + vY * (float)Math.sin(theta),cy - vX * (float)Math.sin(theta) + vY * (float)Math.cos(theta));
        }
    }

    protected void refresh(){
        this.p = null;
        this.path_selector = null;
        this.path_selector_radiusIndicator = null;
        this.updateLabelPos();
    }
    protected void dashedLineTo(Path p,float x1,float y1,float x2,float y2,int seg){
        for(float i = 0;i <= 2 * seg - 2;i += 2){
            p.moveTo(x1 + (i/2/seg) * (x2 - x1),y1 + (i/2/seg) * (y2 - y1));
            p.lineTo(x1 + ((i+1)/2/seg) * (x2 - x1),y1 + ((i+1)/2/seg) * (y2 - y1));
        }
    }
    public void setRadiusVector(float r){
        this.radius = r;
        this.isArc = true;
        refresh();
    }
    public float getRadius(){
        float distance = distance(x1,y1,x2,y2);
        return (float)Math.sqrt(radius * radius + distance * distance / 4);
    }
    public float getRadiusVector(){
        return this.radius;
    }
    public void ConvertToArc(){
        this.radius = 0;
        this.isArc = true;
        refresh();
    }

    public void ConvertToLine(){
        this.isArc = false;
        refresh();
    }

    protected void Delete(){
        if(this.v1 != null) v1.lines.remove(this);
        if(this.v2 != null) v2.lines.remove(this);
    }
    public float[] getCentre(){
        if(isArc){
            float distance = distance(x1,y1,x2,y2);
            float t1x = -(y2 - y1) / distance;
            float t1y = (x2 - x1) / distance;
            return new float[]{(x1 + x2) / 2 + t1x * radius,(y1 + y2) / 2 + t1y * radius};
        }
        else{
            return null;
        }
    }

    public float getMAngle(){
        float length = distance(x1,y1,x2,y2);
        return (float)Math.PI * 2 - 2 * (float)Math.atan2(length / 2,radius);
    }

    public void flip(){
        float x = this.x1;
        this.x1 = this.x2;
        this.x2 = x;
        float y = this.y1;
        this.y1 = this.y2;
        this.y2 = y;
        FVertex v = this.v1;
        this.v1 = this.v2;
        this.v2 = v;
        refresh();
    }

    @Override
    protected void finalize() throws Throwable {
        Log.d("destroctor","line destructed,id = " + Id);
        super.finalize();
        IdCount--;
    }

    public FVertex getStartVertex(){
        return v1;
    }
    public FVertex getEndVertex(){
        return v2;
    }

}