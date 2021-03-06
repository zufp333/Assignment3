package com.example.zufpilosof.assignment3;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.zufpilosof.assignment3.adapter.ReviewsAdapter;
import com.example.zufpilosof.assignment3.model.Review;
import com.example.zufpilosof.assignment3.model.ServiceProvider;
import com.example.zufpilosof.assignment3.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServiceProviderDetailsActivity extends AppCompatActivity {

    public final String TAG = "ServiceProviderDetailsActivity";
    private ServiceProvider mServiceProvider;
    private String mKey;
    private User mUser;

    private FloatingActionButton mWriteReview;
    private Button mBuyPlay;
    private MediaPlayer mMediaPlayer;
    private RecyclerView mRecyclerViewServiceProviderReviews;

    private DatabaseReference mServiceProviderReviewsRef;

    private List<Review> mReviewsList =  new ArrayList<>();

    private boolean mServiceProviderWasPurchased;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(TAG, "onCreate() >>");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_details);

        mKey = getIntent().getStringExtra("mKey");
        mServiceProvider = getIntent().getParcelableExtra("mServiceProvider");
        mUser = getIntent().getParcelableExtra("mUser");

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        StorageReference thumbRef = FirebaseStorage
                .getInstance()
                .getReference()
                .child("thumbs/" + mServiceProvider.getThumbImage());

        // Load the image using Glide
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(thumbRef)
                .into((ImageView) findViewById(R.id.imageViewServiceProvider));

        ((TextView) findViewById(R.id.textViewName)).setText(mServiceProvider.getName());
        ((TextView) findViewById(R.id.textViewService)).setText(mServiceProvider.getService());
        ((TextView) findViewById(R.id.textViewLocation)).setText(mServiceProvider.getLocation());
        ((TextView) findViewById(R.id.textViewYearsOfExperience)).setText(mServiceProvider.getYearsOfExperience());

        //mBuyPlay = ((Button) findViewById(R.id.buttonBuyPlay)); // fix - no need
        //
        //mBuyPlay.setText("BUY $" + mServiceProvider.getPrice()); // fix - no need maybe different
        //Iterator i = mUser.getmMyServiceRequests().iterator();
        //while (i.hasNext()) {
        //    if (i.next().equals(mKey)) {
        //        mServiceProviderWasPurchased = true;
        //        mBuyPlay.setText("PLAY");
        //        break;
        //    }
        //}


       //mBuyPlay.setOnClickListener(new View.OnClickListener() {
       //    @Override
       //    public void onClick(View view) {
       //        Log.e(TAG, "mBuyPlay.onClick() >> file=" + mServiceProvider.getName());
       //        if (mServiceProviderWasPurchased) {
       //            Log.e(TAG, "mBuyPlay.onClick() >> Playing purchased mServiceProvider");
       //            //User purchased the mServiceProvider so he can play it
       //            playCurrentSong(mServiceProvider.getFile());
       //        } else {
       //            //Purchase the mServiceProvider.
       //            Log.e(TAG, "mBuyPlay.onClick() >> Purchase the mServiceProvider");
       //            mUser.getMySongs().add(mKey);
       //            mUser.upgdateTotalPurchase(mServiceProvider.getPrice());
       //            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
       //            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mUser);
       //            mServiceProviderWasPurchased = true;
       //            mBuyPlay.setText("PLAY");
       //        }
       //        Log.e(TAG, "playSong.onClick() <<");
       //    }
       //});

        mWriteReview = (FloatingActionButton) findViewById(R.id.buttonNewReview);

        mWriteReview.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View view) {
                   Log.e(TAG, "mWriteReview.onClick() >>");


                   Intent intent = new Intent(getApplicationContext(),ReviewActivity.class);
                   intent.putExtra("mServiceProvider", mServiceProvider);
                   intent.putExtra("mKey", mKey);
                   intent.putExtra("mUser", mUser);

                   startActivity(intent);
                   finish();

                   Log.e(TAG, "mWriteReview.onClick() <<");
               }
           }
        );

        mRecyclerViewServiceProviderReviews = findViewById(R.id.service_provider_reviews);
        mRecyclerViewServiceProviderReviews.setHasFixedSize(true);
        mRecyclerViewServiceProviderReviews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerViewServiceProviderReviews.setItemAnimator(new DefaultItemAnimator());


        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(mReviewsList);
        mRecyclerViewServiceProviderReviews.setAdapter(reviewsAdapter);

        mServiceProviderReviewsRef = FirebaseDatabase.getInstance().getReference("Service providers/" + mKey +"/reviews");

        mServiceProviderReviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        Log.e(TAG, "onDataChange() >> Songs/" + mKey);

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Review review = dataSnapshot.getValue(Review.class);
                            mReviewsList.add(review);
                        }
                        mRecyclerViewServiceProviderReviews.getAdapter().notifyDataSetChanged();
                        Log.e(TAG, "onDataChange(Review) <<");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Log.e(TAG, "onCancelled(Review) >>" + databaseError.getMessage());
                    }
                });
        Log.e(TAG, "onCreate() <<");

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlayingCurrentSong();
    }

    private void playCurrentSong(String songFile) {

        Log.e(TAG, "playCurrentSong() >> songFile=" + songFile);

        if (stopPlayingCurrentSong()) {
            Log.e(TAG, "playCurrentSong() << Stop playing current mServiceProvider");
            return;
        }

        FirebaseStorage.getInstance()
                .getReference("songs/" + songFile)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        Log.e(TAG, "onSuccess() >> " + downloadUrl.toString());

                        try {

                            mMediaPlayer.setDataSource(downloadUrl.toString());
                            mMediaPlayer.prepare(); // might take long! (for buffering, etc)
                            mMediaPlayer.start();
                            mBuyPlay.setText("STOP");


                        } catch (Exception e) {
                            Log.w(TAG, "playSong() error:" + e.getMessage());
                        }

                        Log.e(TAG, "onSuccess() <<");
                    }
                });
        Log.e(TAG, "playCurrentSong() << ");
    }

    private boolean stopPlayingCurrentSong() {

        if (mMediaPlayer.isPlaying()) {
            Log.e(TAG, "onSuccess() >> Stop the media player");
            //Stop the media player
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mBuyPlay.setText("PLAY");
            return true;
        }
        return false;
    }
}