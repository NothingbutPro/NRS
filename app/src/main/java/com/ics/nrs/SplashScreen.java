package com.ics.nrs;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.nrs.R;

public class SplashScreen extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {


                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

               /* Intent i = new Intent(SplashScreen.this, UserLoginActivity.class);
                startActivity(i);*/

                // close this activity

                finish();
            }
        }, SPLASH_TIME_OUT);

       /* if (!AppPreference.getCustMobileno(this).equalsIgnoreCase("")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }*/

    }



}
