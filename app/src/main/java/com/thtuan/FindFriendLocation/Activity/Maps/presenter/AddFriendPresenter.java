package com.thtuan.FindFriendLocation.Activity.Maps.presenter;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.thtuan.FindFriendLocation.Activity.Maps.model.MapModel;
import com.thtuan.FindFriendLocation.Activity.Maps.model.MapModelMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.view.AddFriendActivity;
import com.thtuan.FindFriendLocation.Activity.Maps.view.AddFriendMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.view.MapsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanhtuan on 12-05-2016.
 */
public class AddFriendPresenter implements AddFriendPresenterMgr{
    private AddFriendMgr addFriendMgr;
    private MapModelMgr mapModelMgr;
    public AddFriendPresenter(AddFriendActivity addFriend) {
        this.addFriendMgr = addFriend;
        this.mapModelMgr = new MapModel(this);
    }

    @Override
    public void addFriend(final ParseUser user, final int position) {
        mapModelMgr.addFriend(user.getUsername(), new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null){
                    if (list.isEmpty()){
                        ParseObject parseObject = new ParseObject("GroupData");
                        parseObject.put("userID", user);
                        parseObject.put("groupName", MapsActivity.itemSelected);
                        parseObject.put("alias",user.getUsername());
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    createToast("Thêm "+user.getUsername()+" vào nhóm thành công");

                                    addFriendMgr.isSuccess(true);
                                }
                                else {
                                    createToast("Lỗi: "+e.getMessage());
                                    return;
                                }
                            }
                        });
                    }
                    else {
                        createToast("Đã có trong nhóm");
                        addFriendMgr.isSuccess(false);
                        return;
                    }
                }
                else {
                    createToast("Lỗi: "+e.getMessage());
                    addFriendMgr.isSuccess(false);
                    return;
                }
            }
        });

    }

    @Override
    public void getFriend(String name, final int position) {
        if (MapsActivity.itemSelected != null){
            addFriendMgr.removeAtPostion(position);
            mapModelMgr.getUser(name, new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if (e == null){
                        if (!list.isEmpty()){
                            addFriend(list.get(0), position);
                        }
                        else {
                            createToast("Không tìm thấy user này");
                        }
                    }
                    else {
                        createToast("Lỗi: "+e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void getCanAddFriendList(String s) {
        if (MapsActivity.itemSelected != null) {
            ParseQuery<ParseObject> queryObject = ParseQuery.getQuery("GroupData");
            queryObject.whereEqualTo("groupName", MapsActivity.itemSelected);
            ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
            queryUser.whereDoesNotMatchKeyInQuery("username","alias",queryObject).whereContains("username",s);
            queryUser.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if(e==null){
                        if(list.size()!=0){
                            ArrayList<String> arrayList = new ArrayList<String>();
                            for (ParseUser obj : list) {
                                arrayList.add(obj.getUsername());
                            }
                            addFriendMgr.showAddList(arrayList);
                        }
                    }
                    else {
                        createToast("Lỗi: "+e.getMessage());
                    }
                }
            });
        }
        else {
            createToast("Bạn chưa có nhóm");
        }
    }

    @Override
    public void createToast(String message) {
        addFriendMgr.showToast(message);
    }
}
