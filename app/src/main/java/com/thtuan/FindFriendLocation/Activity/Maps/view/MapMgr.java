package com.thtuan.FindFriendLocation.Activity.Maps.view;

import com.parse.ParseObject;
import com.thtuan.FindFriendLocation.Class.ListFriendAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanhtuan on 10-05-2016.
 */
public interface MapMgr {
    void showMapData();
    void showInforData(ListFriendAdapter friendAdapter);
    void showGroupData(List<String> grouplLst);
    void showToast(String message);
}
