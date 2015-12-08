package com.cfy.project2;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import FCanvas.BasicCommand;
import FCanvas.FeynmanCanvas;
import FCanvas.OnEditListener;
import Physigraph.Diagram;
import Views.ExportImageDialogue;
import Views.ToolBox;
import Views.ToolButton;


public class DiagramEditActivity extends Activity {
    private FeynmanCanvas sketch = null;
    private ToolButton btn_tool = null;
    private MenuItem undobutton = null;
    private MenuItem redobutton = null;

    private ToolBox tb = null;

    private ActionMode.Callback selectareacallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_main_export_image,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_editdiagram);
        initActionBar(getActionBar());
        redobutton = (MenuItem) findViewById(R.id.command_redo);
        undobutton = (MenuItem) findViewById(R.id.command_undo);
        this.btn_tool = (ToolButton) $(R.id.btn_tool);
        this.sketch = (FeynmanCanvas) $(R.id.sketch);

        tb = new ToolBox(this);
        tb.setFocusable(true);


        sketch.setOnEditListener(new OnEditListener() {
            @Override
            public void onEdit(BasicCommand cmd) {
                updateButtonStatus();
            }
        });
        btn_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tb.showAsDropDown(btn_tool,-tb.getWidth(),-tb.getHeight());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initActionBar(ActionBar ab){
        if(ab != null) {
            ab.setTitle(getResources().getString(R.string.empty));
            ab.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 60, 179, 113)));
        }
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
            case R.id.mainaction_share:
                startActionMode(selectareacallback);
                break;
        }
        updateButtonStatus();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public View $(int id){
        return findViewById(id);
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

    public void selectToSelectMode(View v){
        sketch.setEditType(FeynmanCanvas.EditType.SELECT);
        btn_tool.setShape(ToolButton.ButtonShape.SELECT);
        tb.dismiss();
    }
}