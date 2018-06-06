package com.mta.zufpilosof.assignment3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mta.zufpilosof.assignment3.model.Review;
import com.mta.zufpilosof.assignment3.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.mta.zufpilosof.assignment3.model.ServiceProvider;

public class ReviewActivity extends Activity {

    private final String TAG = "ReviewActivity";
    private ServiceProvider mServiceProvider;
    private String mKey;
    private User mUser;
    private int mPrevRating = -1;
    private AnalyticsManager analyticsManager = AnalyticsManager.getInstance();

    private TextView mUserReview;
    private RatingBar mUserRating;
    private DatabaseReference mServiceProviderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(TAG, "onCreate() >>");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mKey = getIntent().getStringExtra("key");
        mServiceProvider = getIntent().getParcelableExtra("serviceProvider");
        mServiceProvider.setPhone(getIntent().getStringExtra("phone"));
        mUser = getIntent().getParcelableExtra("user");

        mUserReview = findViewById(R.id.new_user_review);
        mUserRating = findViewById(R.id.new_user_rating);


        mServiceProviderRef = FirebaseDatabase.getInstance().getReference("service_providers/" + mKey);

        mServiceProviderRef.child("/reviews/" +  FirebaseAuth.getInstance().getCurrentUser().getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.e(TAG, "onDataChange(Review) >> " + snapshot.getKey());

                Review review = snapshot.getValue(Review.class);
                if (review != null) {
                    mUserReview.setText(review.getUserReview());
                    mUserRating.setRating(review.getUserRating());
                    mPrevRating = review.getUserRating();
                }

                Log.e(TAG, "onDataChange(Review) <<");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "onCancelled(Review) >>" + databaseError.getMessage());
            }
        });

        Log.e(TAG, "onCreate() <<");

    }

    public void onSubmitClick(View v) {

        Log.e(TAG, "onSubmitClick() >>");


        mServiceProviderRef.runTransaction(new Transaction.Handler() {

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                Log.e(TAG, "doTransaction() >>" );


                ServiceProvider serviceProvider = mutableData.getValue(ServiceProvider.class);

                if (serviceProvider == null ) {
                    Log.e(TAG, "doTransaction() << mServiceProvider is null" );
                    return Transaction.success(mutableData);
                }

                if (mPrevRating == -1) {
                    serviceProvider.incrementReviewCount();
                    serviceProvider.incrementRating((int) mUserRating.getRating());
                } else{
                    serviceProvider.incrementRating((int) mUserRating.getRating() - mPrevRating);
                }

                analyticsManager.trackServiceProviderRating(serviceProvider,(int)mUserRating.getRating());
                mutableData.setValue(serviceProvider);
                Log.e(TAG, "doTransaction() << mServiceProvider was set");
                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {

                Log.e(TAG, "onComplete() >>" );

                if (databaseError != null) {
                    Log.e(TAG, "onComplete() << Error:" + databaseError.getMessage());
                    return;
                }

                if (committed) {
                    Review review = new Review(
                            mUserReview.getText().toString(),
                            (int) mUserRating.getRating(),
                            mUser.getEmail());

                    mServiceProviderRef.child("/reviews/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(review);
                }


                Intent intent = new Intent(getApplicationContext(),ServiceProvidersMainActivity.class);
                startActivity(intent);
                finish();

                Log.e(TAG, "onComplete() <<" );
            }
        });
        Log.e(TAG, "onSubmitClick() <<");
    }
}
