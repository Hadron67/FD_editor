package Views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cfy.project2.DiagramEditActivity;
import com.cfy.project2.FileNavigateActivity;
import com.cfy.project2.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


/**
 * Created by cfy on 15-12-8.
 *
 */
public class ExportImageDialogue extends Dialog{

    private Bitmap img = null;

    private String savePath = null;

    public ExportImageDialogue(final Context context,Bitmap img,String filepath) {
        super(context,R.style.exportdialogue_style);
        this.img = img;
        savePath = filepath;
        View v = LayoutInflater.from(context).inflate(R.layout.dialogue_layout_export_image,null);
        ImageView imgview = (ImageView) v.findViewById(R.id.imgview_export_image);
        imgview.setImageBitmap(img);
        this.setContentView(v);
        v.findViewById(R.id.export_img_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        v.findViewById(R.id.export_img_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String name = savePath + java.util.UUID.randomUUID().toString() + ".jpg";
//                try {
//                    saveImage(new File(name));
//                    Toast.makeText(ExportImageDialogue.this.getContext(),"file\"" + name + "\" saved.",Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(ExportImageDialogue.this.getContext(),e.toString(),Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Intent intent = new Intent(context, FileNavigateActivity.class);
                intent.putExtra("path",savePath);
                intent.putExtra("saveFile",true);
                intent.putExtra("RequestCode", DiagramEditActivity.RESCODE_SAVEIMAGE);
                context.startActivity(intent);
                dismiss();
            }
        });

        v.findViewById(R.id.export_img_btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = savePath + "share.jpg";
                try {
                    File f = new File(name);
                    if(f.exists()) {
                        f.delete();
                    }
                    saveImage(f);
                    Toast.makeText(ExportImageDialogue.this.getContext(),"file\"" + name + "\" saved.",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ExportImageDialogue.this.getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(name));
                ExportImageDialogue.this.getContext().startActivity(intent);
                dismiss();
            }
        });
    }
    private void saveImage(File destfile) throws Exception{
        OutputStream os = new FileOutputStream(destfile);
        img.compress(Bitmap.CompressFormat.JPEG,100,os);
        os.close();
    }

}
