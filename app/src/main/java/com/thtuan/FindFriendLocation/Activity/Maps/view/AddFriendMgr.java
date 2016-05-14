package com.thtuan.FindFriendLocation.Activity.Maps.view;

import java.util.ArrayList;

/**
 * Created by thanhtuan on 10-05-2016.
 */
public interface AddFriendMgr {
    void showToast(String message);
    void showAddList(ArrayList<String> arrayList);
    void isSuccess(boolean isSuccess);
    void removeAtPostion(int position);
}
