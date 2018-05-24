package com.example.zufpilosof.assignment3;

/**
 * Created by zuf pilosof on 24-May-18.
 */

import android.content.Context;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

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
        mMixpanel = MixpanelAPI.getInstance(context, "e30ee60247653f376308d11759f38c47");

    }

    public void setUserID(String id, boolean newUser) {

        mFirebaseAnalytics.setUserId(id);


        if (newUser) {
            mMixpanel.alias(id, null);
        }
        mMixpanel.identify(id);
        mMixpanel.getPeople().identify(mMixpanel.getDistinctId());

        //Sender id can be found in the Firebase console under Project Settings >>CLOUD MESSAGING TAB >> Sender ID

        mMixpanel.getPeople().initPushHandling("573543394206");
    }

    public void setUserProperty(String name, String value) {

        mFirebaseAnalytics.setUserProperty(name, value);

        mMixpanel.getPeople().set(name, value);
    }

}

