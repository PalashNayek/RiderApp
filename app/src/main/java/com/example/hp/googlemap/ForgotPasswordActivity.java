package com.example.hp.googlemap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText Ed_Forgotpasword, Edit_verify;
    Button Btn_Submit;
    //String forgot_id;
    String Forgoturl = "http://45.55.64.233/Miles/api/forgotpass_otp.php";
    String VerifyUrl = "http://45.55.64.233/Miles/api/verify_otp.php";
    ProgressDialog progressDialog;
    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();
    ImageView backArrowBtn;
    String fname, lname, user_email, phono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        Ed_Forgotpasword = (EditText) findViewById(R.id.fp);
        Edit_verify = (EditText) findViewById(R.id.Edit_verify);
        backArrowBtn = (ImageView) findViewById(R.id.backArrowBtn);
        backArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LogInPageActivity.class);
                startActivity(intent);
            }
        });

        Btn_Submit = (Button) findViewById(R.id.login_btn);
        Edit_verify.setEnabled(false);

        Btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitBtn();
            }
        });

        //Call verify edit text...view.........

        Edit_verify.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                processButtonByTextLength();
            }
        });


        // Listen to EditText key event to change button state and text accordingly.
        Edit_verify.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                // Get key action, up or down.
                int action = keyEvent.getAction();

                // Only process key up action, otherwise this listener will be triggered twice because of key down action.
                if (action == KeyEvent.ACTION_UP) {
                    processButtonByTextLength();
                }
                return false;
            }
        });
    }

    // Enable or disable and change button text by EditText text length.
    private void processButtonByTextLength() {
        String inputText = Edit_verify.getText().toString();
        if (inputText.length() == 4) {
            VerifyBtn();
        }
    }

    public void submitBtn() {
        progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest currentlocationRequest = new StringRequest(Request.Method.POST, Forgoturl, new Response.Listener<String>() {
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
                    Edit_verify.setEnabled(true);
                    //Toast.makeText(ForgotPasswordActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog.hide();
                    Toast.makeText(ForgotPasswordActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("forgotid", Ed_Forgotpasword.getText().toString());

                Log.i("Set Trip", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(currentlocationRequest);
    }

    public void VerifyBtn() {
        {
            progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
            progressDialog.setMessage("Loading");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();

            StringRequest currentlocationRequest = new StringRequest(Request.Method.POST, VerifyUrl, new Response.Listener<String>() {
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
                            fname = resObj.getString("fname");
                            lname = resObj.getString("lname");
                            user_email = resObj.getString("user_email");
                            phono = resObj.getString("user_phone");

                            Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordEditPage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("Fname", fname);
                            intent.putExtra("Lname", lname);
                            intent.putExtra("Email", user_email);
                            intent.putExtra("Phone", phono);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        progressDialog.hide();
                        Toast.makeText(ForgotPasswordActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                    params.put("otp", Edit_verify.getText().toString());
                    return params;
                }
            };
            Volley.newRequestQueue(getApplicationContext()).add(currentlocationRequest);
        }
    }
}

