package com.example.hp.googlemap.driver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.hp.googlemap.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IdentityVerificationActivity extends AppCompatActivity
{
    TextView underlineyellow;
    Button btn_identity;
    ImageView iv_back3;
    EditText threedigit, twodigit,fourdigit,routing,account,verifyaccount;
    String identity = "http://45.55.64.233/Miles/driver/identity_verification.php";
    String showidentitydetails="http://45.55.64.233/Miles/driver/get_identity_verification_details.php";

    private static final String TAG = IdentityVerificationActivity.class.getSimpleName();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_verification);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        sharedPreferences = getApplicationContext().getSharedPreferences( "driver_pref", 0 );
        editor = sharedPreferences.edit();

        underlineyellow=(TextView)findViewById(R.id.underlineyellow);
        btn_identity=(Button)findViewById(R.id.btn_identity);
        threedigit=(EditText)findViewById(R.id.threedigit);
        twodigit=(EditText)findViewById(R.id.twodigit);
        fourdigit=(EditText)findViewById(R.id.fourdigit);
        routing=(EditText)findViewById(R.id.routing);
        account=(EditText)findViewById(R.id.account);
        verifyaccount=(EditText)findViewById(R.id.verifyaccount);
        iv_back3=(ImageView)findViewById(R.id.iv_back3);


        textChange();

        iv_back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IdentityVerificationActivity.this, YourVehicleActivity.class);
                startActivity(intent);
            }
        });

        // SpannableString demo
        String underlineData = "Background Check Disclaimer";

        SpannableString content = new SpannableString(underlineData);
        content.setSpan(new UnderlineSpan(), 0, underlineData.length(), 0);
        // 0 specify start index and underlineData.length() specify end index of styling
        underlineyellow.setText(content);
        // End of SpannableString demo



        // Paint.STRIKE_THRU_TEXT_FLAG ;
        // Paint.SUBPIXEL_TEXT_FLAG ;

        // Html.fromHtml(htmlString)
        String htmlString = "<u>Background Check Disclaimer</u>";
        underlineyellow.setText(Html.fromHtml(htmlString));

        btn_identity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                StringRequest addresss_request = new StringRequest(Request.Method.POST, identity, new Response.Listener<String>() {
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
                            //progressDialog.hide();
                            Toast.makeText(IdentityVerificationActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(IdentityVerificationActivity.this, DocumentsActivity .class);
                            startActivity(intent1);
                            finish();

                        } else {
                            //progressDialog.hide();
                            //display error msg here
                            Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
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
                        params.put("social_security_number", threedigit.getText().toString()+twodigit.getText().toString()+fourdigit.getText().toString());
                        // params.put("social_security_number", );
                        // params.put("social_security_number", );
                        params.put("routing",routing.getText().toString());
                        params.put("account",  account.getText().toString());
                        params.put("confirm_account", verifyaccount.getText().toString());
                        params.put("driver_id",sharedPreferences.getString( "driver_id", "" ));


                        Log.i("IndentityActivity", "getParams: " + params);
                        return params;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(addresss_request);
            }


        });
    }

    private void textChange() {
        threedigit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                System.out.println(s+" "+start+" "+before+" "+count);
                if(s.length()==3)
                {
                    twodigit.requestFocus();
                }

            }
        });

        twodigit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length()==2)
                {
                    fourdigit.requestFocus();
                }

            }
        });
    }
}
