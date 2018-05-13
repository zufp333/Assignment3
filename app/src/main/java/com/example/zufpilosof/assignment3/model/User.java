package com.example.zufpilosof.assignment3.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    private String mEmail;
    private int mTotalPurchase;
    private List<String> mMyServiceRequests = new ArrayList<>();

    public User() {
    }

    public User(String email, int totalPurchase, List<String> myServiceRequests) {
        this.mEmail = email;
        this.mTotalPurchase = totalPurchase;
        this.mMyServiceRequests = myServiceRequests;
    }

    public String getEmail() {
        return mEmail;
    }


    public void updateTotalPurchase(int newPurcahsePrice) {
        this.mTotalPurchase += newPurcahsePrice;
    }

    public List<String> getMyServiceRequests() {
        return mMyServiceRequests;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mEmail);
        parcel.writeList(mMyServiceRequests);
    }

    public User(Parcel in) {
        this.mEmail = in.readString();
        in.readList(mMyServiceRequests,String.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
