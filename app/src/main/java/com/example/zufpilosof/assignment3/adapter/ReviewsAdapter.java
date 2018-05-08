package com.example.zufpilosof.assignment3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.zufpilosof.assignment3.R;
import com.example.zufpilosof.assignment3.model.Review;

import java.util.List;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private final String TAG = "ReviewsAdapter";

    private List<Review> reviewsList;

    public ReviewsAdapter(List<Review> reviewsList) {

        this.reviewsList = reviewsList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.e(TAG,"onCreateViewHolder() >>");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);

        Log.e(TAG,"onCreateViewHolder() <<");
        return new ReviewViewHolder(parent.getContext(),itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

        Log.e(TAG,"onBindViewHolder() >> " + position);

        Review review = reviewsList.get(position);

        holder.getUserMail().setText(review.getUserEmail());
        holder.getUserReview().setText(review.getUserReview());
        holder.getUserRating().setRating(review.getUserRating());

        Log.e(TAG,"onBindViewHolder() << "+ position);
    }


    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView mUserReview;
        private TextView mUserMail;
        private RatingBar mUserRating;

        public ReviewViewHolder(Context context, View view) {

            super(view);
            mUserReview = (TextView) view.findViewById(R.id.user_review);
            mUserMail = (TextView) view.findViewById(R.id.user_mail);
            mUserRating = (RatingBar) view.findViewById(R.id.user_rating);

        }

        public TextView getUserReview() {
            return mUserReview;
        }

        public void setUserReview(TextView userReview) {
            this.mUserReview = userReview;
        }

        public TextView getUserMail() {
            return mUserMail;
        }

        public void setUserMail(TextView userMail) {
            this.mUserMail = userMail;
        }

        public RatingBar getUserRating() {
            return mUserRating;
        }

        public void setUserRating(RatingBar userRating) {
            this.mUserRating = userRating;
        }
    }
}
