package com.thtuan.FindFriendLocation.Activity.Maps.presenter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by thanhtuan on 10-05-2016.
 */
public interface NewGroupPresenterMgr {
    void newGroup(String groupName);
    void createToast(String message);
}
