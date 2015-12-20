package com.cfy.project2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfy.project2.R;

/**
 * Created by cfy on 15-12-19.
 */
public class SlidingListAdapter extends BaseAdapter{
    public class ViewHolder{
        ImageView img;
        TextView text;
    }

    private static int[] icons = new int[]{R.mipmap.ic_feynman_rule,R.mipmap.ic_documentations,R.mipmap.ic_about};
    private static int[] names = new int[]{R.string.text_feynman_rule,R.string.text_documentations,R.string.text_about};
    private Context ctx;
    public SlidingListAdapter(Context ctx){
        this.ctx = ctx;
    }



    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return names[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_sliding_list_item,null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.image_ic);
            holder.text = (TextView) convertView.findViewById(R.id.textview_name);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(names[position]);
        holder.img.setImageResource(icons[position]);
        return convertView;
    }
}
