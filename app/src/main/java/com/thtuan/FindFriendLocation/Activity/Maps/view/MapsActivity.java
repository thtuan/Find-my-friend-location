package com.thtuan.FindFriendLocation.Activity.Maps.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.MapPresenter;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.MapPresenterMgr;
import com.thtuan.FindFriendLocation.Class.ListFriendAdapter;
import com.thtuan.FindFriendLocation.Class.MyLocation;
import com.thtuan.FindFriendLocation.R;
import com.thtuan.FindFriendLocation.Class.RealPathUtil;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements MapMgr, OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, LocationListener {
    public static String itemSelected;
    public static Activity mContext;
    public static Context mContext1;
    private int pos = -1;
    static ArrayAdapter<String> arrayAdapter;
    com.thtuan.FindFriendLocation.Class.MyLocation myLocation;
    public static LocationManager locationManager;
    public static ParseUser myUser;
    ParseQuery<ParseObject> queryObject;
    NavigationView drawrer;
    Spinner spinner;
    ImageView imgProfile;
    SharedPreferences preferences;
    String path;
    TextView tvName;
    CountDownTimer countDownTimer;
    public static GoogleMap mMap;
    ListView lvFriend;
    MapPresenterMgr mapPresenter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        myUser = ParseUser.getCurrentUser();
        mContext = this;
        mContext1 = this;
        MapFragment mapFragment = (MapFragment) MapsActivity.mContext.getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lvFriend = (ListView) findViewById(R.id.lvFriend);
        drawrer = (NavigationView) findViewById(R.id.navigationView);
        imgProfile = (ImageView) drawrer.getHeaderView(0).findViewById(R.id.imgProfile);
        spinner = (Spinner) drawrer.getHeaderView(0).findViewById(R.id.spinner);
        tvName = (TextView) drawrer.getHeaderView(0).findViewById(R.id.tvName);
        tvName.setText(myUser.getUsername());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocation = new MyLocation(this, locationManager);
        myLocation.getLocation();
        CheckGPS(myLocation.isGpsEnable());
        mapPresenter = new MapPresenter(this);
        progressDialog = new ProgressDialog(MapsActivity.mContext1);
        progressDialog.setTitle("Đang tải map");
        progressDialog.setMessage("Vui lòng đợi trong giây lát");
        progressDialog.setCancelable(true);
        progressDialog.show();
        lvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                mapPresenter.moveCamera(position);
                mapPresenter.showInforFriend(position);

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = (String)spinner.getItemAtPosition(position);
                mapPresenter.loadInfor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 200);
            }
        });
        drawrer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        itemSelected = (String)spinner.getItemAtPosition(0);
        mapPresenter.loadGroup();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String path = preferences.getString("pathImg", "null");
        if (path.equals("null")) {
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imgProfile.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("pathImg", path);
        edit.commit();

    }

    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        countDownTimer.start();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.clear();
        countDownTimer = new CountDownTimer(10000,5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                myLocation.getLocation();
                mapPresenter.setMapModel(myLocation.getLatLng().latitude,myLocation.getLatLng().longitude);
                mapPresenter.loadInfor();
            }

            @Override
            public void onFinish() {
                countDownTimer.start();
            }
        }.start();
        googleMap.setOnMapLoadedCallback(this);
        googleMap.setMyLocationEnabled(true);
    }
    @Override
    public void onMapLoaded() {
        progressDialog.dismiss();
    }

    @Override
    public void onLocationChanged(Location location) {
        //set thông tin user

//        getAndSetLocation(MapsActivity.itemSelected);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(MapsActivity.mContext1, "enable " + provider + " provider", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MapsActivity.mContext1, "disable " + provider + " provider", Toast.LENGTH_LONG).show();
    }

    public void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newgroup:
                Intent intent = new Intent(MapsActivity.this, NewGroupActivity.class);
                startActivity(intent);
                break;
            case R.id.entergroup:
                Intent intentadd = new Intent(MapsActivity.this, AddFriendActivity.class);
                startActivity(intentadd);
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
        if (requestCode == 200 && resultCode == RESULT_OK) {
//            File file = new File(RealPathUtil.getRealPathFromURI_API19(this,data.getData()));
            path = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
            Bitmap bitmap = BitmapFactory.decodeFile(path);
//            ParseFile parseFile = new ParseFile(file);
//            parseFile.saveInBackground();
            imgProfile.setImageBitmap(bitmap);
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
                        pos = -1;
                    }

                }
            });
        }
        else
            Toast.makeText(getApplicationContext(),"Bạn chưa có nhóm",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showInforData(ListFriendAdapter friendAdapter) {
        lvFriend.setAdapter(friendAdapter);
        if (pos != -1){
            mapPresenter.showInforFriend(pos);
        }
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
}

