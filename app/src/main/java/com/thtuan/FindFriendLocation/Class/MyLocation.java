package com.thtuan.FindFriendLocation.Class;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ThanhTuan on 03-03-2016.
 */
public class MyLocation{

    private LatLng latLng;
    private LocationManager locationManager;
    private Location location;
    private Context context;
    private boolean canGetLocation;
//    private static final long MIN_TIME_BW_UPDATE = 2*1000;
//    private static final float MIN_DISTANCE_UPDATE = 1;
    //boolean isGpsEnable;

    public MyLocation(Context context,LocationManager locationManager) {
        this.locationManager = locationManager;
        this.context = context;
        latLng = new LatLng(0,0);
        canGetLocation = false;
    }

    public boolean isGpsEnable(){
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public double getLatitude(){
        return latLng.latitude;
    }

    public double getLongitude(){
        return latLng.latitude;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public LatLng getLocation(){

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location!=null){
            latLng = new LatLng(location.getLatitude(),location.getLongitude());
            canGetLocation = true;
        }
        else {
            canGetLocation = false;
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null){
            latLng = new LatLng(location.getLatitude(),location.getLongitude());
            canGetLocation=true;
        }
        else {
            canGetLocation = false;
        }

        return latLng;
    }

}
