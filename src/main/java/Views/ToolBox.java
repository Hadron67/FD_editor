package Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cfy.project2.R;

import java.util.ArrayList;
import java.util.List;

import Physigraph.FLine;

/**
 * Created by cfy on 15-12-4.
 *
 */
public class ToolBox extends PopupWindow{

    public ToolBox(Context ctx){
        super(ctx, null, R.attr.popupWindowStyle, R.style.Theme_FDeditor_PopupWindow);
        ViewGroup vg = (ViewGroup) LayoutInflater.from(ctx).inflate(R.layout.layout_toolbox,null);
        super.setContentView(vg);
        setWidth(ctx.getResources().getDimensionPixelSize(R.dimen.toolbox_width));
        setHeight(ctx.getResources().getDimensionPixelSize(R.dimen.toolbox_height));
        setOutsideTouchable(true);

    }
}
