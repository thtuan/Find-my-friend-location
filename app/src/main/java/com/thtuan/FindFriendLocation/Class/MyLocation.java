package com.thtuan.FindFriendLocation.Class;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.model.LatLng;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.MapPresenter;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.MapPresenterMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.view.MapsActivity;

/**
 * Created by ThanhTuan on 03-03-2016.
 */
public class MyLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private static LatLng latLng;
    private Location location;
    private Context context;
    private boolean canGetLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private MapPresenterMgr mapPresenterMgr;

    public MyLocation(Context context) {
        this.context = context;
        latLng = new LatLng(0,0);
        mapPresenterMgr = new MapPresenter();
        canGetLocation = false;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public double getLatitude(){
        return latLng.latitude;
    }

    public double getLongitude(){
        return latLng.latitude;
    }

    public static LatLng getLatLng() {
        return latLng;
    }

    @Override
    public void onConnected(Bundle bundle) {
       location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null){
            latLng = new LatLng(location.getLatitude(),location.getLongitude());
            mapPresenterMgr.setMapModel(location.getLatitude(),location.getLongitude());
            canGetLocation = true;
        }
        createLocationRequest();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        canGetLocation = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        canGetLocation = false;
    }
    public void startService(){
        mGoogleApiClient.connect();
    }

    public void stopService(){
        if (mGoogleApiClient.isConnected()){
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }


    }
    public void createLocationRequest(){
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest
                (locationRequest);

    }
    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, locationRequest, this);
    }
    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }
    @Override
    public void onLocationChanged(Location location) {
        mapPresenterMgr.setMapModel(location.getLatitude(),location.getLongitude());
        MapsActivity.mapPresenter.loadInfor();
    }
}
