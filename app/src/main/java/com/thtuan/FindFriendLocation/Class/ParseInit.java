package com.thtuan.FindFriendLocation.Class;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.facebook.FacebookSdk;
/**
 * Created by ThanhTuan on 06-04-2016.
 */
public class ParseInit extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);
        ParseFacebookUtils.initialize(this);
        FacebookSdk.sdkInitialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
