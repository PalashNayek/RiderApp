package com.example.hp.googlemap.driver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.EditPageActivity;
import com.example.hp.googlemap.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class RiderRequestActivity extends AppCompatActivity {
    private CircleProgressbar cv;
    private Button btn_accept;
    String ProfileDetailsAPIURL = "http://45.55.64.233/Miles/driver/accept_ride.php";
    String rejectAPIURL = "http://45.55.64.233/Miles/driver/reject_ride.php";
    String imageBasePath = "http://45.55.64.233/Miles/driver/";
    private static final String TAG = RiderRequestActivity.class.getSimpleName();
    String RidePickUpLat, RiderPickUpLong;
    TextView locationLat, locationLong;
    String lattitude = "", longitude = "";
    String tv_picupaddr;
    String tv_username, tv_userrating, tv_userjoined, circleimg_user = "", drop_lat, drop_long, drop_address;
    private FusedLocationProviderClient client;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String ride_id,user_id;
    TextView car_tierTxt,userNameTxt,ratingTxt,pickUpAddressTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_request);
        sharedPreferences = getApplicationContext().getSharedPreferences("driver_pref", 0);
        editor = sharedPreferences.edit();
        car_tierTxt=(TextView)findViewById(R.id.car_tierTxt);
        userNameTxt=(TextView)findViewById(R.id.userNameTxt);
        ratingTxt=(TextView)findViewById(R.id.ratingTxt);
        pickUpAddressTxt=(TextView)findViewById(R.id.pickUpAddressTxt);

        ride_id = getIntent().getStringExtra("ride_id");
        car_tierTxt.setText(getIntent().getStringExtra("car_tier"));
        userNameTxt.setText(getIntent().getStringExtra("user_name"));
        ratingTxt.setText(getIntent().getStringExtra("rating"));
        pickUpAddressTxt.setText(getIntent().getStringExtra("pickup_address"));
        //getInformation();
        CircleProgressbar circleProgressbar = (CircleProgressbar) findViewById(R.id.cv);
        btn_accept = findViewById(R.id.btn_accept);
        //  circleProgressbar.setForegroundProgressColor(Color.RED);
        //  circleProgressbar.setBackgroundColor(Color.GREEN);
        circleProgressbar.setBackgroundProgressWidth(25);
        circleProgressbar.setForegroundProgressWidth(25);
        circleProgressbar.enabledTouch(false);
        circleProgressbar.setRoundedCorner(false);
        circleProgressbar.setClockwise(false);
        int animationDuration = 40000; // 2500ms = 2,5s
        circleProgressbar.setProgressWithAnimation(0, animationDuration);
        //Current LatLong...........
        locationLat = (TextView) findViewById(R.id.locationLat);
        locationLong = (TextView) findViewById(R.id.locationLong);
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(RiderRequestActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(RiderRequestActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latti = location.getLatitude();
                    double longi = location.getLongitude();
                    lattitude = String.valueOf(latti);
                    longitude = String.valueOf(longi);
                    locationLat.setText(lattitude);
                    locationLong.setText(longitude);
                    System.out.println(lattitude+","+longitude);
                }
            }
        });
    }

    public void onButtonClickaccept(View view) {

        getInformation();
    }

    public void onButtonClickreject(View view) {
        rejectInformation();
    }

    private void getInformation() {
        StringRequest profile_details_request = new StringRequest(Request.Method.POST, ProfileDetailsAPIURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("acceptResponse", "onResponse: " + response);
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
                    try {

                       /* imagePath = resObj.getString("image");
                        Picasso.with(EditPageActivity.this).load(imagePath).resize(100, 100).into(circleImageViewPro);
                        // Log.d(TAG, "onResponse: "+imagePath);*/
                        RidePickUpLat = resObj.getString("pickup_lat");
                        RiderPickUpLong = resObj.getString("pickup_long");
                        tv_picupaddr = resObj.getString("pickup_address");
                        tv_username = resObj.getString("user_name");
                        tv_userrating = resObj.getString("rating");
                        tv_userjoined = resObj.getString("user_joining_date");
                        circleimg_user = resObj.getString("user_image");

                        drop_lat = resObj.getString("drop_lat");
                        drop_long = resObj.getString("drop_long");
                        drop_address = resObj.getString("drop_address");
                        user_id = resObj.getString("user_id");
                       String user_phone = resObj.getString("user_phone");


                        Intent intent = new Intent(RiderRequestActivity.this, MeetYourRiderActivity.class);
                        intent.putExtra("PickUpLat", RidePickUpLat);
                        intent.putExtra("PickUpLong", RiderPickUpLong);
                        intent.putExtra("DriverLat", lattitude);
                        intent.putExtra("DriverLong", longitude);
                        intent.putExtra("pickup_address", tv_picupaddr);
                        intent.putExtra("user_name", tv_username);
                        intent.putExtra("rating", tv_userrating);
                        intent.putExtra("user_joining_date", tv_userjoined);
                        intent.putExtra("user_image", circleimg_user);

                        intent.putExtra("drop_lat", drop_lat);
                        intent.putExtra("drop_long", drop_long);
                        intent.putExtra("drop_address", drop_address);

                        intent.putExtra("ride_id", ride_id);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("user_phone", user_phone);
                        startActivity(intent);


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
                //params.put( "prk_veh_trc_dtl_id", "7" );
                params.put("driver_id", sharedPreferences.getString("driver_id", ""));
                params.put("ride_id", ride_id);
                Log.d("AcceptBtnClick", "getParams: " + params);
                return params;

                //user_admin_id,token

            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(profile_details_request);


    }
    private void rejectInformation() {
        StringRequest reject_request = new StringRequest(Request.Method.POST, rejectAPIURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
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
                    Intent intent = new Intent(RiderRequestActivity.this, HomeActivity.class);
                    startActivity(intent);
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
                //params.put( "prk_veh_trc_dtl_id", "7" );
                params.put("driver_id", sharedPreferences.getString("driver_id", ""));
                params.put("ride_id", ride_id);
                Log.d("AcceptBtnClick", "getParams: " + params);
                return params;

                //user_admin_id,token

            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(reject_request);


    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onBackPressed() {
        if (!shouldAllowBack()) {
            //doSomething();
        } else {
            super.onBackPressed();
        }
    }

    private boolean shouldAllowBack() {
        return false;
    }

}
