package com.thtuan.FindFriendLocation.Class;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thtuan.FindFriendLocation.Activity.Maps.MapsActivity;
import com.thtuan.FindFriendLocation.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanhTuan on 05-03-2016.
 */
public class GetFriendInformation {

    GoogleMap map;
    ArrayList listName;
    ListFriendAdapter adapter;
    ListView listView;
    ArrayList<Marker> lsMarker;
    ArrayList<LatLng> latLngs;
    LatLng latlng;
    ParseGeoPoint geoPoint;
    ParseUser myUser;
    public GetFriendInformation(){
        this.map = MapsActivity.mMap;
        myUser = ParseUser.getCurrentUser();
        lsMarker = new ArrayList<Marker>();
        latLngs = new ArrayList<LatLng>();
        listView = (ListView) MapsActivity.mContext.findViewById(R.id.lvFriend);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(position),12));
                lsMarker.get(position).showInfoWindow();
            }
        });
    }

    public void getInfor(String groupName) {
        if(groupName!=null){
            listName = new ArrayList();
            ParseQuery<ParseObject> queryObject = ParseQuery.getQuery("GroupData");
            queryObject.whereEqualTo("groupName",groupName);
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereMatchesKeyInQuery("username","alias", queryObject);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if (e == null) {
                        map.clear();
                        for (int i = 0; i < list.size(); i++) {
                            final ParseObject obj = list.get(i);
                            geoPoint = obj.getParseGeoPoint("Location");
                            latlng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                            lsMarker.add(map.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                    .title(obj.getString("username"))
                                    .snippet("đang ở đây")));
                            listName.add(obj.getString("username"));
                            latLngs.add(latlng);
                        }
                        adapter = new ListFriendAdapter(MapsActivity.mContext1, listName);
                        listView.setAdapter(adapter);
                    } else {
                    }
                }
            });
        }

    }
    public void setInfor(final LatLng latlng){

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", myUser.getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    ParseGeoPoint geoPoint = new ParseGeoPoint(latlng.latitude,latlng.longitude);
                    if (list.size() != 0) {
                        for (ParseObject obj : list
                                ) {
                            obj.put("Location", geoPoint );
                            obj.saveInBackground();
                        }
                    } else {
                        Toast.makeText(MapsActivity.mContext1, "Có lỗi gì đó" , Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MapsActivity.mContext1, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
