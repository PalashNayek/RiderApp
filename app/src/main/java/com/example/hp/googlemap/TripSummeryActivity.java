package com.example.hp.googlemap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.rider.DashActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TripSummeryActivity extends AppCompatActivity {
    private static final String TAG = TripSummeryActivity.class.getSimpleName();
    String BaseUrl = "http://45.55.64.233/Miles/api/";
    String getTripSummeryURL = BaseUrl + "trip_summary.php";
    String feedbackURL = BaseUrl + "trip_feedback.php";
    String set_id;
    TextView pickUpAddressTxt, dropAddressTxt, costTxt, distanceTxt, arrivalTxt, milesBalanceTxt;
    ProgressDialog progressDialog;
    Button doneBtn;
    RatingBar ratingBar;
    EditText tripAmount, feedBackMsg;
    ImageView profile_pic;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RatingBar rating_bar;
    String rating;
    int backButtonCount;
    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    String driver_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_summery);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
        sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();

        pickUpAddressTxt = (TextView) findViewById(R.id.pickUpAddressTxt);
        dropAddressTxt = (TextView) findViewById(R.id.dropAddressTxt);
        costTxt = (TextView) findViewById(R.id.costTxt);
        distanceTxt = (TextView) findViewById(R.id.distanceTxt);
        arrivalTxt = (TextView) findViewById(R.id.arrivalTxt);
        milesBalanceTxt = (TextView) findViewById(R.id.milesBalanceTxt);
        rating_bar = (RatingBar) findViewById(R.id.rating_bar);

        doneBtn = (Button) findViewById(R.id.doneBtn);
        tripAmount = (EditText) findViewById(R.id.tripAmount);
        tripAmount.addTextChangedListener(new CheckPercentage());
        // ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        feedBackMsg = (EditText) findViewById(R.id.feedBackMsg);
//        String rating=String.valueOf(ratingBar.getRating());
        profile_pic = (ImageView) findViewById(R.id.profile_pic);

        set_id = getIntent().getStringExtra("set_id");
        driver_image = getIntent().getStringExtra("driver_image");
        Picasso.with(TripSummeryActivity.this).load(driver_image).resize(100, 100).into(profile_pic);

        StringRequest getDriverRequest = new StringRequest(Request.Method.POST, getTripSummeryURL, new Response.Listener<String>() {
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
                    //   progressDialog.hide();
                    try {
                        pickUpAddressTxt.setText(resObj.getString("pickup_addr"));
                        dropAddressTxt.setText(resObj.getString("drop_addr"));
                        costTxt.setText(resObj.getString("fare"));
                        distanceTxt.setText(resObj.getString("distance"));
                        arrivalTxt.setText(resObj.getString("arrival"));
                        milesBalanceTxt.setText(resObj.getString("miles_balance"));
                    } catch (JSONException e) {
                    }
                } else {
                    //    progressDialog.hide();
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
                Log.i("get Driver Trip", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(getDriverRequest);

        // ........................Done Button Click..............................................
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDoneOnClick();
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

    public void btnDoneOnClick() {
        final String rating = String.valueOf(rating_bar.getRating());
        //Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
        StringRequest getDriverRequest = new StringRequest(Request.Method.POST, feedbackURL, new Response.Listener<String>() {
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
                    // Toast.makeText(TripSummeryActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TripSummeryActivity.this, DashActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(TripSummeryActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("rating", rating);
                params.put("tip", tripAmount.getText().toString());
                params.put("feedback", feedBackMsg.getText().toString());


                Log.i("get Driver Trip", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(getDriverRequest);
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 200) {
            Intent intent = new Intent(this, DashActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please Click Done Button...", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }


    }

    //Numbers of Riders Numbers Changes.....
    class CheckPercentage implements TextWatcher {
        //int s = 1;

        public void afterTextChanged(Editable s) {
            try {
                Log.d("Percentage", "input: " + s);
                    if (Integer.parseInt(s.toString()) > 21)
                        s.replace(0, s.length(), "21");
                    Toast.makeText(TripSummeryActivity.this, "Maximum Amount $21 . ", Toast.LENGTH_SHORT).show();

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
        }
    }

}
