package com.thtuan.FindFriendLocation.Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thtuan.FindFriendLocation.R;

import java.util.ArrayList;

/**
 * Created by ThanhTuan on 06-04-2016.
 */
public class ListFriendAdapter extends BaseAdapter {
    ArrayList name;
    LayoutInflater inflater;
    public ListFriendAdapter(Context context, ArrayList name){
        inflater = LayoutInflater.from(context);
        this.name = name;
    }
    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_friend,null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvFriend);
        tvName.setText(name.get(position).toString());
        return convertView;
    }
}
