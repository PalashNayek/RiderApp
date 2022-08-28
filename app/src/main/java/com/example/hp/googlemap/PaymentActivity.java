package com.example.hp.googlemap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.cardImageList.ImageAdapter;
import com.example.hp.googlemap.cardImageList.ImageModal;
import com.example.hp.googlemap.rider.AddPaymentMethodActivity;
import com.example.hp.googlemap.rider.DashActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity
{

    private static final String TAG = PaymentActivity.class.getSimpleName();
    String ProfileDetailsAPIURL = "http://45.55.64.233/Miles/api/get_payment_info.php";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    String imagePath;
    //ImageView cardImage;
    //TextView card_num;
    ImageView backArrow;
    LinearLayout add_method_li_layout;
    ScrollView linearLayoutSnackBar;

    ImageAdapter imageListAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ImageModal> imageCardArrayList = new ArrayList<>();

    private RecyclerView vehicleRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();
        linearLayoutSnackBar=(ScrollView)findViewById(R.id.linearLayoutSnackBar);
        layoutManager = new LinearLayoutManager(this);
        //layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vehicleRecyclerView = (RecyclerView) findViewById(R.id.vehicleDetailsRecyclerView1);
        //cardImage=(ImageView)findViewById(R.id.cardImage);
        //card_num=(TextView)findViewById(R.id.card_num);
        backArrow=(ImageView)findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PaymentActivity.this,DashActivity.class);
                startActivity(intent);
            }
        });

        add_method_li_layout=(LinearLayout)findViewById(R.id.add_method_li_layout);
        add_method_li_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PaymentActivity.this,AddPaymentMethodActivity.class);
                startActivity(intent);
            }
        });



        // Set Image ...................
        StringRequest profile_details_request = new StringRequest(Request.Method.POST, ProfileDetailsAPIURL, new Response.Listener<String>()
        {
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
                if (status == 1)
                {
                    try {
                        JSONObject jsonRootObject = new JSONObject(response);
                        JSONArray jsonArray = jsonRootObject.getJSONArray("card_details");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String cardImage = jsonObject.optString("card_type_image");
                            String card_no = jsonObject.optString("card_no");

                            ImageModal vendorList = new ImageModal();
                            vendorList.setCard_details(cardImage);
                            vendorList.setCard_no(card_no);
                            imageCardArrayList.add(vendorList);
                        }
                        vehicleRecyclerView.setLayoutManager(layoutManager);
                        imageListAdapter = new ImageAdapter(imageCardArrayList, PaymentActivity.this);
                        vehicleRecyclerView.setAdapter(imageListAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Snackbar snackbar = Snackbar.make(linearLayoutSnackBar, "" + msg, Snackbar.LENGTH_LONG);// Snackbar message
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
                //params.put( "prk_veh_trc_dtl_id", "7" );
                params.put("user_id", sharedPreferences.getString("user_id", ""));
                Log.d("ImageDDD", "getParams: " + params);
                return params;

                //user_admin_id,token

            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(profile_details_request);
    }
}
