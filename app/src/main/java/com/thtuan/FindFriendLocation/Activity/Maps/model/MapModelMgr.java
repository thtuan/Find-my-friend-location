package com.thtuan.FindFriendLocation.Activity.Maps.model;

import com.google.android.gms.wearable.internal.PackageStorageInfo;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.MapPresenter;

/**
 * Created by thanhtuan on 12-05-2016.
 */
public interface MapModelMgr {
    void newGroup(String groupName,FindCallback<ParseObject> findCallback);
    void addFriend(String friendName, FindCallback<ParseObject> findCallback);
    void getUser(String name, FindCallback<ParseUser> findCallback);
    void getAddList(FindCallback<ParseUser> findCallback);
    void outGroup();
    void loadInfor(FindCallback<ParseObject> findCallback);
    void loadGroup(FindCallback<ParseObject> findCallback);
    void setInfor(double latitude, double longitude);
}
