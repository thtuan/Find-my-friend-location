package com.thtuan.FindFriendLocation.Activity.Maps.presenter;

import com.google.android.gms.maps.model.Marker;
import com.parse.ParseObject;
import com.thtuan.FindFriendLocation.Activity.Maps.model.MapModelMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.view.MapMgr;
import com.thtuan.FindFriendLocation.Class.UserObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanhtuan on 10-05-2016.
 */
public interface MapPresenterMgr {
    void setMapModel(double latitude, double longitude);
    void moveCamera(int position);
    void showInforFriend(Marker marker);
    void showMapData(List<UserObject> listUser, ParseObject parseObject);
    void showListFriend();
    void loadGroup();
    void loadInfor();
}
