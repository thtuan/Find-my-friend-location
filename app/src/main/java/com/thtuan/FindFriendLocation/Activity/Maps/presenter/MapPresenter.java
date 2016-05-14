package com.thtuan.FindFriendLocation.Activity.Maps.presenter;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.thtuan.FindFriendLocation.Activity.Maps.model.MapModel;
import com.thtuan.FindFriendLocation.Activity.Maps.model.MapModelMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.view.MapMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.view.MapsActivity;
import com.thtuan.FindFriendLocation.Class.ListFriendAdapter;
import com.thtuan.FindFriendLocation.R;

import java.util.List;

/**
 * Created by thanhtuan on 12-05-2016.
 */
public class MapPresenter implements MapPresenterMgr {
    private MapModelMgr mapModelMgr;
    private MapMgr mapMgr;
    private ParseGeoPoint geoPoint;
    private LatLng latLng;
    List<LatLng> latLngs;
    List<String> lstGroup;
    List<Marker> lsMarker;
    List<String> listName;
    ListFriendAdapter userAdapter;

    public MapPresenter(MapMgr mapMgr) {
        this.mapMgr = mapMgr;
        this.mapModelMgr = new MapModel(this);
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
            public void done(List<ParseObject> listUser, ParseException e) {
                if(e == null){
                    if(!listUser.isEmpty()){
                        for (int i = 0; i < listUser.size(); i++) {
                            final ParseObject obj = listUser.get(i);
                            geoPoint = obj.getParseGeoPoint("location");
                            latLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                            lsMarker.add(MapsActivity.mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                    .title(obj.getString("alias"))
                                    .snippet("đang ở đây")));
                            listName.add(obj.getString("alias"));
                            latLngs.add(latLng);
                        }
                        userAdapter = new ListFriendAdapter(MapsActivity.mContext1,listName);
                        mapMgr.showInforData(userAdapter);
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


}