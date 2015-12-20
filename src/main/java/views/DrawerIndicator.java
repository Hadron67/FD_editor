package views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;

/**
 * Created by cfy on 15-12-18.
 */
public class DrawerIndicator extends Drawable implements DrawerLayout.DrawerListener {

    private Paint mPaint = null;
    private boolean opening = true;
    private float L1Length = 0.4f;
    private float LineDistance = 0.1f;
    private float squareWidth = 0.25f;
    private float rate = 0;
    private float lineWidth = 5;

    public DrawerIndicator(){
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        rate = slideOffset;
        if(slideOffset == 1) opening = false;
        if(slideOffset == 0) opening = true;
        invalidateSelf();
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        Log.d("drawer state changed",Integer.toString(newState));
    }

    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getWidth();
        float height = canvas.getHeight();

        float l1length = width * L1Length * (1 - rate) + (squareWidth * width - lineWidth / 2) * rate * (float)Math.sqrt(2);

        float phase = (opening ? rate : (1 - rate)) * (float)Math.PI + (opening ? 0 : (float)Math.PI);

        canvas.drawLine(width / 2 + l1length / 2 * (float)Math.cos(phase),height / 2 + l1length / 2 * (float)Math.sin(phase),width / 2 - l1length / 2 * (float)Math.cos(phase),height / 2 - l1length / 2 * (float)Math.sin(phase),mPaint);


        float l2length = width * L1Length * (1 - rate) + (squareWidth * width + lineWidth / 2) * rate;
        float radius = LineDistance * height * (1 - rate) + squareWidth * width / 2 * rate;

        if(opening){
            float angle1 = (float)Math.PI * 5 / 4 * rate;
            float angle2 = (float)Math.PI * 3 / 4 * rate;
            float centre1X = width / 2 + radius * (float)Math.sin(angle1);
            float centre1Y = height / 2 - radius * (float)Math.cos(angle1);
            float centre2X = width / 2 - radius * (float)Math.sin(angle2);
            float centre2Y = height / 2 + radius * (float)Math.cos(angle2);
            canvas.drawLine(centre1X + l2length / 2 * (float)Math.cos(angle1),centre1Y + l2length / 2 * (float)Math.sin(angle1),centre1X - l2length / 2 * (float)Math.cos(angle1),centre1Y - l2length / 2 * (float)Math.sin(angle1),mPaint);
            canvas.drawLine(centre2X + l2length / 2 * (float)Math.cos(angle2),centre2Y + l2length / 2 * (float)Math.sin(angle2),centre2X - l2length / 2 * (float)Math.cos(angle2),centre2Y - l2length / 2 * (float)Math.sin(angle2),mPaint);
        }
        else{
            float angle2 = (float)Math.PI * 5 / 4 * (1 - rate) + (float)Math.PI * 3 / 4;
            float angle1 = (float)Math.PI * 3 / 4 * (1 - rate) + (float)Math.PI * 5 / 4;
            float centre1X = width / 2 + radius * (float)Math.sin(angle1);
            float centre1Y = height / 2 - radius * (float)Math.cos(angle1);
            float centre2X = width / 2 - radius * (float)Math.sin(angle2);
            float centre2Y = height / 2 + radius * (float)Math.cos(angle2);
            canvas.drawLine(centre1X + l2length / 2 * (float)Math.cos(angle1),centre1Y + l2length / 2 * (float)Math.sin(angle1),centre1X - l2length / 2 * (float)Math.cos(angle1),centre1Y - l2length / 2 * (float)Math.sin(angle1),mPaint);
            canvas.drawLine(centre2X + l2length / 2 * (float)Math.cos(angle2),centre2Y + l2length / 2 * (float)Math.sin(angle2),centre2X - l2length / 2 * (float)Math.cos(angle2),centre2Y - l2length / 2 * (float)Math.sin(angle2),mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
