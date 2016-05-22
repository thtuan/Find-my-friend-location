package com.thtuan.FindFriendLocation.Activity.Maps.view;

import com.parse.ParseObject;
import com.thtuan.FindFriendLocation.Class.ListFriendAdapter;
import com.thtuan.FindFriendLocation.Class.UserObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanhtuan on 10-05-2016.
 */
public interface MapMgr {

    void showGroupData(List<String> grouplLst);
    void showToast(String message);
    void showListFriend(List<UserObject> lsUser);
}
