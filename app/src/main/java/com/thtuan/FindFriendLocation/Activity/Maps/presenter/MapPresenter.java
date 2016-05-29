package com.thtuan.FindFriendLocation.Activity.Maps.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thtuan.FindFriendLocation.Activity.Maps.model.MapModel;
import com.thtuan.FindFriendLocation.Activity.Maps.model.MapModelMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.view.MapMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.view.MapsActivity;
import com.thtuan.FindFriendLocation.Class.RoundedImageView;
import com.thtuan.FindFriendLocation.Class.UserObject;
import com.thtuan.FindFriendLocation.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanhtuan on 12-05-2016.
 */
public class MapPresenter implements MapPresenterMgr {
    private MapModelMgr mapModelMgr;
    private MapMgr mapMgr;
    private ParseGeoPoint geoPoint;
    private LatLng latLng;
    private ArrayList<LatLng> latLngs;
    private ArrayList<String> lstGroup;
    private Marker marker;
    private List<UserObject> lsUserObject;

    private double lastLat =0;
    private double lastLong = 0;
    private int sizeUser;
    private boolean isClickedMarker;
    private boolean isDataStream;
    private AsyncTask updateMap;
    private ProgressDialog progressDialog;
    public MapPresenter() {
        this.mapModelMgr = new MapModel(this);
        sizeUser = 0;
        lsUserObject = new ArrayList<>();
        isClickedMarker = false;
        latLngs = new ArrayList<>();
        isDataStream = false;
    }

    public MapPresenter(MapMgr mapMgr) {
        this.mapMgr = mapMgr;
        this.mapModelMgr = new MapModel(this);
        lsUserObject = new ArrayList<>();
        sizeUser = 0;
        isClickedMarker = false;
        latLngs = new ArrayList<>();
        isDataStream = false;
    }

    @Override
    public void moveCamera(int position) {
        MapsActivity.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(position),10));
    }

    @Override
    public void showInforFriend(Marker marker) {
        marker.showInfoWindow();
        isClickedMarker = true;
    }

    @Override
    public void setMapModel(double latitude, double longitude) {
        mapModelMgr.setInfor(latitude,longitude);
    }

    @Override
    public void loadGroup() {
        mapModelMgr.loadGroup(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> listGroup, ParseException e) {
                if(e==null){
                    lstGroup = new ArrayList<String>();
                    if(listGroup.size()!=0){

                        for (ParseObject obj : listGroup){
                            lstGroup.add(obj.getString("groupName"));
                        }
                        mapMgr.showGroupData(lstGroup);
                        showListFriend();
                    }
                    else{
                        lstGroup.add("Bạn chưa có nhóm");
                        mapMgr.showGroupData(lstGroup);
                    }
                }
                else {
                    mapMgr.showToast(e.getMessage());
                }
            }
        });
    }

    @Override
    public void loadInfor() {
        if(!lsUserObject.isEmpty()){
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("GroupData");
            parseQuery.whereEqualTo("groupName",MapsActivity.itemSelected).whereExists("captainGroup");
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null){
                        if (!list.isEmpty()){
                            showMapData(lsUserObject,list.get(0));
                        }
                    }

                }
            });
        }
        /*else {
            mapMgr.showToast("Không có thành viên trong nhóm");
        }*/
    }

    @Override
    public void showMapData(final List<UserObject> listUser,final ParseObject parseObject) {
        mapModelMgr.loadInfor(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if (!objects.isEmpty()){
                        if(isDataStream){
                            MapsActivity.mMap.clear();
                            for (int i = 0; i < objects.size(); i++){
                                ParseObject obj = objects.get(i);
                                View marker = ((LayoutInflater) MapsActivity.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                        .inflate(R.layout
                                                .custom_marker, null);
                                ImageView imageView = (ImageView) marker.findViewById(R.id.ivMarker);
                                imageView.setImageBitmap(listUser.get(i).getAvatar());
                                geoPoint = obj.getParseGeoPoint("location");
                                latLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                                if (listUser.get(i).getName().equals(parseObject.getString("captainGroup"))) {
                                    MarkerOptions captain = new MarkerOptions()
                                            .position(latLng)
                                            .title(listUser.get(i).getName() + "(Đội trưởng)")
                                            .icon(BitmapDescriptorFactory.fromBitmap(RoundedImageView.createDrawableFromView
                                                    (MapsActivity.mContext1,marker)))
                                            .snippet("Cập nhật lúc: "+obj.getString("update"))
                                            .anchor(0.5f,1);
                                    MapsActivity.mMap.addMarker(captain);
                                }
                                else {
                                    MapsActivity.mMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .title(listUser.get(i).getName())
                                            .icon(BitmapDescriptorFactory.fromBitmap(RoundedImageView.createDrawableFromView
                                                    (MapsActivity.mContext1,marker)))
                                            .snippet("Cập nhật lúc: "+obj.getString("update"))
                                            .anchor(0.5f,1));
                                }
                            }
                        }

                    }
                }
            }
        });



    }

    @Override
    public void showListFriend() {
        mapModelMgr.loadInfor(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> listUser, ParseException e) {
                if (e == null) {
                    if (!listUser.isEmpty()) {
                        updateMap = new AsyncTask<List<ParseObject>, Void, List<UserObject>>(){
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog = new ProgressDialog(MapsActivity.mContext1);
                                progressDialog.setTitle("Đang tải danh sách bạn bè");
                                progressDialog.setMessage("Vui lòng đợi trong giây lát");
                                progressDialog.setCancelable(true);
                                progressDialog.show();
                                isDataStream = false;
                            }
                            @Override
                            protected List<UserObject> doInBackground(List<ParseObject>... params) {
                                lsUserObject = new ArrayList<>();
                                for (int i=0; i < params[0].size(); i++){
                                    UserObject user = new UserObject();
                                    ParseObject obj = params[0].get(i);
                                    geoPoint = obj.getParseGeoPoint("location");
                                    latLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                                    user.setLocation(latLng);
                                    try {
                                        Bitmap bitmap = RoundedImageView.getCroppedBitmap
                                                (BitmapFactory.decodeStream(obj.getParseFile("imageUser").getDataStream()), 80);
                                        user.setAvatar(bitmap);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    user.setName(obj.getString("alias"));
                                    user.setPhone(obj.getString("phone"));
                                    user.setLastUpdate(obj.getString("update"));
                                    user.setBirthday(obj.getString("birthday"));
                                    user.setAddr(obj.getString("address"));
                                    user.setContact(obj.getString("detail"));
                                    user.setCharacter(obj.getString("sex"));

                                    lsUserObject.add(user);
                                }
                                return lsUserObject;
                            }
                            @Override
                            protected void onPostExecute(List<UserObject> userObjects) {
                                super.onPostExecute(userObjects);
                                progressDialog.cancel();
                                    isDataStream = true;
                                    sizeUser = userObjects.size();
                                    mapMgr.showListFriend(lsUserObject);
                                    loadInfor();

                            }
                        }.execute(listUser);
                    }
                }
            }
        });
    }

}
