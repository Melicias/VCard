package com.example.melic.vcard.Models;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Contact implements Serializable {
    private String name;
    private String phoneNumber;
    private Bitmap image;
    private boolean vcard;


    public Contact() {
    }

    public Contact(String name, String phoneNumber, Bitmap image) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.image = image;
        vcard = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isVcard() {
        return vcard;
    }

    public void setVcard(boolean vcard) {
        this.vcard = vcard;
    }
}
