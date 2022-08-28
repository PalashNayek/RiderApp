package com.example.hp.googlemap;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.base.MyApplication;
import com.example.hp.googlemap.rider.DashActivity;
import com.example.hp.googlemap.riderdto.LocationDto;
import com.example.hp.googlemap.wrapper.LocationWrapper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SchedulTripActivity extends AppCompatActivity {
    private static final String TAG = SchedulTripActivity.class.getSimpleName();
    String BaseUrl = "http://45.55.64.233/Miles/api/";
    String scheduleTripURL = BaseUrl + "schedule_trip.php";
    String LocalURL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDS-sgfRY8xzB9vil3XcxevXMlopXT6Afg&input=";
    ProgressDialog progressDialog;
    ImageView ImgeBtnSchudel;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String PickUpAdd, DropAdd;
    ImageView backArrowBtn;
    MaterialCalendarView calendarView;
    TextView showDateTxt;
    TextView time, time_txt;
    TimePicker simpleTimePicker;
    EditText searched_address, drop_address;
    EditText search_pick_up_address, search_drop_address;
    boolean isAdapterCreated = false;
    PlaceAdapter placeAdapter;
    RecyclerView rv_location;
    String PickUpAddress = "";
    ProgressBar simpleProgressBar;
    BottomSheetDialog dialog;
    LinearLayout linearLayoutLogin;
    private TextClock tClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedul_trip);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        linearLayoutLogin = (LinearLayout) findViewById(R.id.linearLayoutLogin);
        //ImgeBtnSchudel = (ImageView) findViewById(R.id.ImgeBtnSchudel);
        sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();

        ImgeBtnSchudel = (ImageView) findViewById(R.id.ImgeBtnSchudel);
        ImgeBtnSchudel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnImage();
            }
        });
        backArrowBtn = (ImageView) findViewById(R.id.backArrowBtn);
        time_txt = (TextView) findViewById(R.id.time_txt);
        simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
//        simpleProgressBar.setVisibility(View.GONE);

        //PickUp Address Click...............
        searched_address = (EditText) findViewById(R.id.searched_address);
        searched_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPickUpAddress(); ///////////////////////////////////
            }
        });

        //Drop Address Click...............
        drop_address = (EditText) findViewById(R.id.PickOff);
        drop_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDropAddress(); ///////////////////////////////////
            }
        });

        showDateTxt = (TextView) findViewById(R.id.showDateTxt);
        tClock = (TextClock) findViewById(R.id.textClock1);
        tClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker();
            }
        });

        time_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPickerr();
            }
        });

        backArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SchedulTripActivity.this, DashActivity.class);
                startActivity(intent);
            }
        });

        // For Calendar.........................

        calendarView = (MaterialCalendarView) findViewById(R.id.calendarGridView);
        Date date = new Date();
        showDateTxt.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());
        calendarView.setCurrentDate(date);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                showDateTxt.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());
            }
        });
        time = (TextView) findViewById(R.id.time_txt);

    }

    public void showHourPicker() {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                    tClock.setVisibility(View.GONE);
                    time_txt.setVisibility(View.VISIBLE);
                    time_txt.setText(hourOfDay + ":" + minute);


                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Black_NoTitleBar, myTimeListener, hour, minute, false);
        timePickerDialog.setTitle("Choose hour:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public void showHourPickerr() {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                    // tClock.setVisibility(View.VISIBLE);
                    time_txt.setVisibility(View.VISIBLE);
                    time_txt.setText(hourOfDay + ":" + minute);
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Black_NoTitleBar, myTimeListener, hour, minute, false);
        timePickerDialog.setTitle("Choose hour:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public void btnImage() {
        //validate();
        StringRequest scheduleRequest = new StringRequest(Request.Method.POST, scheduleTripURL, new Response.Listener<String>() {
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
                    Toast.makeText(SchedulTripActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SchedulTripActivity.this, TripHistoryActivity.class);
                    startActivity(intent);


                } else {
                    Snackbar snackbar = Snackbar.make(linearLayoutLogin, "" + msg, Snackbar.LENGTH_LONG);// Snackbar message
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#ff0000")); // snackbar background color
                    snackbar.setActionTextColor(Color.parseColor("#FFFFFF")); // snackbar action text color
                    snackbar.show();

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
                params.put("pickup_addr", searched_address.getText().toString());
                params.put("drop_addr", drop_address.getText().toString());
                params.put("scheduled_time", time_txt.getText().toString());
                params.put("scheduled_dt", showDateTxt.getText().toString());

                Log.i("Set Trip", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(scheduleRequest);

    }

    //Bottom PickUp Address Sheets Dialog.................
    private void openPickUpAddress() {
        View view = getLayoutInflater().inflate(R.layout.pick_up_address, null);

        //Click Destination Address ..............search............
        search_pick_up_address = view.findViewById(R.id.search_pick_up_address);
        rv_location = view.findViewById(R.id.rv_location);
        rv_location.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        search_pick_up_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdapterCreated = false;
                /*pinOnMap.setVisibility(View.VISIBLE);
                destinationPinOnMap();*/

            }
        });
        search_pick_up_address.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
//                simpleProgressBar.setVisibility(View.VISIBLE);
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                pickAddressSearch(s.toString());
            }
        });

        dialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.show();
    }

    //Bottom Drop Address Sheets Dialog.................
    private void openDropAddress() {
        View view = getLayoutInflater().inflate(R.layout.drop_address, null);
        dialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.show();

        //Click Destination Address ..............search............
        search_drop_address = view.findViewById(R.id.search_add_drop);
        rv_location = view.findViewById(R.id.rv_location);
        rv_location.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        search_drop_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdapterCreated = false;
                /*pinOnMap.setVisibility(View.VISIBLE);
                destinationPinOnMap();*/

            }
        });
        search_drop_address.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dropAddressSearch(s.toString());
                // Toast.makeText(SchedulTripActivity.this, "Haha....", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void pickAddressSearch(String location) {
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
                        placeAdapter = new PlaceAdapter(locationWrapper.predictions, SchedulTripActivity.this);
                        rv_location.setAdapter(placeAdapter);
                        isAdapterCreated = true;
                    } else {
                        placeAdapter.updateData(locationWrapper.predictions);
                    }

                    placeAdapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(LocationDto locationDto) {
                            //Your Required Data
                            search_pick_up_address.setText(locationDto.description);
                            //simpleProgressBar.setVisibility(View.VISIBLE);
                            PickUpAddress = (locationDto.description);
                            searched_address.setText(PickUpAddress);
                            //search_pick_up_address.setCancelable(false);
                            dialog.dismiss();
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

    private void dropAddressSearch(String location) {
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
                        placeAdapter = new PlaceAdapter(locationWrapper.predictions, SchedulTripActivity.this);
                        rv_location.setAdapter(placeAdapter);
                        isAdapterCreated = true;
                    } else {
                        placeAdapter.updateData(locationWrapper.predictions);
                    }

                    placeAdapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(LocationDto locationDto) {
                            //Your Required Data
                            search_drop_address.setText(locationDto.description);
                            String DropAddress = (locationDto.description);
                            drop_address.setText(DropAddress);
                            //drop_address.
                            //sheet.dismiss();
                            //search_pick_up_address.setCancelable(false);
                            dialog.dismiss();
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

    // Validation.......................
    private boolean validate() {
        boolean flag = true;
        if (!searched_address.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            searched_address.setError("Please Pick Up Address!");
            flag = false;
        }
        if (!drop_address.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            drop_address.setError("Please Drop Address!");
            flag = false;
        }
        return flag;
    }
}
