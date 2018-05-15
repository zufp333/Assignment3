package com.example.zufpilosof.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.zufpilosof.assignment3.model.ServiceProvider;
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

    private List<ServiceProviderWithKey> mServiceProvidersList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ServiceProviderAdapter mServiceProviderAdapter;
    private User mMyUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(TAG, "onCreate() >>");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_providers_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.service_providers_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        if (fbUser.isAnonymous())
            FirebaseAuth.getInstance().signOut();

        if (fbUser != null) {
            mMyUserRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUser.getUid());

            mMyUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    Log.e(TAG, "onDataChange(User) >> " + snapshot.getKey());

                    // fix always null:
                    mMyUser = snapshot.getValue(User.class);

                    getAllServiceProviders();

                    Log.e(TAG, "onDataChange(User) <<");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Log.e(TAG, "onCancelled(Users) >>" + databaseError.getMessage());
                }
            });

            Log.e(TAG, "onCreate() <<");
        } else {
            getAllServiceProviders();
        }
    }

    private void getAllServiceProviders() {
        mServiceProvidersList.clear();
        mServiceProviderAdapter = new com.example.zufpilosof.assignment3.adapter.ServiceProviderAdapter(mServiceProvidersList, mMyUser);
        mRecyclerView.setAdapter(mServiceProviderAdapter);

      //  getAllServiceProvidersUsingValueListenrs();fix
        getAllServiceProvidersUsingChildListeners();


    }
    private void getAllServiceProvidersUsingValueListenrs() {

        mAllServicesRef = FirebaseDatabase.getInstance().getReference("service_providers");

        mAllServicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.e(TAG, "onDataChange(Songs) >> " + snapshot.getKey());

                updateServiceProvidersList(snapshot);

                Log.e(TAG, "onDataChange(Songs) <<");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "onCancelled(service_providers) >>" + databaseError.getMessage());
            }
        });
    }
    private void getAllServiceProvidersUsingChildListeners() {

        mAllServicesRef = FirebaseDatabase.getInstance().getReference("service_providers");

        mAllServicesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName){

                Log.e(TAG, "onChildAdded(service_providers) >> " + snapshot.getKey());

                com.example.zufpilosof.assignment3.adapter.ServiceProviderWithKey serviceProviderWithKey = new com.example.zufpilosof.assignment3.adapter.ServiceProviderWithKey(snapshot.getKey(),snapshot.getValue(ServiceProvider.class));
                mServiceProvidersList.add(serviceProviderWithKey);
                mRecyclerView.getAdapter().notifyDataSetChanged();

                Log.e(TAG, "onChildAdded(service_providers) <<");

            }
            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName){

                Log.e(TAG, "onChildChanged(service_providers) >> " + snapshot.getKey());

                ServiceProvider song =snapshot.getValue(ServiceProvider.class);
                String key = snapshot.getKey();

                for (int sp = 0; sp < mServiceProvidersList.size() ; sp++) {
                    com.example.zufpilosof.assignment3.adapter.ServiceProviderWithKey serviceProviderWithKey = (com.example.zufpilosof.assignment3.adapter.ServiceProviderWithKey) mServiceProvidersList.get(sp);
                    if (serviceProviderWithKey.getKey().equals(snapshot.getKey())) {
                        serviceProviderWithKey.setServiceProvider(song);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        break;
                    }
                }

                Log.e(TAG, "onChildChanged(service_providers) <<");

            }
            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName){

                Log.e(TAG, "onChildMoved(service_providers) >> " + snapshot.getKey());


                Log.e(TAG, "onChildMoved(service_providers) << Doing nothing");

            }
            @Override
            public void onChildRemoved(DataSnapshot snapshot){

                Log.e(TAG, "onChildRemoved(service_providers) >> " + snapshot.getKey());

                ServiceProvider serviceProvider = snapshot.getValue(ServiceProvider.class);
                String key = snapshot.getKey();

                for (int sp = 0; sp < mServiceProvidersList.size() ; sp++) {
                    com.example.zufpilosof.assignment3.adapter.ServiceProviderWithKey serviceProviderWithKey = (com.example.zufpilosof.assignment3.adapter.ServiceProviderWithKey) mServiceProvidersList.get(sp);
                    if (serviceProviderWithKey.getKey().equals(snapshot.getKey())) {
                        mServiceProvidersList.remove(sp);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        Log.e(TAG, "onChildRemoved(service_providers) >> sp="+sp);
                        break;
                    }
                }

                Log.e(TAG, "onChildRemoved(service_providers) <<");

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "onCancelled(service_providers) >>" + databaseError.getMessage());
            }
        });

    }

    private void updateServiceProvidersList(DataSnapshot snapshot) {


        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            ServiceProvider serviceProvider = dataSnapshot.getValue(ServiceProvider.class);
            Log.e(TAG, "updateSongList() >> adding service provider: " + serviceProvider.getName());
            String key = dataSnapshot.getKey();
            mServiceProvidersList.add(new com.example.zufpilosof.assignment3.adapter.ServiceProviderWithKey(key,serviceProvider));
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();

    }


    public void onSearchButtonClick(View v) {

        String searchString = ((EditText)findViewById(R.id.edit_text_search_service)).getText().toString();
        String orderBy = ((RadioButton)findViewById(R.id.radioButtonByYearsOfExperience)).isChecked() ? "yearsOfExperience" : "price";
        Query searchServiceProvider;

        Log.e(TAG, "onSearchButtonClick() >> searchString="+searchString+ ",orderBy="+orderBy);

        mServiceProvidersList.clear();

        if (searchString != null && !searchString.isEmpty()) {
            searchServiceProvider = mAllServicesRef.orderByChild("service").startAt(searchString).endAt(searchString + "\uf8ff");
        } else {
            searchServiceProvider = mAllServicesRef.orderByChild(orderBy);
        }


        searchServiceProvider.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.e(TAG, "onDataChange(Query) >> " + snapshot.getKey());

                updateServiceProvidersList(snapshot);

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
                ((RadioButton)findViewById(R.id.radioButtonByYearsOfExperience)).setChecked(false);
                break;
            case R.id.radioButtonByYearsOfExperience:
                ((RadioButton)findViewById(R.id.radioButtonByPrice)).setChecked(false);
                break;
        }
    }

    public void onSignOutButtonCLick(View v) {
        Log.e(TAG, "onSignOutButtonCLick() >>");

        if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
            FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(i);
        finish();
        Log.e(TAG, "onSignOutButtonCLick() <<");
    }
}


