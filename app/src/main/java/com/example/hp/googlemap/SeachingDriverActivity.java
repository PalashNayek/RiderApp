package com.example.hp.googlemap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.rider.ConfirmTrip;
import com.example.hp.googlemap.rider.DashActivity;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.jackandphantom.circularprogressbar.CircleProgressbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class SeachingDriverActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    String set_id;
    private static final String TAG = "SearchingDriverActivity";
    String showPopUpAPIURL = "http://45.55.64.233/Miles/api/search_for_driver.php";
    String car_image, car_tier, driver_name, driver_lat, driver_long, driver_city, car_number, register_date, driver_image;
    TextView avTxt;
    Button backBtn,btnretry;
    CircleProgressbar circleProgressbar;
    String Rider_Lat,Rider_Long;
    //ProgressDialog progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seaching_driver);
        circleProgressbar = (CircleProgressbar) findViewById(R.id.cv);
        circleProgressbar.setBackgroundProgressWidth(25);
        circleProgressbar.setForegroundProgressWidth(25);
        circleProgressbar.enabledTouch(false);
        circleProgressbar.setRoundedCorner(false);
        circleProgressbar.setClockwise(false);
        int animationDuration = 40000; // 2500ms = 2,5s
        circleProgressbar.setProgressWithAnimation(2, animationDuration);
        avTxt = findViewById(R.id.avTxt);
        mToastRunnable.run();
        set_id = getIntent().getStringExtra("set_id");
        //Rider_Lat = getIntent().getStringExtra("Clat");
        Rider_Lat = getIntent().getStringExtra("RiderLat");
        Rider_Long = getIntent().getStringExtra("RiderLong");

        Log.d("RiderCurrentLat", "onCreate: "+Rider_Lat+","+Rider_Long);

        avTxt.setText(getIntent().getStringExtra("TotalDriveAvl"));

        backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SeachingDriverActivity.this,DashActivity.class);
                startActivity(intent);
            }
        });
        btnretry=findViewById(R.id.btnretry);
        btnretry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressFun();
            }
        });
    }

    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            RiderFound();
            mHandler.postDelayed(this, 15000);
        }
    };

    private void RiderFound() {
        StringRequest popUpShow_request = new StringRequest(Request.Method.POST, showPopUpAPIURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("DriverSearchResponse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 400;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }
                if (status == 200) {
                    mHandler.removeCallbacks(mToastRunnable);
                    try {
                        car_image = resObj.getString("car_image");
                        car_tier = resObj.getString("car_tier");
                        driver_name = resObj.getString("driver_name");
                        driver_lat = resObj.getString("driver_lat");
                        driver_long = resObj.getString("driver_long");
                        driver_city = resObj.getString("driver_city");
                        car_number = resObj.getString("car_number");
                        register_date = resObj.getString("register_date");
                        driver_image = resObj.getString("driver_image");
                        String driver_phone = resObj.getString("driver_phone");

                        Intent intent = new Intent(SeachingDriverActivity.this, MeetYourDriverActivity.class);
                        intent.putExtra("car_image", car_image);
                        intent.putExtra("driver_image", driver_image);
                        intent.putExtra("car_tier", car_tier);
                        intent.putExtra("driver_name", driver_name);
                        intent.putExtra("driver_lat", driver_lat);
                        intent.putExtra("driver_long", driver_long);
                        intent.putExtra("driver_city", driver_city);
                        intent.putExtra("car_number", car_number);
                        intent.putExtra("register_date", register_date);
                        intent.putExtra("Rider_Lat", Rider_Lat);
                        intent.putExtra("Rider_Long", Rider_Long);
                        intent.putExtra("set_id", set_id);
                        intent.putExtra("driver_phone", driver_phone);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ", "Error" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ride_id", set_id);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(popUpShow_request);
    }

    private void progressFun()
    {
        finish();
        startActivity(getIntent());
    }



}
