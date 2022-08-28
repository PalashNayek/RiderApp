package com.example.hp.googlemap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

public class ConfirmTripPopupActivity extends Activity implements View.OnClickListener{
    String image_selected;
    TextView driverWasLatetxt,showUptxt,tooFartxt,longerNeededtxt,evenCanceltxt,othertxt;
    Button confirmTripbtn;
    String ConfirmTripAPIURL = "http://45.55.64.233/Miles/api/cancel_ride.php";
    private static final String TAG = ConfirmTripPopupActivity.class.getSimpleName();
    String set_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_trip_popup);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        driverWasLatetxt=(TextView)findViewById(R.id.driverWasLatetxt);
        showUptxt=(TextView)findViewById(R.id.showUptxt);
        tooFartxt=(TextView)findViewById(R.id.tooFartxt);
        evenCanceltxt=(TextView)findViewById(R.id.evenCanceltxt);
        longerNeededtxt=(TextView)findViewById(R.id.longerNeededtxt);
        othertxt=(TextView)findViewById(R.id.othertxt);
        confirmTripbtn=(Button) findViewById(R.id.confirmTripbtn);


        set_id = getIntent().getStringExtra("set_id");
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.7));

        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);

        // Array of ImageView
        final TextView[] levelsArray = {driverWasLatetxt, showUptxt, tooFartxt, longerNeededtxt, evenCanceltxt,othertxt};

        for(int i = 0; i <6; i++) {
            levelsArray[i].setOnClickListener(this);

        }

        confirmTripbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClk();
            }
        });
    }

    public void onClick(View v) {
        // isClicked = false;

        switch (v.getId()) {
            case R.id.driverWasLatetxt:
                image_selected = "1";
                Toast.makeText(this, "Driver Was Late selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.showUptxt:
                image_selected = "2";
                Toast.makeText(this, "Driver did not show up selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tooFartxt:
                image_selected = "3";
                Toast.makeText(this, "Driver was too far selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.longerNeededtxt:
                image_selected = "4";
                Toast.makeText(this, "No Longer needed selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.evenCanceltxt:
                image_selected = "5";
                Toast.makeText(this, "Even cancelled selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.othertxt:
                image_selected = "6";
                Toast.makeText(this, "You are other Selected", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    public void onButtonClk()
    {
        StringRequest profile_details_request = new StringRequest(Request.Method.POST, ConfirmTripAPIURL, new Response.Listener<String>() {

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
                    Intent intent=new Intent(ConfirmTripPopupActivity.this,DashActivity.class);
                    startActivity(intent);

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
                params.put("cancel_ride", "1");
                params.put("set_id", set_id);
                params.put("cancel_reason_id", image_selected);
                Log.d("ImageDDD", "getParams: " + params);
                return params;

                //user_admin_id,token

            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(profile_details_request);
    }
}
