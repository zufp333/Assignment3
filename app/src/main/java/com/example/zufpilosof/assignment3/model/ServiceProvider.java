package com.example.zufpilosof.assignment3.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class ServiceProvider implements Parcelable {

    private String mName;
    private String mService;
    private String mLocation;
    private String mThumbImage;
    private int mPrice;
    private int mRating;
    private int mReviewsCount;
    private int mYearsOfExperience;
    private Map<String,Review> reviews;

    public ServiceProvider(String name, String service, String location, String thumbImage, int price, int rating, int reviewsCount, int yearsOfExperience, Map<String,Review> reviews) {
        this.mName = name;
        this.mService = service;
        this.mLocation = location;
        this.mThumbImage = thumbImage;
        this.mPrice = price;
        this.mRating = rating;
        this.mReviewsCount = reviewsCount;
        this.mYearsOfExperience = yearsOfExperience;
        this.reviews = reviews;
    }

    public ServiceProvider() {
    }

    public String getName() { return mName; }

    public void setName(String mName) { this.mName = mName; }

    public String getService() { return mService; }

    public void setService(String mService) { this.mService = mService; }

    public String getLocation() { return mLocation; }

    public void setLocation(String mLocation) { this.mLocation = mLocation; }

    public String getThumbImage() { return mThumbImage; }

    public void setThumbImage(String mThumbImage) { this.mThumbImage = mThumbImage; }

    public int getPrice() { return mPrice; }

    public void setPrice(int mPrice) { this.mPrice = mPrice; }

    public int getRating() { return mRating; }

    public void setRating(int mRating) { this.mRating = mRating; }

    public int getReviewsCount() { return mReviewsCount; }

    public void setReviewsCount(int mReviewsCount) { this.mReviewsCount = mReviewsCount; }

    public int getYearsOfExperience() { return mYearsOfExperience; }

    public void setYearsOfExperience(int YearsOfExperience) { this.mYearsOfExperience = YearsOfExperience; }

    public Map<String, Review> getReviews() { return reviews; }

    public void setReviews(Map<String, Review> reviews) { this.reviews = reviews; }


    public void incrementReviewCount() { mReviewsCount++;}

    public void incrementRating(int newRating) { mRating +=newRating;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mService);
        parcel.writeString(mLocation);
        parcel.writeString(mThumbImage);
        parcel.writeInt(mPrice);
        parcel.writeInt(mRating);
        parcel.writeInt(mReviewsCount);
        parcel.writeInt(mYearsOfExperience);
    }

    private ServiceProvider(Parcel in){
        this.mName = in.readString();
        this.mService = in.readString();
        this.mLocation = in.readString();
        this.mThumbImage = in.readString();
        this.mPrice = in.readInt();
        this.mRating = in.readInt();
        this.mReviewsCount = in.readInt();
        this.mYearsOfExperience = in.readInt();
    }

    public static final Parcelable.Creator<ServiceProvider> CREATOR = new Parcelable.Creator<ServiceProvider>() {
        @Override
        public ServiceProvider createFromParcel(Parcel source) {
            return new ServiceProvider(source);
        }

        @Override
        public ServiceProvider[] newArray(int size) {
            return new ServiceProvider[size];
        }
    };


}
