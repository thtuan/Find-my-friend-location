package com.thtuan.FindFriendLocation.Activity.Maps.model;

import android.util.Log;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.AddFriendPresenter;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.AddFriendPresenterMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.MapPresenter;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.MapPresenterMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.NewGroupPresenter;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.NewGroupPresenterMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.view.MapsActivity;

import java.util.List;

/**
 * Created by thanhtuan on 12-05-2016.
 */
public class MapModel implements MapModelMgr {

    private MapPresenterMgr mapPresenter;
    private NewGroupPresenterMgr newGroupPresenter;
    private AddFriendPresenterMgr addFriendPresenterMgr;

    public MapModel() {
    }

    public MapModel(MapPresenter presenter){
        this.mapPresenter = presenter;
    }

    public MapModel(NewGroupPresenter presenter){
        this.newGroupPresenter = presenter;
    }

    public MapModel(AddFriendPresenter presenter){
        this.addFriendPresenterMgr = presenter;
    }

    @Override
    public void newGroup(final String groupName, FindCallback<ParseObject> findCallback) {
        if(groupName.isEmpty()){
            newGroupPresenter.createToast("Phải nhập tên nhóm");
        }
        else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("GroupData");
            query.whereEqualTo("groupName",groupName);
            query.findInBackground(findCallback);
        }
    }

    @Override
    public void getUser(String name, FindCallback<ParseUser> findCallback) {
        ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
        queryUser.whereEqualTo("username", name);
        queryUser.findInBackground(findCallback);
    }

    @Override
    public void addFriend(String friendName, FindCallback<ParseObject> findCallback) {
        ParseQuery<ParseObject> queryObject = ParseQuery.getQuery("GroupData");
        queryObject.whereEqualTo("groupName", MapsActivity.itemSelected).whereEqualTo("alias", friendName);
        queryObject.findInBackground(findCallback);
    }

    @Override
    public void getAddList(FindCallback<ParseUser> findCallback) {

    }

    @Override
    public void outGroup() {

    }

    @Override
    public void loadInfor(FindCallback<ParseObject> findCallback) {
        if(MapsActivity.itemSelected !=null){
            ParseQuery<ParseObject> queryObject = ParseQuery.getQuery("GroupData");
            queryObject.whereEqualTo("groupName",MapsActivity.itemSelected);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("DataUser");
            query.whereMatchesKeyInQuery("alias","alias", queryObject);
            query.findInBackground(findCallback);
        }
        else{

        }
    }

    @Override
    public void loadGroup(FindCallback<ParseObject> findCallback) {
        ParseQuery<ParseObject>queryGroup = ParseQuery.getQuery("GroupData");
        queryGroup.whereEqualTo("userID",MapsActivity.myUser);
        queryGroup.findInBackground(findCallback);
    }

    @Override
    public void setInfor(final double latitude, final double longitude) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DataUser");
        query.whereEqualTo("alias", MapsActivity.myUser.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ParseGeoPoint geoPoint = new ParseGeoPoint(latitude,longitude);
                    if (list.size() == 0) {
                        ParseObject object = new ParseObject("DataUser");
                        object.put("userID",MapsActivity.myUser);
                        object.put("location", geoPoint );
                        object.put("alias",MapsActivity.myUser.getUsername());
                        object.saveInBackground();
                    }
                    else {
                        list.get(0).put("location",geoPoint);
                        list.get(0).saveInBackground();
                    }
                } else {
                    Toast.makeText(MapsActivity.mContext1, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
