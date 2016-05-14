package com.thtuan.FindFriendLocation.Activity.Maps.presenter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.thtuan.FindFriendLocation.Activity.Maps.model.MapModel;
import com.thtuan.FindFriendLocation.Activity.Maps.model.MapModelMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.view.MapsActivity;
import com.thtuan.FindFriendLocation.Activity.Maps.view.NewGroupMgr;

import java.util.List;

/**
 * Created by thanhtuan on 12-05-2016.
 */
public class NewGroupPresenter implements NewGroupPresenterMgr {
    NewGroupMgr newGroupMgr;
    MapModelMgr mapModelMgr;

    public NewGroupPresenter(NewGroupMgr newGroupMgr){
        this.newGroupMgr = newGroupMgr;
        mapModelMgr = new MapModel(this);
    }
    @Override
    public void newGroup(final String groupName) {
        mapModelMgr.newGroup(groupName, new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null){
                    if(!list.isEmpty()){
                        createToast("Tên nhóm đã tồn tại");
                    }
                    else {
                        ParseObject parseObject = new ParseObject("GroupData");
                        parseObject.put("groupName",groupName);
                        parseObject.put("captainGroup", MapsActivity.myUser);
                        parseObject.put("userID",MapsActivity.myUser);
                        parseObject.put("alias",MapsActivity.myUser.getUsername());
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null){
                                    createToast("Tạo nhóm thành công");
                                    // load map khi finish
                                }
                                else{
                                    createToast(e.getMessage());
                                }
                            }
                        });
                    }
                }
                else {
                    createToast(e.getMessage());
                }
            }
        });
    }

    @Override
    public void createToast(String message) {
        newGroupMgr.showNotifycation(message);
    }


}
