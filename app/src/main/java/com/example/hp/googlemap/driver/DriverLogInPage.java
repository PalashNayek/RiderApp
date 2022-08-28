package com.example.hp.googlemap.driver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.ChooseActivity;
import com.example.hp.googlemap.LogInPageActivity;
import com.example.hp.googlemap.R;
import com.example.hp.googlemap.rider.DashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DriverLogInPage extends AppCompatActivity {
    TextView tv_new_miles;
    TextView forgotPass;
    EditText ed_email, ed_password;
    Button bt_signin;
    String signin = "http://45.55.64.233/Miles/driver/login_driver.php ";
    private static final String TAG = DriverLogInPage.class.getSimpleName();
    SharedPreferences.Editor editor;
    private SharedPreferences sharedPreference;
    int backButtonCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_log_in_page);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
        if (!marshMallowPermission.checkPermissionForExternalStorage())
            marshMallowPermission.requestPermissionForExternalStorage();

        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        bt_signin = (Button) findViewById(R.id.bt_signin);
        tv_new_miles = (TextView) findViewById(R.id.tv_new_miles);

       /* ed_email.setText("shandyp@udel.edu");
        ed_password.setText("miles18");*/

        tv_new_miles.setOnClickListener(new View.OnClickListener() {///////////////////
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverLogInPage.this, AboutyourselfActivity.class);
                startActivity(intent);
            }
        });

        forgotPass = (TextView) findViewById(R.id.forgotPass);
        forgotPass.setOnClickListener(new View.OnClickListener() {///////////////////////////////
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverLogInPage.this, DriverForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        sharedPreference = getApplicationContext().getSharedPreferences("driver_pref", 0);
        editor = sharedPreference.edit();

        if (sharedPreference.getString("isLoggedInDriver", "").equals("11")) {
            startActivity(new Intent(DriverLogInPage.this, HomeActivity.class));
//            if (sharedPreference.getString( "isLoggedIn", "" ).equals( "1" )){
//                startActivity( new Intent( LoginActivity.this, ParkUserInActivitity.class ) );
//            }
        }

        bt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest addresss_request = new StringRequest(Request.Method.POST, signin, new Response.Listener<String>() {
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
                            Log.i(TAG, "onResponse: " + e);
                        }
                        if (status == 200) {
                            try {
                                editor.putString("driver_id", resObj.getString("driver_id"));
                                editor.putString("first_name", resObj.getString("first_name"));
                                editor.putString("last_name", resObj.getString("last_name"));
                                editor.putString("isLoggedInDriver", "11");
                                editor.commit();
                                Toast.makeText(DriverLogInPage.this, "" + msg, Toast.LENGTH_SHORT).show();
                                Intent homeintent = new Intent(DriverLogInPage.this, HomeActivity.class);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                startActivity(homeintent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DriverLogInPage.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                        params.put("driver_email", ed_email.getText().toString());
                        params.put("driver_password", ed_password.getText().toString());
                        return params;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(addresss_request);
            }


        });
    }
    @Override
    public void onBackPressed() {
        if (backButtonCount >= 2) {
            Intent intent = new Intent(DriverLogInPage.this, ChooseActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press the back again to Exit.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
