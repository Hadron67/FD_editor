package views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.cfy.project2.R;

import physigraph.ArrowedDashedLine;
import physigraph.ArrowedDoubleLine;
import physigraph.ArrowedLine;
import physigraph.DashedLine;
import physigraph.DoubleLine;
import physigraph.FLine;
import physigraph.GluonLine;
import physigraph.PhotonLine;

/**
 * Created by cfy on 15-12-5.
 */
public class LineView extends View{
    private FLine line;

    private static Paint mpaint = null;

    public LineView(Context ctx,AttributeSet attrs){
        super(ctx,attrs);

        if(mpaint == null){
            mpaint = new Paint();
            mpaint.setStyle(Paint.Style.FILL);
            mpaint.setARGB(100,255,255,255);
        }

        TypedArray ta = ctx.obtainStyledAttributes(attrs, R.styleable.LineView,0,0);
        switch (ta.getInteger(R.styleable.LineView_LineType,0)){
            case 0:
                line = new FLine();
                break;
            case 1:
                line = new ArrowedLine();
                break;
            case 2:
                line = new DashedLine();
                break;
            case 3:
                line = new ArrowedDashedLine();
                break;
            case 4:
                line = new DoubleLine();
                break;
            case 5:
                line = new ArrowedDoubleLine();
                break;
            case 6:
                line = new PhotonLine();
                break;
            case 7:
                line = new GluonLine();
                break;
        }

        line.getPaint().setColor(ta.getColor(R.styleable.LineView_LineColor, Color.BLACK));
        ta.recycle();

    }

    public void setLine(FLine line){
        this.line = line;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureHanlder(heightMeasureSpec);
        int width = measureHanlder(widthMeasureSpec);
        setMeasuredDimension(width,height);


        if(line instanceof GluonLine){
            line.setEndPoint(width / 10, height / 2);
            line.setStartPoint(width * 9 / 10, height / 2);
        }
        else {
            line.setStartPoint(width / 10, height / 2);
            line.setEndPoint(width * 9 / 10, height / 2);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        line.Draw(canvas);
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
