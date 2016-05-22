package com.thtuan.FindFriendLocation.Class;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Mo ta class
 *
 * @author thtuan
 * @since 9:35 AM 18-05-2016
 */
public class UserObject {
    private String name;
    private String phone;
    private String addr;
    private String character;
    private String email;
    private Date birthday;
    private String contact;
    private Bitmap avatar;
    private LatLng location;
    private boolean captain;
    private String lastUpdate;

    public UserObject() {
    }

    public UserObject(String name, String phone, String addr, String character, Date birthday, String contact, Bitmap avatar, LatLng location, boolean captain) {
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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

}
