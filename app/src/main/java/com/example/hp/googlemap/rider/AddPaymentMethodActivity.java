package com.example.hp.googlemap.rider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.PaymentActivity;
import com.example.hp.googlemap.R;
import com.example.hp.googlemap.TransparentProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddPaymentMethodActivity extends AppCompatActivity {
    private static final String TAG = AddPaymentMethodActivity.class.getSimpleName();
    String AddPaymentMethodAPIURL = "http://45.55.64.233/Miles/api/edit_payment.php";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    ImageView backArrow;
    EditText card_name, ed_number, date_month, cvc;
    Button btn_done;
    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_method);
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
        //findViewById(R.id.the_button).setOnClickListener(this);
        pd.show();
        h.postDelayed(r, 3000);
        sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();


        card_name = (EditText) findViewById(R.id.card_name);
        ed_number = (EditText) findViewById(R.id.ed_number);
        date_month = (EditText) findViewById(R.id.date_month);
        cvc = (EditText) findViewById(R.id.cvc);

        date_month.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void afterTextChanged(Editable s) {
                String str = date_month.getText().toString();
                if (str.length() == 2 && len < str.length()) {//len check for backspace
                    date_month.append("/");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                String str = date_month.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


        });

        //Card Number Space.......................
        ed_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (count <= ed_number.getText().toString().length()
                        && (ed_number.getText().toString().length() == 4
                        || ed_number.getText().toString().length() == 9
                        || ed_number.getText().toString().length() == 14)) {
                    ed_number.setText(ed_number.getText().toString() + " ");
                    int pos = ed_number.getText().length();
                    ed_number.setSelection(pos);
                } else if (count >= ed_number.getText().toString().length()
                        && (ed_number.getText().toString().length() == 4
                        || ed_number.getText().toString().length() == 9
                        || ed_number.getText().toString().length() == 14)) {
                    ed_number.setText(ed_number.getText().toString().substring(0, ed_number.getText().toString().length() - 1));
                    int pos = ed_number.getText().length();
                    ed_number.setSelection(pos);
                }
                count = ed_number.getText().toString().length();
            }
        });

        backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPaymentMethodActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // validate();
                StringRequest add_payment_request = new StringRequest(Request.Method.POST, AddPaymentMethodAPIURL, new Response.Listener<String>() {

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
                            Intent intent = new Intent(AddPaymentMethodActivity.this, PaymentActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddPaymentMethodActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                        params.put("user_id", sharedPreferences.getString("user_id", ""));
                        params.put("name_on_card", card_name.getText().toString());
                        params.put("card_no", ed_number.getText().toString());
                        params.put("validity_dt", date_month.getText().toString());
                        params.put("cvc_no", cvc.getText().toString());
                        Log.d("AddPaymentMethod", "getParams: " + params);
                        return params;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(add_payment_request);
            }
        });
    }

    // Validation.......................
    private boolean validate() {
        boolean flag = false;


        if (!card_name.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            card_name.setError("Invalid Nane");
            flag = false;
        }

        if (!ed_number.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            ed_number.setError("Invalid Card No");
            flag = false;
        }
        if (!date_month.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            date_month.setError("Invalid Date ");
            flag = false;
        }
        if (!cvc.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            cvc.setError("Invalid CVC");
            flag = false;
        }

        return flag;
    }
}
