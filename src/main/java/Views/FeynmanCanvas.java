package Views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.cfy.project2.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Physigraph.ArrowedDashedLine;
import Physigraph.ArrowedDoubleLine;
import Physigraph.ArrowedLine;
import Physigraph.CounterVertex;
import Physigraph.DashedLine;
import Physigraph.Diagram;
import Physigraph.DoubleLine;
import Physigraph.FLine;
import Physigraph.FVertex;
import Physigraph.GluonLine;
import Physigraph.Lattice;
import Physigraph.LineRadiusSetter;
import Physigraph.PhotonLine;

/**
 * Created by cfy on 15-11-19.
 */
public class FeynmanCanvas extends View{
    private class MenuIndicator extends View{

        public MenuIndicator(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }
    }

    private View mindicator;

    private boolean refreshAllowed;
    private Timer limiter;
    private FrameLayout container;

    private boolean isSettingRadius = false;

    private Lattice drawingSketch;
    private FLine currentLine = null;
    private ArrayList<FLine> diagram = null;

    private FLine selectedLine = null;
    private FVertex selectedVertex = null;

    private LineRadiusSetter linesetter = null;

    private Diagram fdiagram;

    private LineType line_type = LineType.STRATE_LINE;
    private VertexType vertexType = VertexType.NORMAL;
    private EditType state = EditType.DRAW_LINE;

    private float touchX,touchY;

    private ScaleGestureDetector zoomDetector = null;
    public enum LineType {
        STRATE_LINE,ARROWED_STRATE_LINE,DASED_LINE,ARROWED_DASHED_LINE,PHOTON,GLUON,DOUBLE_LINE,ARROWED_DOUBLE_LINE
    }
    public enum VertexType{
        NORMAL,COUNTER
    }
    public enum EditType{
        DRAW_LINE,DRAW_VERTEX,SELECT,MOVE
    }
    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        private float lastscale, newscale;
        private float zoomx, zoomy;
        private boolean zooming;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            newscale = detector.getScaleFactor();
            drawingSketch.rescale(newscale,detector.getFocusX(), detector.getFocusY());
            fdiagram.rescale(newscale,detector.getFocusX(), detector.getFocusY());
            if (drawingSketch.getScale() > 2 || drawingSketch.getScale() < 0.5) {
                drawingSketch.rescale(1 / newscale, detector.getFocusX(), detector.getFocusY());
                fdiagram.rescale(1 / newscale,detector.getFocusX(), detector.getFocusY());
            }
            drawingSketch.moveCentre(detector.getFocusX() - zoomx, detector.getFocusY() - zoomy);
            fdiagram.moveOrigin(detector.getFocusX() - zoomx,detector.getFocusY() - zoomy);
            zoomx = detector.getFocusX();
            zoomy = detector.getFocusY();
            FeynmanCanvas.this.update();
            lastscale = newscale;
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            zooming = true;
            lastscale = newscale = 1;
            zoomx = detector.getFocusX();
            zoomy = detector.getFocusY();

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            zooming = false;
        }

    }
    public FeynmanCanvas(Context ctx,AttributeSet attr) {
        super(ctx, attr);
        this.mindicator = new View(ctx);

        this.zoomDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
        this.drawingSketch = new Lattice(250, 0, 125f, -125f * (float) Math.sqrt(3));
        this.drawingSketch.moveCentre(this.getWidth() / 2, this.getHeight() / 2);

        this.diagram = new ArrayList<>();
        this.fdiagram = new Diagram();
        this.fdiagram.moveOrigin(this.getWidth() / 2, this.getHeight() / 2);
        linesetter = new LineRadiusSetter(fdiagram);

        this.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                touchX = event.getX();
                touchY = event.getY();
                FeynmanCanvas.this.zoomDetector.onTouchEvent(event);
                if (FeynmanCanvas.this.zoomDetector.isInProgress()) return true;

                float[] coordinate = drawingSketch.getNearestPoint(event.getX(), event.getY(), Lattice.POINT_RADIUS * 2f);
                FVertex vertex = fdiagram.getNearestVertex(event.getX(), event.getY(), Lattice.POINT_RADIUS * 2f);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (linesetter.Touched(event.getX(), event.getY(), 50)) {
                            isSettingRadius = true;
                            return true;
                        }
                        switch (state) {
                            case DRAW_LINE:
                                if (vertex != null) {
                                    currentLine = getNewLine();
                                    currentLine.setStartVertex(vertex);
                                    fdiagram.addLine(currentLine);
                                    vertex.addLine(currentLine);
                                }
                                break;
                            case DRAW_VERTEX:
                                if (coordinate != null) {
                                    if (!fdiagram.addVertex(getNewVertex(coordinate[0], coordinate[1]))) {
                                        Toast.makeText(FeynmanCanvas.this.getContext(), "A vertex is already there.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                break;
                            case SELECT:
                                selectedLine = fdiagram.selectLine(event.getX(), event.getY(), Lattice.POINT_RADIUS * 2f);
                                linesetter.setLine(selectedLine);
                                if (selectedLine == null) {
                                    linesetter.setLine(null);
                                    selectedVertex = fdiagram.selectVertex(event.getX(), event.getY(), Lattice.POINT_RADIUS * 2f);
                                }
                                if (selectedLine == null && selectedVertex == null)
                                    fdiagram.Deselect();
                                break;
                        }


                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (currentLine != null) {
                            if (vertex != null) {
                                currentLine.setEndPoint(vertex.getX(), vertex.getY());
                            } else {
                                currentLine.setEndPoint(fdiagram.transform(event.getX(), event.getY()));
                            }
                        }
                        if (isSettingRadius) {
                            linesetter.setRadiusByCoodinates(event.getX(), event.getY());
                        }
                        FeynmanCanvas.this.update();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (vertex != null && currentLine != null) {
                            currentLine.setEndVertex(vertex);
                        }
                        if (vertex == null) {
                            fdiagram.removeLine(currentLine);
                        }
                        currentLine = null;
                        FeynmanCanvas.this.update();

                        isSettingRadius = false;
                        return false;
                    default:
                }
                return false;
            }
        });
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(container == null) return false;
                mindicator.setX(touchX);
                mindicator.setY(touchY);
                PopupMenu pm = new PopupMenu(mindicator.getContext(), FeynmanCanvas.this.mindicator);
                MenuInflater inflater = pm.getMenuInflater();
                if(selectedLine != null) {
                    inflater.inflate(R.menu.menu_line_operations, pm.getMenu());
                    if(selectedLine.IsArc()){
                        pm.getMenu().getItem(1).setTitle(getResources().getString(R.string.lineoperations_ConvertToline));
                    }
                    else{
                        pm.getMenu().getItem(1).setTitle(getResources().getString(R.string.lineoperations_ConvertToArc));
                    }
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.lineoperations_delete:
                                    fdiagram.DeleteLine(selectedLine);
                                    selectedLine = null;
                                    linesetter.setLine(null);
                                    break;
                                case R.id.lineoperations_convertion:
                                    if (!selectedLine.IsArc()) {
                                        selectedLine.ConvertToArc();
                                    } else {
                                        selectedLine.ConvertToLine();
                                    }
                                    break;
                                case R.id.lineoperations_flip:
                                    selectedLine.flip();
                                    break;
                            }
                            fdiagram.Deselect();
                            FeynmanCanvas.this.update();
                            return true;
                        }
                    });
                }
                else if(selectedVertex != null){
                    inflater.inflate(R.menu.menu_vertex_operations,pm.getMenu());
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.vertexoperations_delete:
                                    fdiagram.DeleteVertex(selectedVertex);
                                    selectedVertex = null;
                                    break;
                            }
                            fdiagram.Deselect();
                            FeynmanCanvas.this.update();
                            return true;
                        }
                    });
                }
                pm.show();
                return false;
            }
        });
        refreshAllowed = true;
        this.limiter = new Timer();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.container = (FrameLayout)this.getParent();
        if(container != null) {
            container.addView(mindicator);
        }
    }

    public void onDraw(Canvas canvas){

        if(state == EditType.DRAW_VERTEX) {
            this.drawingSketch.draw(canvas);
        }
        if(linesetter.hasLine()){
            linesetter.Draw(canvas);
        }
        this.fdiagram.Draw(canvas);

    }
    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){}
            this.postInvalidate();
        }
    }

    private FLine getNewLine(){
        switch (this.line_type){
            case STRATE_LINE: return new FLine();
            case PHOTON:return new PhotonLine();
            case ARROWED_STRATE_LINE:return new ArrowedLine();
            case GLUON:return new GluonLine();
            case DASED_LINE:return new DashedLine();
            case ARROWED_DASHED_LINE:return new ArrowedDashedLine();
            case DOUBLE_LINE:return new DoubleLine();
            case ARROWED_DOUBLE_LINE:return new ArrowedDoubleLine();
            default:
        }
        return null;
    }
    private FVertex getNewVertex(float x,float y){
        switch (vertexType){
            case NORMAL:return new FVertex(fdiagram.transform(x,y));
            case COUNTER:return new CounterVertex(fdiagram.transform(x,y));
        }
        return null;
    }
    public void setLineType(LineType type){
        this.line_type = type;
    }
    public void setVertexType(VertexType type){
        this.vertexType = type;
    }
    public void setEditType(EditType type){
        this.state = type;
    }
    public void update(){
        if(!refreshAllowed) return;
        refreshAllowed = false;
        this.postInvalidate();
        this.limiter.schedule(new TimerTask() {
            @Override
            public void run() {
                refreshAllowed = true;
            }
        },20);
    }
}
