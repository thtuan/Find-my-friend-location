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

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.parse.GetDataStreamCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.thtuan.FindFriendLocation.Activity.Maps.view.MapsActivity;
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
        return user.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_friend,parent,false);
        }
        ImageView ivNavigation = (ImageView) convertView.findViewById(R.id.ivNavigation);
        ivNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.googleDirection.from(MyLocation.getLatLng()).to(user.get(position).getLocation())
                        .avoid(AvoidType.FERRIES)
                        .avoid(AvoidType.HIGHWAYS)
                        .alternativeRoute(true)
                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction, String rawBody) {
                                if (direction.isOK()){
                                    
                                }
                                else {

                                }
                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {

                            }
                        });
            }
        });
        TextView tvName = (TextView) convertView.findViewById(R.id.tvFriendName);
        TextView tvDetail = (TextView) convertView.findViewById(R.id.tvFriendDetail);
        TextView tvUpdate = (TextView) convertView.findViewById(R.id.tvFriendUpdate);
        final ImageView imAvatar = (ImageView) convertView.findViewById(R.id.ivListFriendAvatar);
        tvName.setText(user.get(position).getName());
        tvDetail.setText(user.get(position).getPhone());
        tvUpdate.setText(user.get(position).getLastUpdate());
        try {
            imAvatar.setImageBitmap(user.get(position).getAvatar());
        } catch (Exception e) {
            imAvatar.setImageResource(android.R.drawable.ic_menu_info_details);
        }
        return convertView;
    }
}
