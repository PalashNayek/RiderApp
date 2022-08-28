package com.example.hp.googlemap.driver;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener,
        View.OnClickListener {
    private static final String TAG = "Home Activity";
    private static final int REQUEST_LOCATION = 1;
    String showPopUpAPIURL = "http://45.55.64.233/Miles/driver/check_for_new_ride.php";
    String checkStatusAPI = "http://45.55.64.233/Miles/driver/check_status.php";
    String updateStatusAPI = "http://45.55.64.233/Miles/driver/update_status.php";
    String updateDriverLatLong = "http://45.55.64.233/Miles/driver/update_current_location.php";
    /*TimerTask t;
    int count = 0;
    Boolean isProcessing = false;*/
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int backButtonCount;
    String riderId, car_tier, user_name, rating, pickup_address;
    TextView goToOnlineTxt, goToOfflineTxt;
    LinearLayout lOut, liStatusTwo, liStatusZero, liStatusOne, onlineLinear, reservedLinear;
    LocationManager locationManager;
    String CurrentLat, CurrentLong;
    private GoogleMap mMap;
    private ImageView iv_user, iv_cash;
    //  private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;
    private Handler mHandler = new Handler();
    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            popUpShowDriver();
            driverLatLongUpdate();
            mHandler.postDelayed(this, 15000);
            Log.d("Runnable", "run: Hello");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        iv_user = findViewById(R.id.iv_user);
        iv_cash = findViewById(R.id.iv_cash);

        sharedPreferences = getApplicationContext().getSharedPreferences("driver_pref", 0);
        editor = sharedPreferences.edit();
        checkStatusFun();

        mToastRunnable.run();
        goToOnlineTxt = (TextView) findViewById(R.id.goToOnlineTxt);
        goToOfflineTxt = (TextView) findViewById(R.id.goToOfflineTxt);
        lOut = (LinearLayout) findViewById(R.id.lOut);
        liStatusTwo = (LinearLayout) findViewById(R.id.liStatusTwo);
        liStatusZero = (LinearLayout) findViewById(R.id.liStatusZero);
        liStatusOne = (LinearLayout) findViewById(R.id.liStatusOne);

        //Current LatLong From GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        goToOnlineTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lOut.setVisibility(View.VISIBLE);
                goToOnlineTxt.setVisibility(View.GONE);
                goToOfflineTxt.setVisibility(View.VISIBLE);
            }
        });

        goToOfflineTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lOut.setVisibility(View.GONE);
                goToOnlineTxt.setVisibility(View.VISIBLE);
                goToOfflineTxt.setVisibility(View.GONE);
                liStatusZero.setVisibility(View.GONE);
                liStatusOne.setVisibility(View.GONE);
                liStatusTwo.setVisibility(View.VISIBLE);
                updateOfflineStatusFunc();

            }
        });

        onlineLinear = (LinearLayout) findViewById(R.id.onlineLinear);

        onlineLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusOnline();
            }
        });

        reservedLinear = (LinearLayout) findViewById(R.id.reservedLinear);

        reservedLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusReserve();
            }
        });


        iv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        iv_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EarningActivity.class);
                startActivity(intent);
            }
        });

        //Initializing googleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng latLng = new LatLng(-34, 151);
        //Toast.makeText(this, ""+latLng, Toast.LENGTH_SHORT).show();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    public void onButtonClickback(View view) {
        Intent intent = new Intent(HomeActivity.this, RatingsActivity.class);
        startActivity(intent);
    }

    public void onButtonClickbackbell(View view) {
        switch (view.getId()) {
            case R.id.iv_bell:
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(intent);
                break;
        }
    }

    //Getting current location
    private void getCurrentLocation() {
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            moveMap();
        }
    }

    private void moveMap() {
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).snippet("Your Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.bluemarker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onClick(View view) {
        Log.v(TAG, "view click event");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(HomeActivity.this, "onMarkerDragStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Toast.makeText(HomeActivity.this, "onMarkerDrag", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // getting the Co-ordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        moveMap();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //Toast.makeText(HomeActivity.this, "" + marker, Toast.LENGTH_SHORT).show();
        return true;
    }

    private void popUpShowDriver() {
        //isProcessing = true;
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
                    //Intent intent = new Intent(HomeActivity.this, RiderRequestActivity.class);

                    try {
                        riderId = resObj.getString("ride_id");
                        car_tier = resObj.getString("car_tier");
                        user_name = resObj.getString("user_name");
                        rating = resObj.getString("rating");
                        pickup_address = resObj.getString("pickup_address");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i(TAG, "onResponse: " + e);
                    }
                    Intent intent = new Intent(HomeActivity.this, RiderRequestActivity.class);
                    intent.putExtra("ride_id", riderId);
                    intent.putExtra("car_tier", car_tier);
                    intent.putExtra("user_name", user_name);
                    intent.putExtra("rating", rating);
                    intent.putExtra("pickup_address", pickup_address);
                    startActivity(intent);
                    startActivity(intent);
                    mHandler.removeCallbacks(mToastRunnable);
                    finish();
                } else if (status == 201) {
                    //popUpShowDriver();
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
                params.put("driver_id", sharedPreferences.getString("driver_id", ""));
                Log.d("driverIdGet", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(popUpShow_request);
    }

    private void driverLatLongUpdate() {

        getLocation();
        StringRequest popUpShow_request = new StringRequest(Request.Method.POST, updateDriverLatLong, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("DriverLatLongUpdateRes", "onResponse: " + response);
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
                } else {

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
                params.put("driver_id", sharedPreferences.getString("driver_id", ""));
                params.put("lat", CurrentLat);
                params.put("long", CurrentLong);

                Log.d("driverIdGet", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(popUpShow_request);
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 2) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Don't Back Press", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    private void checkStatusFun() {
        StringRequest checkStatus_request = new StringRequest(Request.Method.POST, checkStatusAPI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("checkStatusResponse", "onResponse: " + response);
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
                        String checkStatus = resObj.getString("online_status");
                        if (checkStatus.equals("3")) {
                            liStatusZero.setVisibility(View.VISIBLE);
                            liStatusTwo.setVisibility(View.GONE);
                            liStatusOne.setVisibility(View.GONE);
                        }
                        String checkStatusTwo = resObj.getString("online_status");
                        if (checkStatusTwo.equals("2")) {
                            liStatusTwo.setVisibility(View.VISIBLE);
                            liStatusZero.setVisibility(View.GONE);
                            liStatusOne.setVisibility(View.GONE);
                        }
                        String checkStatusOne = resObj.getString("online_status");
                        if (checkStatusOne.equals("1")) {
                            liStatusOne.setVisibility(View.VISIBLE);
                            liStatusTwo.setVisibility(View.GONE);
                            liStatusZero.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (status == 201) {
                    //popUpShowDriver();
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
                params.put("driver_id", sharedPreferences.getString("driver_id", ""));
                Log.d("checkStatusParams", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(checkStatus_request);
    }

    private void updateStatusOnline() {
        StringRequest checkStatus_request = new StringRequest(Request.Method.POST, updateStatusAPI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("checkStatusResponse", "onResponse: " + response);
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
                        String checkStatus = resObj.getString("online_status");
                        if (checkStatus.equals("3")) {
                            liStatusZero.setVisibility(View.VISIBLE);
                            liStatusTwo.setVisibility(View.GONE);
                            liStatusOne.setVisibility(View.GONE);
                        }
                        String checkStatusTwo = resObj.getString("online_status");
                        if (checkStatusTwo.equals("2")) {
                            liStatusTwo.setVisibility(View.VISIBLE);
                            liStatusZero.setVisibility(View.GONE);
                            liStatusOne.setVisibility(View.GONE);
                        }
                        String checkStatusOne = resObj.getString("online_status");
                        if (checkStatusOne.equals("1")) {
                            liStatusOne.setVisibility(View.VISIBLE);
                            liStatusTwo.setVisibility(View.GONE);
                            liStatusZero.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (status == 201) {
                    //popUpShowDriver();
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
                params.put("driver_id", sharedPreferences.getString("driver_id", ""));
                params.put("status", "1");

                Log.d("checkStatusParams", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(checkStatus_request);
    }

    private void updateStatusReserve() {
        StringRequest checkStatus_request = new StringRequest(Request.Method.POST, updateStatusAPI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("checkStatusResponse", "onResponse: " + response);
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
                        String checkStatus = resObj.getString("online_status");
                        if (checkStatus.equals("3")) {
                            liStatusZero.setVisibility(View.VISIBLE);
                            liStatusTwo.setVisibility(View.GONE);
                            liStatusOne.setVisibility(View.GONE);
                        }
                        String checkStatusTwo = resObj.getString("online_status");
                        if (checkStatusTwo.equals("2")) {
                            liStatusTwo.setVisibility(View.VISIBLE);
                            liStatusZero.setVisibility(View.GONE);
                            liStatusOne.setVisibility(View.GONE);
                        }
                        String checkStatusOne = resObj.getString("online_status");
                        if (checkStatusOne.equals("1")) {
                            liStatusOne.setVisibility(View.VISIBLE);
                            liStatusTwo.setVisibility(View.GONE);
                            liStatusZero.setVisibility(View.GONE);
                        }
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
                params.put("driver_id", sharedPreferences.getString("driver_id", ""));
                params.put("status", "3");

                Log.d("checkStatusParams", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(checkStatus_request);
    }

    private void updateOfflineStatusFunc() {
        StringRequest checkStatus_request = new StringRequest(Request.Method.POST, updateStatusAPI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("checkStatusResponse", "onResponse: " + response);
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
                        String checkStatus = resObj.getString("online_status");
                        if (checkStatus.equals("3")) {
                            liStatusZero.setVisibility(View.VISIBLE);
                            liStatusTwo.setVisibility(View.GONE);
                            liStatusOne.setVisibility(View.GONE);
                        }
                        String checkStatusTwo = resObj.getString("online_status");
                        if (checkStatusTwo.equals("2")) {
                            liStatusTwo.setVisibility(View.VISIBLE);
                            liStatusZero.setVisibility(View.GONE);
                            liStatusOne.setVisibility(View.GONE);
                        }
                        String checkStatusOne = resObj.getString("online_status");
                        if (checkStatusOne.equals("1")) {
                            liStatusOne.setVisibility(View.VISIBLE);
                            liStatusTwo.setVisibility(View.GONE);
                            liStatusZero.setVisibility(View.GONE);
                        }
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
                params.put("driver_id", sharedPreferences.getString("driver_id", ""));
                params.put("status", "2");

                Log.d("checkStatusParams", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(checkStatus_request);
    }

    //Current Lat Long retrive from GPS.........
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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

}