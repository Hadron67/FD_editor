package Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by cfy on 15-11-21.
 */
public class ToolButton extends View {

    private int background_color;
    private Paint linepaint;
    private Paint vertexpaint;
    private Paint textpaint;
    private ButtonShape shape_btn;

    private boolean disabled = false;
    //private Path path_photon,path_arrow;
    public enum ButtonShape{
        LINE,LINE_ARROW,DASHED,DASHED_ARROW,PHOTON,NORMALVERTEX,COUNTERVERTEX,GLUON,DOUBLELINE,ARROWEDDOUBLELINE,CHOOSE,SELECT
    }
    public ToolButton(Context ctx,AttributeSet attr){
        super(ctx,attr);
        this.background_color = Color.argb(200,255,0,50);
        this.shape_btn = ButtonShape.LINE;
        this.linepaint = new Paint();
        linepaint.setARGB(255,255,255,255);
        linepaint.setStyle(Paint.Style.STROKE);
        linepaint.setStrokeWidth(5);
        linepaint.setAntiAlias(true);
        vertexpaint = new Paint();
        vertexpaint.setARGB(255,255,255,255);
        vertexpaint.setStyle(Paint.Style.FILL);
        vertexpaint.setAntiAlias(true);
        textpaint = new Paint();
        textpaint.setStyle(Paint.Style.STROKE);
        textpaint.setARGB(255, 255, 255, 255);
        textpaint.setStrokeWidth(2);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(disabled){
                    return true;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        background_color = Color.argb(200, 255, 125, 50);
                        postInvalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        background_color = Color.argb(200, 255, 0, 50);
                        postInvalidate();
                }
                return false;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setStyle(Paint.Style.FILL);
        mpaint.setColor(background_color);


        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getHeight() / 2, mpaint);

        switch(this.shape_btn){
            case LINE:
                draw_line(canvas);
                break;
            case PHOTON:
                draw_photon(canvas);
                break;
            case LINE_ARROW:
                draw_arrow(canvas);
                break;
            case NORMALVERTEX:
                draw_normalvertex(canvas);
                break;
            case COUNTERVERTEX:
                draw_counter(canvas);
                break;
            case CHOOSE:
                draw_choose(canvas);
                break;
            case GLUON:
                draw_gluon(canvas);
                break;
            case DASHED:
                draw_dashedline(canvas);
                break;
            case DASHED_ARROW:
                draw_arroweddashedline(canvas);
                break;
            case DOUBLELINE:
                draw_doubleline(canvas);
                break;
            case ARROWEDDOUBLELINE:
                draw_arroweddoubleline(canvas);
                break;
            case SELECT:
                draw_select(canvas);
                break;
            default:;
        }

        super.onDraw(canvas);
    }
    protected void draw_line(Canvas canvas){
        canvas.drawLine(canvas.getWidth() / 4, canvas.getHeight() / 2, canvas.getWidth() * 3 / 4, canvas.getHeight() / 2, linepaint);
    }
    protected void draw_photon(Canvas canvas){
        Path p = new Path();
        p.moveTo(canvas.getWidth() / 4, canvas.getHeight() / 2);
        for(float i = 0;i <= 40;i++){
            p.lineTo((canvas.getWidth() * 3 / 4 - canvas.getWidth() / 4) * i/40 + canvas.getWidth() / 4,canvas.getHeight() / 2 + canvas.getHeight() /16 * (float)Math.sin((i/40) * Math.PI  * 8));
        }
        canvas.drawPath(p, linepaint);
    }
    protected void draw_arrow(Canvas canvas){
        canvas.drawLine(canvas.getWidth() / 4, canvas.getHeight() / 2, canvas.getWidth() * 3 / 4, canvas.getHeight() / 2, linepaint);
        canvas.drawLine(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2 - 20, canvas.getHeight() / 2 - 20, linepaint);
        canvas.drawLine(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2 - 20, canvas.getHeight() / 2 + 20, linepaint);
    }
    protected void draw_normalvertex(Canvas canvas){
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 20, vertexpaint);
    }
    protected void draw_counter(Canvas canvas){
        float r = 10 * (float)Math.sqrt(2);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 20, linepaint);
        canvas.drawLine(canvas.getWidth() / 2 - r, canvas.getHeight() / 2 - r,canvas.getWidth() / 2 + r, canvas.getHeight() / 2 + r,linepaint);
        canvas.drawLine(canvas.getWidth() / 2 + r, canvas.getHeight() / 2 - r, canvas.getWidth() / 2 - r, canvas.getHeight() / 2 + r, linepaint);
    }
    protected void draw_select(Canvas canvas){
        dashedLine(canvas, canvas.getWidth() / 4, canvas.getHeight() / 4, canvas.getWidth()*3 / 4, canvas.getHeight() / 4, 4);
        dashedLine(canvas, canvas.getWidth() * 3 / 4, canvas.getHeight() / 4, canvas.getWidth() * 3 / 4, canvas.getHeight() * 3 / 4, 4);
        dashedLine(canvas, canvas.getWidth() * 3 / 4, canvas.getHeight() * 3 / 4, canvas.getWidth() / 4, canvas.getHeight() * 3 / 4, 4);
        dashedLine(canvas, canvas.getWidth() / 4, canvas.getHeight() * 3 / 4, canvas.getWidth() / 4, canvas.getHeight() / 4, 4);

    }

    protected void draw_choose(Canvas canvas){
        float x1 = canvas.getWidth() / 4;
        float x2 = canvas.getWidth()*3 / 4;
        float y1 = canvas.getHeight()*3 / 8;
        float y2 = canvas.getHeight()*5 / 8;
        float r = canvas.getHeight() / 4;

        canvas.drawRect(x1, y1, x2, y2, linepaint);
    }
    protected void draw_gluon(Canvas canvas){
        Path p = new Path();
        float x1 = canvas.getWidth() / 4;
        float x2 = canvas.getWidth()*3 / 4;
        float y1 = canvas.getHeight() / 2;
        float y2 = y1;
        float r = canvas.getHeight() / 8;
        p.moveTo(x1, y1);
        float l = 40;
        for(float i = 0;i <= l;i++){
            float phase = 4 * (float)Math.PI * i/l;
            p.lineTo(x1 + (x2 - x1) * i/l + r * (float)Math.sin(phase),y1 + (y2 - y1) * i/l + r * (1 - (float)Math.cos(phase)));
        }
        canvas.drawPath(p, linepaint);
    }
    protected void draw_dashedline(Canvas canvas){
        dashedLine(canvas, canvas.getWidth() / 4, canvas.getHeight() / 2, canvas.getWidth() * 3 / 4, canvas.getHeight() / 2, 4);
    }
    protected void draw_arroweddashedline(Canvas canvas){
        dashedLine(canvas, canvas.getWidth() / 4, canvas.getHeight() / 2, canvas.getWidth()*3 / 4, canvas.getHeight() / 2, 4);
        canvas.drawLine(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2 - 20, canvas.getHeight() / 2 - 20, linepaint);
        canvas.drawLine(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2 - 20, canvas.getHeight() / 2 + 20, linepaint);
    }
    protected void draw_doubleline(Canvas canvas){
        canvas.drawLine(canvas.getWidth() / 4, canvas.getHeight() / 2 - 5, canvas.getWidth() * 3 / 4, canvas.getHeight() / 2 - 5, linepaint);
        canvas.drawLine(canvas.getWidth() / 4, canvas.getHeight() / 2 + 5, canvas.getWidth() * 3 / 4, canvas.getHeight() / 2 + 5, linepaint);
    }
    protected void draw_arroweddoubleline(Canvas canvas){
        canvas.drawLine(canvas.getWidth() / 4, canvas.getHeight() / 2 - 5, canvas.getWidth() * 3 / 4, canvas.getHeight() / 2 - 5, linepaint);
        canvas.drawLine(canvas.getWidth() / 4, canvas.getHeight() / 2 + 5, canvas.getWidth() * 3 / 4, canvas.getHeight() / 2 + 5, linepaint);
        canvas.drawLine(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2 - 20, canvas.getHeight() / 2 - 20, linepaint);
        canvas.drawLine(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2 - 20, canvas.getHeight() / 2 + 20, linepaint);
    }
    protected void dashedLine(Canvas canvas,float x1,float y1,float x2,float y2,int seg){
        for(float i = 0;i <= 2*seg - 2;i+=2){
            canvas.drawLine(x1 + (i/2/seg) * (x2 - x1),y1 + (i/2/seg) * (y2 - y1),x1 + ((i+1)/2/seg) * (x2 - x1),y1 + ((i+1)/2/seg) * (y2 - y1),linepaint);
        }
    }
    public void setShape(ButtonShape shape){
        this.shape_btn = shape;
        this.postInvalidate();
    }

    public void Disable(){
        disabled = true;
        background_color = Color.argb(200, 219, 164, 194);
        setClickable(false);
        postInvalidate();
    }

    public void Enable(){
        disabled = false;
        background_color = Color.argb(200, 255, 0, 50);
        setClickable(true);
        postInvalidate();
    }
    private void initPaths(){

    }
}
