package com.cfy.project2.activities;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cfy.project2.R;
import com.cfy.project2.adapter.SlidingListAdapter;

import java.io.File;

import fcanvas.BasicCommand;
import fcanvas.FeynmanCanvas;
import fcanvas.OnEditListener;
import views.DrawerIndicator;
import views.ExportImageDialogue;
import views.HackyDrawerLayout;
import views.SlidingPaneLayout;
import views.ToolBox;
import views.ToolButton;


public class DiagramEditActivity extends AppCompatActivity implements OnItemClickListener{

    private FeynmanCanvas sketch = null;
    private ToolButton btn_tool = null;
    private MenuItem undobutton = null;
    private MenuItem redobutton = null;
    private ActionBarDrawerToggle mDrawerToggle = null;
    private HackyDrawerLayout mDrawerLayout = null;
    private SlidingPaneLayout mSlidingPane = null;
    private ListView mSlidingDrawer = null;
    private SlidingListAdapter DrawerAdapter = null;
    private ActionMode mActionMode = null;

    private DrawerIndicator mDrawerIndicator = null;

    private ToolBox tb = null;

    private Toolbar mToolbar = null;

    private String savePath = "";

    private ActionMode.Callback selectareacallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_main_export_image,menu);
            mActionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            switch(item.getItemId()){
                case R.id.action_select_export:
                    Bitmap diagram = sketch.getSelectedImage();
                    ExportImageDialogue dia = new ExportImageDialogue(DiagramEditActivity.this,diagram,savePath + "Images/");
                    dia.show();

                    break;
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            selectToStraightLine(null);
            mActionMode = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_editdiagram);
        mDrawerIndicator = new DrawerIndicator();
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        redobutton = (MenuItem) findViewById(R.id.command_redo);
        undobutton = (MenuItem) findViewById(R.id.command_undo);
        this.btn_tool = (ToolButton) $(R.id.btn_tool);
        this.sketch = (FeynmanCanvas) $(R.id.sketch);
        mDrawerLayout = (HackyDrawerLayout) findViewById(R.id.drawer_main);
        mSlidingDrawer = (ListView) findViewById(R.id.main_drawer);
        mSlidingPane = (SlidingPaneLayout) findViewById(R.id.Pane1);


        DrawerAdapter = new SlidingListAdapter(this);
        mSlidingDrawer.setAdapter(DrawerAdapter);
        mSlidingDrawer.setOnItemClickListener(this);



        setSupportActionBar(mToolbar);
        tb = new ToolBox(this);
        tb.setFocusable(true);

        initActionBar();
        sketch.setOnEditListener(new OnEditListener() {
            @Override
            public void onEdit(BasicCommand cmd) {
                updateButtonStatus();
            }
        });
        btn_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mActionMode != null){
                    mActionMode.finish();
                }
                else {
                    tb.showAsDropDown(btn_tool, -tb.getWidth(), -tb.getHeight());
                }
            }
        });

        initFiles();

//        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open,R.string.drawer_close){
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);
//                Log.d("drawer moved",Float.toString(slideOffset));
//            }
//        };
        mDrawerLayout.setDrawerListener(mDrawerIndicator);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initActionBar(){
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if(ab == null) return;

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setHomeAsUpIndicator(mDrawerIndicator);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        redobutton = menu.findItem(R.id.command_redo);
        undobutton = menu.findItem(R.id.command_undo);
        updateButtonStatus();
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateButtonStatus(){
        redobutton.setEnabled(sketch.canRedo());
        undobutton.setEnabled(sketch.canUndo());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case android.R.id.home:
                if(OpenDrawer()){
                    return true;
                }
                break;
            case R.id.command_undo:
                if(sketch.canUndo()){
                    sketch.Undo();
                }
                break;
            case R.id.command_redo:
                if(sketch.canRedo()){
                    sketch.Redo();
                }
                break;
            case R.id.command_clear:
                sketch.clear();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        updateButtonStatus();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public boolean OpenDrawer() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    public View $(int id){
        return findViewById(id);
    }

    private boolean initFiles(){
        boolean result = true;
        savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FDeditor/";
        File f = new File(savePath);
        if(!f.exists() || (f.exists() && !f.isDirectory())){
            result = f.mkdir();
        }
        f = new File(savePath + "Images/");
        result = result && f.mkdir();
        f = new File(savePath + "Diagrams/");
        result = result && f.mkdir();

        return result;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void selectToStraightLine(View v){
        sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
        sketch.setLineType(FeynmanCanvas.LineType.STRATE_LINE);
        btn_tool.setShape(ToolButton.ButtonShape.LINE);
        tb.dismiss();
    }

    public void selectToDashedLine(View v){
        sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
        sketch.setLineType(FeynmanCanvas.LineType.DASED_LINE);
        btn_tool.setShape(ToolButton.ButtonShape.DASHED);
        tb.dismiss();
    }

    public void selectToDoubleLine(View v){
        sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
        sketch.setLineType(FeynmanCanvas.LineType.DOUBLE_LINE);
        btn_tool.setShape(ToolButton.ButtonShape.DOUBLELINE);
        tb.dismiss();
    }

    public void selectToArrowedLine(View v){
        sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
        sketch.setLineType(FeynmanCanvas.LineType.ARROWED_STRATE_LINE);
        btn_tool.setShape(ToolButton.ButtonShape.LINE_ARROW);
        tb.dismiss();
    }

    public void selectToArrowedDashedLine(View v){
        sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
        sketch.setLineType(FeynmanCanvas.LineType.ARROWED_DASHED_LINE);
        btn_tool.setShape(ToolButton.ButtonShape.DASHED_ARROW);
        tb.dismiss();
    }

    public void selectToArrowedDoubleLine(View v){
        sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
        sketch.setLineType(FeynmanCanvas.LineType.ARROWED_DOUBLE_LINE);
        btn_tool.setShape(ToolButton.ButtonShape.ARROWEDDOUBLELINE);
        tb.dismiss();
    }

    public void selectToPhotonLine(View v){
        sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
        sketch.setLineType(FeynmanCanvas.LineType.PHOTON);
        btn_tool.setShape(ToolButton.ButtonShape.PHOTON);
        tb.dismiss();
    }

    public void selectToGluonLine(View v){
        sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
        sketch.setLineType(FeynmanCanvas.LineType.GLUON);
        btn_tool.setShape(ToolButton.ButtonShape.GLUON);
        tb.dismiss();
    }

    public void selectToNVertex(View v){
        sketch.setEditType(FeynmanCanvas.EditType.DRAW_VERTEX);
        sketch.setVertexType(FeynmanCanvas.VertexType.NORMAL);
        btn_tool.setShape(ToolButton.ButtonShape.NORMALVERTEX);
        tb.dismiss();
    }

    public void selectToCounterVertex(View v){
        sketch.setEditType(FeynmanCanvas.EditType.DRAW_VERTEX);
        sketch.setVertexType(FeynmanCanvas.VertexType.COUNTER);
        btn_tool.setShape(ToolButton.ButtonShape.COUNTERVERTEX);
        tb.dismiss();
    }

    public void selectToChooseMode(View v){
        sketch.setEditType(FeynmanCanvas.EditType.CHOOSE);
        btn_tool.setShape(ToolButton.ButtonShape.CHOOSE);
        tb.dismiss();
    }

    public void selectToSelectMode(View v){
        sketch.setEditType(FeynmanCanvas.EditType.SELECT_AREA);
        btn_tool.setShape(ToolButton.ButtonShape.SELECT);
        //btn_tool.Disable();
        startActionMode(selectareacallback);
        tb.dismiss();
    }
}
