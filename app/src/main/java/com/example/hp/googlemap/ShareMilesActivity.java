package com.example.hp.googlemap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.rider.DashActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShareMilesActivity extends AppCompatActivity implements View.OnClickListener
{
    String ShareMilesURL = "http://45.55.64.233/Miles/api/get_miles_info.php";
    String doneURL = "http://45.55.64.233/Miles/api/share_miles.php";
    private static final String TAG = ShareMilesActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView avBalance;
    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    ImageView backImg;
    String SelectedBtn;
    Button MineBtnNonBorder,MineBtnBorder,PurchaseBtnNonBorder,PurchaseBtnBorder;
    RecyclerView amountRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Vehicle> vehicleArrayList = new ArrayList<>();
    MilesAmountAdapter milesAmountAdapter;
    EditText emailTxt;
    String ItemName;
    Button doneBtn;
    LinearLayout linearLayoutSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_miles);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();
        h = new Handler();
        pd = new TransparentProgressDialog(this, R.drawable.spinner);
        r =new Runnable() {
            @Override
            public void run() {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }
        };
        pd.show();
        h.postDelayed(r,3000);
        avBalance=(TextView)findViewById(R.id.availableBalanceTxt);
        backImg=(ImageView)findViewById(R.id.backImg);
        emailTxt=(EditText) findViewById(R.id.emailTxt);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShareMilesActivity.this,DashActivity.class);
                startActivity(intent);
            }
        });
        getInformationMilesShare();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("custom-message"));
        doneBtn=(Button)findViewById(R.id.doneBtn);
        MineBtnNonBorder=(Button)findViewById(R.id.MineBtnNonBorder);
        MineBtnBorder=(Button)findViewById(R.id.MineBtnBorder);
        PurchaseBtnNonBorder=(Button)findViewById(R.id.PurchaseBtnNonBorder);
        PurchaseBtnBorder=(Button)findViewById(R.id.PurchaseBtnBorder);
        linearLayoutSnackBar=(LinearLayout)findViewById(R.id.linearLayoutSnackBar);
        amountRecyclerView = (RecyclerView) findViewById( R.id.amountDetailsRecyclerView );
        layoutManager = new LinearLayoutManager( this,LinearLayoutManager.HORIZONTAL, false );
        SelectedBtn="useMine";
        final Button[] levelsArray = {MineBtnNonBorder, MineBtnBorder, PurchaseBtnNonBorder, PurchaseBtnBorder};

        for (int i = 0; i < 4; i++) {
            levelsArray[i].setOnClickListener(this);
        }
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // validate();
                doneFunc();
            }
        });

    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            ItemName = intent.getStringExtra("item");
            //String qty = intent.getStringExtra("quantity");
            //Toast.makeText(ShareMilesActivity.this,ItemName +" ",Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onDestroy() {
        h.removeCallbacks(r);
        if (pd.isShowing() ) {
            pd.dismiss();
        }
        super.onDestroy();
    }
    public void getInformationMilesShare()
    {
        StringRequest shareMilesRequest = new StringRequest(Request.Method.POST, ShareMilesURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("UpdateAddressResponse", "onResponse: " + response);
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

                if (status == 1)
                {
                    try {
                        avBalance.setText(resObj.getString("miles_remain"));

                        JSONObject jsonRootObject = new JSONObject( response );
                        JSONArray jsonArray = jsonRootObject.getJSONArray( "miles_amount_details" );

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject( i );
                            String id = jsonObject.optString( "id" );
                            String amount = jsonObject.optString( "amount" );

                            Vehicle vehicle = new Vehicle();
                            vehicle.setAmountId( id );
                            vehicle.setAmount( amount);
                            vehicleArrayList.add( vehicle );
                        }
                        amountRecyclerView.setLayoutManager( layoutManager );
                        milesAmountAdapter = new MilesAmountAdapter( vehicleArrayList, ShareMilesActivity.this );
                        amountRecyclerView.setAdapter( milesAmountAdapter );
                    }catch (Exception e)
                    {
                        Toast.makeText(ShareMilesActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ShareMilesActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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

                params.put("user_id", sharedPreferences.getString("user_id", ""));
                Log.i("getParamsValue", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(shareMilesRequest);
    }

    public void doneFunc()
    {
        StringRequest doneRequest = new StringRequest(Request.Method.POST, doneURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("UpdateAddressResponse", "onResponse: " + response);
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

                if (status == 1)
                {
                    finish();
                    startActivity(getIntent());

                } else {
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
                Log.e("error", "error" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_id", sharedPreferences.getString("user_id", ""));
                params.put("type",SelectedBtn );
                params.put("send_to",emailTxt.getText().toString() );
                params.put("send_miles_amt",milesAmountAdapter.getSelectedData());
                Log.i("doneParams", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(doneRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MineBtnNonBorder:
                SelectedBtn = "useMine";
                MineBtnNonBorder.setVisibility(View.GONE);
                MineBtnBorder.setVisibility(View.VISIBLE);
                PurchaseBtnBorder.setVisibility(View.GONE);
                PurchaseBtnNonBorder.setVisibility(View.VISIBLE);
                break;
            case R.id.PurchaseBtnNonBorder:
                SelectedBtn = "purchase";
                PurchaseBtnNonBorder.setVisibility(View.GONE);
                PurchaseBtnBorder.setVisibility(View.VISIBLE);
                MineBtnNonBorder.setVisibility(View.VISIBLE);
                MineBtnBorder.setVisibility(View.GONE);
                break;
        }
    }

    private boolean validate() {
        boolean flag = true;
        if (!emailTxt.getText().toString().matches("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$")) {
            emailTxt.setError("Alphabets Only");
            flag = false;
        }
        return flag;
    }
}
