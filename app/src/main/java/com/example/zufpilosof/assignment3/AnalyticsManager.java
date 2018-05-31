package com.example.zufpilosof.assignment3;

/**
 * Created by zuf pilosof on 24-May-18.
 */

import android.content.Context;
import android.os.Bundle;

import com.example.zufpilosof.assignment3.model.ServiceProvider;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public class AnalyticsManager {
    private static String TAG = "AnalyticsManager";
    private static AnalyticsManager mInstance = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    private MixpanelAPI mMixpanel;

    private AnalyticsManager() {
    }

    public static AnalyticsManager getInstance() {

        if (mInstance == null) {
            mInstance = new AnalyticsManager();
        }
        return (mInstance);
    }

    public void init(Context context) {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        //Token  can be found in Mixpanel console under Project Settings >> Management >> Token
        mMixpanel = MixpanelAPI.getInstance(context, "5c66ca1814711713856b4c2c9200b108");

    }

    public void setUserID(String id, boolean newUser) {

        mFirebaseAnalytics.setUserId(id);


        if (newUser) {
            mMixpanel.alias(id, null);
        }
        mMixpanel.identify(id);
        mMixpanel.getPeople().identify(mMixpanel.getDistinctId());

        //Sender id can be found in the Firebase console under Project Settings >>CLOUD MESSAGING TAB >> Sender ID
        mMixpanel.getPeople().initPushHandling("21826983071");
    }

    public void setUserProperty(String name, String value) {

        mFirebaseAnalytics.setUserProperty(name, value);

        mMixpanel.getPeople().set(name, value);
    }

    public void trackSearchEvent(String searchString) {

        String eventName = "search";

        //Firebase
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchString);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,params);

        //MixPanel
        Map<String, Object> eventParams = new HashMap<String, Object>();
        eventParams.put("search term", searchString);
        mMixpanel.trackMap(eventName,eventParams);
    }

    public void trackCallEvent(String event , ServiceProvider serviceProvider) {
        Bundle params = new Bundle();

        params.putString("serviceProvider_phone",serviceProvider.getPhone());
        mFirebaseAnalytics.logEvent(event,params);

        Map<String, Object> eventParams = new HashMap<String, Object>();
        eventParams.put("serviceProvider_phone", serviceProvider.getPhone());
        mMixpanel.trackMap(event,eventParams);
    }

    public void trackSignupEvent(String signupMethod) {

        String eventName = "signup";

        //Firebase
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, signupMethod);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP,params);

        //MixPanel
        Map<String, Object> eventParams = new HashMap<String, Object>();
        eventParams.put("signup method", signupMethod);
        mMixpanel.trackMap(eventName,eventParams);
    }


    public void trackLoginEvent(String loginMethod) {

        String eventName = "login";

        //FireBase
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, loginMethod);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN,params);

        //MixPanel
        Map<String, Object> eventParams = new HashMap<String, Object>();
        eventParams.put("signup method", loginMethod);
        mMixpanel.trackMap(eventName,eventParams);
    }

    public void trackServiceProviderEvent(String event , ServiceProvider serviceProvider) {
        Bundle params = new Bundle();

        params.putString("serviceProvider_name", serviceProvider.getName());
        params.putString("serviceProvider_service", serviceProvider.getService());
        params.putString("serviceProvider_location",serviceProvider.getLocation());
        params.putString("serviceProvider_phone",serviceProvider.getPhone());
        params.putInt("serviceProvider_rating",serviceProvider.getRating());
        params.putInt("serviceProvider_price",serviceProvider.getPrice());
        params.putInt("serviceProvider_yearsOfExperience",serviceProvider.getYearsOfExperience());

        mFirebaseAnalytics.logEvent(event,params);

        Map<String, Object> eventParams = new HashMap<String, Object>();
        eventParams.put("serviceProvider_name", serviceProvider.getName());
        eventParams.put("serviceProvider_service", serviceProvider.getService());
        eventParams.put("serviceProvider_location", serviceProvider.getLocation());
        eventParams.put("serviceProvider_phone", serviceProvider.getPhone());
        eventParams.put("serviceProvider_price",String.valueOf(serviceProvider.getPrice()));
        eventParams.put("serviceProvider_rating",String.valueOf(serviceProvider.getRating()));
        eventParams.put("serviceProvider_yearsOfExperience",String.valueOf(serviceProvider.getYearsOfExperience()));

        mMixpanel.trackMap(event,eventParams);
    }

    public void trackPurchase(ServiceProvider serviceProvider) {

        String eventName = "purchase";
        Bundle params = new Bundle();
        params.putDouble(FirebaseAnalytics.Param.PRICE,serviceProvider.getPrice());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE,params);

        Map<String, Object> eventParams = new HashMap<String, Object>();
        eventParams.put("serviceProvider_name", serviceProvider.getName());
        eventParams.put("serviceProvider_service", serviceProvider.getService());
        eventParams.put("serviceProvider_location", serviceProvider.getLocation());
        eventParams.put("serviceProvider_phone", serviceProvider.getPhone());
        eventParams.put("serviceProvider_price",String.valueOf(serviceProvider.getPrice()));
        eventParams.put("serviceProvider_rating",String.valueOf(serviceProvider.getRating()));
        eventParams.put("serviceProvider_yearsOfExperience",String.valueOf(serviceProvider.getYearsOfExperience()));

        mMixpanel.trackMap(eventName,eventParams);
    }

    public void trackServiceProviderRating(ServiceProvider serviceProvider , int userRating) {

        String eventName = "serviceProvider_rating";
        Bundle params = new Bundle();

        params.putString("serviceProvider_name", serviceProvider.getName());
        params.putString("serviceProvider_service", serviceProvider.getService());
        params.putString("serviceProvider_location", serviceProvider.getLocation());
        params.putString("serviceProvider_phone", serviceProvider.getPhone());
        params.putInt("serviceProvider_price",serviceProvider.getPrice());
        params.putInt("serviceProvider_reviews_count",serviceProvider.getReviewsCount());
        params.putInt("serviceProvider_total_rating",serviceProvider.getRating());
        params.putInt("serviceProvider_yearsOfExperience",serviceProvider.getYearsOfExperience());
        params.putInt("serviceProvider_user_rating",userRating);

        mFirebaseAnalytics.logEvent(eventName,params);

        Map<String, Object> eventParams = new HashMap<String, Object>();
        eventParams.put("serviceProvider_name", serviceProvider.getName());
        eventParams.put("serviceProvider_service", serviceProvider.getService());
        eventParams.put("serviceProvider_location", serviceProvider.getLocation());
        eventParams.put("serviceProvider_phone", serviceProvider.getPhone());
        eventParams.put("serviceProvider_price",serviceProvider.getPrice());
        eventParams.put("serviceProvider_reviews_count",serviceProvider.getReviewsCount());
        eventParams.put("serviceProvider_total_rating",serviceProvider.getRating());
        eventParams.put("serviceProvider_yearsOfExperience",serviceProvider.getYearsOfExperience());
        eventParams.put("serviceProvider_user_rating",userRating);

        mMixpanel.trackMap(eventName,eventParams);
    }


    public void trackAnonymousLogin(String loginMethod) {
        String eventName = "login";

        //FireBase
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, loginMethod);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN,params);

        //MixPanel
        Map<String, Object> eventParams = new HashMap<String, Object>();
        eventParams.put("signup method", loginMethod);
        mMixpanel.trackMap(eventName,eventParams);
    }
}