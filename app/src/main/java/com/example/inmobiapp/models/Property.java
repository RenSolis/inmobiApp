package com.example.inmobiapp.models;

import com.google.firebase.database.Exclude;

public class Property {
    int mMeters;
    int mRooms;
    int mFloors;
    int mPrice;
    String mId;
    String mType;
    String mAddress;
    String mAcquisition;
    String mImage;

    public Property() {}

    public Property(int meters, int rooms, int floors, int price, String type, String address, String acquisition, String image) {
        mMeters = meters;
        mRooms = rooms;
        mFloors = floors;
        mPrice = price;
        mType = type;
        mAddress = address;
        mAcquisition = acquisition;
        mImage = image;
    }

    public int getMeters() {
        return mMeters;
    }

    public void setMeters(int mMeters) {
        this.mMeters = mMeters;
    }

    public int getRooms() {
        return mRooms;
    }

    public void setRooms(int rooms) {
        this.mRooms = rooms;
    }

    public int getFloors() {
        return mFloors;
    }

    public void setFloors(int floors) {
        this.mFloors = floors;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        this.mPrice = price;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getAcquisition() {
        return mAcquisition;
    }

    public void setAcquisition(String acquisition) {
        this.mAcquisition = acquisition;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }
}
