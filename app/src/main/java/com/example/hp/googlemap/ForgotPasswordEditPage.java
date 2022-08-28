package com.example.hp.googlemap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForgotPasswordEditPage extends AppCompatActivity
{
    private static final String TAG = EditPageActivity.class.getSimpleName();
   // String user_details_show_url="http://45.55.64.233/Miles/api/temp_login.php";
    String url_update="http://45.55.64.233/Miles/api/edit_user.php";
    String ProfileDetailsAPIURL = "http://45.55.64.233/Miles/api/getUserImg.php";
    TextView ed_name,ed_last_name,ed_email;
    EditText ed_phone,ed_password;
    ImageView btn_update;
    CircleImageView circleImageView;
    ProgressDialog progressDialog;
    String imagePath;
    ImageView backArrowImgBtn;
    String FirstNme,LName,UserEmail,UserPhono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_edit_page);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        ed_name=(TextView)findViewById(R.id.ed_name);
        ed_last_name=(TextView)findViewById(R.id.ed_last_name);
        ed_email=(TextView)findViewById(R.id.ed_email);
        ed_phone=(EditText) findViewById(R.id.ed_phone);
        ed_password=(EditText) findViewById(R.id.ed_password);
        btn_update=(ImageView) findViewById(R.id.btn_update);
        circleImageView=(CircleImageView) findViewById(R.id.circleImageView);
        backArrowImgBtn=(ImageView)findViewById(R.id.backArrowImgBtn);
        backArrowImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ForgotPasswordEditPage.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


        FirstNme=getIntent().getStringExtra("Fname");
        LName=(getIntent().getStringExtra("Lname"));
        UserEmail=(getIntent().getStringExtra("Email"));
        UserPhono=(getIntent().getStringExtra("Phone"));

        ed_name.setText(FirstNme);
        ed_last_name.setText(LName);
        ed_email.setText(UserEmail);
        ed_phone.setText(UserPhono);
       // System.out.println(UserPhono);

       /* //User Details Show .................
        progressDialog = new ProgressDialog(ForgotPasswordEditPage.this);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest signupRequest = new StringRequest(Request.Method.POST, user_details_show_url, new Response.Listener<String>() {
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
                    progressDialog.hide();
                    try {
                        ed_name.setText( resObj.getString( "firstname" ) );
                        ed_last_name.setText( resObj.getString( "lastname" ) );
                        ed_email.setText( resObj.getString( "mail" ) );

                    }catch (Exception e){
                    }

                } else {
                    progressDialog.hide();
                    //display error msg here
                    Toast.makeText(getApplicationContext(), ""+msg, Toast.LENGTH_SHORT).show();
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

                params.put("user_email", sharedPreferences.getString("mail", ""));
                params.put("user_password", sharedPreferences.getString("pass", ""));
                Log.i("EditPage", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(signupRequest);*/

        // Set Image ...................//////////////
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
                        Picasso.with(ForgotPasswordEditPage.this).load(imagePath).resize(100, 100).into(circleImageView);
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
                //params.put( "prk_veh_trc_dtl_id", "7" );
                params.put("user_email", ed_email.getText().toString());
                Log.d("ImageDDD", "getParams: " + params);
                return params;

                //user_admin_id,token

            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(profile_details_request);

        //Done Button Click.....................//////////////
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitBtn();
            }
        });
    }
    public void submitBtn()
    {
        StringRequest signupRequest = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
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
//                    progressDialog.hide();
                    Intent intent=new Intent(ForgotPasswordEditPage.this,LogInPageActivity.class);
                    startActivity(intent);

                } else {
             //       progressDialog.hide();
                    //display error msg here
                    Toast.makeText(getApplicationContext(), ""+msg, Toast.LENGTH_SHORT).show();
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

                params.put("user_email", ed_email.getText().toString());
                params.put("user_password", ed_password.getText().toString());
                params.put("user_phone", ed_phone.getText().toString());
                Log.i("EditPage", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(signupRequest);
    }
    private boolean validate(EditText ed_phone,EditText ed_password) {
        boolean flag = true;


        if (!ed_phone.getText().toString().matches("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$")) {
            ed_phone.setError("Check Phone number");
            flag = false;
        }

        if (!ed_password.getText().toString().matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})")) {
            ed_password.setError("Check password");
            flag = false;
        }

        return flag;
    }
}
