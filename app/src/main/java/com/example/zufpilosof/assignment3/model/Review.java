package com.example.zufpilosof.assignment3.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Review {
    private String mUserReview;
    private int mUserRating;
    private String mUserEmail;

    public Review(String userReview, int userRating, String userEmail) {
        this.mUserReview = userReview;
        this.mUserRating = userRating;
        this.mUserEmail = userEmail;
    }

    public Review() {
    }

    public String getUserReview() {
        return mUserReview;
    }

    public int getUserRating() {
        return mUserRating;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("userReview", mUserReview);
        result.put("userRating", mUserRating);
        result.put("userEmail", mUserEmail);
        return result;
    }
}
