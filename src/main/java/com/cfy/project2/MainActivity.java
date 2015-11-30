package com.cfy.project2;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import Views.FeynmanCanvas;
import Views.ToolButton;


public class MainActivity extends Activity {
    private FeynmanCanvas sketch = null;
    private Menu m;
    private ToolButton btn_tool = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.btn_tool = (ToolButton) $(R.id.btn_tool);
        this.sketch = (FeynmanCanvas) $(R.id.sketch);
        try {
            getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 60, 179, 113)));
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        btn_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_tool, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.tool_strateline:
                                sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
                                sketch.setLineType(FeynmanCanvas.LineType.STRATE_LINE);
                                btn_tool.setShape(ToolButton.ButtonShape.LINE);
                                break;
                            case R.id.tool_photon:
                                sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
                                sketch.setLineType(FeynmanCanvas.LineType.PHOTON);
                                btn_tool.setShape(ToolButton.ButtonShape.PHOTON);
                                break;
                            case R.id.tool_arrowedline:
                                sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
                                sketch.setLineType(FeynmanCanvas.LineType.ARROWED_STRATE_LINE);
                                btn_tool.setShape(ToolButton.ButtonShape.LINE_ARROW);
                                break;
                            case R.id.tool_normalvertex:
                                sketch.setEditType(FeynmanCanvas.EditType.DRAW_VERTEX);
                                sketch.setVertexType(FeynmanCanvas.VertexType.NORMAL);
                                btn_tool.setShape(ToolButton.ButtonShape.NORMALVERTEX);
                                break;
                            case R.id.tool_countervertex:
                                sketch.setEditType(FeynmanCanvas.EditType.DRAW_VERTEX);
                                sketch.setVertexType(FeynmanCanvas.VertexType.COUNTER);
                                btn_tool.setShape(ToolButton.ButtonShape.COUNTERVERTEX);
                                break;
                            case R.id.tool_select:
                                sketch.setEditType(FeynmanCanvas.EditType.SELECT);
                                btn_tool.setShape(ToolButton.ButtonShape.SELECT);
                                break;
                            case R.id.tool_gluon:
                                sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
                                sketch.setLineType(FeynmanCanvas.LineType.GLUON);
                                btn_tool.setShape(ToolButton.ButtonShape.GLUON);
                                break;
                            case R.id.tool_dashedline:
                                sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
                                sketch.setLineType(FeynmanCanvas.LineType.DASED_LINE);
                                btn_tool.setShape(ToolButton.ButtonShape.DASHED);
                                break;
                            case R.id.tool_arroweddashedline:
                                sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
                                sketch.setLineType(FeynmanCanvas.LineType.ARROWED_DASHED_LINE);
                                btn_tool.setShape(ToolButton.ButtonShape.DASHED_ARROW);
                                break;
                            case R.id.tool_doubleline:
                                sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
                                sketch.setLineType(FeynmanCanvas.LineType.DOUBLE_LINE);
                                btn_tool.setShape(ToolButton.ButtonShape.DOUBLELINE);
                                break;
                            case R.id.tool_arroweddoubleline:
                                sketch.setEditType(FeynmanCanvas.EditType.DRAW_LINE);
                                sketch.setLineType(FeynmanCanvas.LineType.ARROWED_DOUBLE_LINE);
                                btn_tool.setShape(ToolButton.ButtonShape.ARROWEDDOUBLELINE);
                        }
                        sketch.update();
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tool) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public View $(int id){
        return findViewById(id);
    }
}
