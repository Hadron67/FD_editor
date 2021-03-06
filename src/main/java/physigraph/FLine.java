package physigraph;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

/**
 * Created by cfy on 15-11-20.
 */
public class FLine implements Cloneable,Selectable{
    public enum LineShape{
        LINE,ARC,LOOP
    }
   // protected boolean isArc;
    protected LineShape shape;

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

    //when (x1,y1) = (x2,y2),these three parametres works.
    protected float arcVectorX = 0;
    protected float arcVectorY = 50;
    protected boolean ACW = false;

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

        shape = LineShape.LINE;
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
    private void checkShape(){
        if(v1 != null && v1 == v2){
            shape = LineShape.LOOP;
        }
    }
    public Paint getPaint(){
        return mpaint;
    }
    public boolean IsArc(){
        return shape == LineShape.ARC;
    }
    public boolean IsLine(){
        return shape == LineShape.LINE;
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

        switch(shape){
            case LINE:
                p.moveTo(x1, y1);
                p.lineTo(x2, y2);
                break;
            case ARC:
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
                break;
            case LOOP:
                p.addCircle(x1 + arcVectorX,y1 + arcVectorY, (float) Math.sqrt(arcVectorX * arcVectorX + arcVectorY * arcVectorY), Path.Direction.CW);
                break;
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
        switch(shape){
            case LINE:
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

                break;
            case ARC:
                float length = distance(x1,y1,x2,y2);
                t1x = -(y2 - y1) / length;
                t1y = (x2 - x1) / length;
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

                break;
            case LOOP:
                centreX = x1 + arcVectorX;
                centreY = y1 + arcVectorY;
                vectorX = x1 - centreX;
                vectorY = y1 - centreY;
                uvectorX = vectorX / (float)Math.sqrt(arcVectorX * arcVectorX + arcVectorY * arcVectorY);
                uvectorY = vectorY / (float)Math.sqrt(arcVectorX * arcVectorX + arcVectorY * arcVectorY);

                mtheta = (float)Math.PI * 2;

                vectorX2 = x2 - centreX;
                vectorY2 = y2 - centreY;
                uvectorX2 = vectorX2 / (float)Math.sqrt(arcVectorX * arcVectorX + arcVectorY * arcVectorY);
                uvectorY2 = vectorY2 / (float)Math.sqrt(arcVectorX * arcVectorX + arcVectorY * arcVectorY);

                path_selector.moveTo(centreX + vectorX - uvectorX * r, centreY + vectorY - uvectorY * r);
                drawArc(path_selector, centreX, centreY, vectorX - uvectorX * r, vectorY - uvectorY * r, mtheta, 40, false);
                drawArc(path_selector, x1, y1, -uvectorX2 * r, -uvectorY2 * r, (float) Math.PI, 20, true);
                drawArc(path_selector, centreX, centreY, vectorX2 + uvectorX2 * r, vectorY2 + uvectorY2 * r, mtheta, 40, true);
                drawArc(path_selector, x1, y1, uvectorX * r, uvectorY * r, (float) Math.PI, 20, true);

                dashedLineTo(path_selector_radiusIndicator, centreX, centreY, x1, y1, 10);
                break;
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

    public void setArcVector(float x,float y){
        arcVectorX = x;
        arcVectorY = y;
        refresh();
    }
    protected boolean Touched(float x,float y,float criticaldistance){
        float length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        float k1x = (x2 - x1) / length;
        float k1y = (y2 - y1) / length;
        float r1 = this.v1 == null ? 0 : v1.radius;
        float r2 = this.v2 == null ? 0 : v2.radius;

        switch(shape){
            case LINE:
                return ((x2 - x1) * (x - x1 - k1x * r1) + (y2 - y1) * (y - y1 - k1y * r1) >= 0 && (x2 - x1) * (x - x2 + k1x * r2) + (y2 - y1) * (y - y2 + k1y * r2) <=0 && Math.abs((y2 - y1) * (x - x1) - (x2 - x1) * (y - y1)) / distance(x1,y1,x2,y2) <= criticaldistance);
            case ARC:
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
                break;
            case LOOP:
                radius = (float)Math.sqrt(arcVectorX * arcVectorX + arcVectorY * arcVectorY);
                centreX = x1 + arcVectorX;
                centreY = y1 + arcVectorY;
                vectorX = x - centreX;
                vectorY = y - centreY;
                distance = distance(x,y,centreX,centreY);
                float cosine = (vectorX * arcVectorX + vectorY * arcVectorY) / radius / distance;
                if(cosine > -(1 - r1 * r1 / radius/radius / 2)){
                    return Math.abs(distance - radius) <= criticaldistance;
                }
                break;
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
        checkShape();
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
        shape = LineShape.ARC;
        this.radius = r;
        refresh();
    }
    public float getRadius(){
        if(shape != LineShape.LOOP) {
            float distance = distance(x1, y1, x2, y2);
            return (float) Math.sqrt(radius * radius + distance * distance / 4);
        }
        else{
            return (float) Math.sqrt(arcVectorX * arcVectorX + arcVectorY * arcVectorY);
        }
    }
    public float getRadiusVector(){
        return this.radius;
    }
    public void ConvertToArc(){
        if(shape == LineShape.LOOP) throw new IllegalStateException("shape convertion not allowed when the line is a loop.");
        this.radius = 0;
        shape = LineShape.ARC;
        refresh();
    }

    public void ConvertToLine(){
        if(shape == LineShape.LOOP) throw new IllegalStateException("shape convertion not allowed when the line is a loop.");
        shape = LineShape.LINE;
        refresh();
    }

    public boolean IsLoop(){
        return shape == LineShape.LOOP;
    }
    public float getRadiusVectorX(){
        if(shape != LineShape.LOOP) throw new IllegalStateException("not a loop");
        return arcVectorX;
    }
    public float getRadiusVectorY(){
        if(shape != LineShape.LOOP) throw new IllegalStateException("not a loop");
        return arcVectorY;
    }

    protected void Delete(){
        if(shape != LineShape.LOOP) {
            if (this.v1 != null) v1.lines.remove(this);
            if (this.v2 != null) v2.lines.remove(this);
        }
        else{
            if(v1 != null) v1.lines.remove(this);
        }
    }
    public float[] getCentre(){
        switch (shape){
            case LINE:return null;
            case ARC:
                float distance = distance(x1,y1,x2,y2);
                float t1x = -(y2 - y1) / distance;
                float t1y = (x2 - x1) / distance;
                return new float[]{(x1 + x2) / 2 + t1x * radius,(y1 + y2) / 2 + t1y * radius};
            case LOOP:return new float[]{x1 + arcVectorX,y1 + arcVectorY};
        }
        return null;
    }

    public float getMAngle(){
        if(shape != LineShape.LOOP) {
            float length = distance(x1, y1, x2, y2);
            return (float) Math.PI * 2 - 2 * (float) Math.atan2(length / 2, radius);
        }
        else{
            return 2 * (float) Math.PI;
        }
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

        ACW = !ACW;
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