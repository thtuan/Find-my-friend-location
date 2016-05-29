package com.thtuan.FindFriendLocation.Class;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;

/**
 * Mo ta class
 *
 * @author thtuan
 * @since 9:35 AM 18-05-2016
 */
public class UserObject implements Parcelable{

    private String name;
    private String phone;
    private String addr;
    private String character;
    private String email;
    private String birthday;
    private String contact;
    private Bitmap avatar;
    private LatLng location;
    private boolean captain;
    private String lastUpdate;

    public UserObject() {
    }

    public UserObject(String name, String phone, String addr, String character, String birthday, String contact, Bitmap
            avatar, LatLng location, boolean captain) {
        this.name = name;
        this.phone = phone;
        this.addr = addr;
        this.character = character;
        this.birthday = birthday;
        this.contact = contact;
        this.avatar = avatar;
        this.location = location;
        this.captain = captain;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isCaptain() {
        return captain;
    }

    public void setCaptain(boolean captain) {
        this.captain = captain;
    }

    public String getCharacter() {
        return character;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String isCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.addr);
        dest.writeString(this.character);
        dest.writeString(this.email);
        dest.writeString(this.birthday);
        dest.writeString(this.contact);
        dest.writeParcelable(this.avatar, flags);
        dest.writeParcelable(this.location, flags);
        dest.writeByte(this.captain ? (byte) 1 : (byte) 0);
        dest.writeString(this.lastUpdate);
    }

    protected UserObject(Parcel in) {
        this.name = in.readString();
        this.phone = in.readString();
        this.addr = in.readString();
        this.character = in.readString();
        this.email = in.readString();
        this.birthday = in.readString();
        this.contact = in.readString();
        this.avatar = in.readParcelable(Bitmap.class.getClassLoader());
        this.location = in.readParcelable(LatLng.class.getClassLoader());
        this.captain = in.readByte() != 0;
        this.lastUpdate = in.readString();
    }

    public static final Creator<UserObject> CREATOR = new Creator<UserObject>() {
        @Override
        public UserObject createFromParcel(Parcel source) {
            return new UserObject(source);
        }

        @Override
        public UserObject[] newArray(int size) {
            return new UserObject[size];
        }
    };
}
