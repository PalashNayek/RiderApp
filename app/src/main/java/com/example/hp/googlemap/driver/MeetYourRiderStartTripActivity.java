package com.example.hp.googlemap.driver;

import android.Manifest;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class MeetYourRiderStartTripActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MeetYourRiderStartTripActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION = 1;
    String PickUpLat, PickUpLong, drop_lat, drop_long;
    String markAdd, markAddCurrentLoc, user_image = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String upDateDriverLatLongURL = "http://45.55.64.233/Miles/driver/checking_destination_reach.php";
    TimerTask t;
    int count = 0;
    Boolean isProcessing = false;
    LocationManager locationManager;
    TextView textView;
    String lattitude, longitude, ride_id, user_id;
    private CircleImageView circleimg_user;
    private TextView tv_username, tv_userrating, tv_userjoined, tv_pickupaddr, tv_userdropaddr;
    private GoogleMap mMap;
    String drop_latitute,drop_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_your_rider_start_trip);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPreferences = getApplicationContext().getSharedPreferences("driver_pref", 0);
        editor = sharedPreferences.edit();

        circleimg_user = findViewById(R.id.circleimg_user);
        tv_username = findViewById(R.id.tv_username);
        tv_userrating = findViewById(R.id.tv_userrating);
        tv_userjoined = findViewById(R.id.tv_userjoined);
        tv_pickupaddr = findViewById(R.id.tv_pickupaddr);
        tv_userdropaddr = findViewById(R.id.tv_userdropaddr);
        tv_pickupaddr.setText(getIntent().getStringExtra("pickup_address"));
        tv_userdropaddr.setText(getIntent().getStringExtra("drop_address"));

        PickUpLat = getIntent().getStringExtra("PickUpLat");
        PickUpLong = getIntent().getStringExtra("PickUpLong");
        drop_latitute = getIntent().getStringExtra("drop_lat");
        drop_longitude = getIntent().getStringExtra("drop_long");
        user_image = getIntent().getStringExtra("user_image");
        Picasso.with(MeetYourRiderStartTripActivity.this).load(user_image).resize(100, 100).into(circleimg_user);
        tv_username.setText(getIntent().getStringExtra("user_name"));
        tv_userrating.setText(getIntent().getStringExtra("rating"));
        tv_userjoined.setText(getIntent().getStringExtra("user_joining_date"));

        ride_id = (getIntent().getStringExtra("ride_id"));
        user_id = (getIntent().getStringExtra("user_id"));
        //UpdateDriverLatLong();

        //Update.......few second...........//////////// ............Update Driver,.....LatLong.......
        Timer timer = new Timer();
        t = new TimerTask() {
            @Override
            public void run() {
                count++;
                if (!isProcessing && count <= 1) {
                    UpdateDriverLatLong();
                }
            }
        };
        timer.scheduleAtFixedRate(t, 25000, 30000);

        //GPS from Current Lat Long...................
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        textView = (TextView) findViewById(R.id.text_location);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    public void onButtonClicstarttrip(View view) {
       /* Intent intent = new Intent(MeetYourRiderStartTripActivity.this, RootDirectionFinalActivity.class);
        startActivity(intent);*/
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + drop_latitute + "," + drop_longitude + "&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double currentLat = Double.parseDouble(PickUpLat);
        double currentLong = Double.parseDouble(PickUpLong);
        double dropLatdw = Double.parseDouble(drop_latitute);
        double dropLongdw = Double.parseDouble(drop_longitude);

        LatLng origin = new LatLng(currentLat, currentLong);
        LatLng destination = new LatLng(dropLatdw, dropLongdw);

        DrawRouteMaps.getInstance(this).draw(origin, destination, mMap);
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 40));
        List<Marker> lstMarcadores = new ArrayList<>();
        Marker marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(currentLat, currentLong)).title(markAddCurrentLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.greenmarker)));
        lstMarcadores.add(marker1);
        //lstMarcadores.get(0).showInfoWindow();
        Marker markers = mMap.addMarker(new MarkerOptions().position(new LatLng(dropLatdw, dropLongdw)).title(markAdd).icon(BitmapDescriptorFactory.fromResource(R.drawable.redmarker)));
        lstMarcadores.add(markers);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MeetYourRiderStartTripActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MeetYourRiderStartTripActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MeetYourRiderStartTripActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                textView.setText(lattitude + "," + longitude);
                drop_lat = lattitude;
                drop_long = longitude;

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                textView.setText(lattitude + "," + longitude);
                drop_lat = lattitude;
                drop_long = longitude;


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                textView.setText(lattitude + "," + longitude);
                drop_lat = lattitude;
                drop_long = longitude;

            } else {

                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();

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
        showPolyline();
        isProcessing = true;
        StringRequest getDriverRequest = new StringRequest(Request.Method.POST, upDateDriverLatLongURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("UpdateDriverLatResponse", "onResponse: " + response);
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
                            /*Intent intent = new Intent(MeetYourRiderStartTripActivity.this, RateYourRiderActivity.class);
                            intent.putExtra("user_image", user_image);
                            intent.putExtra("rating", tv_userrating.getText().toString());
                            intent.putExtra("user_name", tv_username.getText().toString());
                            intent.putExtra("user_joining_date", tv_userjoined.getText().toString());

                            intent.putExtra("user_id", user_id);
                            intent.putExtra("ride_id", ride_id);
                            intent.putExtra("PickUpLat", PickUpLat);
                            intent.putExtra("PickUpLong", PickUpLong);

                            intent.putExtra("drop_lat", drop_lat);
                            intent.putExtra("drop_long", drop_long);
                            intent.putExtra("dropAdd", tv_userdropaddr.getText().toString());
                            startActivity(intent);*/
                        } else if (statusFalg.equals("1")) {
                            Intent intent = new Intent(MeetYourRiderStartTripActivity.this, RateYourRiderActivity.class);
                            intent.putExtra("user_image", user_image);
                            intent.putExtra("rating", tv_userrating.getText().toString());
                            intent.putExtra("user_name", tv_username.getText().toString());
                            intent.putExtra("user_joining_date", tv_userjoined.getText().toString());

                            intent.putExtra("user_id", user_id);
                            intent.putExtra("ride_id", ride_id);
                            intent.putExtra("PickUpLat", PickUpLat);
                            intent.putExtra("PickUpLong", PickUpLong);

                            intent.putExtra("drop_lat", drop_lat);
                            intent.putExtra("drop_long", drop_long);
                            intent.putExtra("dropAdd", tv_userdropaddr.getText().toString());
                            startActivity(intent);
                        }
                    } catch (Exception e) {

                    }
                } else {
                    Toast.makeText(MeetYourRiderStartTripActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                }
                isProcessing = false;
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
                params.put("current_lat", PickUpLat);
                params.put("current_long", PickUpLong);
                params.put("destination_lat", drop_latitute);
                params.put("destination_long", drop_longitude);

                //current_lat','current_long','destination_lat','destination_long'

                Log.i("UpdateDriverLatLong", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(getDriverRequest);
    }

    // Subhadip Polyline code start

    public void showPolyline() {

        final String direction_url = "https://maps.googleapis" +
                ".com/maps/api/directions/json?origin=" + URLEncoder.encode(PickUpLat + "," + PickUpLong) + "&destination=" +
                URLEncoder.encode(drop_latitute + "," + drop_longitude) + "&key=AIzaSyDS" +
                //Driver_Lat,Driver_Long
                "-sgfRY8xzB9vil3XcxevXMlopXT6Afg";

        StringRequest directionRequest = new StringRequest(Request.Method.GET, direction_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Direction: " + response);
               // Log.d("TwoAdd", "onResponse: "+CurrentLat+","+CurrentLong+",,"+PickUpLat+","+PickUpLong);

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
