package com.thtuan.FindFriendLocation.Activity.Maps.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.request.DirectionOriginRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.MapPresenter;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.MapPresenterMgr;
import com.thtuan.FindFriendLocation.Class.Constants;
import com.thtuan.FindFriendLocation.Class.ListFriendAdapter;
import com.thtuan.FindFriendLocation.Class.MyLocation;
import com.thtuan.FindFriendLocation.Class.RealPathUtil;
import com.thtuan.FindFriendLocation.Class.RoundedImageView;
import com.thtuan.FindFriendLocation.Class.UserObject;
import com.thtuan.FindFriendLocation.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements MapMgr, OnMapReadyCallback, GoogleMap
        .OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {
    public static String itemSelected;
    public static Activity mContext;
    public static Context mContext1;
    private ListFriendAdapter adapter;
    static ArrayAdapter<String> arrayAdapter;
    com.thtuan.FindFriendLocation.Class.MyLocation myLocation;
    public static ParseUser myUser;
    ParseQuery<ParseObject> queryObject;
    NavigationView drawrer;
    Spinner spinner;
    ImageView imgProfile;
    SharedPreferences preferences;
    String path;
    TextView tvName, tvRefreshList;
    public static GoogleMap mMap;
    ListView lvFriend;
    public static MapPresenterMgr mapPresenter;
    private ProgressDialog progressDialog;
    private List<UserObject> listUser;
    public static DirectionOriginRequest googleDirection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        googleDirection = GoogleDirection.withServerKey("AIzaSyAhc_Sq6mbuuVqKZRuyWW20087nuY9FMiw");
        lvFriend = (ListView) findViewById(R.id.lvFriend);
        drawrer = (NavigationView) findViewById(R.id.navigationView);
        imgProfile = (ImageView) drawrer.getHeaderView(0).findViewById(R.id.imgProfile);
        spinner = (Spinner) drawrer.getHeaderView(0).findViewById(R.id.spinner);
        tvName = (TextView) drawrer.getHeaderView(0).findViewById(R.id.tvName);
        tvRefreshList = (TextView) findViewById(R.id.tvRefreshList);
        tvRefreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapPresenter.showListFriend();
            }
        });
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        CheckGPS(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        myUser = ParseUser.getCurrentUser();
        mContext = this;
        mContext1 = this;
        MapFragment mapFragment = (MapFragment)getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tvName.setText(myUser.getUsername());
        mapPresenter = new MapPresenter(this);
        myLocation = new MyLocation(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang tải map");
        progressDialog.setMessage("Vui lòng đợi trong giây lát");
        progressDialog.setCancelable(true);
        progressDialog.show();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = (String)spinner.getItemAtPosition(position);
                mapPresenter.showListFriend();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this,EditProfile.class);
                intent.putExtra("data",myUser.getUsername());
                startActivityForResult(intent,Constants.REQUEST_EDIT_PROFILE);
            }
        });
        drawrer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
        lvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == lvFriend.getAdapter().getCount()-1){
                    Intent intentadd = new Intent(MapsActivity.this, AddFriendActivity.class);
                    startActivityForResult(intentadd,Constants.REQUEST_NEW_FRIEND);
                }
                else {
                    Intent profileIntent = new Intent(MapsActivity.this, ProfileActivity.class);
                    profileIntent.putExtra("data", listUser.get(position));
                    startActivity(profileIntent);
                }
            }
        });
        mapPresenter.loadGroup();
    }
    @Override
    protected void onResume() {
        super.onResume();
        itemSelected = (String)spinner.getItemAtPosition(0);
        myLocation.startService();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String path = preferences.getString("pathImg", "null");
        if (path.equals("null")) {
        } else {
            Bitmap bitmap = RoundedImageView.getCroppedBitmap(BitmapFactory.decodeFile(path),240);
            imgProfile.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myLocation.stopService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("pathImg", path);
        edit.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        mapPresenter.showListFriend();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.clear();
        googleMap.setOnMapLoadedCallback(this);
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMarkerClickListener(this);
    }
    @Override
    public void onMapLoaded() {
        progressDialog.dismiss();
    }

    public void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newgroup:
                Intent intent = new Intent(MapsActivity.this, NewGroupActivity.class);
                startActivityForResult(intent,Constants.REQUEST_NEW_GROUP);
                break;
            case R.id.entergroup:
                Intent intentadd = new Intent(MapsActivity.this, AddFriendActivity.class);
                startActivityForResult(intentadd,Constants.REQUEST_NEW_FRIEND);
                break;
            case R.id.Logout:
                ParseUser.logOut();
                finish();
                break;
            case R.id.outgroup:
                outGroup();
                break;
            case R.id.contact:
                Intent intentContact = new Intent(MapsActivity.this, ContactActivity.class);
                startActivity(intentContact);
                break;
            case R.id.infor:
                Intent intentInfor = new Intent(MapsActivity.this, InforActivity.class);
                startActivity(intentInfor);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_EDIT_PROFILE && resultCode == RESULT_OK) {
            final UserObject userObject = data.getParcelableExtra("data");
            path = data.getStringExtra("stringpath");
            Bitmap bitmap = userObject.getAvatar();
            if (bitmap!=null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                final byte[] byteArray = stream.toByteArray();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("DataUser");
                query.whereEqualTo("alias",tvName.getText().toString());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        ParseFile parseFile = new ParseFile(byteArray);
                        list.get(0).put("imageUser",parseFile);
                        list.get(0).put("detail",userObject.getContact());
                        list.get(0).put("sex",userObject.getCharacter());
                        list.get(0).put("birthday",userObject.getBirthday());
                        list.get(0).put("phone",userObject.getPhone());
                        list.get(0).put("address",userObject.getAddr());
                        list.get(0).put("email",userObject.getEmail());
                        list.get(0).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                showToast("lưu thành công");
                                mapPresenter.showListFriend();
                            }
                        });
                    }
                });

                imgProfile.setImageBitmap(bitmap);
            }
            else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("DataUser");
                query.whereEqualTo("alias",tvName.getText().toString());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        list.get(0).put("detail",userObject.getContact());
                        list.get(0).put("sex",userObject.getCharacter());
                        list.get(0).put("birthday",userObject.getBirthday());
                        list.get(0).put("phone",userObject.getPhone());
                        list.get(0).put("address",userObject.getAddr());
                        list.get(0).put("email",userObject.getEmail());
                        list.get(0).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                showToast("lưu thành công");
                                mapPresenter.showListFriend();
                            }
                        });
                    }
                });

            }

        }
        if(requestCode == Constants.REQUEST_NEW_GROUP && resultCode == RESULT_OK){
            mapPresenter.loadGroup();
        }
        if (requestCode == Constants.REQUEST_NEW_FRIEND && resultCode == RESULT_OK){
            mapPresenter.showListFriend();
        }
    }

    private void CheckGPS(boolean isGPS) {
        if (!isGPS) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chưa bật GPS");
            builder.setMessage("Bạn có muốn bật GPS");
            builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            //builder.create();
            builder.show();
        }

    }
    private void outGroup(){
        if(itemSelected!=null){
            queryObject = ParseQuery.getQuery("GroupData");
            queryObject.whereEqualTo("groupName",itemSelected).whereEqualTo("alias",myUser.getUsername());
            queryObject.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> list, ParseException e) {
                    if(e == null){
                        if (!list.isEmpty()){
                            if(list.get(0).getString("alias").equals(myUser.getUsername())){
                                ParseQuery<ParseObject> queryObject1 = ParseQuery.getQuery("GroupData");
                                queryObject1.whereEqualTo("groupName",itemSelected);
                                queryObject1.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> list1, ParseException e) {
                                        for(int i = 0; i < list1.size(); i++){
                                            ParseObject object = list1.get(i);
                                            if(i == list1.size()-1){
                                                object.deleteInBackground(new DeleteCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        mapPresenter.loadGroup();
                                                        showToast("Bạn đã giải tán nhóm "+ itemSelected);
                                                    }
                                                });
                                            }
                                            else {
                                                object.deleteInBackground();
                                            }
                                        }
                                    }
                                });
                            }
                            else {
                                list.get(0).deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        mapPresenter.loadGroup();
                                        showToast("Bạn đã rời nhóm "+ itemSelected);
                                    }
                                });
                            }
                        }
                        else {
                            showToast("bạn không có nhóm");
                        }

//                        ListFriendFragment.pos = -1;
                    }

                }
            });
        }
        else
            Toast.makeText(getApplicationContext(),"Bạn chưa có nhóm",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showGroupData(List<String> grouplLst) {
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,grouplLst);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(MapsActivity.arrayAdapter);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showListFriend(List<UserObject> lsUser) {
        UserObject user = new UserObject();
            Resources resources = getResources();
            user.setAvatar(BitmapFactory.decodeResource
                    (resources,R.drawable.add_user));
            user.setName("Thêm bạn");
            lsUser.add(user);
        listUser = lsUser;
        adapter = new ListFriendAdapter(this, lsUser);
        lvFriend.setAdapter(adapter);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mapPresenter.showInforFriend(marker);
        return true;
    }
}

