package Views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.cfy.project2.R;

/**
 * Created by cfy on 15-12-8.
 */
public class ExportImageDialogue extends Dialog{
    public ExportImageDialogue(Context context,Bitmap img) {
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.dialogue_layout_export_image,null);
        ImageView imgview = (ImageView) v.findViewById(R.id.imgview_export_image);
        imgview.setImageBitmap(img);
        this.setContentView(v);
    }
}
