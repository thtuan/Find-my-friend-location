package com.thtuan.FindFriendLocation.Activity.Maps.presenter;

import com.parse.ParseObject;
import com.thtuan.FindFriendLocation.Activity.Maps.model.MapModelMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.view.MapMgr;

import java.util.List;

/**
 * Created by thanhtuan on 10-05-2016.
 */
public interface MapPresenterMgr {
    void setMapModel(double latitude, double longitude);
    void loadGroup();
    void loadInfor();
}
