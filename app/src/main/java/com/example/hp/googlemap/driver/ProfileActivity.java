package com.example.hp.googlemap.driver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.hp.googlemap.ChooseActivity;
import com.example.hp.googlemap.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity
{
    private CircleImageView car_Image,driver_Image;
    private TextView tv_profilename,tv_email,tv_city,tv_state,tv_license,tv_regdate;
    String ProfileDetailsAPIURL="http://45.55.64.233/Miles/driver/get_driver_profile.php";
    private static final String TAG = ProfileActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String imagePath="";
    String driverImagePath="";
    String imageBasePath="http://45.55.64.233/Miles/driver/";
    String updateStatusAPI = "http://45.55.64.233/Miles/driver/update_status.php";
    TextView tv_signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences = getApplicationContext().getSharedPreferences("driver_pref", 0);
        editor = sharedPreferences.edit();

        car_Image=findViewById(R.id.car_Image);
        driver_Image=findViewById(R.id.driver_Image);

        tv_profilename=findViewById(R.id.tv_profilename);
        tv_email=findViewById(R.id.tv_email);
        tv_city=findViewById(R.id.tv_city);
        tv_state=findViewById(R.id.tv_state);
        tv_license=findViewById(R.id.tv_license);
        tv_regdate=findViewById(R.id.tv_regdate);
        tv_signout=findViewById(R.id.tv_signout);


        getProfileDetails();

        tv_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusOnline();
            }
        });
    }

    public void onButtonClick(View view) {

        Intent intent = new Intent(ProfileActivity.this, UpdateAccountActivity.class);
        startActivity(intent);
    }

    private void getProfileDetails()
    {
        // Set Image ...................
        StringRequest profile_details_request = new StringRequest(Request.Method.POST, ProfileDetailsAPIURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
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
                        imagePath =imageBasePath+resObj.getString("car_image");
                        driverImagePath =imageBasePath+resObj.getString("driver_image");
                        Picasso.with(ProfileActivity.this).load(imagePath).resize(100, 100).into(car_Image);
                        Picasso.with(ProfileActivity.this).load(driverImagePath).resize(100, 100).into(driver_Image);
                        //Log.d("ImgPath", "onResponse: "+imagePath);
                        tv_profilename.setText(resObj.getString("first_name"));
                        tv_email.setText(resObj.getString("driver_email"));
                        tv_city.setText(resObj.getString("driver_city"));
                        tv_state.setText(resObj.getString("driver_state"));
                        tv_license.setText(resObj.getString("car_number"));
                        tv_regdate.setText(resObj.getString("joined"));


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
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(profile_details_request);
    }

    /*public void onButtonClicksignout(View view) {
        //Toast.makeText(ProfileActivity.this, "Logout Successfully.", Toast.LENGTH_SHORT).show();

    }*/

    public void onButtonClickback(View view) {
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void onButtonClickdocument(View view) {
        Intent intent = new Intent(ProfileActivity.this, UpdateDocumentActivity.class);
        startActivity(intent);
    }

    public void onButtonClickpayment(View view) {
        Intent intent = new Intent(ProfileActivity.this, DriverPaymentActivity.class);
        startActivity(intent);
    }

    public void onButtonClicktermsandcondition(View view) {
        Uri uri = Uri.parse("https://www.milespa.com/terms-policies");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onButtonClickearning(View view) {
        Intent intent = new Intent(ProfileActivity.this, EarningActivity.class);
        startActivity(intent);
    }

    public void onButtonClickrecruit(View view) {
        Intent intent = new Intent(ProfileActivity.this, RecruitActivity.class);
        startActivity(intent);
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
                    SharedPreferences.Editor editor = getSharedPreferences("driver_pref", Context.MODE_PRIVATE).edit();
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(ProfileActivity.this, ChooseActivity.class);
                    startActivity(intent);
                    finish();
                    /*Intent intent=new Intent(ProfileActivity.this,DriverLogInPage.class);
                    startActivity(intent);*/

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
                params.put("status", "2");

                Log.d("checkStatusParams", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(checkStatus_request);
    }
}
