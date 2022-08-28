package com.example.hp.googlemap.driver;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.DrawRouteMaps;
import com.example.hp.googlemap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeetYourRiderActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Button btn_navigate;
    private CircleImageView circleimg_user;
    private TextView tv_username, tv_userrating, tv_userjoined, tv_picupaddr;
    private GoogleMap mMap;
    String DriverLat, DriverLong;
    String PickUpLat, PickUpLong;
    String markAdd, markAddCurrentLoc;
    String pickup_address;
    String user_name, rating, user_joining_date, user_image = "", drop_lat, drop_long, drop_address;
    String imageBasePath = "http://45.55.64.233/Miles/driver/";
    String upDateDriverLatLongURL = "http://45.55.64.233/Miles/driver/checking_pickup_reach.php";
    String cancelResonAPI = "http://45.55.64.233/Miles/driver/cancel_ride.php";
    String reginTripAPI = "http://45.55.64.233/Miles/driver/reassign_trip.php";
    private static final String TAG = MeetYourRiderActivity.class.getSimpleName();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final int REQUEST_LOCATION = 1;
    TextView textView;
    LocationManager locationManager;
    String lattitude, longitude;
    String DriverCurrentLat = "";
    String DriverCurrentLong = "";
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE=1;
    String ride_id,user_id;
    Button reginTripBtn;
    ImageView cancelTrip;
    String values;
    ImageView phoneCallImageView;
    String user_phone;
    String CurrentLat, CurrentLong;
    private Handler mHandler = new Handler();

    //TextView txtNotCharge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_your_rider);

        sharedPreferences = getApplicationContext().getSharedPreferences("driver_pref", 0);
        editor = sharedPreferences.edit();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_navigate = findViewById(R.id.btn_navigate);
        circleimg_user = findViewById(R.id.circleimg_user);
        tv_username = findViewById(R.id.tv_username);
        tv_userrating = findViewById(R.id.tv_userrating);
        tv_userjoined = findViewById(R.id.tv_userjoined);
        tv_picupaddr = findViewById(R.id.tv_picupaddr);
        cancelTrip = findViewById(R.id.cancelTrip);
        reginTripBtn = findViewById(R.id.reginTripBtn);

        ride_id = getIntent().getStringExtra("ride_id");
        user_id = getIntent().getStringExtra("user_id");


        PickUpLat = getIntent().getStringExtra("PickUpLat");
        PickUpLong = getIntent().getStringExtra("PickUpLong");

        pickup_address = getIntent().getStringExtra("pickup_address");
        tv_picupaddr.setText(pickup_address);
        drop_lat = getIntent().getStringExtra("drop_lat");
        drop_long = getIntent().getStringExtra("drop_long");
        /*DriverLat = getIntent().getStringExtra("22.503193");
        DriverLong = getIntent().getStringExtra("88.388237");*/
        user_name = getIntent().getStringExtra("user_name");
        tv_username.setText(user_name);
        rating = getIntent().getStringExtra("rating");
        tv_userrating.setText(rating);
        user_joining_date = getIntent().getStringExtra("user_joining_date");
        tv_userjoined.setText(user_joining_date);
        user_image = getIntent().getStringExtra("user_image");
        Picasso.with(MeetYourRiderActivity.this).load(user_image).resize(100, 100).into(circleimg_user);
        //drop_lat = getIntent().getStringExtra("drop_lat");
        //drop_long = getIntent().getStringExtra("drop_long");
        drop_address = getIntent().getStringExtra("drop_address");
        user_phone = getIntent().getStringExtra("user_phone");
        Log.d("Ans", "onCreate: " + PickUpLat + "," + PickUpLong + "," + drop_lat + "," + drop_long);



        phoneCallImageView=findViewById(R.id.phoneCallImageView);
        phoneCallImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber=user_phone;
                if (!TextUtils.isEmpty(phoneNumber))
                {
                    if (checkPermission(android.Manifest.permission.CALL_PHONE)) {
                        String dial = "tel:" + phoneNumber;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }else {
                        Toast.makeText(MeetYourRiderActivity.this, "Permission call phone denied", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(MeetYourRiderActivity.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
                }


            }
        });

        if (checkPermission(android.Manifest.permission.CALL_PHONE))
        {
            phoneCallImageView.setEnabled(true);
        }else {
            phoneCallImageView.setEnabled(false);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},MAKE_CALL_PERMISSION_REQUEST_CODE);
        }

        cancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPicture(MeetYourRiderActivity.this);
            }
        });
        reginTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reginTripFunc();
            }
        });
        //UpdateDriverLatLong();
        //Update.......few second...........//////////// ............Update Driver,.....LatLong.......
        //mToastRunnable.run();

        btn_navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + PickUpLat + "," + PickUpLong + "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        //GPS from Current Lat Long...................
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        textView = (TextView) findViewById(R.id.text_location);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        //Call UpdateFun........
        UpdateDriverLatLong();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("TwoAdd", "onResponse: "+CurrentLat+","+CurrentLong+",,"+PickUpLat+","+PickUpLong);
        double CurrentLati = Double.parseDouble(CurrentLat);
        double CurrentLongi = Double.parseDouble(CurrentLong);
        double PickLatt = Double.parseDouble(PickUpLat);
        double PickLongg = Double.parseDouble(PickUpLong);

        LatLng origin = new LatLng(CurrentLati, CurrentLongi);
        LatLng destination = new LatLng(PickLatt, PickLongg);

        DrawRouteMaps.getInstance(this).draw(origin, destination, mMap);
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 40));
        List<Marker> lstMarcadores = new ArrayList<>();
        Marker marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(CurrentLati, CurrentLongi)).title(markAddCurrentLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.greenmarker)));
        lstMarcadores.add(marker1);
        //lstMarcadores.get(0).showInfoWindow();
        Marker markers = mMap.addMarker(new MarkerOptions().position(new LatLng(PickLatt, PickLongg)).title(markAdd).icon(BitmapDescriptorFactory.fromResource(R.drawable.redmarker)));
        lstMarcadores.add(markers);
    }



    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MeetYourRiderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MeetYourRiderActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MeetYourRiderActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {

            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                double crrtLat;
                double crrtLong;

                if (location != null) {
                    crrtLat = location.getLatitude();
                    CurrentLat = String.valueOf(crrtLat);
                    crrtLong = location.getLongitude();
                    CurrentLong = String.valueOf(crrtLong);
                } else if (location1 != null) {
                    crrtLat = location.getLatitude();
                    CurrentLat = String.valueOf(crrtLat);
                    crrtLong = location.getLongitude();
                    CurrentLong = String.valueOf(crrtLong);
                } else if (location2 != null) {
                    crrtLat = location.getLatitude();
                    CurrentLat = String.valueOf(crrtLat);
                    crrtLong = location.getLongitude();
                    CurrentLong = String.valueOf(crrtLong);
                } else {

                    Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void UpdateDriverLatLong() {
        getLocation();
        showPolyline();
        StringRequest getDriverRequest = new StringRequest(Request.Method.POST, upDateDriverLatLongURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("PalashUpdateLatLongRes", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 400;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                    //UpdateDriverLatLong();
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }
                if (status == 200) {
                    try {
                        String statusFalg = resObj.getString("reach_falg");
                        if (statusFalg.equals("0")) {
                            //mToastRunnable.run();
                            {
                                /*mHandler.removeCallbacks(mToastRunnable);
                                Intent intent = new Intent(MeetYourRiderActivity.this, MeetYourRiderStartTripActivity.class);
                                intent.putExtra("PickUpLat", PickUpLat);
                                intent.putExtra("PickUpLong", PickUpLong);
                                intent.putExtra("pickup_address", pickup_address);
                                intent.putExtra("drop_lat", drop_lat);
                                intent.putExtra("drop_long", drop_long);
                                intent.putExtra("drop_address", drop_address);
                                intent.putExtra("user_image", user_image);
                                intent.putExtra("rating", rating);
                                intent.putExtra("user_name", user_name);
                                intent.putExtra("user_joining_date", user_joining_date);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("ride_id", ride_id);
                                Log.d("NextPage", "onResponse: "+PickUpLat+","+PickUpLong+","+drop_lat+","+drop_long);
                                startActivity(intent);*/
                            }
                        }
                        //String statusFalg = resObj.getString("reach_falg");
                        else if (statusFalg.equals("1")) {
                             mHandler.removeCallbacks(mToastRunnable);
                            Intent intent = new Intent(MeetYourRiderActivity.this, MeetYourRiderStartTripActivity.class);
                            intent.putExtra("PickUpLat", PickUpLat);
                            intent.putExtra("PickUpLong", PickUpLong);
                            intent.putExtra("pickup_address", pickup_address);
                            intent.putExtra("drop_lat", drop_lat);
                            intent.putExtra("drop_long", drop_long);
                            intent.putExtra("drop_address", drop_address);
                            intent.putExtra("user_image", user_image);
                            intent.putExtra("rating", rating);
                            intent.putExtra("user_name", user_name);
                            intent.putExtra("user_joining_date", user_joining_date);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("ride_id", ride_id);
                            startActivity(intent);
                        }
                    } catch (Exception e) {

                    }
                } else {
                    Toast.makeText(MeetYourRiderActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "error" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("driver_id", sharedPreferences.getString("driver_id", ""));
                params.put("current_lat", CurrentLat);
                params.put("current_long", CurrentLong);
                params.put("pickup_lat", PickUpLat);
                params.put("pickup_long", PickUpLong);

                //current_lat','current_long','destination_lat','destination_long'

                Log.i("PickUpLatLong", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(getDriverRequest);
    }

    @Override
    public void onBackPressed() {
        if (!shouldAllowBack()) {
            //doSomething();
        } else {
            super.onBackPressed();
        }
    }

    private boolean shouldAllowBack()
    {
        return false;
    }

    //Cancel Trip..................Reason.............

    public void selectPicture(final Activity mActivity)
    {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.setCancelable(false);
        dialog.setContentView(R.layout.driverpopup_cancelride);


        TextView txtNotCharge = dialog.findViewById(R.id.txtNotCharge);
        TextView txtchargeRider = dialog.findViewById(R.id.txtchargeRider);
        TextView txtRiderNotFound = dialog.findViewById(R.id.txtRiderNotFound);
        TextView txtRiderDidNotShow = dialog.findViewById(R.id.txtRiderDidNotShow);
        TextView txtRiderReqeuestToCancel = dialog.findViewById(R.id.txtRiderReqeuestToCancel);
        TextView txtOther = dialog.findViewById(R.id.txtOther);
        txtNotCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "Selected then Click Cancel Button", Toast.LENGTH_SHORT).show();
            }
        });
        txtchargeRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "Selected then Click Cancel Button", Toast.LENGTH_SHORT).show();
            }
        });
        txtRiderNotFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "Selected then Click Cancel Button", Toast.LENGTH_SHORT).show();
            }
        });
        txtRiderDidNotShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "Selected then Click Cancel Button", Toast.LENGTH_SHORT).show();
            }
        });
        txtRiderReqeuestToCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "Selected then Click Cancel Button", Toast.LENGTH_SHORT).show();
            }
        });
        txtOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "Selected then Click Cancel Button", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnCancel=dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               CancelResonFun();

            }
        });

        System.out.println(values);


       //dialog.show();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });

    }

   private void CancelResonFun(){
       StringRequest getDriverRequest = new StringRequest(Request.Method.POST, cancelResonAPI, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {

               Log.i("PickUpLatResponse", "onResponse: " + response);
               JSONObject resObj = null;
               int status = 400;
               String msg = "";
               try {
                   resObj = new JSONObject(response);
                   status = resObj.getInt("status");
                   msg = resObj.getString("message");
                   //UpdateDriverLatLong();
               } catch (JSONException e) {
                   //e.printStackTrace();
                   Log.i(TAG, "onResponse: " + e);
               }

               if (status == 200) {
                  Intent intent=new Intent(MeetYourRiderActivity.this,HomeActivity.class);
                  startActivity(intent);
               } else {
                   Toast.makeText(MeetYourRiderActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.e("error", "error" + error);
           }
       }) {
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> params = new HashMap<>();
               params.put("ride_id", ride_id);
               params.put("cancel_reason_id", "3");
               params.put("cancel_ride", "1");

               //current_lat','current_long','destination_lat','destination_long'

               Log.i("PickUpLatLong", "getParams: " + params);
               return params;
           }
       };
       Volley.newRequestQueue(getApplicationContext()).add(getDriverRequest);
   }

   //Click Regin Trip Button............
   private void reginTripFunc(){
       StringRequest getDriverRequest = new StringRequest(Request.Method.POST, reginTripAPI, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {

               Log.i("PickUpLatResponse", "onResponse: " + response);
               JSONObject resObj = null;
               int status = 400;
               String msg = "";
               try {
                   resObj = new JSONObject(response);
                   status = resObj.getInt("status");
                   msg = resObj.getString("message");
                   //UpdateDriverLatLong();
               } catch (JSONException e) {
                   //e.printStackTrace();
                   Log.i(TAG, "onResponse: " + e);
               }

               if (status == 200) {
                   Intent intent=new Intent(MeetYourRiderActivity.this,HomeActivity.class);
                   startActivity(intent);
               } else {
                   Toast.makeText(MeetYourRiderActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.e("error", "error" + error);
           }
       }) {
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> params = new HashMap<>();
               params.put("ride_id", ride_id);
               params.put("driver_id", sharedPreferences.getString("driver_id", ""));
               params.put("reassign", "1");

               //current_lat','current_long','destination_lat','destination_long'

               Log.i("PickUpLatLong", "getParams: " + params);
               return params;
           }
       };
       Volley.newRequestQueue(getApplicationContext()).add(getDriverRequest);
   }

   //Call Rider/////////
   private boolean checkPermission(String permission)
   {
       return ContextCompat.checkSelfPermission(this,permission)== PackageManager.PERMISSION_GRANTED;
   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    phoneCallImageView.setEnabled(true);
                    Toast.makeText(this, "You can call the number by checking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
    // Subhadip Polyline code start

    public void showPolyline() {

        final String direction_url = "https://maps.googleapis" +
                ".com/maps/api/directions/json?origin=" + URLEncoder.encode(CurrentLat + "," + CurrentLong) + "&destination=" +
                URLEncoder.encode(PickUpLat + "," + PickUpLong) + "&key=AIzaSyDS" +
                //Driver_Lat,Driver_Long
                "-sgfRY8xzB9vil3XcxevXMlopXT6Afg";

        StringRequest directionRequest = new StringRequest(Request.Method.GET, direction_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Direction: " + response);
                Log.d("TwoAdd", "onResponse: "+CurrentLat+","+CurrentLong+",,"+PickUpLat+","+PickUpLong);

                String[] directionList;
                directionList = parseDirections(response);
                displayDirection(directionList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(directionRequest);
    }

    //Parse Direction
    public String[] parseDirections(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPaths(jsonArray);
    }

    public String getPath(JSONObject googlePathJson) {
        String polyline = "";
        try {
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }

    public String[] getPaths(JSONArray googleStepsJson) {
        int count = googleStepsJson.length();
        String[] polylines = new String[count];

        for (int i = 0; i < count; i++) {
            try {
                polylines[i] = getPath(googleStepsJson.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return polylines;
    }

    //display polyline
    public void displayDirection(String[] directionsList) {

        int count = directionsList.length;
        for (int i = 0; i < count; i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.BLACK);
            options.width(5);
            options.addAll(PolyUtil.decode(directionsList[i]));
            mMap.addPolyline(options);
        }
    }

    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            //Toast.makeText(MeetYourRiderActivity.this, "This is a delayed toast", Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(this, 15000);
            //UpdateDriverLatLong();
        }
    };

}

