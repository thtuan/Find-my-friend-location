package com.thtuan.FindFriendLocation.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataStreamCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.thtuan.FindFriendLocation.R;

import java.io.InputStream;
import java.util.List;

/**
 * Created by ThanhTuan on 06-04-2016.
 */
public class ListFriendAdapter extends BaseAdapter {
    List<UserObject> user;
    LayoutInflater inflater;
    Context context;
    public static Bitmap resizeBitmap;
    public ListFriendAdapter(Context context, List<UserObject> user){
        inflater = LayoutInflater.from(context);
        this.user = user;
        this.context = context;
    }
    @Override
    public int getCount() {
        return user.size();
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
            convertView = inflater.inflate(R.layout.list_friend,parent,false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvFriendName);
        TextView tvDetail = (TextView) convertView.findViewById(R.id.tvFriendDetail);
        final ImageView imAvatar = (ImageView) convertView.findViewById(R.id.ivListFriendAvatar);
        tvName.setText(user.get(position).getName());
        tvDetail.setText(user.get(position).getPhone());
        try {
            imAvatar.setImageBitmap(user.get(position).getAvatar());
        } catch (Exception e) {
            imAvatar.setImageResource(android.R.drawable.ic_menu_info_details);
        }
        return convertView;
    }
}
