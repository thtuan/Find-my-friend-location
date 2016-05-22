package com.thtuan.FindFriendLocation.Activity.Maps.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
    private ArrayList<Marker> lsMarker;
    private List<UserObject> lsUserObject;
    private int lastChangeUser = 0;

    public MapPresenter() {
        this.mapModelMgr = new MapModel(this);
    }

    public MapPresenter(MapMgr mapMgr) {
        this.mapMgr = mapMgr;
        this.mapModelMgr = new MapModel(this);

    }

    @Override
    public void moveCamera(int position) {
        MapsActivity.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(position),10));
    }

    @Override
    public void showInforFriend(int position) {
        lsMarker.get(position).showInfoWindow();
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
                    }
                    else{

                        mapMgr.showToast("Bạn chưa có nhóm nào");
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
        mapModelMgr.loadInfor(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> listUser, ParseException e) {
                if(e == null){
                    if(!listUser.isEmpty()){

                        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("GroupData");
                        parseQuery.whereEqualTo("groupName",MapsActivity.itemSelected).whereExists("captainGroup");
                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                if (e == null){
                                    if (!list.isEmpty()){
                                        showMapData(listUser,list.get(0));
                                    }
                                }

                            }
                        });
                    }
                    else {
                        mapMgr.showToast("Không có thành viên trong nhóm");
                    }
                }
                else {
                    mapMgr.showToast(e.getMessage());
                }
            }
        });
    }

    @Override
    public void showMapData(final List<ParseObject> listUser,final ParseObject parseObject) {
        lsMarker = new ArrayList<>();
        latLngs = new ArrayList<>();
        new AsyncTask<List<ParseObject>, UserObject, List<UserObject>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lsUserObject = new ArrayList<UserObject>();
                MapsActivity.mMap.clear();
            }

            @Override
            protected void onProgressUpdate(UserObject... values) {
                super.onProgressUpdate(values);
                if (values[0].getName().equals(parseObject.getString("captainGroup"))) {

                    lsMarker.add(MapsActivity.mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(values[0].getName() + "(Đội trưởng)")
                            .icon(BitmapDescriptorFactory.fromBitmap(values[0].getAvatar()))
                            .snippet(values[0].getLastUpdate())));
                }
                else {
                    lsMarker.add(MapsActivity.mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(values[0].getName())
                            .icon(BitmapDescriptorFactory.fromBitmap(values[0].getAvatar()))
                            .snippet(values[0].getLastUpdate())));
                }
            }

            @Override
            protected List<UserObject> doInBackground(List<ParseObject>... params) {
                for (ParseObject obj : params[0]){
                    UserObject user = new UserObject();
                    user.setName(obj.getString("alias"));
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
                    user.setPhone(obj.getString("phone"));
                    user.setLastUpdate(obj.getString("updateAt"));
                    lsUserObject.add(user);
                    publishProgress(user);
                }
                return lsUserObject;
            }

            @Override
            protected void onPostExecute(List<UserObject> userObjects) {
                super.onPostExecute(userObjects);
                mapMgr.showListFriend(userObjects);
            }

        }.execute(listUser);

    }
}
