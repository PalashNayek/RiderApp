package com.example.hp.googlemap;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.hp.googlemap.rider.DashActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendRiderActivity extends AppCompatActivity {
    TextView sender_name, sender_email, sender_phone, rider_name, rider_email, rider_phone, pickUpLocation, drop_off, dateShowTxt;
    Button login_btn;
    //private static final String TAG = SendRiderActivity.class.getSimpleName();
    String BaseUrl = "http://45.55.64.233/Miles/api/";
    String riderURL = BaseUrl + "send_ride.php";
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String PickUpAdd, DropAdd;
    ImageView backArrowBtn;
    MaterialCalendarView calendarView;
    TextView showDateTxt;
    TextView time, time_txt;
    TimePicker simpleTimePicker;
    LinearLayout linearLayoutLogin;
    private TextClock tClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_rider);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        linearLayoutLogin = (LinearLayout) findViewById(R.id.linearLayoutLogin);

        sender_name = (TextView) findViewById(R.id.sender_name);
        sender_email = (TextView) findViewById(R.id.sender_email);
        sender_phone = (TextView) findViewById(R.id.sender_phone);
        rider_name = (TextView) findViewById(R.id.rider_name);
        rider_email = (TextView) findViewById(R.id.rider_email);
        rider_phone = (TextView) findViewById(R.id.rider_phone);
        showDateTxt = (TextView) findViewById(R.id.showDateTxt);
        time_txt = (TextView) findViewById(R.id.time_txt);

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

        login_btn = (Button) findViewById(R.id.login_btn);
        sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();
        backArrowBtn = (ImageView) findViewById(R.id.backArrowBtn);

        backArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SendRiderActivity.this, DashActivity.class);
                startActivity(intent);
            }
        });

        // For Time///////////////////////////////...................................

        tClock = (TextClock) findViewById(R.id.textClock1);
        tClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  openBottomSheet();
                showHourPicker();
            }
        });

        time_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  openBottomSheet();
                showHourPickerr();
            }
        });
        DropAdd = "";
        PickUpAdd = "";

        sender_name.setText(sharedPreferences.getString("firstname", "") + " " + sharedPreferences.getString("lastname", ""));
        sender_email.setText(sharedPreferences.getString("mail", ""));
        sender_phone.setText(sharedPreferences.getString("phone", ""));

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate();
                StringRequest signupRequest = new StringRequest(Request.Method.POST, riderURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", "onResponse: " + response);
                        JSONObject resObj = null;
                        int status = 0;
                        String msg = "";
                        try {
                            resObj = new JSONObject(response);
                            status = resObj.getInt("status");
                            msg = resObj.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (status == 1) {
                            Toast.makeText(SendRiderActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(SendRiderActivity.this, DashActivity.class);
                            startActivity(intent1);
                            finish();

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
                        params.put("rider_name", rider_name.getText().toString());
                        params.put("rider_email", rider_email.getText().toString());
                        params.put("rider_phone", rider_phone.getText().toString());
                        params.put("pickup_addr", PickUpAdd);
                        params.put("drop_addr", DropAdd);
                        params.put("pickup_dt", showDateTxt.getText().toString());
                        params.put("pickup_time", time_txt.getText().toString());
                        //params.put("state", "West Bengal");
                        Log.i("SendValue", "getParams: " + params);
                        return params;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(signupRequest);
            }
        });
    }

    public void findPlace(View view) {
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

    public void PickOFF(View view) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
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
                ((TextView) findViewById(R.id.searched_address)).setText(place.getAddress() + "\n" + place.getPhoneNumber());
                PickUpAdd = place.getAddress() + "\n" + place.getPhoneNumber();
                Log.d("PickUp", "onActivityResult: " + PickUpAdd);
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
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
                ((TextView) findViewById(R.id.PickOff)).setText(place.getAddress() + "\n" + place.getPhoneNumber());
                DropAdd = place.getAddress() + "\n" + place.getPhoneNumber();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
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

    // Validation.......................
    private boolean validate() {
        boolean flag = true;


        if (!rider_name.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            rider_name.setError("Invalid Name");
            flag = false;
        }
        if (!rider_email.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            rider_email.setError("Invalid Email");
            flag = false;
        }
        if (!rider_phone.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            rider_phone.setError("Invalid Phone");
            flag = false;
        }


        return flag;
    }
}
