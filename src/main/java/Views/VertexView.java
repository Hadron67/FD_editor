package Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cfy.project2.R;

import Physigraph.CounterVertex;
import Physigraph.FVertex;

/**
 * Created by cfy on 15-12-7.
 */
public class VertexView extends View{

    private FVertex vertex;

    private static Paint mpaint = null;


    public VertexView(Context ctx,AttributeSet attrs){
        super(ctx,attrs);

        if(mpaint == null){
            mpaint = new Paint();
            mpaint.setARGB(100,255,255,255);
            mpaint.setStyle(Paint.Style.FILL);
        }

        TypedArray ta = ctx.obtainStyledAttributes(attrs, R.styleable.VertexView,0,0);
        switch(ta.getInteger(R.styleable.VertexView_VertexType,0)){
            case 0:
                vertex = new FVertex();
                break;
            case 1:
                vertex = new CounterVertex();
                break;
        }
        vertex.getPaint().setColor(ta.getColor(R.styleable.VertexView_VertexColor, Color.BLACK));
        ta.recycle();

        vertex.getPaint().setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureHanlder(heightMeasureSpec);
        int width = measureHanlder(widthMeasureSpec);
        setMeasuredDimension(width,height);

        vertex.setPos(width / 2,height / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        vertex.Draw(canvas);
    }

    private int measureHanlder(int measureSpec){
        int result = 60;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(60, specSize);
        } else {
            result = 60;
        }
        return result;
    }
}
