package com.cfy.project2;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by cfy on 15-12-11.
 */
public class FileNavigateActivity extends Activity{

    public class FileAdapter extends BaseAdapter{

        public class SortFile implements Comparator{

            @Override
            public int compare(Object lhs, Object rhs) {
                File f1 = (File) lhs;
                File f2 = (File) rhs;
                String s1 = f1.getName();
                String s2 = f2.getName();
//                if(f1.isDirectory() && f2.isDirectory()){
//                    return s1.compareTo(s2);
//                }
//                else if(f1.isDirectory()){
//                    return 1;
//                }
//                else if(f2.isDirectory()){
//                    return 0;
//                }
//                else{
//                    return s1.compareTo(s2);
//                }
                return s1.compareTo(s2);
            }
        }

        private SortFile sorter = null;
        private ArrayList<File> paths;
        private Context ctx;
        private boolean hasParent = true;



        public FileAdapter(Context ctx,File path){
            this.ctx = ctx;
            setPath(path);
            sorter = new SortFile();
        }

        public void setPath(File path){
            File[] fs = path.listFiles();
            paths = new ArrayList<>();
            if(fs != null) {
                Collections.addAll(paths,fs);
            }

            Collections.sort(paths,sorter);

            File par = path.getParentFile();
            if(par != null){
                paths.add(0,par);
            }
            hasParent = par != null;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public Object getItem(int position) {
            return paths.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position + 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_filelistitem,null);
            }
            ImageView img = (ImageView) convertView.findViewById(R.id.imgview_file);
            TextView name = (TextView) convertView.findViewById(R.id.textview_filename);
            File f = paths.get(position);

            name.setText((hasParent && position == 0) ? ctx.getResources().getString(R.string.text_parentdirectory) : f.getName());
            if(f.isDirectory()){
                img.setImageResource(R.mipmap.ic_folder);
            }
            else if(f.isFile()){
                img.setImageResource(R.mipmap.ic_file);
            }
            return convertView;
        }
    };

    private enum Type{
        SAVEFILE,OPENFILE
    }

    private Type type;
    private ListView filelist = null;
    private FileAdapter fa = null;
    private EditText filename = null;
    private Button btn_enter = null;

    private String navigatingPath;

    private int rescode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_file_navigator);
        initActionBar();

        filelist = (ListView) findViewById(R.id.listview_filelist);
        filename = (EditText) findViewById(R.id.edittext_filename);
        btn_enter = (Button) findViewById(R.id.btn_enter);

        final File path = getPath();
        navigatingPath = path.getAbsolutePath();
        getActionBar().setTitle(path.getAbsolutePath());
        fa = new FileAdapter(this,path);
        filelist.setAdapter(fa);
        filelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File selected = (File) parent.getItemAtPosition(position);
                if(selected.isDirectory()){
                    fa.setPath(selected);
                    fa.notifyDataSetChanged();
                    navigatingPath = selected.getAbsolutePath();
                    FileNavigateActivity.this.getActionBar().setTitle(selected.getAbsolutePath());
                }
                else{
                    filename.setText(selected.getName());
                }
            }
        });

        switch (type){
            case SAVEFILE:
                btn_enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = filename.getText().toString();
                        final File f = new File(navigatingPath + "/" + name);
                        if(name.equals("")){
                            Toast.makeText(FileNavigateActivity.this,"enter file name",Toast.LENGTH_SHORT).show();
                        }
                        else if(f.exists()){
                            new AlertDialog.Builder(FileNavigateActivity.this)
                                    .setMessage(FileNavigateActivity.this.getString(R.string.hint_when_file_exists))
                                    .setPositiveButton(FileNavigateActivity.this.getString(R.string.text_enter), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Return(f.getAbsolutePath());
                                }
                            })
                                    .setNegativeButton(FileNavigateActivity.this.getString(R.string.text_cancel),null).show();
                        }
                        else{
                            Return(f.getAbsolutePath());
                        }
                    }
                });
                break;
        }


    }

    private File getPath(){
        Intent intent = getIntent();
        if(intent == null){
            throw new IllegalStateException("you must specify the file path in the intent.");
        }
        Bundle b = intent.getExtras();
        if(b == null){
            throw new IllegalStateException("you must specify the file path in the intent.");
        }
        String p = b.getString("path");
        if(p == null){
            throw new IllegalStateException("you must specify the file path in the intent.");
        }
        boolean savefile = b.getBoolean("saveFile");
        rescode = b.getInt("RequestCode");
        type = savefile ? Type.SAVEFILE : Type.OPENFILE;
        return new File(p);
    }

    private void initActionBar(){
        ActionBar ab = getActionBar();
        if(ab == null) return;
        ab = getActionBar();
        ab.setTitle(getResources().getString(R.string.empty));
        ab.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 60, 179, 113)));
        ab.show();
    }
    private void Return(String path){
        Intent intent = new Intent();
        intent.putExtra("filePath",path);
        setResult(rescode,intent);
        finish();
    }

}
