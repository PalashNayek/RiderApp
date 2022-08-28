package com.example.hp.googlemap.driver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.R;
import com.example.hp.googlemap.rider.DashActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RateYourRiderActivity extends AppCompatActivity {
    TextView txtName, ratingTxt, joiningDateTxt;
    CircleImageView circleDriverImage;
    String user_image;
    EditText edtFeedbackMsg;
    String feedbackDriverToRiderAPI = "http://45.55.64.233/Miles/driver/feedback_to_user.php";
    String completeRideAPI = "http://45.55.64.233/Miles/driver/ride_complete.php";
    private static final String TAG = RateYourRiderActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RatingBar rating_bar;
    int backButtonCount;
    String ride_id,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_your_rider);
        txtName = (TextView) findViewById(R.id.txtName);
        ratingTxt = (TextView) findViewById(R.id.ratingTxt);
        joiningDateTxt = (TextView) findViewById(R.id.joiningDateTxt);
        circleDriverImage = (CircleImageView) findViewById(R.id.circleDriverImage);
        edtFeedbackMsg=(EditText)findViewById(R.id.edtFeedbackMsg);
        rating_bar=(RatingBar)findViewById(R.id.rating_bar);
        sharedPreferences = getApplicationContext().getSharedPreferences("driver_pref", 0);
        editor = sharedPreferences.edit();
        user_image = getIntent().getStringExtra("user_image");
        Picasso.with(RateYourRiderActivity.this).load(user_image).resize(100, 100).into(circleDriverImage);
        txtName.setText(getIntent().getStringExtra("user_name"));
        ratingTxt.setText(getIntent().getStringExtra("rating"));
        joiningDateTxt.setText(getIntent().getStringExtra("user_joining_date"));

        ride_id=(getIntent().getStringExtra("ride_id"));
        user_id=(getIntent().getStringExtra("user_id"));
        completeRideFunc();

    }

    public void onButtonClickdone(View view) {
        getProfileDetails();
    }

    private void getProfileDetails() {
        final String rating=String.valueOf(rating_bar.getRating());
        StringRequest feedback_request = new StringRequest(Request.Method.POST, feedbackDriverToRiderAPI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("DebRes", "onResponse: " + response);
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
                    Toast.makeText(RateYourRiderActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RateYourRiderActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RateYourRiderActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("feedback_to_user ", edtFeedbackMsg.getText().toString());
                params.put("user_rating", rating);
                params.put("user_id", user_id);
                params.put("ride_id", ride_id);
                Log.d("FEEDBACK", "getParams: "+params);

                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(feedback_request);
    }
    private void completeRideFunc() {
        final String rating=String.valueOf(rating_bar.getRating());
        StringRequest feedback_request = new StringRequest(Request.Method.POST, completeRideAPI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("CompleteResponse", "onResponse: " + response);
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
                    /*Toast.makeText(RateYourRiderActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RateYourRiderActivity.this, HomeActivity.class);
                    startActivity(intent);*/
                } else {
                    Toast.makeText(RateYourRiderActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("ride_id", ride_id);
                Log.d("CompleteParams", "getParams: "+params);

                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(feedback_request);
    }
    @Override
    public void onBackPressed() {
        if (backButtonCount >= 300) {
            Intent intent = new Intent(this, DashActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Click Done !", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
