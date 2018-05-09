package com.example.zufpilosof.assignment3.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zufpilosof.assignment3.model.ServiceProvider;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.zufpilosof.assignment3.R;
import com.example.zufpilosof.assignment3.ServiceProviderDetailsActivity;
import com.example.zufpilosof.assignment3.model.User;

import java.util.Iterator;
import java.util.List;

public class ServiceProviderAdapter extends RecyclerView.Adapter<com.example.zufpilosof.assignment3.adapter.ServiceProviderAdapter.ServiceProviderViewHolder> {

    private final String TAG = "ServiceProviderAdapter";

    private List<ServiceProviderWithKey> mServiceProvidersList;

    private User user;


    public ServiceProviderAdapter(List<ServiceProviderWithKey> serviceProvidersList, User user) {

        this.mServiceProvidersList = serviceProvidersList;
        this.user = user;
    }

    @Override
    public ServiceProviderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.e(TAG,"onCreateViewHolder() >>");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_provider, parent, false);

        Log.e(TAG,"onCreateViewHolder() <<");
        return new ServiceProviderViewHolder(parent.getContext(),itemView);
    }

    @Override
    public void onBindViewHolder(ServiceProviderViewHolder holder, int position) {

        Log.e(TAG,"onBindViewHolder() >> " + position);

        ServiceProvider serviceProvider = mServiceProvidersList.get(position).getServiceProvider();
        String serviceProviderKey = mServiceProvidersList.get(position).getKey();

        StorageReference thumbRef = FirebaseStorage
                .getInstance()
                .getReference()
                .child("thumbs/"+serviceProvider.getThumbImage());
        // Load the image using Glide
        Glide.with(holder.getContext())
                .using(new FirebaseImageLoader())
                .load(thumbRef)
                .into(holder.getThumbImage());

        holder.setSelectedServiceProvider(serviceProvider);
        holder.setSelectedServiceProviderKey(serviceProviderKey);
        holder.getName().setText(serviceProvider.getName());
        holder.getService().setText(serviceProvider.getService());
        holder.getLocation().setText(serviceProvider.getLocation());
        holder.getYearsOfExperience().setText(serviceProvider.getYearsOfExperience()+" Years");
        holder.setThumbFile(serviceProvider.getThumbImage());

        if (serviceProvider.getReviewsCount() >0) {
            holder.getReviewsCount().setText("("+serviceProvider.getReviewsCount()+")");
            holder.getRating().setRating((float)(serviceProvider.getRating() / serviceProvider.getReviewsCount()));
        }

        holder.getPrice().setText("$"+serviceProvider.getPrice());

        Iterator i = user.getMyServiceRequests().iterator();
        while (i.hasNext()) {
            if (i.next().equals(serviceProviderKey)) {
                holder.getPrice().setTextColor(R.color.colorPrimary);
                break;
            }
        }

        Log.e(TAG,"onBindViewHolder() << "+ position);
    }


    @Override
    public int getItemCount() {
        return mServiceProvidersList.size();
    }

    public class ServiceProviderViewHolder extends RecyclerView.ViewHolder {
        //fix
        private CardView mServiceProviderCardView;
        private ImageView mThumbImage;
        private String mThumbFile;
        private TextView mName;
        private TextView mService;
        private TextView mLocation;
        private TextView mPrice;
        private TextView mYearsOfExperience;
        private TextView mReviewsCount;
        private Context context;
        private RatingBar mRating;
        private ServiceProvider mSelectedServiceProvider;
        private String mSelectedServiceProviderKey;


        public ServiceProviderViewHolder(Context context, View view) {
            super(view);
            // fix - build the UI with all params
            mServiceProviderCardView = (CardView) view.findViewById(R.id.card_view_song);
            mThumbImage = (ImageView) view.findViewById(R.id.provider_thumb_image);
            mName = (TextView) view.findViewById(R.id.provider_name);
            mService = (TextView) view.findViewById(R.id.provider_service);
            mLocation = (TextView) view.findViewById(R.id.provider_loctaion);
            mPrice = (TextView) view.findViewById(R.id.provider_price);
            mReviewsCount = (TextView) view.findViewById(R.id.provider_reviews_count);
            mRating = (RatingBar) view.findViewById(R.id.provider_rating);
            mYearsOfExperience = (TextView) view.findViewById(R.id.provider_years_of_experience);
            this.context = context;

            mServiceProviderCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e(TAG, "CardView.onClick() >> mName=" + mSelectedServiceProvider.getName());

                    Context context = view.getContext();
                    Intent intent = new Intent(context, ServiceProviderDetailsActivity.class);
                    intent.putExtra("serviceProvider", mSelectedServiceProvider);
                    intent.putExtra("key", mSelectedServiceProviderKey);
                    intent.putExtra("user",user);
                    context.startActivity(intent);
                }
            });
        }

        public TextView getPrice() {
            return mPrice;
        }

        public TextView getName() {
            return mName;
        }

        public TextView getService() {
            return mService;
        }

        public void setService(TextView mService) {
            this.mService = mService;
        }

        public TextView getLocation() {
            return mLocation;
        }

        public void setLocation(TextView mLocation) {
            this.mLocation = mLocation;
        }

        public TextView getYearsOfExperience() {
            return mYearsOfExperience;
        }

        public void setYearsOfExperience(TextView mYearsOfExperience) {
            this.mYearsOfExperience = mYearsOfExperience;
        }

        public ImageView getThumbImage() {
            return mThumbImage;
        }

        public void setThumbFile(String mThumbFile) {
            this.mThumbFile = mThumbFile;
        }

        public Context getContext() {
            return context;
        }

        public RatingBar getRating() {
            return mRating;
        }

        public void setSelectedServiceProvider(ServiceProvider mSelectedServiceProvider) {
            this.mSelectedServiceProvider = mSelectedServiceProvider;
        }

        public void setSelectedServiceProviderKey(String selectedServiceProviderKey) {
            this.mSelectedServiceProviderKey = selectedServiceProviderKey;
        }

        public TextView getReviewsCount() {return mReviewsCount;}
    }
}
