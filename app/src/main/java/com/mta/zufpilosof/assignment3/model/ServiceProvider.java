package com.mta.zufpilosof.assignment3.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class ServiceProvider implements Parcelable {

    private String name;
    private String service;
    private String location;
    private String phone;
    private String thumbImage;
    private int price;
    private int rating;
    private int reviewsCount;
    private int yearsOfExperience;
    private Map<String,Review> reviews;

    public ServiceProvider(String name, String service, String location, String thumbImage, String phone, int price, int rating, int reviewsCount, int yearsOfExperience, Map<String,Review> reviews) {
        this.name = name;
        this.service = service;
        this.location = location;
        this.thumbImage = thumbImage;
        this.phone = phone;
        this.price = price;
        this.rating = rating;
        this.reviewsCount = reviewsCount;
        this.yearsOfExperience = yearsOfExperience;
        this.reviews = reviews;
    }

    public ServiceProvider() {
    }

    public String getName() { return name; }

    public void setName(String mName) { this.name = mName; }

    public String getService() { return service; }

    public void setService(String mService) { this.service = mService; }

    public String getLocation() { return location; }

    public void setLocation(String mLocation) { this.location = mLocation; }

    public String getThumbImage() { return thumbImage; }

    public void setThumbImage(String mThumbImage) { this.thumbImage = mThumbImage; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public int getPrice() { return price; }

    public void setPrice(int mPrice) { this.price = mPrice; }

    public int getRating() { return rating; }

    public void setRating(int mRating) { this.rating = mRating; }

    public int getReviewsCount() { return reviewsCount; }

    public void setReviewsCount(int mReviewsCount) { this.reviewsCount = mReviewsCount; }

    public int getYearsOfExperience() { return yearsOfExperience; }

    public void setYearsOfExperience(int YearsOfExperience) { this.yearsOfExperience = YearsOfExperience; }

    public Map<String, Review> getReviews() { return reviews; }

    public void setReviews(Map<String, Review> reviews) { this.reviews = reviews; }


    public void incrementReviewCount() { reviewsCount++;}

    public void incrementRating(int newRating) { rating +=newRating;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(service);
        parcel.writeString(location);
        parcel.writeString(thumbImage);
        parcel.writeInt(price);
        parcel.writeInt(rating);
        parcel.writeInt(reviewsCount);
        parcel.writeInt(yearsOfExperience);
    }

    private ServiceProvider(Parcel in){
        this.name = in.readString();
        this.service = in.readString();
        this.location = in.readString();
        this.thumbImage = in.readString();
        this.price = in.readInt();
        this.rating = in.readInt();
        this.reviewsCount = in.readInt();
        this.yearsOfExperience = in.readInt();
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
