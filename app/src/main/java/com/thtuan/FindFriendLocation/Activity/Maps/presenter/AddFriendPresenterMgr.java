package com.thtuan.FindFriendLocation.Activity.Maps.presenter;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by thanhtuan on 10-05-2016.
 */
public interface AddFriendPresenterMgr {
    void addFriend(ParseUser user, int pos);
    void getFriend(String name, int pos);
    void createToast(String message);
    void getCanAddFriendList(String s);
}
