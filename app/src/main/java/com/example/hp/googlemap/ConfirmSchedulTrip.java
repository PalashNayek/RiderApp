package com.example.hp.googlemap;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConfirmSchedulTrip extends AppCompatActivity
{
    EditText searched_address,PickOff;
    MaterialCalendarView calendarView;
    TextView time,showDateTxt,time_txt;
    private TextClock tClock;
    ImageView backArrowBtn,ImgeBtnSchudel;
    private static final String TAG = SchedulTripActivity.class.getSimpleName();
    String BaseUrl = "http://45.55.64.233/Miles/api/";
    String scheduleTripURL = BaseUrl + "schedule_trip.php";
    LinearLayout linearLayoutLogin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_schedul_trip);
        sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();
        tClock = (TextClock) findViewById(R.id.textClock1);
        tClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker();
            }
        });
        searched_address=(EditText)findViewById(R.id.searched_address);
        PickOff=(EditText)findViewById(R.id.PickOff);
        time_txt=(TextView)findViewById(R.id.time_txt);
        showDateTxt=(TextView)findViewById(R.id.showDateTxt);
        calendarView=(MaterialCalendarView) findViewById(R.id.calendarGridView);
        PickOff.setText(getIntent().getStringExtra("locatin_name"));
        searched_address.setText(getIntent().getStringExtra("Current_locatin_name"));
        backArrowBtn=(ImageView)findViewById(R.id.backArrowBtn);
        linearLayoutLogin = (LinearLayout) findViewById(R.id.linearLayoutLogin);
        backArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ConfirmSchedulTrip.this,DashActivity.class);
                startActivity(intent);
            }
        });
        time_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPickerr();
            }
        });
        Date date = new Date();
        showDateTxt.setText(date.getDay()+"/"+date.getMonth()+"/"+date.getYear());
        calendarView.setCurrentDate(date);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                showDateTxt.setText(date.getDay()+"/"+date.getMonth()+"/"+date.getYear());
            }
        });
        time = (TextView) findViewById(R.id.time_txt);

        ImgeBtnSchudel=(ImageView)findViewById(R.id.ImgeBtnSchudel);
        ImgeBtnSchudel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnImage();
            }
        });

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
                    time_txt.setText(hourOfDay+":"+minute);



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
                    time_txt.setText(hourOfDay+":"+minute);
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Black_NoTitleBar, myTimeListener, hour, minute, false);
        timePickerDialog.setTitle("Choose hour:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }
    public void btnImage()
    {
        //validate();
        StringRequest scheduleRequest = new StringRequest(Request.Method.POST, scheduleTripURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("SchedulRes", "onResponse: " + response);
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
                    Toast.makeText(ConfirmSchedulTrip.this, ""+msg, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ConfirmSchedulTrip.this,TripHistoryActivity.class);
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
                params.put("pickup_addr", PickOff.getText().toString());
                params.put("drop_addr", searched_address.getText().toString());
                params.put("scheduled_time", time_txt.getText().toString());
                params.put("scheduled_dt", showDateTxt.getText().toString());

                Log.i("SchedulTripSendParams", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(scheduleRequest);

    }
}
