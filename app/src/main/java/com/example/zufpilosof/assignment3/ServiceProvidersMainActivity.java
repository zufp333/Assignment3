package com.example.zufpilosof.assignment3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.zufpilosof.assignment3.adapter.ServiceProviderWithKey;
import com.example.zufpilosof.assignment3.adapter.ServiceProviderAdapter;
import com.example.zufpilosof.assignment3.model.User;

import java.util.ArrayList;
import java.util.List;

public class ServiceProvidersMainActivity extends AppCompatActivity {

    private final String TAG = "ServiceProvidersMainActivity";
    private DatabaseReference mAllServicesRef;
    private DatabaseReference mMyUserRef;

    private List<ServiceProviderWithKey> songsList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ServiceProviderAdapter mServiceProviderAdapter;
    private User mMyUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(TAG, "onCreate() >>");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_main);


        mRecyclerView = (RecyclerView) findViewById(R.id.songs_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());



        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        if (fbUser != null) {
            mMyUserRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUser.getUid());

            mMyUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    Log.e(TAG, "onDataChange(User) >> " + snapshot.getKey());

                    mMyUser = snapshot.getValue(User.class);

                    getAllSongs();

                    Log.e(TAG, "onDataChange(User) <<");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Log.e(TAG, "onCancelled(Users) >>" + databaseError.getMessage());
                }
            });

            Log.e(TAG, "onCreate() <<");
        } else {
            getAllSongs();
        }
    }

    private void getAllSongs() {


        songsList.clear();
        mServiceProviderAdapter = new com.yanivshani.advancedmusicplayer2.adapter.SongsAdapter(songsList, mMyUser);
        mRecyclerView.setAdapter(mServiceProviderAdapter);

        //getAllSongsUsingValueListenrs();
        getAllSongsUsingChildListenrs();


    }
    private void getAllSongsUsingValueListenrs() {

        mAllServicesRef = FirebaseDatabase.getInstance().getReference("Songs");

        mAllServicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.e(TAG, "onDataChange(Songs) >> " + snapshot.getKey());

                updateSongsList(snapshot);

                Log.e(TAG, "onDataChange(Songs) <<");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "onCancelled(Songs) >>" + databaseError.getMessage());
            }
        });
    }
    private void getAllSongsUsingChildListenrs() {

        mAllServicesRef = FirebaseDatabase.getInstance().getReference("Songs");

        mAllServicesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName){

                Log.e(TAG, "onChildAdded(Songs) >> " + snapshot.getKey());

                com.yanivshani.advancedmusicplayer2.adapter.SongWithKey songWithKey = new com.yanivshani.advancedmusicplayer2.adapter.SongWithKey(snapshot.getKey(),snapshot.getValue(Song.class));
                songsList.add(songWithKey);
                mRecyclerView.getAdapter().notifyDataSetChanged();

                Log.e(TAG, "onChildAdded(Songs) <<");

            }
            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName){

                Log.e(TAG, "onChildChanged(Songs) >> " + snapshot.getKey());

                Song song =snapshot.getValue(Song.class);
                String key = snapshot.getKey();

                for (int i = 0 ; i < songsList.size() ; i++) {
                    com.yanivshani.advancedmusicplayer2.adapter.SongWithKey songWithKey = (com.yanivshani.advancedmusicplayer2.adapter.SongWithKey) songsList.get(i);
                    if (songWithKey.getKey().equals(snapshot.getKey())) {
                        songWithKey.setSong(song);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        break;
                    }
                }

                Log.e(TAG, "onChildChanged(Songs) <<");

            }
            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName){

                Log.e(TAG, "onChildMoved(Songs) >> " + snapshot.getKey());


                Log.e(TAG, "onChildMoved(Songs) << Doing nothing");

            }
            @Override
            public void onChildRemoved(DataSnapshot snapshot){

                Log.e(TAG, "onChildRemoved(Songs) >> " + snapshot.getKey());

                Song song =snapshot.getValue(Song.class);
                String key = snapshot.getKey();

                for (int i = 0 ; i < songsList.size() ; i++) {
                    com.yanivshani.advancedmusicplayer2.adapter.SongWithKey songWithKey = (com.yanivshani.advancedmusicplayer2.adapter.SongWithKey) songsList.get(i);
                    if (songWithKey.getKey().equals(snapshot.getKey())) {
                        songsList.remove(i);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        Log.e(TAG, "onChildRemoved(Songs) >> i="+i);
                        break;
                    }
                }

                Log.e(TAG, "onChildRemoved(Songs) <<");

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "onCancelled(Songs) >>" + databaseError.getMessage());
            }
        });

    }

    private void updateSongsList(DataSnapshot snapshot) {


        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            Song song = dataSnapshot.getValue(Song.class);
            Log.e(TAG, "updateSongList() >> adding song: " + song.getName());
            String key = dataSnapshot.getKey();
            songsList.add(new com.yanivshani.advancedmusicplayer2.adapter.SongWithKey(key,song));
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();

    }


    public void onSearchButtonClick(View v) {

        String searchString = ((EditText)findViewById(R.id.edit_text_search_song)).getText().toString();
        String orderBy = ((RadioButton)findViewById(R.id.radioButtonByReviews)).isChecked() ? "reviewsCount" : "price";
        Query searchSong;

        Log.e(TAG, "onSearchButtonClick() >> searchString="+searchString+ ",orderBy="+orderBy);

        songsList.clear();

        if (searchString != null && !searchString.isEmpty()) {
            searchSong = mAllServicesRef.orderByChild("name").startAt(searchString).endAt(searchString + "\uf8ff");
        } else {
            searchSong = mAllServicesRef.orderByChild(orderBy);
        }


        searchSong.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.e(TAG, "onDataChange(Query) >> " + snapshot.getKey());

                updateSongsList(snapshot);

                Log.e(TAG, "onDataChange(Query) <<");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "onCancelled() >>" + databaseError.getMessage());
            }

        });
        Log.e(TAG, "onSearchButtonClick() <<");
    }

    public void onRadioButtonCLick(View v) {
        switch (v.getId()) {
            case R.id.radioButtonByPrice:
                ((RadioButton)findViewById(R.id.radioButtonByReviews)).setChecked(false);
                break;
            case R.id.radioButtonByReviews:
                ((RadioButton)findViewById(R.id.radioButtonByPrice)).setChecked(false);
                break;
        }
    }
}


