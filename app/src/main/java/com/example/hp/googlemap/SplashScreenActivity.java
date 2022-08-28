package com.example.hp.googlemap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity
{
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run() {

            }
        }, SPLASH_TIME_OUT);

        // Check for internetConnection....................
        TextView tv=(TextView)findViewById(R.id.txtview);

        try{
            ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if( activeNetwork==null){
                tv.setText("Not connected to the internet");
                tv.setTextColor(Color.parseColor("#ff0000"));

            }
            /*else if(activeNetwork.isConnected()){
                Intent i = new Intent(SplashScreenActivity.this, LogInPageActivity.class);
                startActivity(i);
                finish();
            }*/

        }catch(Exception e){}

    }
    }

