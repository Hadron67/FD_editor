package FCanvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.cfy.project2.R;

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

    private View mindicator;
    private OnEditListener editListener = null;
    private boolean refreshAllowed;
    private Timer limiter;
    private FrameLayout container;

    private boolean isSettingRadius = false;

    private int selectedSelectorAreaVertex = 0;

    private float oldradius;

    private Lattice drawingSketch;
    private FLine currentLine = null;

    private FLine selectedLine = null;
    private FVertex selectedVertex = null;

    private LineRadiusSetter linesetter = null;

    protected Diagram fdiagram;

    private AreaSelectorView areaselector = null;

    private LineType line_type = LineType.STRATE_LINE;
    private VertexType vertexType = VertexType.NORMAL;
    private EditType mode = EditType.DRAW_LINE;

    private float lastTouchX, lastTouchY;

    private CommandManager cmdmgr = null;

    private ScaleGestureDetector zoomDetector = null;
    public enum LineType {
        STRATE_LINE,ARROWED_STRATE_LINE,DASED_LINE,ARROWED_DASHED_LINE,PHOTON,GLUON,DOUBLE_LINE,ARROWED_DOUBLE_LINE
    }
    public enum VertexType{
        NORMAL,COUNTER
    }
    public enum EditType{
        DRAW_LINE,DRAW_VERTEX,CHOOSE,MOVE,SELECT_AREA
    }
    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        private float  newscale;
        private float zoomx, zoomy;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            newscale = detector.getScaleFactor();
            drawingSketch.rescale(newscale,detector.getFocusX(), detector.getFocusY());
            fdiagram.rescale(newscale, detector.getFocusX(), detector.getFocusY());
            if (drawingSketch.getScale() > 2 || drawingSketch.getScale() < 0.5) {
                drawingSketch.rescale(1 / newscale, detector.getFocusX(), detector.getFocusY());
                fdiagram.rescale(1 / newscale,detector.getFocusX(), detector.getFocusY());
            }
            drawingSketch.moveCentre(detector.getFocusX() - zoomx, detector.getFocusY() - zoomy);
            fdiagram.moveOrigin(detector.getFocusX() - zoomx,detector.getFocusY() - zoomy);
            zoomx = detector.getFocusX();
            zoomy = detector.getFocusY();
            FeynmanCanvas.this.update();
            //lastscale = newscale;
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            //zooming = true;
            //lastscale = newscale = 1;
            zoomx = detector.getFocusX();
            zoomy = detector.getFocusY();

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            //zooming = false;
        }

    }
    public FeynmanCanvas(Context ctx,AttributeSet attr) {
        super(ctx, attr);
        this.mindicator = new View(ctx);

        this.areaselector = new AreaSelectorView();
        areaselector.setCornerRadius(ctx.getResources().getDimensionPixelSize(R.dimen.areaselector_cornerradius));
        float defaultpadding = ctx.getResources().getDimensionPixelSize(R.dimen.areaselector_defaultpadding);
        float defaultpos = ctx.getResources().getDimensionPixelSize(R.dimen.areaselctor_defaultwidth);
        areaselector.setPos1(defaultpadding,defaultpadding);
        areaselector.setPos2(defaultpadding + defaultpos,defaultpadding + defaultpos);

        this.cmdmgr = new CommandManager(this);

        this.zoomDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
        this.drawingSketch = new Lattice(250, 0, 125f, -125f * (float) Math.sqrt(3));
        this.drawingSketch.moveCentre(this.getWidth() / 2, this.getHeight() / 2);

        this.fdiagram = new Diagram();
        this.fdiagram.moveOrigin(this.getWidth() / 2, this.getHeight() / 2);
        linesetter = new LineRadiusSetter(fdiagram);

        this.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                FeynmanCanvas.this.zoomDetector.onTouchEvent(event);
                if (FeynmanCanvas.this.zoomDetector.isInProgress()) return true;

                float[] coordinate = drawingSketch.getNearestPoint(event.getX(), event.getY(), Lattice.POINT_RADIUS * 2f);
                FVertex vertex = fdiagram.getNearestVertex(event.getX(), event.getY(), Lattice.POINT_RADIUS * 2f);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        switch (mode) {
                            case DRAW_LINE:
                                if (vertex != null) {
                                    currentLine = getNewLine();
                                    currentLine.setStartVertex(vertex);
                                    fdiagram.addLine(currentLine);
                                }
                                break;
                            case DRAW_VERTEX:
                                if (coordinate != null) {
                                    FVertex newvertex = getNewVertex(coordinate[0], coordinate[1]);
                                    if (!fdiagram.addVertex(newvertex)) {
                                        Toast.makeText(FeynmanCanvas.this.getContext(), "A vertex is already there.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        cmdmgr.add(new AddVertexCommand(newvertex));
                                    }
                                }
                                break;
                            case CHOOSE:
                                if (linesetter.Touched(event.getX(), event.getY(), 80)) {
                                    isSettingRadius = true;
                                    oldradius = selectedLine.getRadiusVector();
                                    return true;
                                }
                                selectedLine = fdiagram.selectLine(event.getX(), event.getY(), Lattice.POINT_RADIUS * 2f);
                                linesetter.setLine(selectedLine);
                                if (selectedLine == null) {
                                    linesetter.setLine(null);
                                    selectedVertex = fdiagram.selectVertex(event.getX(), event.getY(), Lattice.POINT_RADIUS * 2f);
                                }
                                if (selectedLine == null && selectedVertex == null)
                                    fdiagram.Deselect();
                                break;
                            case SELECT_AREA:
                                selectedSelectorAreaVertex = areaselector.hitTest(event.getX(),event.getY(),Lattice.POINT_RADIUS * 2f);
                                break;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        switch(mode) {
                            case DRAW_LINE:
                                if (currentLine != null) {
                                    if (vertex != null) {
                                        currentLine.setEndPoint(vertex.getX(), vertex.getY());
                                    } else {
                                        currentLine.setEndPoint(fdiagram.transform(event.getX(), event.getY()));
                                    }
                                }
                                FeynmanCanvas.this.update();
                                break;
                            case CHOOSE:
                                if (isSettingRadius) {
                                    linesetter.setRadiusByCoordinates(event.getX(), event.getY());
                                }
                                FeynmanCanvas.this.update();
                                break;
                            case SELECT_AREA:
                                areaselector.setVertexPos(selectedSelectorAreaVertex,event.getX(),event.getY());
                                FeynmanCanvas.this.update();
                                break;
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (vertex == null && currentLine != null) {
                            fdiagram.DeleteLine(currentLine);
                        } else if (vertex != null && currentLine != null) {
                            if(vertex == currentLine.getStartVertex()){
                                fdiagram.DeleteLine(currentLine);
                            }
                            else{
                                currentLine.setEndVertex(vertex);
                                cmdmgr.add(new AddLineCommand(currentLine));
                            }
                        }
                        currentLine = null;
                        FeynmanCanvas.this.update();
                        if (isSettingRadius) {
                            isSettingRadius = false;
                            cmdmgr.add(new SetLineRadiusCommand(selectedLine, oldradius));
                        }
                        selectedSelectorAreaVertex = 0;
                        return false;
                    default:
                }
                return false;
            }
        });
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (container == null) return false;
                mindicator.setX(lastTouchX);
                mindicator.setY(lastTouchY);
                if (selectedLine != null) {
                    getLineOperationsMenu().show();
                } else if (selectedVertex != null) {
                    getVertexOperationMenu().show();
                }
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

        if(mode == EditType.DRAW_VERTEX) {
            this.drawingSketch.draw(canvas);
        }
        if(linesetter.hasLine()){
            linesetter.Draw(canvas);
        }
        this.fdiagram.Draw(canvas);
        if(mode == EditType.SELECT_AREA){
            areaselector.Draw(canvas);
        }
    }

    protected void callOnEdit(BasicCommand cmd){
        if(this.editListener != null){
            editListener.onEdit(cmd);
        }
    }

    public void setOnEditListener(OnEditListener listener){
        this.editListener = listener;
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
        update();
    }
    public void setEditType(EditType type){
        this.mode = type;
        update();
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
        }, 20);
    }

    private PopupMenu getLineOperationsMenu(){
        PopupMenu pop = new PopupMenu(getContext(),mindicator);
        pop.getMenuInflater().inflate(R.menu.menu_line_operations, pop.getMenu());
        if(selectedLine.IsArc()){
            pop.getMenu().getItem(1).setTitle(getResources().getString(R.string.lineoperations_ConvertToline));
        }
        else{
            pop.getMenu().getItem(1).setTitle(getResources().getString(R.string.lineoperations_ConvertToArc));
        }
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.lineoperations_delete:
                        cmdmgr.Do(new DeleteLineCommand(selectedLine));
                        selectedLine = null;
                        linesetter.setLine(null);
                        fdiagram.Deselect();
                        break;
                    case R.id.lineoperations_convertion:
                        cmdmgr.Do(new ConvertLineCommand(selectedLine));
                        break;
                    case R.id.lineoperations_flip:
                        cmdmgr.Do(new FlipLineCommand(selectedLine));
                        break;
                    case R.id.splitlineoperation_2:
                        cmdmgr.Do(new SplitLineCommand(fdiagram, selectedLine, 2));
                        break;
                    case R.id.splitlineoperation_3:
                        cmdmgr.Do(new SplitLineCommand(fdiagram, selectedLine, 3));
                        break;
                    case R.id.splitlineoperation_4:
                        cmdmgr.Do(new SplitLineCommand(fdiagram, selectedLine, 4));
                        break;
                }
                FeynmanCanvas.this.update();
                return true;
            }
        });
        return pop;
    }

    private PopupMenu getVertexOperationMenu(){
        PopupMenu pop = new PopupMenu(getContext(),mindicator);
        pop.getMenuInflater().inflate(R.menu.menu_vertex_operations, pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.vertexoperations_delete:
                        cmdmgr.Do(new DeleteVertexCommand(selectedVertex));
                        selectedVertex = null;
                        fdiagram.Deselect();
                        break;
                }
                FeynmanCanvas.this.update();
                return true;
            }
        });
        return pop;
    }

    public void Undo(){
        cmdmgr.Undo();
        this.update();
    }
    public void Redo(){
        cmdmgr.Redo();
        this.update();
    }
    public boolean canUndo(){
        return cmdmgr.canUndo();
    }
    public boolean canRedo(){
        return cmdmgr.canRedo();
    }
    public void clear(){
        if(!fdiagram.IsEmpty()) {
            cmdmgr.Do(new ClearCommand(fdiagram));
            this.update();
        }
    }

    public float getCX(){
        return fdiagram.getCX();
    }

    public float getCY(){
        return fdiagram.getCY();
    }
    public void setOrigin(float x,float y){
        fdiagram.setOrigin(x,y);
        drawingSketch.setCentre(x,y);
    }
    public void setScale(float scale){
        fdiagram.setScale(scale);
        drawingSketch.setScale(scale);
    }
    public float getScale(){
        return fdiagram.getScale();
    }
    public void Deselect(){
        fdiagram.Deselect();
        linesetter.setLine(null);
    }

    public void setDiagram(Diagram diagram){
        this.fdiagram = diagram;
        linesetter = new LineRadiusSetter(diagram);
    }

    public Bitmap getSelectedImage(){
        Bitmap map = Bitmap.createBitmap((int)areaselector.getWidth(),(int)areaselector.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(map);
        canvas.drawColor(Color.WHITE);
        canvas.translate(-areaselector.getX1(),-areaselector.getY1());
        fdiagram.Draw(canvas);
        return map;
    }
}
