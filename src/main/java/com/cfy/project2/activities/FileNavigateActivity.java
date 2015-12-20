package com.cfy.project2.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cfy.project2.adapter.FileListAdapter;
import com.cfy.project2.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by cfy on 15-12-11.
 */
public class FileNavigateActivity extends AppCompatActivity{

    private enum Type{
        SAVEFILE,OPENFILE
    }

    private Type type;
    private ListView filelist = null;
    private FileListAdapter fa = null;
    private EditText filename = null;
    private Button btn_enter = null;
    private TextView text_path = null;
    private Toolbar mToolbar = null;

    private String fileExtensionName;

    private String navigatingPath;

    private byte[] data = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDatas();
        this.setContentView(R.layout.layout_file_navigator);

        filelist = (ListView) findViewById(R.id.listview_filelist);
        filename = (EditText) findViewById(R.id.edittext_filename);
        btn_enter = (Button) findViewById(R.id.btn_enter);
        text_path = (TextView) findViewById(R.id.text_path);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        initActionbar();


        filename.setText("untitled." + fileExtensionName);
        filename.setSelection(0,8);

        final File path = getPath();
        navigatingPath = path.getAbsolutePath();
        text_path.setText(path.getAbsolutePath());
        fa = new FileListAdapter(this,path);
        filelist.setAdapter(fa);
        filelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File selected = (File) parent.getItemAtPosition(position);
                if(selected.isDirectory()){
                    fa.setPath(selected);
                    fa.notifyDataSetChanged();
                    navigatingPath = selected.getAbsolutePath();
                    text_path.setText(selected.getAbsolutePath());
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
                        final String name = filename.getText().toString();
                        final File f = new File(navigatingPath + "/" + name);
                        if(name.equals("")){
                            Toast.makeText(FileNavigateActivity.this,getString(R.string.enter_fille_name),Toast.LENGTH_SHORT).show();
                        }
                        else if(f.exists()){
                            new AlertDialog.Builder(FileNavigateActivity.this)
                                    .setMessage(FileNavigateActivity.this.getString(R.string.hint_when_file_exists))
                                    .setPositiveButton(FileNavigateActivity.this.getString(R.string.text_enter), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        saveData(name);
                                        Toast.makeText(FileNavigateActivity.this, getString(R.string.hint_file_saved) + "\n" + name, Toast.LENGTH_SHORT).show();
                                        finish();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(FileNavigateActivity.this,getString(R.string.hint_file_save_failed),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).setNegativeButton(FileNavigateActivity.this.getString(R.string.text_cancel),null).show();
                        }
                        else{
                            try {
                                if(f.createNewFile()){
                                    saveData(name);
                                    Toast.makeText(FileNavigateActivity.this, getString(R.string.hint_file_saved) + "\n" + name, Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(FileNavigateActivity.this,getString(R.string.hint_file_save_failed),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                break;
        }
    }

    private void initActionbar(){
        android.support.v7.app.ActionBar a = getSupportActionBar();
        a.setHomeButtonEnabled(true);
        a.setDisplayHomeAsUpEnabled(true);
        switch (type){
            case SAVEFILE:
                a.setTitle(R.string.save_file);
                break;
            case OPENFILE:
                a.setTitle(R.string.open_file);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
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

        return new File(p);
    }

    private void getDatas(){
        Intent intent = getIntent();
        boolean savefile = intent.getBooleanExtra("saveFile", true);
        type = savefile ? Type.SAVEFILE : Type.OPENFILE;
        data = intent.getByteArrayExtra("Data");
        fileExtensionName = intent.getStringExtra("extensionName");
    }

    private void initActionBar(){
        ActionBar ab = getActionBar();
        if(ab == null) return;
        ab = getActionBar();
        ab.setTitle(getResources().getString(R.string.empty));
        ab.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 60, 179, 113)));
        ab.show();
    }

    private void saveData(String name) throws IOException {
        File file = new File(navigatingPath + "/" + name);
        OutputStream os = new FileOutputStream(file);
        os.write(data);
        os.close();
    }
}
