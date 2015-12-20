package views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.cfy.project2.R;

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
