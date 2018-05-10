package com.example.zufpilosof.assignment3;

import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent;

               if(user.isAnonymous() || user == null)
                   intent = new Intent(getApplicationContext(), SignInActivity.class);

               else
                   intent = new Intent(getApplicationContext(), ServiceProvidersMainActivity.class);

                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
