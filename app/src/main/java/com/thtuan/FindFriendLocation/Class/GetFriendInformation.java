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
    public static ArrayList listName;
    ListFriendAdapter adapter;
    ListView listView;
    ArrayList<Marker> lsMarker;
    ArrayList<LatLng> latLngs;
    LatLng latlng;
    ParseGeoPoint geoPoint;
    ParseUser myUser;
    int pos = -1;
    public GetFriendInformation(){
        this.map = MapsActivity.mMap;
        myUser = ParseUser.getCurrentUser();

        listView = (ListView) MapsActivity.mContext.findViewById(R.id.lvFriend);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(position),12));
                lsMarker.get(position).showInfoWindow();
                pos = position;
            }
        });
    }

    public void getInfor(String groupName) {
        if(groupName!=null){
            ParseQuery<ParseObject> queryObject = ParseQuery.getQuery("GroupData");
            queryObject.whereEqualTo("groupName",groupName);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("DataUser");
            query.whereMatchesKeyInQuery("alias","alias", queryObject);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null) {
                        listName = new ArrayList();
                        lsMarker = new ArrayList<Marker>();
                        latLngs = new ArrayList<LatLng>();
                        map.clear();
                        for (int i = 0; i < list.size(); i++) {
                            final ParseObject obj = list.get(i);
                            geoPoint = obj.getParseGeoPoint("location");
                            latlng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                            lsMarker.add(map.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                    .title(obj.getString("alias"))
                                    .snippet("đang ở đây")));
                            listName.add(obj.getString("alias"));
                            latLngs.add(latlng);
                        }
                        if(pos!=-1){
                            lsMarker.get(pos).showInfoWindow();
                        }
                        adapter = new ListFriendAdapter(MapsActivity.mContext1, listName);
                        listView.setAdapter(adapter);
                    } else {
                    }
                }
            });
        }
        else{
            listName = new ArrayList();
            adapter = new ListFriendAdapter(MapsActivity.mContext1, listName);
            listView.setAdapter(adapter);
        }
    }
    public void setInfor(final LatLng latlng){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("DataUser");
        query.whereEqualTo("alias", myUser.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ParseGeoPoint geoPoint = new ParseGeoPoint(latlng.latitude,latlng.longitude);
                    if (list.size() == 0) {
                        ParseObject object = new ParseObject("DataUser");
                        object.put("location", geoPoint );
                        object.put("alias",myUser.getUsername());
                        object.saveInBackground();

                    }
                    else {
                        list.get(0).put("location",geoPoint);
                        list.get(0).saveInBackground();
                    }
                } else {
                    Toast.makeText(MapsActivity.mContext1, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    public void setInfor(final double latitude, final double longitude){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("DataUser");
        query.whereEqualTo("alias", myUser.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ParseGeoPoint geoPoint = new ParseGeoPoint(latitude,longitude);
                    if (list.size() == 0) {
                        ParseObject object = new ParseObject("DataUser");
                        object.put("location", geoPoint );
                        object.put("alias",myUser.getUsername());
                        object.saveInBackground();

                    }
                    else {
                        list.get(0).put("location",geoPoint);
                        list.get(0).saveInBackground();
                    }
                } else {
                    Toast.makeText(MapsActivity.mContext1, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



}
