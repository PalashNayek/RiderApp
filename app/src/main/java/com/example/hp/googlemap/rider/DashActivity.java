package com.example.hp.googlemap.rider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.ChooseActivity;
import com.example.hp.googlemap.CustomTypefaceSpan;
import com.example.hp.googlemap.EditPageActivity;
import com.example.hp.googlemap.MapsActivity;
import com.example.hp.googlemap.MilesInfoActivity;
import com.example.hp.googlemap.PaymentActivity;
import com.example.hp.googlemap.PlaceAdapter;
import com.example.hp.googlemap.PushNotification;
import com.example.hp.googlemap.R;
import com.example.hp.googlemap.SchedulTripActivity;
import com.example.hp.googlemap.SendRiderActivity;
import com.example.hp.googlemap.ShareMilesActivity;
import com.example.hp.googlemap.TransparentProgressDialog;
import com.example.hp.googlemap.TripHistoryActivity;
import com.example.hp.googlemap.base.MyApplication;
import com.example.hp.googlemap.driver.DriverLogInPage;
import com.example.hp.googlemap.riderdto.LocationDto;
import com.example.hp.googlemap.wrapper.LocationWrapper;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION = 1;
    private final static int PLACE_PICKER_REQUEST = 1;
    private final static int PLACE_PICKER_DESTI = 2;
    DrawerLayout drawer;
    ImageView profile_pic;
    String imagePath = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LocationManager locationManager;
    EditText destination;
    String ProfileDetailsAPIURL = "http://45.55.64.233/Miles/api/getUserImg.php";
    String LocalURL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDS-sgfRY8xzB9vil3XcxevXMlopXT6Afg&input=";
    String CurrentLocationURL = "http://45.55.64.233/Miles/api/get_address.php";
    String getHomeAddressURL = "http://45.55.64.233/Miles/api/getSavedAddress.php";
    String updateHomeAddressURL = "http://45.55.64.233/Miles/api/setHome.php";
    String updateWorkAddressURL = "http://45.55.64.233/Miles/api/setWork.php";
    ImageView img_send_riders;
    TextView textView, textView2;
    LocationManager locationManagers;
    String lattitude, longitude;
    EditText confirmTripEdit;
    int backButtonCount;
    String image_selected;
    ImageView img1, img2, img3, img4, img5;
    RecyclerView rv_location;
    PlaceAdapter placeAdapter;
    boolean isAdapterCreated = false;
    EditText searched_address, currentLocation, search_add_home, search_add_work;
    String currentAddress = "";
    ImageView pinOnMap;
    TextView homeAddress, workAddress;
    String savedAddress;
    LinearLayout linearLayoutHome;
    String homeAddressEmpty = "";
    String workAddressEmpty = "";
    ProgressBar simpleProgressBar;
    EditText number_rider_txt;
    ImageView car_suv_b;
    LinearLayout locationBottomSheet;
    Typeface quickSandTf;
    private GoogleMap mMap;
    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    TextView one,oneBack,two,twoBack,three,threeBack,four,fourBack,five,fiveBack,six,sixBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_navigation);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        //Initilization variable........
        one=findViewById(R.id.one);
        oneBack=findViewById(R.id.oneBack);
        two=findViewById(R.id.two);
        twoBack=findViewById(R.id.twoBack);
        three=findViewById(R.id.three);
        threeBack=findViewById(R.id.threeBack);
        four=findViewById(R.id.four);
        fourBack=findViewById(R.id.fourBack);
        five=findViewById(R.id.five);
        fiveBack=findViewById(R.id.fiveBack);
        six=findViewById(R.id.six);
        sixBack=findViewById(R.id.sixBack);

        //QuickSand TypeFace
        quickSandTf = Typeface.createFromAsset(getAssets(), "font/quicksand_regular.ttf");

        destination = (EditText) findViewById(R.id.destination);
        destination.setTypeface(quickSandTf);

        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManagers = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManagers.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManagers.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }
                System.out.println(lattitude + "," + longitude);
                currentLocationMap();
                showBottomSheet();
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //Check the network provider enable..............
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mMap.clear();
                    //get the latitude
                    double latitude = location.getLatitude();
                    //get the longitude
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getLocality() + " , ";
                        str += addressList.get(0).getCountryName();
                        //mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.addMarker(new MarkerOptions().position(latLng).snippet("Your Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocationmarker)));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //get the latitude
                    double latitude = location.getLatitude();
                    //get the longitude
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getLocality() + " , ";
                        str += addressList.get(0).getCountryName();
                        // mMap.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocationmarker));
                        // mMap.addMarker(new MarkerOptions().position(latLng).title("Your Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocationmarker)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 137.0f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }


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

        //Number of Riders......
        number_rider_txt = (EditText) findViewById(R.id.number_rider_txt);
        number_rider_txt.setTypeface(quickSandTf);

        number_rider_txt.addTextChangedListener(new CheckPercentage());
        //car_suv_b=(ImageView)findViewById(R.id.car_suv_b);
        pd.show();
        h.postDelayed(r, 2000);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();
        simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        profile_pic = (ImageView) findViewById(R.id.profile_img);
        confirmTripEdit = (EditText) findViewById(R.id.confirmTripEdit);
        // getImages();
        confirmTripEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashActivity.this, SchedulTripActivity.class);
                startActivity(intent);
            }
        });

        // turnGPSOn();

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);
        image_selected = "milesComfort";

        // Array of ImageView
        final ImageView[] levelsArray = {img1, img2, img3, img4, img5};

        for (int i = 0; i < 5; i++) {
            levelsArray[i].setOnClickListener(this);
        }
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        final ImageView imgvw = (CircleImageView) hView.findViewById(R.id.imageView);
        TextView name = (TextView) hView.findViewById(R.id.txt_name);
        TextView email = (TextView) hView.findViewById(R.id.txt_email);
        name.setText(sharedPreferences.getString("firstname", "") + " " + sharedPreferences.getString("lastname", ""));
        email.setText(sharedPreferences.getString("joined", ""));
        System.out.println(imagePath);
        hView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashActivity.this, EditPageActivity.class);
                startActivity(intent);
            }
        });

        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem, i);
                }
            }
            applyFontToMenuItem(mi, i);
        }

        //Click Car Image.............
        img_send_riders = (ImageView) findViewById(R.id.img_send_riders);
        img_send_riders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashActivity.this, SendRiderActivity.class);
                startActivity(intent);
            }
        });

        // Click Destination.............


        // Set Image ...................
        StringRequest profile_details_request = new StringRequest(Request.Method.POST, ProfileDetailsAPIURL, new Response.Listener<String>() {

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
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }
                if (status == 1) {
                    try {
                        imagePath = resObj.getString("image");
                        Picasso.with(DashActivity.this).load(imagePath).resize(100, 100).into(imgvw);
                        //Log.d("ImgPath", "onResponse: "+imagePath);
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
                params.put("user_email", sharedPreferences.getString("mail", ""));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(profile_details_request);

        // For LatLong..............
        textView = (TextView) findViewById(R.id.text_location);
        textView2 = (TextView) findViewById(R.id.text_location_long);


        //Select Rider Number
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneBack.setVisibility(View.GONE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                twoBack.setVisibility(View.GONE);
                three.setVisibility(View.VISIBLE);
                threeBack.setVisibility(View.GONE);
                four.setVisibility(View.VISIBLE);
                fourBack.setVisibility(View.GONE);
                five.setVisibility(View.VISIBLE);
                fiveBack.setVisibility(View.GONE);
                six.setVisibility(View.VISIBLE);
                sixBack.setVisibility(View.GONE);

            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneBack.setVisibility(View.GONE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.GONE);
                twoBack.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                threeBack.setVisibility(View.GONE);
                four.setVisibility(View.VISIBLE);
                fourBack.setVisibility(View.GONE);
                five.setVisibility(View.VISIBLE);
                fiveBack.setVisibility(View.GONE);
                six.setVisibility(View.VISIBLE);
                sixBack.setVisibility(View.GONE);
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneBack.setVisibility(View.GONE);
                one.setVisibility(View.VISIBLE);
                twoBack.setVisibility(View.GONE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.GONE);
                threeBack.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);
                fourBack.setVisibility(View.GONE);
                five.setVisibility(View.VISIBLE);
                fiveBack.setVisibility(View.GONE);
                six.setVisibility(View.VISIBLE);
                sixBack.setVisibility(View.GONE);

            }
        });

    }//****************************************************************************************************

    private void showBottomSheet() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(destination.getWindowToken(), 0);
        View view = getLayoutInflater().inflate(R.layout.content_bottom_sheet, null);

        AlertDialog dialog = new AlertDialog.Builder(DashActivity.this, R.style.DialogTheme)
                .setView(view)
                .create();

//        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        //Pin On Map..................
        pinOnMap = view.findViewById(R.id.pinOnMap);
        // pinOnMap.setVisibility(View.GONE);

        //Add Home Click...............
        ImageView addHome = view.findViewById(R.id.addHome);
        addHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHomeAddress();
            }
        });

        //Add Work Click...............
        ImageView addWork = view.findViewById(R.id.addWork);
        addWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWorkAddress();
            }
        });

        //Click Current Address .........................
        currentLocation = view.findViewById(R.id.currentLocation);
        rv_location = view.findViewById(R.id.rv_location);
        rv_location.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //locationBottomSheet= view.findViewById(R.id.locationBottomSheet);
//        locationBottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        currentLocation.setTypeface(quickSandTf);

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdapterCreated = false;
                pinOnMap.setVisibility(View.VISIBLE);
                currentPinOnMap();
            }
        });

        currentLocation.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                currentLocationAPI(s.toString());
                //Toast.makeText(DashActivity.this, "Hi....", Toast.LENGTH_SHORT).show();
            }
        });

        //Click Destination Address ..............search............
        searched_address = view.findViewById(R.id.searched_address);
        searched_address.setTypeface(quickSandTf);

        rv_location = view.findViewById(R.id.rv_location);
        rv_location.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        searched_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdapterCreated = false;
                pinOnMap.setVisibility(View.VISIBLE);
                destinationPinOnMap();
            }
        });
        searched_address.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                callAPI(s.toString());
            }
        });


    }

    @Override
    protected void onDestroy() {
        h.removeCallbacks(r);
        if (pd.isShowing()) {
            pd.dismiss();
        }
        super.onDestroy();
    }

    ////////////////////////................Selected Image Information........////////////////////
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img1:
                image_selected = "milesComfort";
                img1.setImageResource(R.drawable.comfort_br);
                img2.setImageResource(R.drawable.suv_w);
                img3.setImageResource(R.drawable.pickup_w);
                img4.setImageResource(R.drawable.black_w);
                img5.setImageResource(R.drawable.execlusive_w);
                break;
            case R.id.img2:
                image_selected = "milesSUV";
                img1.setImageResource(R.drawable.comfort_w);
                img2.setImageResource(R.drawable.suv_br);
                img3.setImageResource(R.drawable.pickup_w);
                img4.setImageResource(R.drawable.black_w);
                img5.setImageResource(R.drawable.execlusive_w);
                break;
            case R.id.img3:
                image_selected = "milesPickup";
                img1.setImageResource(R.drawable.comfort_w);
                img2.setImageResource(R.drawable.suv_w);
                img3.setImageResource(R.drawable.pickup_br);
                img4.setImageResource(R.drawable.black_w);
                img5.setImageResource(R.drawable.execlusive_w);
                break;
            case R.id.img4:
                image_selected = "milesBlack";
                img1.setImageResource(R.drawable.comfort_w);
                img2.setImageResource(R.drawable.suv_w);
                img3.setImageResource(R.drawable.pickup_w);
                img4.setImageResource(R.drawable.black_br);
                img5.setImageResource(R.drawable.execlusive_w);
                break;
            case R.id.img5:
                image_selected = "milesExecutive";
                img1.setImageResource(R.drawable.comfort_w);
                img2.setImageResource(R.drawable.suv_w);
                img3.setImageResource(R.drawable.pickup_w);
                img4.setImageResource(R.drawable.black_w);
                img5.setImageResource(R.drawable.ex_br);
                break;
        }
    }

    private void applyFontToMenuItem(MenuItem mi, int pos) {
        Typeface font = Typeface.createFromAsset(getAssets(), "font/quicksand_regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        if (pos < 8) {
            mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        } else {
            mNewTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bgColor)), 0, mNewTitle.length(), 0); // fix the color to white
            mNewTitle.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //  mNewTitle.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.defaultFromStyle(sty)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // mNewTitle.setSpan(new android.text.style.fo

        }
        mi.setTitle(mNewTitle);
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    //For LatLong.............
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(DashActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (DashActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManagers.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManagers.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManagers.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                textView.setText("Lattitude = " + lattitude);
                textView2.setText("Longitude = " + longitude);

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                textView.setText("Lattitude = " + lattitude);
                textView2.setText("Longitude = " + longitude);


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                textView.setText("Lattitude = " + lattitude);
                textView2.setText("Longitude = " + longitude);

            } else {
                // Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 2) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            // Toast.makeText(this, "Press the back again to Exit.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.testing_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(DashActivity.this, DashActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_payment) {
            Intent intent = new Intent(DashActivity.this, PaymentActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_trip_history) {
            Intent intent = new Intent(DashActivity.this, TripHistoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_miles_info) {
            Intent intent = new Intent(DashActivity.this, MilesInfoActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_notification) {
            Intent intent = new Intent(DashActivity.this, PushNotification.class);
            startActivity(intent);

        } else if (id == R.id.nav_share_miles) {
            Intent intent = new Intent(DashActivity.this, ShareMilesActivity.class);
            startActivity(intent);

            //nav_become_driver
        } else if (id == R.id.nav_become_driver) {
            SharedPreferences.Editor editor = getSharedPreferences("user_pref", Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(DashActivity.this, DriverLogInPage.class);
            startActivity(intent);
            finish();
           /* SharedPreferences.Editor editor = getSharedPreferences("user_pref", Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(DashActivity.this, DriverLogInPage.class);
            startActivity(intent);
            finish();*/

        } else if (id == R.id.nav_send) {
        } else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = getSharedPreferences("user_pref", Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(DashActivity.this, ChooseActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    //Bottom Sheets Dialog.................
    private void openBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.content_bottom_sheet, null);

        //Pin On Map..................
        pinOnMap = view.findViewById(R.id.pinOnMap);
        // pinOnMap.setVisibility(View.GONE);

        //Add Home Click...............
        ImageView addHome = view.findViewById(R.id.addHome);
        addHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHomeAddress();
            }
        });

        //Add Work Click...............
        ImageView addWork = view.findViewById(R.id.addWork);
        addWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWorkAddress();
            }
        });

        quickSandTf = Typeface.createFromAsset(getAssets(), "font/quicksand_regular.ttf");

        //Click Current Address .........................
        currentLocation = view.findViewById(R.id.currentLocation);
        rv_location = view.findViewById(R.id.rv_location);
        rv_location.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //locationBottomSheet= view.findViewById(R.id.locationBottomSheet);
//        locationBottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        currentLocation.setTypeface(quickSandTf);

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdapterCreated = false;
                pinOnMap.setVisibility(View.VISIBLE);
                currentPinOnMap();
            }
        });

        currentLocation.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                currentLocationAPI(s.toString());
                //Toast.makeText(DashActivity.this, "Hi....", Toast.LENGTH_SHORT).show();
            }
        });

        //Click Destination Address ..............search............
        searched_address = view.findViewById(R.id.searched_address);
        searched_address.setTypeface(quickSandTf);

        rv_location = view.findViewById(R.id.rv_location);
        rv_location.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        searched_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdapterCreated = false;
                pinOnMap.setVisibility(View.VISIBLE);
                destinationPinOnMap();
            }
        });
        searched_address.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                callAPI(s.toString());
            }
        });

        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.show();

    }

    private void callAPI(String location) {
        StringRequest profile_details_request = new StringRequest(Request.Method.POST, LocalURL + location, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                JSONObject resObj = null;
                String status = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getString("status");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }
                if (status.equals("OK")) {
                    LocationWrapper locationWrapper = MyApplication.gson.fromJson(response, LocationWrapper.class);
                    if (!isAdapterCreated) {
                        placeAdapter = new PlaceAdapter(locationWrapper.predictions, DashActivity.this);
                        rv_location.setAdapter(placeAdapter);
                        isAdapterCreated = true;
                    } else {
                        placeAdapter.updateData(locationWrapper.predictions);
                    }

                    placeAdapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(LocationDto locationDto) {
                            //Your Required Data
                            Intent intent = new Intent(DashActivity.this, ConfirmTrip.class);
                            intent.putExtra("locatin_name", locationDto.description);
                            intent.putExtra("Current_locatin_name", currentAddress);
                            intent.putExtra("car_tier", image_selected);
                            searched_address.setText(locationDto.description);
                            startActivity(intent);
                        }
                    });
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
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(profile_details_request);
    }

    private void currentLocationAPI(final String location) {
        StringRequest profile_details_request = new StringRequest(Request.Method.POST, LocalURL + location, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                JSONObject resObj = null;
                String status = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getString("status");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }
                if (status.equals("OK")) {
                    LocationWrapper locationWrapper = MyApplication.gson.fromJson(response, LocationWrapper.class);
                    if (!isAdapterCreated) {
                        placeAdapter = new PlaceAdapter(locationWrapper.predictions, DashActivity.this);
                        rv_location.setAdapter(placeAdapter);
                        isAdapterCreated = true;
                    } else {
                        placeAdapter.updateData(locationWrapper.predictions);
                    }

                    placeAdapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(LocationDto locationDto) {
                            //Your Required Data
                            currentLocation.setText(locationDto.description);
                            Log.d("CuurentAdd", "onItemClick: " + currentLocation);
                        }
                    });
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
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(profile_details_request);

    }

    private void currentLocationMap() {
        StringRequest currentlocationRequest = new StringRequest(Request.Method.POST, CurrentLocationURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("CurrentLocationResponse", "onResponse: " + response);
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
                    //  progressDialog.hide();
                    try {
                        //  currentLocation.setText(resObj.getString("details"));
                        currentAddress = (resObj.getString("details"));
                    } catch (Exception e) {

                    }

                } else {
                    //progressDialog.hide();
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
                params.put("latitude", lattitude);
                params.put("longitude", longitude);
                Log.i("Registration Value", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(currentlocationRequest);
    }

    private void currentPinOnMap() {
        pinOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(DashActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void destinationPinOnMap() {
        pinOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(DashActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_DESTI);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(DashActivity.this, data);
                //placeNameText.setText(place.getName());
                currentLocation.setText(place.getAddress());
                pinOnMap.setVisibility(View.GONE);
            }
        }
        if (requestCode == PLACE_PICKER_DESTI) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(DashActivity.this, data);
                //placeNameText.setText(place.getName());
                searched_address.setText(place.getAddress());
                pinOnMap.setVisibility(View.GONE);
                Intent intent = new Intent(DashActivity.this, ConfirmTrip.class);
                intent.putExtra("locatin_name", searched_address.getText().toString());
                intent.putExtra("Current_locatin_name", currentAddress);
                intent.putExtra("car_tier", image_selected);
                startActivity(intent);
            }
        }
    }

    private void addHomeAddress() {
        View view = getLayoutInflater().inflate(R.layout.add_home_dialog, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
        final TextView homeAddress = view.findViewById(R.id.homeAddress);

        //Get Home Address
        StringRequest getHomeAddRequest = new StringRequest(Request.Method.POST, getHomeAddressURL, new Response.Listener<String>() {
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
                    Log.i("PalashNayek", "onResponse: " + e);
                }

                if (status == 1) {
                    try {
                        homeAddressEmpty = (resObj.getString("home"));
                        if (homeAddressEmpty.equals("Please add home address")) {
                            linearLayoutHome.setVisibility(View.GONE);  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        } else {
                            homeAddress.setText(resObj.getString("home"));
                        }

                    } catch (Exception e) {
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
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
                Log.i("PPPP", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(getHomeAddRequest);


        linearLayoutHome = view.findViewById(R.id.linearLayoutHome);
        linearLayoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(DashActivity.this, "Click Add Home Address", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashActivity.this, ConfirmTrip.class);
                intent.putExtra("locatin_name", homeAddress.getText().toString());
                intent.putExtra("Current_locatin_name", currentAddress);
                intent.putExtra("car_tier", image_selected);
                startActivity(intent);
            }
        });

        //Click Home Address ..............search............
        search_add_home = view.findViewById(R.id.search_add_home);
        rv_location = view.findViewById(R.id.rv_location);
        rv_location.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        search_add_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdapterCreated = false;
                /*pinOnMap.setVisibility(View.VISIBLE);
                destinationPinOnMap();*/

            }
        });
        search_add_home.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                calladdHomeAddressSearch(s.toString());
            }
        });
        dialog.show();
    }

    private void addWorkAddress() {
        View view = getLayoutInflater().inflate(R.layout.add_work_dialog, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
        final TextView workAddress = view.findViewById(R.id.workAddress);

        //Get Home Address
        StringRequest getHomeAddRequest = new StringRequest(Request.Method.POST, getHomeAddressURL, new Response.Listener<String>() {
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
                    Log.i("PalashNayek", "onResponse: " + e);
                }

                if (status == 1) {
                    try {

                        workAddressEmpty = (resObj.getString("work"));
                        if (workAddressEmpty.equals("Please add work address")) {
                            linearLayoutHome.setVisibility(View.GONE);  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        } else {
                            workAddress.setText(resObj.getString("work"));
                        }
                    } catch (Exception e) {
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
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
                Log.i("PPPP", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(getHomeAddRequest);

        linearLayoutHome = view.findViewById(R.id.linearLayoutHome);
        linearLayoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(DashActivity.this, "Click Add Home Address", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashActivity.this, ConfirmTrip.class);
                intent.putExtra("locatin_name", workAddress.getText().toString());
                intent.putExtra("Current_locatin_name", currentAddress);
                intent.putExtra("car_tier", image_selected);
                startActivity(intent);
            }
        });

        //Click Home Address ..............search............
        search_add_work = view.findViewById(R.id.search_add_work);
        rv_location = view.findViewById(R.id.rv_location);
        rv_location.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        search_add_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdapterCreated = false;
            }
        });
        search_add_work.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                calladdWorkAddressSearch(s.toString());
            }
        });
        dialog.show();
    }

    private void calladdHomeAddressSearch(String location) {
        StringRequest profile_details_request = new StringRequest(Request.Method.POST, LocalURL + location, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                JSONObject resObj = null;
                String status = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getString("status");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }
                if (status.equals("OK")) {
                    LocationWrapper locationWrapper = MyApplication.gson.fromJson(response, LocationWrapper.class);
                    if (!isAdapterCreated) {
                        placeAdapter = new PlaceAdapter(locationWrapper.predictions, DashActivity.this);
                        rv_location.setAdapter(placeAdapter);
                        isAdapterCreated = true;
                    } else {
                        placeAdapter.updateData(locationWrapper.predictions);
                    }

                    placeAdapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(LocationDto locationDto) {
                            //Your Required Data

                            // address=locationDto.description;***************************************************************************************
                            search_add_home.setText(locationDto.description);
                            updateHomeAddress();
                            Intent intent = new Intent(DashActivity.this, ConfirmTrip.class);
                            intent.putExtra("locatin_name", search_add_home.getText().toString());
                            intent.putExtra("Current_locatin_name", currentAddress);
                            intent.putExtra("car_tier", image_selected);
                            startActivity(intent);
                        }
                    });
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
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(profile_details_request);
    }

    private void calladdWorkAddressSearch(String location) {
        StringRequest profile_details_request = new StringRequest(Request.Method.POST, LocalURL + location, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                JSONObject resObj = null;
                String status = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getString("status");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }
                if (status.equals("OK")) {
                    LocationWrapper locationWrapper = MyApplication.gson.fromJson(response, LocationWrapper.class);
                    if (!isAdapterCreated) {
                        placeAdapter = new PlaceAdapter(locationWrapper.predictions, DashActivity.this);
                        rv_location.setAdapter(placeAdapter);
                        isAdapterCreated = true;
                    } else {
                        placeAdapter.updateData(locationWrapper.predictions);
                    }
                    placeAdapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(LocationDto locationDto) {
                            search_add_work.setText(locationDto.description);
                            updateWorkAddress();
                            Intent intent = new Intent(DashActivity.this, ConfirmTrip.class);
                            intent.putExtra("locatin_name", search_add_work.getText().toString());
                            intent.putExtra("Current_locatin_name", currentAddress);
                            intent.putExtra("car_tier", image_selected);
                            startActivity(intent);
                        }
                    });
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
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(profile_details_request);
    }

    //Update Home Address..................................
    private void updateHomeAddress() {
        StringRequest addHomeAddRequest = new StringRequest(Request.Method.POST, updateHomeAddressURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("UpdateResponse", "onResponse: " + response);
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
                        homeAddress.setText(resObj.getString("home"));
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                    } catch (Exception e) {

                    }

                } else {
                    // progressDialog.hide();
                    //display error msg here
                    Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("home_addr", search_add_home.getText().toString());
                Log.i("Update Add Home Value", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(addHomeAddRequest);
    }

    private void updateWorkAddress() {
        StringRequest addHomeAddRequest = new StringRequest(Request.Method.POST, updateWorkAddressURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("UpdateResponse", "onResponse: " + response);
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
                        workAddress.setText(resObj.getString("work"));
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("work_addr", search_add_work.getText().toString());
                Log.i("Update Add Home Value", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(addHomeAddRequest);
    }

    //Numbers of Riders Numbers Changes.....
    class CheckPercentage implements TextWatcher {
        //int s = 1;

        public void afterTextChanged(Editable s) {
            try {
                Log.d("Percentage", "input: " + s);
                //int s = 1;
                if (Integer.parseInt(s.toString()) > 0) {

                    if (Integer.parseInt(s.toString()) == 5) {
                        img2.setVisibility(View.VISIBLE);
                        img5.setVisibility(View.VISIBLE);
                        img1.setVisibility(View.GONE);
                        img3.setVisibility(View.GONE);
                        img4.setVisibility(View.GONE);

                    }
                    if (Integer.parseInt(s.toString()) == 6) {
                        img2.setVisibility(View.VISIBLE);
                        img5.setVisibility(View.VISIBLE);
                        img1.setVisibility(View.GONE);
                        img3.setVisibility(View.GONE);
                        img4.setVisibility(View.GONE);

                    }

                    if (Integer.parseInt(s.toString()) == 1) {
                        img2.setVisibility(View.VISIBLE);
                        img5.setVisibility(View.VISIBLE);
                        img1.setVisibility(View.VISIBLE);
                        img3.setVisibility(View.VISIBLE);
                        img4.setVisibility(View.VISIBLE);

                    }
                    if (Integer.parseInt(s.toString()) == 3) {
                        img2.setVisibility(View.VISIBLE);
                        img5.setVisibility(View.VISIBLE);
                        img1.setVisibility(View.VISIBLE);
                        img3.setVisibility(View.VISIBLE);
                        img4.setVisibility(View.VISIBLE);

                    }
                    if (Integer.parseInt(s.toString()) == 4) {
                        img2.setVisibility(View.VISIBLE);
                        img5.setVisibility(View.VISIBLE);
                        img1.setVisibility(View.VISIBLE);
                        img3.setVisibility(View.VISIBLE);
                        img4.setVisibility(View.VISIBLE);

                    }

                    if (Integer.parseInt(s.toString()) > 6)
                        s.replace(0, s.length(), "6");
                    Toast.makeText(DashActivity.this, "Maximum Numbers of Riders 6 . ", Toast.LENGTH_SHORT).show();


                } else {
                    img2.setVisibility(View.VISIBLE);
                    img5.setVisibility(View.VISIBLE);
                    img1.setVisibility(View.VISIBLE);
                    img3.setVisibility(View.VISIBLE);
                    img4.setVisibility(View.VISIBLE);
                }

            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not used, details on text just before it changed
            // used to track in detail changes made to text, e.g. implement an undo
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Not used, details on text at the point change made
            number_rider_txt.setTypeface(quickSandTf);
        }
    }
}
