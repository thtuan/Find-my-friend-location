package com.thtuan.FindFriendLocation.Activity.Maps;

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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.thtuan.FindFriendLocation.Class.GetFriendInformation;
import com.thtuan.FindFriendLocation.Class.MyLocation;
import com.thtuan.FindFriendLocation.R;
import com.thtuan.FindFriendLocation.Class.RealPathUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, LocationListener, GoogleMap.OnMyLocationButtonClickListener {
    public static String itemSelected;
    public static Activity mContext;
    public static Context mContext1;
    static ArrayAdapter<String> arrayAdapter;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    com.thtuan.FindFriendLocation.Class.MyLocation myLocation;
    LocationManager locationManager;
    LatLng latLng;
    ParseUser myUser;
    ParseObject parseObject;
    ParseQuery<ParseUser>queryUser;
    DrawerLayout drawerLayout;
    NavigationView drawrer;
    Spinner spinner;
    GetFriendInformation tim;
    ImageView imgProfile;
    SharedPreferences preferences;
    String path;
    TextView tvName;
    CountDownTimer countDownTimer;
    public static GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        myUser = ParseUser.getCurrentUser();
        mContext = this;
        mContext1 = this;
        latLng = new LatLng(0,0);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.MainActivity);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        spinner = (Spinner) findViewById(R.id.spinner);
        tvName = (TextView) findViewById(R.id.tvName);
        drawrer = (NavigationView) findViewById(R.id.navigationView);
        parseObject = new ParseObject("LocationData");
        tvName.setText(myUser.getUsername());
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang tải map");
        progressDialog.setMessage("Vui lòng đợi trong giây lát");
        progressDialog.setCancelable(true);
        progressDialog.show();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocation = new MyLocation(this, locationManager, latLng);
        latLng = myLocation.getLocation();
        CheckGPS(myLocation.isGpsEnable());
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

        refreshSpiner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = arrayAdapter.getItem(position);
                mMap.clear();
                tim.getInfor(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                itemSelected = arrayAdapter.getItem(0);
            }
        });
        countDownTimer = new CountDownTimer(60000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getAndSetLocation();
            }

            @Override
            public void onFinish() {
                countDownTimer.start();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownTimer.start();
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
        countDownTimer.cancel();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        tim = new GetFriendInformation();
        googleMap.setOnMapLoadedCallback(this);
        googleMap.setMyLocationEnabled(true);

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
        }

    }

    @Override
    public void onMapLoaded() {
        progressDialog.dismiss();
    }


    @Override
    public void onLocationChanged(Location location) {
        getAndSetLocation();
    }

    public void getAndSetLocation() {
        if(itemSelected!=null){
            latLng = myLocation.getLocation();
            tim.setInfor(latLng);
            tim.getInfor(itemSelected);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "enable " + provider + " provider", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "disable " + provider + " provider", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
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
        ParseQuery<ParseUser> obj = ParseUser.getQuery();
        obj.whereEqualTo("groupName",itemSelected).whereEqualTo("username",myUser.getUsername());
        obj.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if(e == null){
                    list.get(0).remove("groupName");
                    JSONArray jsonArray = list.get(0).getJSONArray("groupName");
                    ArrayList<String> group = new ArrayList<String>();
                    for(int i = 0; i < jsonArray.length(); i++){
                        try {
                            String name = jsonArray.getString(i);
                            if(!name.equals(itemSelected)){
                                group.add(name);
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    list.get(0).addAllUnique("groupName",group);
                    arrayAdapter.remove(spinner.getItemAtPosition(spinner.getLastVisiblePosition()).toString());
                    spinner.setAdapter(arrayAdapter);
                    tim.getInfor(itemSelected);
                }
            }
        });

    }
    private void refreshSpiner(){
        queryUser = ParseUser.getQuery();
        queryUser.whereEqualTo("username",myUser.getUsername());
        queryUser.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if(e==null){
                    if(list.size()!=0){
                        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item);
                        JSONArray jsonArray = list.get(0).getJSONArray("groupName");
                        for (int i = 0 ; i < jsonArray.length(); i++){
                            try {
                                arrayAdapter.add(jsonArray.getString(i));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        spinner.setAdapter(arrayAdapter);
                    }
                }
                else {

                }
            }
        });
    }
}
