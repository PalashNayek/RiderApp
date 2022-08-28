package com.example.hp.googlemap.rider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.ConfirmSchedulTrip;
import com.example.hp.googlemap.DrawRouteMaps;
import com.example.hp.googlemap.R;
import com.example.hp.googlemap.SeachingDriverActivity;
import com.example.hp.googlemap.TransparentProgressDialog;
import com.example.hp.googlemap.rider.CardImageAdapter.ImageAdapter;
import com.example.hp.googlemap.rider.CardImageAdapter.ModalAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfirmTrip extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = ConfirmTrip.class.getSimpleName();
    String BaseUrl = "http://45.55.64.233/Miles/api/";
    String ConfirmTripURLSET = BaseUrl + "set_ride_info.php";
    String ConfirmTripURLGET = BaseUrl + "get_ride_info.php";
    String ConfirmTripURL = BaseUrl + "confirm_trip.php";
    String UpdateTierURL = BaseUrl + "update_car_tier.php";
    String AddressLatLongURL = BaseUrl + "getLatLong.php";
    String UpdateAddressURL = "http://45.55.64.233/Miles/api/update_pickup.php";
    String car_tier, set_id, PickLat, PickLong;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CircleImageView carImageType;
    TextView cast, distance, arrival, car_type, card_num, PickUpLatt, txtEstTime;
    Button btnConfirm;
    EditText PromoCode;
    ImageView schedulTripImage, cardImage;
    ImageView img1, img2, img3, img4, img5;
    String markAdd, markAddCurrentLoc;
    String UpdatePickUpAddress;
    int backButtonCount;
    String DestinationLocationName = "", Current_locatin_name = "";
    double destiLat, destiLong, currentLat, currentLong;
    String imageCarType = "";
    ImageAdapter imageListAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ModalAdapter> imageCardArrayList = new ArrayList<>();
    //Subhadip
    LatLng origin;
    LatLng destination;
    TextView currentTextMarker, destTextMarker;
    String RiderCurrentLat, RiderCurrentLong;
    Polyline polyline = null;
    TextView distanceTxt;
    private GoogleMap mMap;
    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    private RecyclerView vehicleRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        h = new Handler();
        pd = new TransparentProgressDialog(this, R.drawable.spinner);
        r = new Runnable() {
            @Override
            public void run() {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }
        };
        pd.show();
        h.postDelayed(r, 3000);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        // System.out.print(UpdatePickUpAddress);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        schedulTripImage = (ImageView) findViewById(R.id.schedulTripImage);
        cast = (TextView) findViewById(R.id.cast);
        distance = (TextView) findViewById(R.id.distance);
        arrival = (TextView) findViewById(R.id.arrival);
        car_type = (TextView) findViewById(R.id.car_type);
        carImageType = (CircleImageView) findViewById(R.id.carTypeImage);
        PromoCode = (EditText) findViewById(R.id.edtPromoCode);
        card_num = (TextView) findViewById(R.id.card_num);
        PickUpLatt = (TextView) findViewById(R.id.PickUpLatt);
        cardImage = (ImageView) findViewById(R.id.cardImage);
        distanceTxt = (TextView) findViewById(R.id.distanceTxt);
        //txtEstTime = (TextView) findViewById(R.id.txtEstTime);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vehicleRecyclerView = (RecyclerView) findViewById(R.id.vehicleDetailsRecyclerView1);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);

        sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();

        //Car tier Image Click..............
        carImageType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // horiImage.setVisibility(View.VISIBLE);
                openBottomSheet();
            }
        });
        car_tier = getIntent().getStringExtra("car_tier");
        DestinationLocationName = getIntent().getStringExtra("locatin_name");
        Current_locatin_name = getIntent().getStringExtra("Current_locatin_name");
        Log.d("LocationName", "onCreate: " + DestinationLocationName + "," + Current_locatin_name);
        destTextMarker = findViewById(R.id.destTextMarker);
        currentTextMarker = findViewById(R.id.currentTextMarker);
        destTextMarker.setText(DestinationLocationName);
        currentTextMarker.setText(Current_locatin_name);
        getLocationFromAddress(DestinationLocationName);
        getCurrentLocationFromAddress(Current_locatin_name);

        schedulTripImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmTrip.this, ConfirmSchedulTrip.class);
                intent.putExtra("locatin_name", DestinationLocationName);
                intent.putExtra("Current_locatin_name", Current_locatin_name);
                startActivity(intent);
            }
        });

        StringRequest currentlocationRequest = new StringRequest(Request.Method.POST, ConfirmTripURLSET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("PrintResponse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }

                if (status == 1) {
                    try {
                        set_id = (resObj.getString("set_id"));

                        setRide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

                params.put("user_id", sharedPreferences.getString("user_id", ""));
                params.put("pickup_lat", String.valueOf(currentLat));
                params.put("pickup_long", String.valueOf(currentLong));
                params.put("pickup_addr", Current_locatin_name);

                params.put("drop_addr", DestinationLocationName);
                params.put("drop_lat", String.valueOf(destiLat));
                params.put("drop_long", String.valueOf(destiLong));

                params.put("car_tier", car_tier);

                Log.d("GetParamsConfirmActivit", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(currentlocationRequest);

        // Confirm Trip Button Click and Call function.........................

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        conformBtnFunc();


        //Subhadip polyline
        showPolyline();
    }

    @Override
    protected void onDestroy() {
        h.removeCallbacks(r);
        if (pd.isShowing()) {
            pd.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        origin = new LatLng(currentLat, currentLong);
        // LatLng origin = new LatLng(PickUpLath, PickUpLongh);
        destination = new LatLng(destiLat, destiLong);
        DrawRouteMaps.getInstance(this).draw(origin, destination, mMap);
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 40));
        List<Marker> lstMarcadores = new ArrayList<>();
        Marker marker1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(currentLat, currentLong))
                .title(markAddCurrentLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.greenmarker)));
        lstMarcadores.add(marker1);
        lstMarcadores.get(0).showInfoWindow();
        Marker markers = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(destiLat, destiLong))
                .title(markAdd).icon(BitmapDescriptorFactory.fromResource(R.drawable.redmarker)));
        lstMarcadores.add(markers);
        lstMarcadores.get(1).showInfoWindow();

        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    // For Get Ride show ..................
    private void setRide() {
        Double PickLat;
        StringRequest getConfirmTripRequest = new StringRequest(Request.Method.POST, ConfirmTripURLGET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("ConfirmTripResponse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }

                if (status == 1) {
//                    progressDialog.hide();
                    try {
                        cast.setText(resObj.getString("cost"));
                        distance.setText(resObj.getString("distance"));
                        arrival.setText(resObj.getString("arrival"));
                        car_type.setText(resObj.getString("car_type"));
                        distanceTxt.setText(resObj.getString("duration"));

                        // imagePath = resObj.getString("car_image");
                       /* cardImagePath = resObj.getString("card_img");
                        card_num.setText(resObj.getString("card"));*/
                        //System.out.println(imagePath);

                        imageCarType = resObj.getString("car_image");
                        Log.d("ImagePath", "onResponse: " + imageCarType);
                        //System.out.println(imageCarType);

                        //Picasso.with(ConfirmTrip.this).load(imagePath).resize(100, 100).into(carImage);
                        Picasso.with(ConfirmTrip.this).load(imageCarType).resize(110, 110).into(carImageType);

                        JSONObject jsonRootObject = new JSONObject(response);
                        JSONArray jsonArray = jsonRootObject.getJSONArray("card_details");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String cardImage = jsonObject.optString("card_type_image");
                            String cardNum = jsonObject.optString("card_no");

                            ModalAdapter vendorList = new ModalAdapter();
                            vendorList.setCard_details(cardImage);
                            vendorList.setCard_no(cardNum);
                            imageCardArrayList.add(vendorList);
                        }
                        vehicleRecyclerView.setLayoutManager(layoutManager);
                        imageListAdapter = new ImageAdapter(imageCardArrayList, ConfirmTrip.this);
                        vehicleRecyclerView.setAdapter(imageListAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                params.put("set_id", set_id);
                Log.d("SetIdParams", "getParams: " + set_id);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(getConfirmTripRequest);
    }

    public void basic() {

        StringRequest currentlocationRequest = new StringRequest(Request.Method.POST, UpdateTierURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("CarTierResponse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }
                if (status == 1) {
                    setRide();
                } else {
                    Toast.makeText(ConfirmTrip.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("set_id", set_id);
                params.put("car_tier", "milesComfort");
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(currentlocationRequest);
    }

    public void confort() {
        StringRequest currentlocationRequest = new StringRequest(Request.Method.POST, UpdateTierURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("CarTierResponse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }

                if (status == 1) {
                    setRide();
                } else {
                    Toast.makeText(ConfirmTrip.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("set_id", set_id);
                params.put("car_tier", "milesSUV");

                Log.i("Set Trip", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(currentlocationRequest);
    }

    public void milesSUV() {
        StringRequest currentlocationRequest = new StringRequest(Request.Method.POST, UpdateTierURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("CarTierResponse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }

                if (status == 1) {
                    setRide();
                } else {
                    Toast.makeText(ConfirmTrip.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("set_id", set_id);
                params.put("car_tier", "milesPickup");

                Log.i("Set Trip", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(currentlocationRequest);
    }

    public void milesPickup() {
        StringRequest currentlocationRequest = new StringRequest(Request.Method.POST, UpdateTierURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("CarTierResponse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }

                if (status == 1) {
                    setRide();
                } else {
                    Toast.makeText(ConfirmTrip.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("set_id", set_id);
                params.put("car_tier", "milesBlack");

                Log.i("Set Trip", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(currentlocationRequest);
    }

    public void milesBlack() {
        StringRequest currentlocationRequest = new StringRequest(Request.Method.POST, UpdateTierURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("CarTierResponse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }

                if (status == 1) {
                    setRide();
                } else {
                    Toast.makeText(ConfirmTrip.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("set_id", set_id);
                params.put("car_tier", "milesExecutive");

                Log.i("Set Trip", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(currentlocationRequest);
    }

    public void updatePickUpAddr() {
        StringRequest currentlocationRequest = new StringRequest(Request.Method.POST, AddressLatLongURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("UpdateAddressResponse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }

                if (status == 1) {
//                    progressDialog.hide();
                    try {
                        PickLat = (resObj.getString("latitude"));
                        PickLong = (resObj.getString("longitude"));
                        updateAddress();

                    } catch (Exception e) {

                    }

                } else {
                    // progressDialog.hide();
                    Toast.makeText(ConfirmTrip.this, "" + msg, Toast.LENGTH_SHORT).show();
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

                params.put("address", UpdatePickUpAddress);
                // params.put("address", UpdatePickUpAddress);

                Log.i("Registration Value", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(currentlocationRequest);
    }

    public void updateAddress() {
        StringRequest currentlocationRequest = new StringRequest(Request.Method.POST, UpdateAddressURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("UpdateAddressResponse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }

                if (status == 1) {
                    setRide();
                } else {
                    Toast.makeText(ConfirmTrip.this, "" + msg, Toast.LENGTH_SHORT).show();
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

                params.put("set_ride_id", set_id);
                params.put("pickup_lat", PickLat);
                params.put("pickup_long", PickLong);
                // Log.i("Registration Value", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(currentlocationRequest);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 2) {
            Intent intent = new Intent(this, DashActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press the back again to Home Page.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    //Current Address to Lat & Long..................
    public Address getCurrentLocationFromAddress(String current_locatin_name) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        Address location = null;

        try {
            address = coder.getFromLocationName(current_locatin_name, 5);

            // Address
            Log.i(TAG, "getCurrentLocationFromAddress: " + address);

            if (address == null) {
                return null;
            }
            location = address.get(0);
            currentLat = location.getLatitude();
            currentLong = location.getLongitude();
            // System.out.println(lll+","+nnn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    //Destination Address to Lat & Long..............
    public Address getLocationFromAddress(String DestinationLocationName) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        Address location = null;

        try {
            address = coder.getFromLocationName(DestinationLocationName, 5);
            if (address == null) {
                return null;
            }
            Log.i(TAG, "getLocationFromAddress: " + address);

            location = address.get(0);
            destiLat = location.getLatitude();
            destiLong = location.getLongitude();
            // System.out.println(lll+","+nnn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    private void openBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.car_type_dialog, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
        ImageView milesBasic = view.findViewById(R.id.img1);
        milesBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basic();
                dialog.dismiss();
            }
        });
        ImageView milesComfort = view.findViewById(R.id.img2);
        milesComfort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confort();
                dialog.dismiss();
            }
        });
        ImageView milesSUV = view.findViewById(R.id.img3);
        milesSUV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                milesSUV();
                dialog.dismiss();
            }
        });
        ImageView milesPicup = view.findViewById(R.id.img4);
        milesPicup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                milesPickup();
                dialog.dismiss();
            }
        });
        ImageView milesBlack = view.findViewById(R.id.img5);
        milesBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                milesBlack();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Subhadip Polyline code start

    public void showPolyline() {

        final String direction_url = "https://maps.googleapis" +
                ".com/maps/api/directions/json?origin=" + URLEncoder.encode(Current_locatin_name) + "&destination=" +
                URLEncoder.encode(DestinationLocationName) + "&key=AIzaSyDS" +
                "-sgfRY8xzB9vil3XcxevXMlopXT6Afg";
        StringRequest directionRequest = new StringRequest(Request.Method.GET, direction_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Direction: " + response);

                /*try {
                    JSONArray jsonArray = null;
                    JSONObject jsonObject = null;
                    jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
                    txtEstTime.setText(jsonArray.getJSONObject("duration").getString("text"));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }*/

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

            //txtEstTime.setText(googlePathJson.getJSONObject("duration").getString("text"));
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

    //Current Address Edit and Change...
    public void findPlace(View view) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, 1);

        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    //Drop Address Edit and Change...
    public void dropPlace(View view) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, 2);

        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
                currentTextMarker.setText(place.getAddress() + "\n" + place.getPhoneNumber());
                Current_locatin_name = place.getAddress().toString();
                //currentTextMarker.setText(place.getAddress() + "\n" + place.getPhoneNumber());
                Log.d("Current Address Click", "onActivityResult: " + currentTextMarker);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                destTextMarker.setText(place.getAddress() + "\n" + place.getPhoneNumber());
                DestinationLocationName = place.getAddress().toString();
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        showPolyline();


        Log.d("PolyLine", "onActivityResult: ");

    }

    private void conformBtnFunc() {
        RiderCurrentLat = String.valueOf(currentLat);
        RiderCurrentLong = String.valueOf(currentLong);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest ConfirmTripRequest = new StringRequest(Request.Method.POST, ConfirmTripURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: " + response);
                        JSONObject resObj = null;
                        int status = 0;
                        String msg = "";
                        try {
                            resObj = new JSONObject(response);
                            status = resObj.getInt("status");
                            msg = resObj.getString("message");
                        } catch (JSONException e) {
                            //e.printStackTrace();
                            Log.i(TAG, "onResponse: " + e);
                        }

                        if (status == 1) {
                            try {
                                // progressDialog.hide();
                                String TotalDriveAvl = (resObj.getString("total_driver"));
                               /* DriverLat = (resObj.getString("driverLat"));
                                DriverLong = (resObj.getString("driverLong"));*/
                                Intent intent = new Intent(ConfirmTrip.this, SeachingDriverActivity.class);
                                /*intent.putExtra("Clat", currentLat);
                                intent.putExtra("currentLati", currentLong);*/
                                intent.putExtra("RiderLat", RiderCurrentLat);
                                intent.putExtra("RiderLong", RiderCurrentLong);
                                intent.putExtra("set_id", set_id);
                                intent.putExtra("TotalDriveAvl", TotalDriveAvl);
                                // intent.putExtra("TotalDriveAvl", set_id);
                                Log.d("hahahalalal", "onResponse: " + currentLat + currentLong + "," + set_id);

                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
                        params.put("set_id", set_id);
                        params.put("promo_code", PromoCode.getText().toString());
                        params.put("confirm", "1");
                        Log.i("Set Trip", "getParams: " + params);
                        return params;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(ConfirmTripRequest);
            }
        });
    }


    //display polyline
    public void displayDirection(String[] directionsList) {
        mMap.clear();

        int count = directionsList.length;
        for (int i = 0; i < count; i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.BLACK);
            options.width(5);
            options.addAll(PolyUtil.decode(directionsList[i]));

            polyline = mMap.addPolyline(options);

        }
    }
    // Subhadip Polyline code ends here

    private void show(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
