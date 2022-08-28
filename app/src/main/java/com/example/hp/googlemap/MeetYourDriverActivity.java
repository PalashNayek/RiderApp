package com.example.hp.googlemap;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.rider.DashActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeetYourDriverActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = MeetYourDriverActivity.class.getSimpleName();
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    String BaseUrl = "http://45.55.64.233/Miles/api/";
    String cancelResonAPI = "http://45.55.64.233/Miles/api/cancel_ride.php";
    String set_id;
    TextView driverName, driver_rating, car_type, car_make, car_num, car_color, join_date;
    CircleImageView driver_Image, CarImage;
    LinearLayout linearLayout1;
    ImageView backArrow;
    String Driver_Lat, Driver_Long, Rider_Lat, Rider_Long;
    TextView popupHeadertxt, driverWasLatetxt, showUptxt, tooFartxt, longerNeededtxt, evenCanceltxt, othertxt;
    Button confirmTripbtn;
    int backButtonCount;
    String driver_image;
    ImageView phoneCallImageView, cancelTrip;
    String reason = "";
    String driver_phone;
    String reachDestinationLocationAPI = "http://45.55.64.233/Miles/api/check_destination_reach.php";
    String reassignAPI = "http://45.55.64.233/Miles/api/check_reassigned.php";
    Dialog dialog;
    private GoogleMap mMap;
    private Handler mHandler = new Handler();
    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            // reachDestinationFunc();
            //reassignnedDriverFunc();
            mHandler.postDelayed(this, 25000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_your_driver);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        driverName = (TextView) findViewById(R.id.driverName);
        driver_rating = (TextView) findViewById(R.id.driver_rating);
        car_type = (TextView) findViewById(R.id.car_type);
        car_make = (TextView) findViewById(R.id.car_make);
        car_num = (TextView) findViewById(R.id.car_num);
        car_color = (TextView) findViewById(R.id.car_color);
        join_date = (TextView) findViewById(R.id.join_date);
        driver_Image = (CircleImageView) findViewById(R.id.driver_Image);
        // car_Image = (CircleImageView) findViewById(R.id.car_Image);
        CarImage = (CircleImageView) findViewById(R.id.car_Image);
        linearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
        popupHeadertxt = (TextView) findViewById(R.id.popupHeadertxt);
        phoneCallImageView = (ImageView) findViewById(R.id.phoneCallImageView);
        cancelTrip = (ImageView) findViewById(R.id.cancelTrip);

        confirmTripbtn = (Button) findViewById(R.id.confirmTripbtn);
        driverWasLatetxt = (TextView) findViewById(R.id.driverWasLatetxt);
        showUptxt = (TextView) findViewById(R.id.showUptxt);
        tooFartxt = (TextView) findViewById(R.id.tooFartxt);
        longerNeededtxt = (TextView) findViewById(R.id.longerNeededtxt);
        evenCanceltxt = (TextView) findViewById(R.id.evenCanceltxt);
        othertxt = (TextView) findViewById(R.id.othertxt);
        othertxt = (TextView) findViewById(R.id.othertxt);
        driver_phone = getIntent().getStringExtra("driver_phone");
        set_id = getIntent().getStringExtra("set_id");
        Rider_Lat = getIntent().getStringExtra("Rider_Lat");
        System.out.println(Rider_Lat);
        Rider_Long = getIntent().getStringExtra("Rider_Long");
        Driver_Lat = getIntent().getStringExtra("driver_lat");
        Driver_Long = getIntent().getStringExtra("driver_long");

        String carImage = getIntent().getStringExtra("car_image");
        Picasso.with(MeetYourDriverActivity.this).load(carImage).resize(100, 100).into(CarImage);
        //driver_image
        driver_image = getIntent().getStringExtra("driver_image");
        Picasso.with(MeetYourDriverActivity.this).load(driver_image).resize(100, 100).into(driver_Image);

        car_make.setText(getIntent().getStringExtra("car_tier"));
        driverName.setText(getIntent().getStringExtra("driver_name"));
        car_type.setText(getIntent().getStringExtra("driver_city"));
        car_num.setText(getIntent().getStringExtra("car_number"));
        join_date.setText(getIntent().getStringExtra("register_date"));
        //reachDestinationFunc();
        // mToastRunnable.run();
        cancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPicture(MeetYourDriverActivity.this);
            }
        });


        phoneCallImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = driver_phone;
                if (!TextUtils.isEmpty(phoneNumber)) {
                    if (checkPermission(android.Manifest.permission.CALL_PHONE)) {
                        String dial = "tel:" + phoneNumber;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    } else {
                        Toast.makeText(MeetYourDriverActivity.this, "Permission call phone denied", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MeetYourDriverActivity.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (checkPermission(android.Manifest.permission.CALL_PHONE)) {
            phoneCallImageView.setEnabled(true);
        } else {
            phoneCallImageView.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
        }

        //Navigate Button Click....
        ImageView navigationImgBtn = (ImageView) findViewById(R.id.navigationImgBtn);
        navigationImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Driver_Lat + "," + Driver_Long + "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        showPolyline();
    }

    //Cancel Trip..................Reason.............

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double PickUpLath = Double.parseDouble(Rider_Lat);
        double PickUpLongh = Double.parseDouble(Rider_Long);
        double DestinationLat = Double.parseDouble(Driver_Lat);
        double DestinationLong = Double.parseDouble(Driver_Long);

        Log.d("MIITDRIVER", "onMapReady: " + PickUpLath + PickUpLongh + DestinationLat + DestinationLong);

        LatLng origin = new LatLng(PickUpLath, PickUpLongh);
        LatLng destination = new LatLng(DestinationLat, DestinationLong);
        DrawRouteMaps.getInstance(this)
                .draw(origin, destination, mMap);
        DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.pickuplocation, "Origin Location");
        DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.carddd, "Destination Location");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 40));
    }

    public void selectPicture(final Activity mActivity) {
        dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.setCancelable(false);
        dialog.setContentView(R.layout.driverpopup_cancelride);


        final TextView txtNotCharge = dialog.findViewById(R.id.txtNotCharge);
        final TextView txtNotCharge_a = dialog.findViewById(R.id.txtNotCharge_a);
        final TextView txtchargeRider = dialog.findViewById(R.id.txtchargeRider);
        final TextView txtchargeRider_a = dialog.findViewById(R.id.txtchargeRider_a);
        final TextView txtRiderNotFound = dialog.findViewById(R.id.txtRiderNotFound);
        final TextView txtRiderNotFound_a = dialog.findViewById(R.id.txtRiderNotFound_a);
        final TextView txtRiderDidNotShow = dialog.findViewById(R.id.txtRiderDidNotShow);
        final TextView txtRiderDidNotShow_a = dialog.findViewById(R.id.txtRiderDidNotShow_a);
        final TextView txtRiderReqeuestToCancel = dialog.findViewById(R.id.txtRiderReqeuestToCancel);
        final TextView txtRiderReqeuestToCancel_a = dialog.findViewById(R.id.txtRiderReqeuestToCancel_a);
        final TextView txtOther = dialog.findViewById(R.id.txtOther);
        final TextView txtOther_a = dialog.findViewById(R.id.txtOther_a);

        txtNotCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNotCharge.setVisibility(View.GONE);


                txtNotCharge_a.setVisibility(View.VISIBLE);

                txtchargeRider_a.setVisibility(View.GONE);
                txtRiderNotFound_a.setVisibility(View.GONE);
                txtRiderDidNotShow_a.setVisibility(View.GONE);
                txtRiderReqeuestToCancel_a.setVisibility(View.GONE);
                txtOther_a.setVisibility(View.GONE);

                txtchargeRider.setVisibility(View.VISIBLE);
                txtRiderNotFound.setVisibility(View.VISIBLE);
                txtRiderDidNotShow.setVisibility(View.VISIBLE);
                txtRiderReqeuestToCancel.setVisibility(View.VISIBLE);
                txtOther.setVisibility(View.VISIBLE);
                reason = "1";

            }
        });
        txtchargeRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtchargeRider.setVisibility(View.GONE);
                txtchargeRider_a.setVisibility(View.VISIBLE);

                txtNotCharge_a.setVisibility(View.GONE);
                txtRiderNotFound_a.setVisibility(View.GONE);
                txtRiderDidNotShow_a.setVisibility(View.GONE);
                txtRiderReqeuestToCancel_a.setVisibility(View.GONE);
                txtOther_a.setVisibility(View.GONE);

                txtNotCharge.setVisibility(View.VISIBLE);
                txtRiderNotFound.setVisibility(View.VISIBLE);
                txtRiderDidNotShow.setVisibility(View.VISIBLE);
                txtRiderReqeuestToCancel.setVisibility(View.VISIBLE);
                txtOther.setVisibility(View.VISIBLE);
                reason = "2";
            }
        });
        txtRiderNotFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtRiderNotFound.setVisibility(View.GONE);
                txtRiderNotFound_a.setVisibility(View.VISIBLE);

                txtNotCharge_a.setVisibility(View.GONE);
                txtchargeRider_a.setVisibility(View.GONE);
                txtRiderDidNotShow_a.setVisibility(View.GONE);
                txtRiderReqeuestToCancel_a.setVisibility(View.GONE);
                txtOther_a.setVisibility(View.GONE);

                txtNotCharge.setVisibility(View.VISIBLE);
                txtchargeRider.setVisibility(View.VISIBLE);
                txtRiderDidNotShow.setVisibility(View.VISIBLE);
                txtRiderReqeuestToCancel.setVisibility(View.VISIBLE);
                txtOther.setVisibility(View.VISIBLE);
                reason = "3";
            }
        });
        txtRiderDidNotShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtRiderDidNotShow.setVisibility(View.GONE);
                txtRiderDidNotShow_a.setVisibility(View.VISIBLE);

                txtNotCharge_a.setVisibility(View.GONE);
                txtchargeRider_a.setVisibility(View.GONE);
                txtRiderNotFound_a.setVisibility(View.GONE);
                txtRiderReqeuestToCancel_a.setVisibility(View.GONE);
                txtOther_a.setVisibility(View.GONE);

                txtNotCharge.setVisibility(View.VISIBLE);
                txtchargeRider.setVisibility(View.VISIBLE);
                txtRiderNotFound.setVisibility(View.VISIBLE);
                txtRiderReqeuestToCancel.setVisibility(View.VISIBLE);
                txtOther.setVisibility(View.VISIBLE);
                reason = "4";
            }
        });
        txtRiderReqeuestToCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtRiderReqeuestToCancel.setVisibility(View.GONE);
                txtRiderReqeuestToCancel_a.setVisibility(View.VISIBLE);

                txtNotCharge_a.setVisibility(View.GONE);
                txtchargeRider_a.setVisibility(View.GONE);
                txtRiderNotFound_a.setVisibility(View.GONE);
                txtRiderDidNotShow_a.setVisibility(View.GONE);
                txtOther_a.setVisibility(View.GONE);

                txtNotCharge.setVisibility(View.VISIBLE);
                txtchargeRider.setVisibility(View.VISIBLE);
                txtRiderNotFound.setVisibility(View.VISIBLE);
                txtRiderDidNotShow.setVisibility(View.VISIBLE);
                txtOther.setVisibility(View.VISIBLE);
                reason = "5";
            }
        });
        txtOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtOther.setVisibility(View.GONE);
                txtOther_a.setVisibility(View.VISIBLE);

                txtNotCharge_a.setVisibility(View.GONE);
                txtchargeRider_a.setVisibility(View.GONE);
                txtRiderNotFound_a.setVisibility(View.GONE);
                txtRiderDidNotShow_a.setVisibility(View.GONE);
                txtRiderReqeuestToCancel_a.setVisibility(View.GONE);

                txtNotCharge.setVisibility(View.VISIBLE);
                txtchargeRider.setVisibility(View.VISIBLE);
                txtRiderNotFound.setVisibility(View.VISIBLE);
                txtRiderDidNotShow.setVisibility(View.VISIBLE);
                txtRiderReqeuestToCancel.setVisibility(View.VISIBLE);
                reason = "6";
            }
        });

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelResonFun();

            }
        });


        //dialog.show();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });

    }

    private void CancelResonFun() {
        StringRequest getDriverRequest = new StringRequest(Request.Method.POST, cancelResonAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("CancelReasonResonse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
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

                if (status == 1) {
                    dialog.dismiss();
                    Intent intent = new Intent(MeetYourDriverActivity.this, DashActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MeetYourDriverActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("ride_id", set_id);
                params.put("cancel_reason_id", reason);
                Log.i("CancelReasonParams", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(getDriverRequest);
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 300) {
            Intent intent = new Intent(this, DashActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Don't Back...", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    //Call Rider/////////
    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    phoneCallImageView.setEnabled(true);
                    Toast.makeText(this, "You can call the number by checking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

   /* @Override
    protected void onResume() {

    }*/

   /* @Override
    protected void onResume() {
        //reachDestinationFunc();
        Intent intent = new Intent(MeetYourDriverActivity.this, SeachingDriverActivity.class);
        intent.putExtra("set_id", set_id);
        startActivity(intent);
        Toast.makeText(this, "hahahah", Toast.LENGTH_SHORT).show();
        super.onResume();
    }*/

    private void reachDestinationFunc() {
        StringRequest popUpShow_request = new StringRequest(Request.Method.POST, reachDestinationLocationAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("DestinationLocationReachResponse", "onResponse: " + response);
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
                        String statusFalg = resObj.getString("reach_falg");
                        if (statusFalg.equals("0")) {
                        } else if (statusFalg.equals("1")) {
                            mHandler.removeCallbacks(mToastRunnable);
                            Intent intent = new Intent(MeetYourDriverActivity.this, TripSummeryActivity.class);
                            intent.putExtra("set_id", set_id);
                            intent.putExtra("driver_image", driver_image);
                            startActivity(intent);
                        }
                    } catch (Exception e) {

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
                params.put("current_lat", Driver_Lat);
                params.put("current_long", Driver_Long);
                Log.d("reachDestinationParams", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(popUpShow_request);
    }

    private void reassignnedDriverFunc() {
        StringRequest popUpShow_request = new StringRequest(Request.Method.POST, reassignAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ReassignDriverResponse", "onResponse: " + response);
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
                        String statusFalg = resObj.getString("reassign_flag");
                        if (statusFalg.equals("1")) {
                            mHandler.removeCallbacks(mToastRunnable);
                            Intent intent = new Intent(MeetYourDriverActivity.this, SeachingDriverActivity.class);
                            intent.putExtra("set_id", set_id);
                            startActivity(intent);
                        }
                    } catch (Exception e) {

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
                Log.d("reassigned", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(popUpShow_request);
    }

    // Subhadip Polyline code start

    public void showPolyline() {

        final String direction_url = "https://maps.googleapis" +
                ".com/maps/api/directions/json?origin=" + URLEncoder.encode(Rider_Lat + "," + Rider_Long) + "&destination=" +
                URLEncoder.encode(Driver_Lat + "," + Driver_Long) + "&key=AIzaSyDS" +
                //Driver_Lat,Driver_Long
                "-sgfRY8xzB9vil3XcxevXMlopXT6Afg";

        StringRequest directionRequest = new StringRequest(Request.Method.GET, direction_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Direction: " + response);
                Log.d("TwoAdd", "onResponse: "+Rider_Lat+","+Rider_Long+",,"+Driver_Lat+","+Driver_Long);

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
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "haha", Toast.LENGTH_SHORT).show();
       // reachDestinationFunc();
    }
}

