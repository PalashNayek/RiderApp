package com.example.hp.googlemap;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PreviousFragment extends Fragment
{
    //String CurrentAPIurl = "http://45.55.64.233/Miles/api/trip_previous.php";
    String CurrentAPIurl = "http://45.55.64.233/Miles/api/trip_previous.php";
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Vehicle> vehicleArrayList = new ArrayList<>();
    PriviousFragmentAdapter sharedFragmentAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView txtNotDataFound;
    private RecyclerView scheduledRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_previous, container, false);
        sharedPreferences = getContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();
        layoutManager = new LinearLayoutManager(inflater.getContext());
        scheduledRecyclerView = rootView.findViewById(R.id.scheduledRecyclerView);
        txtNotDataFound = rootView.findViewById(R.id.txtNotDataFound);
        //Call Current Function
        currentFunc();
        return rootView;

    }

    //Current Function............
    private void currentFunc() {
        StringRequest dash_request = new StringRequest(Request.Method.POST, CurrentAPIurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("PreviousTripResponseDA", "onResponse: " + response);
                JSONObject resObj = null;
                int status = 0;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("error something occurs", "onResponse: " + e);
                }
                if (status == 2) {
                    //Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                    txtNotDataFound.setVisibility(View.VISIBLE);
                    txtNotDataFound.setText(msg);
                }
                if (status == 1) {
                    if (!vehicleArrayList.isEmpty()) {
                        vehicleArrayList.clear();

                    }
                    try {
                        JSONObject jsonRootObject = new JSONObject(response);
                        JSONArray jsonArray = jsonRootObject.getJSONArray("trip_details");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String driverName = jsonObject.optString("driver_name");
                            String pickUpDateTime = jsonObject.optString("pickup_datetime");
                            String carTier = jsonObject.optString("car_tier");
                            String amount = jsonObject.optString("fare_amount");


                            Vehicle vehicle = new Vehicle();
                            vehicle.setPreDriverName(driverName);
                            vehicle.setPreDateTime(pickUpDateTime);
                            vehicle.setPreCarTier(carTier);
                            vehicle.setPreAmount(amount);

                            vehicleArrayList.add(vehicle);
                        }
                        scheduledRecyclerView.setLayoutManager(layoutManager);
                        sharedFragmentAdapter = new PriviousFragmentAdapter(vehicleArrayList, getContext());
                        scheduledRecyclerView.setAdapter(sharedFragmentAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //sharedPreferences.getString("user_id", "")
                params.put("user_id",sharedPreferences.getString("user_id", "") );
                Log.i("PreviousParams", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(dash_request);
    }
}
