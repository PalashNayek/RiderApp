package com.example.hp.googlemap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.rider.CardDetailsAdapter;
import com.example.hp.googlemap.rider.DashActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MilesInfoActivity extends AppCompatActivity {

    private static final String TAG = MilesInfoActivity.class.getSimpleName();
    String InformAPIURL = "http://45.55.64.233/Miles/api/get_miles_info.php";
    String UpdateMilesInfoAPIURL = "http://45.55.64.233/Miles/api/update_miles.php";
    String MembershipStatusAPIURL = "http://45.55.64.233/Miles/api/update_membership_active.php";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    String MembershipStatus;
    String ReplenishStatus = "";
    String imagePath = "";
    TextView MemberShipFee, Member_Val_dt, MilesExpireDate, MilesRemain, RefillDistance, CardNo;
    ImageView CardImage;
    Switch memShipActive, AutoReplinish;
    Button reloadbtn;
    RecyclerView amountRecyclerView;
    RecyclerView amountDetailsRecyclerView1;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Vehicle> vehicleArrayList = new ArrayList<>();
    ArrayList<Vehicle> imageArrayList = new ArrayList<>();
    MilesInfoAdapter milesAmountAdapter;
    CardDetailsAdapter cardDetailsAdapter;
    ArrayList<String> CountryName;
    String country;
    ImageView iv_back1, balanceCheckInactive, balanceCheckActive;
    String statusSwitch1, statusSwitch2;
    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miles_info);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();
        amountRecyclerView = (RecyclerView) findViewById(R.id.amountDetailsRecyclerView);
        //amountDetailsRecyclerView1=(RecyclerView)findViewById(R.id.amountDetailsRecyclerView1);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        MemberShipFee = (TextView) findViewById(R.id.membershipFeeTxt);
        Member_Val_dt = (TextView) findViewById(R.id.memberValDateTxt);
        MilesExpireDate = (TextView) findViewById(R.id.milesExpDate);
        MilesRemain = (TextView) findViewById(R.id.milesRemain);
        RefillDistance = (TextView) findViewById(R.id.refillDistanceTxt);
        CardNo = (TextView) findViewById(R.id.cardNumber);
        memShipActive = (Switch) findViewById(R.id.memActive);
        AutoReplinish = (Switch) findViewById(R.id.AutoReplinish);
        reloadbtn = (Button) findViewById(R.id.reloadbtn);
        iv_back1 = (ImageView) findViewById(R.id.iv_back1);
        balanceCheckInactive = (ImageView) findViewById(R.id.balanceCheckInactive);
        balanceCheckActive = (ImageView) findViewById(R.id.balanceCheckActive);

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

        //Member Active Switch.....
        memShipActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (memShipActive.isChecked()) {
                    statusSwitch1 = memShipActive.getTextOn().toString();
                    balanceCheckActive.setVisibility(View.VISIBLE);
                    balanceCheckInactive.setVisibility(View.GONE);
                    //Toast.makeText(MilesInfoActivity.this, "hahaha", Toast.LENGTH_SHORT).show();**********************
                    //call Membership Status function
                    membershipStatusFunc();
                } else {
                    statusSwitch1 = memShipActive.getTextOff().toString();
                    balanceCheckActive.setVisibility(View.GONE);
                    balanceCheckInactive.setVisibility(View.VISIBLE);
                    membershipStatusFunc();
                }
            }
        });


        // setContentView(R.layout.miles_add_bottom_sheet);
        //init();
        RefillDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheet();
            }
        });

        iv_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MilesInfoActivity.this, DashActivity.class);
                startActivity(intent);
            }
        });
        getInfoMiles();
        // UpdateMilesInfo();
        reloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateMilesInfo();
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

    public void getInfoMiles() {
        // get user information ...................................

        StringRequest inform_request = new StringRequest(Request.Method.POST, InformAPIURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MilesInfoResponse", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
                String msg = "";
//                progressDialog.hide();
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }

                if (status == 1) {
                    //       progressDialog.hide();
                    //Toast.makeText(PaymentActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                    try {
                        MembershipStatus = resObj.getString("membership_status");
                        ReplenishStatus = resObj.getString("auto_replenish_status");

                        MemberShipFee.setText(resObj.getString("membership_fee"));
                        Member_Val_dt.setText(resObj.getString("mem_val_dt"));
                        MilesExpireDate.setText(resObj.getString("miles_expiry_dt"));
                        MilesRemain.setText(resObj.getString("miles_remain"));
                        RefillDistance.setText(resObj.getString("refill_distance_value"));

                        ///////////////////////...............Amount details.........///////////////////

                        JSONObject jsonRootObject = new JSONObject(response);
                        JSONArray jsonArray = jsonRootObject.getJSONArray("miles_amount_details");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.optString("id");
                            String amount = jsonObject.optString("amount");

                            Vehicle vehicle = new Vehicle();
                            vehicle.setAmountId(id);
                            vehicle.setAmount(amount);
                            vehicleArrayList.add(vehicle);
                        }

                        ///////////////////////Card Image///////////////////

                        JSONObject jsonRootObjectImage = new JSONObject(response);
                        JSONArray jsonArrayImage = jsonRootObjectImage.getJSONArray("card_details");

                        for (int i = 0; i < jsonArrayImage.length(); i++) {
                            JSONObject jsonObject = jsonArrayImage.getJSONObject(i);
                            String imageType = jsonObject.optString("card_type_image1");

                            Vehicle vehicle = new Vehicle();
                            vehicle.setCardTypeImage(imageType);
                            imageArrayList.add(vehicle);
                        }
                        amountRecyclerView.setLayoutManager(layoutManager);
                        milesAmountAdapter = new MilesInfoAdapter(vehicleArrayList, MilesInfoActivity.this, MembershipStatus);
                        amountRecyclerView.setAdapter(milesAmountAdapter);

                        //Spinner Load.....
                        JSONArray jsonArraya = jsonRootObject.getJSONArray("miles_refill_details");
                        for (int i = 0; i < jsonArraya.length(); i++) {
                            JSONObject jsonObject1 = jsonArraya.getJSONObject(i);
                            String country = jsonObject1.getString("amount");
//                            CountryName.add(country);
                        }

                        if (MembershipStatus.equals("ON")) {
                            memShipActive.setChecked(true);
                            balanceCheckInactive.setVisibility(View.GONE);
                            balanceCheckActive.setVisibility(View.VISIBLE);
                        } else {
                            balanceCheckInactive.setVisibility(View.VISIBLE);
                            balanceCheckActive.setVisibility(View.GONE);
                        }
                        if (ReplenishStatus.equals("ON")) {
                            AutoReplinish.setChecked(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    //  progressDialog.hide();
                    if (!msg.equals("")) {
                        Toast.makeText(com.example.hp.googlemap.MilesInfoActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("user_id", sharedPreferences.getString("user_id", ""));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(inform_request);
    }

    public void UpdateMilesInfo() {

        if (AutoReplinish.isChecked())
            statusSwitch2 = AutoReplinish.getTextOn().toString();
        else
            statusSwitch2 = AutoReplinish.getTextOff().toString();

        StringRequest inform_request = new StringRequest(Request.Method.POST, UpdateMilesInfoAPIURL, new Response.Listener<String>() {
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
                    // progressDialog.hide();
                    finish();
                    startActivity(getIntent());
                } else {
                    // progressDialog.hide();
                    if (!msg.equals("")) {
                        Toast.makeText(MilesInfoActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("user_id", sharedPreferences.getString("user_id", ""));
                params.put("membership_status", statusSwitch1.toString());
                params.put("amount", milesAmountAdapter.getSelectedData());
                params.put("auto_replenish", statusSwitch2.toString());
                params.put("refill_dist", RefillDistance.getText().toString());
                Log.d("PkPalashNayek", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(inform_request);
    }

    private void openBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.miles_add_bottom_sheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);

        TextView txt10 = view.findViewById(R.id.txt10);
        TextView txt20 = view.findViewById(R.id.txt20);
        TextView txt30 = view.findViewById(R.id.txt30);
        TextView txt40 = view.findViewById(R.id.txt40);

        txt10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefillDistance.setText("10");
                //show("Hi");
                dialog.dismiss();
            }
        });
        txt20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefillDistance.setText("20");
                //show("Refresh");
                dialog.dismiss();
            }
        });
        txt30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefillDistance.setText("30");
                //show("Refresh");
                dialog.dismiss();
            }
        });
        txt40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefillDistance.setText("40");
                //show("Refresh");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //Membership Status Fuction.....
    private void membershipStatusFunc() {
        StringRequest inform_request = new StringRequest(Request.Method.POST, MembershipStatusAPIURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("MembershipResponse", "onResponse: " + response);
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
                        MembershipStatus = resObj.getString("membership_status");
                        MemberShipFee.setText(resObj.getString("membership_fee"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MilesInfoActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                params.put("membership_status", statusSwitch1.toString());

                Log.d("MembershipStusParam", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(inform_request);
    }
}
