package com.example.zufpilosof.assignment3.adapter;

import com.example.zufpilosof.assignment3.model.ServiceProvider;


public class ServiceProviderWithKey {
    private String mKey;
    private ServiceProvider mServiceProvider;

    public ServiceProviderWithKey(String key, ServiceProvider serviceProvider) {
        this.mKey = key;
        this.mServiceProvider = serviceProvider;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public ServiceProvider getServiceProvider() {
        return mServiceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.mServiceProvider = serviceProvider;
    }
}
