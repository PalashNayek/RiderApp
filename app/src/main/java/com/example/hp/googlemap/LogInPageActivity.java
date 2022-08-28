package com.example.hp.googlemap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.rider.DashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInPageActivity extends AppCompatActivity {
    private static final String TAG = LogInPageActivity.class.getSimpleName();
    EditText edt_phone_login, edt_pass_login;
    Button tv_signin;
    ProgressDialog progressDialog;
    String BaseUrl = "http://45.55.64.233/Miles/api/";
    String loginURL = BaseUrl + "login_user.php";
    SharedPreferences.Editor editor;
    String name;
    TextView tv_new_miles, forgotPass;
    String email;
    LinearLayout linearLayoutProgress;
    ScrollView linearLayoutLogin;
    DotsTextView dotsTextView;
    String uniqueID = "";
    int backButtonCount;
    private SharedPreferences sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        tv_new_miles = (TextView) findViewById(R.id.tv_new_miles);
        edt_phone_login = (EditText) findViewById(R.id.ed_email);
        edt_pass_login = (EditText) findViewById(R.id.ed_password);
        dotsTextView = (DotsTextView) findViewById(R.id.dots);
        linearLayoutProgress = (LinearLayout) findViewById(R.id.linearLayoutProgress);

        linearLayoutLogin = findViewById(R.id.linearLayoutLogin);

        turnGPSOn();
        turnGPSOff();

        tv_new_miles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInPageActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        forgotPass = (TextView) findViewById(R.id.forgotPass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInPageActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        tv_signin = (Button) findViewById(R.id.btn_login);

        sharedPreference = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreference.edit();

        if (sharedPreference.getString("isLoggedIn", "").equals("1")) {
            startActivity(new Intent(LogInPageActivity.this, DashActivity.class));
//            if (sharedPreference.getString( "isLoggedIn", "" ).equals( "1" )){
//                startActivity( new Intent( LoginActivity.this, ParkUserInActivitity.class ) );
//            }
        }
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validate();
                checkConnection();

            }


        });
    }

    // Validation.......................
    private boolean validate() {
        boolean flag = true;
        if (!edt_phone_login.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            edt_phone_login.setError("Invalid Email!");
            flag = false;
        }
        if (!edt_pass_login.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            edt_pass_login.setError("Enter Valid Password!");
            flag = false;
        }
        return flag;
    }

    //Check Network..................

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void checkConnection() {
        if (isOnline()) {
            logINFunc();
        } else {
            Snackbar snackbar = Snackbar.make(linearLayoutLogin, "You are not connected to Internet", Snackbar.LENGTH_LONG);// Snackbar message
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#ff0000")); // snackbar background color
            snackbar.setActionTextColor(Color.parseColor("#FFFFFF")); // snackbar action text color
            snackbar.show();
        }
    }

    //SignUp API call.......................
    private void logINFunc() {
        linearLayoutProgress.setVisibility(View.VISIBLE);
        linearLayoutProgress.setAlpha((float) 0.3);
        dotsTextView.start();
        StringRequest addresss_request = new StringRequest(Request.Method.POST, loginURL, new Response.Listener<String>() {
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
//                            e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }
                if (status == 1) {
                    try {
                        // Toast.makeText(LogInPageActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        String lName = (resObj.getString("lastname"));
                        if (lName.equals("null")) {
                            editor.putString("", "lastname");
                        } else {
                            editor.putString("lastname", resObj.getString("lastname"));
                        }
                        editor.putString("firstname", resObj.getString("firstname"));
                        editor.putString("phone", resObj.getString("phone"));
                        editor.putString("mail", resObj.getString("mail"));
                        editor.putString("user_id", resObj.getString("user_id"));
                        editor.putString("joined", resObj.getString("joined"));
                        editor.putString("isLoggedIn", "1");
                        editor.commit();
                        dotsTextView.stop();
                        linearLayoutProgress.setVisibility(View.GONE);
                        Intent dashIntent = new Intent(LogInPageActivity.this, DashActivity.class);
                        startActivity(dashIntent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    dotsTextView.stop();
                    linearLayoutProgress.setVisibility(View.GONE);

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
                Log.e("Error ", "Error" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", edt_phone_login.getText().toString());
                params.put("user_password", edt_pass_login.getText().toString());
                params.put("device_id", uniqueID);
                Log.i("LogInActivity", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(addresss_request);
    }

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.example.hp.googlemap", "LogInPageActivity");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    private void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.example.hp.googlemap", "LogInPageActivity");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 2) {
            Intent intent = new Intent(LogInPageActivity.this, ChooseActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press the back again to Exit.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

}
